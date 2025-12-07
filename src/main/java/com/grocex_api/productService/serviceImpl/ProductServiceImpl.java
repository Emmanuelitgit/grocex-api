package com.grocex_api.productService.serviceImpl;

import com.grocex_api.config.AppProperties;
import com.grocex_api.exception.NotFoundException;
import com.grocex_api.imageUtility.ImageUtil;
import com.grocex_api.productService.dto.*;
import com.grocex_api.productService.models.Product;
import com.grocex_api.productService.models.Vendor;
import com.grocex_api.productService.repo.ProductRepo;
import com.grocex_api.productService.repo.VendorRepo;
import com.grocex_api.productService.service.ProductService;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.utils.AppConstants;
import com.grocex_api.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;


@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepo productRepo;
    private final AppProperties appProperties;
    private final VendorRepo vendorRepo;
    private final ResourceLoader resourceLoader;
    @Value("${grocex.uploads.path}")
    private String uploadDirectory;

    @Autowired
    public ProductServiceImpl(ProductRepo productRepo, AppProperties appProperties, VendorRepo vendorRepo, ResourceLoader resourceLoader) {
        this.productRepo = productRepo;
        this.appProperties = appProperties;
        this.vendorRepo = vendorRepo;
        this.resourceLoader = resourceLoader;
    }

    /**
     * @description This method is used to save product to the db
     * @param productRequest The payload of the new product to be added
     * @return ResponseEntity containing the saved record and status info
     * @auther Emmanuel Yidana
     * @createdAt 30h April 2025
     */
    @Transactional
    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    public ResponseEntity<ResponseDTO> saveProduct(ProductRequest productRequest) {
        try{
            log.info("In save product method:->>");
            if (productRequest == null){
                log.error("payload cannot be null:->>{}", HttpStatus.BAD_REQUEST);
                ResponseDTO  response = AppUtils.getResponseDto("product payload cannot be null", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
            //check if the vendor the product is creating under is OPEN or CLOSE
            Optional<Vendor> vendorOptional = vendorRepo.findByUserId(UUID.fromString(AppUtils.getAuthenticatedUserId()));
            if (vendorOptional.isEmpty()){
                log.error("vendor not found:->>{}", AppUtils.getAuthenticatedUserId());
                ResponseDTO  response = AppUtils.getResponseDto("Vendor record not found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            if (!vendorOptional.get().getStatus().equalsIgnoreCase(AppConstants.VENDOR_OPEN)){
                log.error("Vendor is closed:->>{}", vendorOptional.get().getName());
                ResponseDTO  response = AppUtils.getResponseDto("Vendor is closed", HttpStatus.BAD_REQUEST);
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }

            // save image to uploads directory when provided
            String newFileName = null;
            if (productRequest.getFile()!=null){
                Path directory = Paths.get(uploadDirectory);
                if (!Files.exists(directory)){
                    log.info("Creating directory:->>{}", directory);
                    Files.createDirectories(directory);
                }
                //building unique file name
                String originalFilename = productRequest.getFile().getOriginalFilename();
                String timestamp = String.valueOf(System.currentTimeMillis());
                newFileName = timestamp + "_" + originalFilename;
                //saving file
                Path resolveFile = directory.resolve(newFileName);
                productRequest.getFile().transferTo(resolveFile);
            }

            //saving product
            Product data = Product.builder()
                    .name(productRequest.getName())
                    .unitPrice(productRequest.getPrice())
                    .quantity(productRequest.getQuantity())
                    .productOwnerId(UUID.fromString(AppUtils.getAuthenticatedUserId()))
                    .categoryId(productRequest.getCategoryId())
                    .fileName(newFileName)
                    .build();
            Product productRes = productRepo.save(data);

            log.info("product record added successfully:->>{}", productRes);
            ResponseDTO  response = AppUtils.getResponseDto("Product record added successfully", HttpStatus.CREATED, productRes);
            return new ResponseEntity<>(response, HttpStatus.CREATED);

        } catch (Exception e) {
            log.info("Exception->>>{}", (Object) e.getStackTrace());
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to get all products from the db
     * @return ResponseEntity containing the retrieved records and status info
     * @auther Emmanuel Yidana
     * @createdAt 30h April 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll(PaginationPayload paginationPayload) {
        try{
            log.info("In get all products method:->>");
            boolean isPaginate = paginationPayload.isPaginate();
            Page<ProductProjection> productsPage = null;
            List<ProductProjection> products = null;
            if (isPaginate){
                Pageable pageable = AppUtils.getPageRequest(paginationPayload);
                productsPage = productRepo.getProductAndCategory(paginationPayload.getCategory(), paginationPayload.getProduct(), pageable);
                if (productsPage.isEmpty()){
                    log.error("No product record found:->>{}", HttpStatus.NOT_FOUND);
                    ResponseDTO  response = AppUtils.getResponseDto("No product record found", HttpStatus.NOT_FOUND);
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
            }else {
                products = productRepo.getProductAndCategory(paginationPayload.getCategory(), paginationPayload.getProduct());
                if (products.isEmpty()){
                    log.error("No product record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                    ResponseDTO  response = AppUtils.getResponseDto("No product record found", HttpStatus.NOT_FOUND);
                    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
                }
            }

            // mapping response to include the image url
           List<Object> res = new ArrayList<>();
            for (ProductProjection projection: isPaginate?productsPage:products){
                ProductResponse productResponse = ProductResponse.builder()
                        .id(projection.getId())
                        .product(projection.getProduct())
                        .quantity(projection.getQuantity())
                        .unitPrice(projection.getUnitPrice())
                        .vendor(projection.getVendor())
                        .category(projection.getCategory())
                        .imageUrl(appProperties.getBaseUrl()+projection.getId())
                        .build();

                res.add(productResponse);
            }

            // if paginated add paginated items to the response
            if (isPaginate){
                Map<String, Object> paginationItems = new HashMap<>();
                paginationItems.put("pageable", productsPage.getPageable());
                paginationItems.put("totalElements", productsPage.getTotalElements());
                paginationItems.put("totalPages", productsPage.getTotalPages());

                res.add(paginationItems);
            }

            ResponseDTO  response = AppUtils.getResponseDto("products records fetched successfully", HttpStatus.OK, res);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to get product records given the product id.
     * @param productId Id of the product to be retrieved
     * @return ResponseEntity containing the retrieved record and status info
     * @auther Emmanuel Yidana
     * @createdAt 27h April 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findProductById(UUID productId) {
        try{
            log.info("In get product by id method:->>>>>>");
            ProductProjection product = productRepo.getProductAndCategoryById(productId);
            if (product == null){
                log.error("no product record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            // mapping response to include the image url
            ProductResponse productResponse = ProductResponse.builder()
                    .id(product.getId())
                    .product(product.getProduct())
                    .quantity(product.getQuantity())
                    .unitPrice(product.getUnitPrice())
                    .vendor(product.getVendor())
                    .category(product.getCategory())
                    .imageUrl(appProperties.getBaseUrl()+product.getId())
                    .build();

            log.info("Product record retrieved successfully:->>{}", productResponse);
            ResponseDTO  response = AppUtils.getResponseDto("product records fetched successfully", HttpStatus.OK, productResponse);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to get product records given the product category.
     * @param category The name of the category
     * @return ResponseEntity containing the retrieved record and status info
     * @auther Emmanuel Yidana
     * @createdAt 5th May 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findProductByCategory(String category) {
        try{
            log.info("In get product by category method:->>>>>>");
            List<ProductProjection> products = productRepo.getProductByCategory(category);
            if (products.isEmpty()){
                log.error("no product record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            // mapping response to include the image url
            List<ProductResponse> res = new ArrayList<>();
            for (ProductProjection projection: products){
                ProductResponse productResponse = ProductResponse.builder()
                        .id(projection.getId())
                        .product(projection.getProduct())
                        .quantity(projection.getQuantity())
                        .unitPrice(projection.getUnitPrice())
                        .vendor(projection.getVendor())
                        .category(projection.getCategory())
                        .imageUrl(appProperties.getBaseUrl()+projection.getId())
                        .build();

                res.add(productResponse);
            }
            ResponseDTO  response = AppUtils.getResponseDto("product records fetched successfully", HttpStatus.OK, res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to get product records given the product vendor.
     * @param vendor The name of the vendor
     * @return ResponseEntity containing the retrieved record and status info
     * @auther Emmanuel Yidana
     * @createdAt 5th May 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findProductByVendor(String vendor) {
        try{
            log.info("In get product by vendor method:->>>>>>");
            List<ProductProjection> products = productRepo.getProductsByVendor(vendor);
            if (products.isEmpty()){
                log.error("no product record found:->>>>>>>{}", HttpStatus.NOT_FOUND);
                ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }

            // mapping response to include the image url
            List<ProductResponse> res = new ArrayList<>();
            for (ProductProjection projection: products){
                ProductResponse productResponse = ProductResponse.builder()
                        .id(projection.getId())
                        .product(projection.getProduct())
                        .quantity(projection.getQuantity())
                        .unitPrice(projection.getUnitPrice())
                        .vendor(projection.getVendor())
                        .category(projection.getCategory())
                        .imageUrl(appProperties.getBaseUrl()+projection.getId())
                        .build();

                res.add(productResponse);
            }
            ResponseDTO  response = AppUtils.getResponseDto("product records fetched successfully", HttpStatus.OK, res);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used to update product records.
     * @param updateProductPayload The new data to be updated with
     * @return ResponseEntity containing updated record and status info
     * @auther Emmanuel Yidana
     * @createdAt 30h April 2025
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    @Override
    public ResponseEntity<ResponseDTO> updateProduct(UpdateProductPayload updateProductPayload) {
       try {
           log.info("In update product method:->>>>>>");
           Optional<Product> productOptional = productRepo.findById(updateProductPayload.getId());
           if (productOptional.isEmpty()) {
               log.error("Product record does not exist:->>{}", updateProductPayload.getId());
               ResponseDTO responseDTO = AppUtils.getResponseDto("Product record does not exist", HttpStatus.NOT_FOUND);
               return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
           }

           //populating updated fields
           Product existingData = productOptional.get();
           existingData.setName(updateProductPayload.getName() !=null? updateProductPayload.getName() : existingData.getName());
           existingData.setCategoryId(updateProductPayload.getCategoryId() !=null? updateProductPayload.getCategoryId() : existingData.getCategoryId());
           existingData.setRating(updateProductPayload.getRating() !=null? updateProductPayload.getRating() : existingData.getRating());
           existingData.setUnitPrice(updateProductPayload.getPrice() !=null ? updateProductPayload.getPrice() : existingData.getUnitPrice());
           existingData.setQuantity(updateProductPayload.getQuantity()!=null? updateProductPayload.getQuantity() : existingData.getQuantity());

           //remove image if available and add new one
           if (updateProductPayload.getFile()!=null){
              Path existingFile = Paths.get(Objects.requireNonNull(existingData.getFileName()));
              Path directory = Paths.get(uploadDirectory);
              Path resolvedFile = directory.resolve(existingFile);
              if (!Files.exists(resolvedFile)){
                  log.error("file does not exist:->>{}", existingFile.getFileName());
                  ResponseDTO responseDTO = AppUtils.getResponseDto("file does not exist", HttpStatus.NOT_FOUND);
                  return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
              }
              Files.delete(resolvedFile);

              //add the new file
              log.info("About to add new image");
              Path newFile = Paths.get(Objects.requireNonNull(updateProductPayload.getFile().getOriginalFilename()));
               String timestamp = String.valueOf(System.currentTimeMillis());
               String newFileName = timestamp + "_" + newFile;
               log.info("New image file created:->> {}", newFileName);
               Path newResolvedFile = directory.resolve(newFileName);
              updateProductPayload.getFile().transferTo(newResolvedFile);
              existingData.setFileName(newFileName);
          }

           //save updated data
           Product productRes = productRepo.save(existingData);

           log.info("Product updated successfully:->>{}", productRes);
           ResponseDTO  response = AppUtils.getResponseDto("product records updated successfully", HttpStatus.OK, productRes);
           return new ResponseEntity<>(response, HttpStatus.OK);

       } catch (Exception e) {
           log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
           ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
           return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    /**
     * @description This method is used to remove product records from the db.
     * @param productId
     * @return ResponseEntity containing message and status info
     * @auther Emmanuel Yidana
     * @createdAt 30th April 2025
     */
    @PreAuthorize("hasAnyAuthority('ADMIN', 'CLIENT')")
    @Override
    public ResponseEntity<ResponseDTO> removeProduct(UUID productId) {
        try {
            log.info("In remove product method:->>>>>>{}", productId);
            Optional<Product> productOptional = productRepo.findById(productId);
            if (productOptional.isEmpty()){
                ResponseDTO  response = AppUtils.getResponseDto("no product record found", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
            productRepo.deleteById(productId);
            //remove product image
            Path fileToDelete = Paths.get(productOptional.get().getFileName());
            Path directory = Paths.get(uploadDirectory);
            Path resolvedFile = directory.resolve(fileToDelete);
            if (!Files.exists(resolvedFile)){
                log.error("File does not exist:->>{}", fileToDelete.getFileName());
                ResponseDTO responseDTO = AppUtils.getResponseDto("File does not exist", HttpStatus.NOT_FOUND);
                return new ResponseEntity<>(responseDTO, HttpStatus.NOT_FOUND);
            }
            Files.delete(resolvedFile);

            log.info("product removed successfully:->>>>>>");
            ResponseDTO  response = AppUtils.getResponseDto("product record removed successfully", HttpStatus.OK);
            return new ResponseEntity<>(response, HttpStatus.OK);

        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            ResponseDTO  response = AppUtils.getResponseDto("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @description This method is used get a specific product image and displays it to the ui
     * @param id The id of the product
     * @return ResponseEntity containing the retrieved image and status info
     * @throws MalformedURLException
     */
    public Resource getImage(UUID id) throws MalformedURLException {
        log.info("In get image method:->>>>");
        Product dbImage = productRepo.findById(id)
                .orElseThrow(()-> new NotFoundException("image record not found"));
        Path directory = Paths.get(uploadDirectory);
        Path fileToLoad = Paths.get(dbImage.getFileName());
        Path resolvePath = directory.resolve(fileToLoad);
        if (!resolvePath.toFile().exists()) {
            log.error("image file does not exist");
            throw new NotFoundException("image file does not exist");
        }
        return new UrlResource(resolvePath.toUri());
    }

    @Cacheable(value = "userRecords", key = "#name")
    public String testRedis(String name){
        log.info("In testRedis method:->>>>>>");
        return name;
    }
}