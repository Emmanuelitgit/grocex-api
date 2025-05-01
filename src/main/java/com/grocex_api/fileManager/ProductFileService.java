//package com.grocex_api.fileManager;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//
//@Service
//public class ProductFileService {
//
//    private final ProductFileRepo productFileRepo;
//
//    public ProductFileService(ProductFileRepo productFileRepo) {
//        this.productFileRepo = productFileRepo;
//    }
//
//
//    public void saveProductFile(MultipartFile file) throws IOException {
//        ProductFile productFile = ProductFile.builder()
//                .fileName(file.getOriginalFilename())
//                .fileType(file.getContentType())
//                .build();
//    }
//}
