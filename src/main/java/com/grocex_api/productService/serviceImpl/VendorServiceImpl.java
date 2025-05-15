package com.grocex_api.productService.serviceImpl;

import com.grocex_api.exception.BadRequestException;
import com.grocex_api.exception.NotFoundException;
import com.grocex_api.exception.ServerException;
import com.grocex_api.productService.models.Vendor;
import com.grocex_api.productService.repo.VendorRepo;
import com.grocex_api.productService.service.VendorService;
import com.grocex_api.response.ResponseDTO;
import com.grocex_api.userService.models.User;
import com.grocex_api.userService.repo.UserRepo;
import com.grocex_api.utils.AppUtils;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;

@Slf4j
@Service
public class VendorServiceImpl implements VendorService {

    private final VendorRepo vendorRepo;
    private final UserRepo userRepo;

    @Autowired
    public VendorServiceImpl(VendorRepo vendorRepo, UserRepo userRepo) {
        this.vendorRepo = vendorRepo;
        this.userRepo = userRepo;
    }

    /**
     * @description This method is used return all vendors.
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 15th  May 2025
     */
    @Override
    public ResponseEntity<ResponseDTO> findAll() {
        List<Vendor> vendors = vendorRepo.findAll();

        if (vendors.isEmpty()){
            throw new NotFoundException("no vendor record found");
        }
        log.info("vendors fetched success:->>>>>>");
        ResponseDTO  response = AppUtils.getResponseDto("vendors records", HttpStatus.OK, vendors);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    /**
     * @description This method is used to save vendor on user creation. it is called in the user service.
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 15th  May 2025
     */
    @Override
    public void saveVendor(Vendor vendor) {
        try{
            if (vendor == null){
                throw new BadRequestException("vendor payload cannot be null");
            }
            if (vendor.getStatus() == null || vendor.getName() == null){
                String message = vendor.getName() == null? "name" : "status";
                throw new BadRequestException("vendor" + " " + message + " " + "cannot be null");
            }
            User user = userRepo.findById(vendor.getUserId())
                    .orElseThrow(()-> new NotFoundException("user record not found"));

            vendorRepo.save(vendor);
        } catch (Exception e) {
            log.error("Exception Occurred!, statusCode -> {} and Cause -> {} and Message -> {}", 500, e.getCause(), e.getMessage());
            throw new ServerException("Error saving vendor details");
        }
    }

    /**
     * @description This method is used to save vendor on user update. it is called in the user service.
     * @param vendor
     * @return
     * @auther Emmanuel Yidana
     * @createdAt 15th  May 2025
     */
    @Override
    public void updateVendor(Vendor vendor) {
        // checking if vendor exist by id. if not throw exception
        Vendor existingData = vendorRepo.findById(vendor.getId())
                .orElseThrow(()-> new NotFoundException("vendor record not found"));

        // populating vendor update details
        existingData.setName(vendor.getName() !=null? vendor.getName() : existingData.getName());
        existingData.setStatus(vendor.getStatus() !=null? vendor.getStatus() :existingData.getStatus());
        
        // saving vendor
        vendorRepo.save(vendor);
    }
}
