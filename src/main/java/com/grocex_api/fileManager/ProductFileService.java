package com.grocex_api.fileManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProductFileService {

    private final ProductFileRepo productFileRepo;
    private final GoogleDriveService googleDriveService;

    @Autowired
    public ProductFileService(ProductFileRepo productFileRepo, GoogleDriveService googleDriveService) {
        this.productFileRepo = productFileRepo;
        this.googleDriveService = googleDriveService;
    }

    public void saveProductFile(MultipartFile file) throws IOException {
        String fileId = googleDriveService.uploadFile(file);
        ProductFile productFile = ProductFile.builder()
                .fileName(file.getOriginalFilename())
                .fileId(fileId)
                .fileType(file.getContentType())
                .build();
    }
}
