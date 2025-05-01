//package com.grocex_api.fileManager;
//
//import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
//import com.google.api.client.http.FileContent;
//import com.google.api.client.http.javanet.NetHttpTransport;
//import com.google.api.client.json.JsonFactory;
//import com.google.api.client.json.gson.GsonFactory;
//import com.google.api.services.drive.Drive;
//import com.google.api.services.drive.DriveScopes;
//import com.google.api.services.drive.model.File;
//import com.google.api.services.drive.model.FileList;
//import com.google.auth.http.HttpCredentialsAdapter;
//import com.google.auth.oauth2.GoogleCredentials;
//import com.grocex_api.components.ProfileNameProvider;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.nio.file.Files;
//import java.nio.file.Path;
//import java.nio.file.StandardOpenOption;
//import java.security.GeneralSecurityException;
//import java.util.Collections;
//import java.util.List;
//
//@Slf4j
//@Service
//public class GoogleDriveService {
//    private static final String APPLICATION_NAME = "Google Drive API Java";
//    private static final JsonFactory JSON_FACTORY = GsonFactory.getDefaultInstance();
//    private static final String GOOGLE_CREDENTIALS_PATH = System.getenv("GOOGLE_CREDENTIALS_PATH");
//    private static final String FOLDER_ID = System.getenv("FOLDER_ID");
//    private final ProfileNameProvider profileNameProvider;
//    private Drive driveService;
//
//
//    public static void main(String[] args) {
//        if (FOLDER_ID == null) {
//            throw new IllegalStateException("FOLDER_ID environment variable is not set.");
//        }
//
//        System.out.println("Google Drive Folder ID: " + FOLDER_ID);
//    }
//
//
//    public GoogleDriveService(ProfileNameProvider profileNameProvider) throws GeneralSecurityException, IOException {
//        this.profileNameProvider = profileNameProvider;
//        final NetHttpTransport HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
//        this.driveService = new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, new HttpCredentialsAdapter(getCredentials()))
//                .setApplicationName(APPLICATION_NAME)
//                .build();
//    }
//
//
//    private GoogleCredentials getCredentials() throws IOException {
//        if (profileNameProvider.getActiveProfileName().equals("prod")){
//            String credentialsPath = "/etc/secrets/GOOGLE_CREDENTIALS_PATH";
//
//            try (InputStream serviceAccountStream = new FileInputStream(credentialsPath)) {
//                return GoogleCredentials.fromStream(serviceAccountStream)
//                        .createScoped(Collections.singleton(DriveScopes.DRIVE));
//            }
//        }else {
//            ClassLoader classLoader = getClass().getClassLoader();
//            InputStream serviceAccountStream = classLoader.getResourceAsStream("credentials.json");
//
//            if (serviceAccountStream == null) {
//                throw new IllegalStateException("Could not find credentials.json in resources folder.");
//            }
//
//            return GoogleCredentials.fromStream(serviceAccountStream)
//                    .createScoped(Collections.singleton(DriveScopes.DRIVE));
//        }
//    }
//
//
//    public String uploadFile(MultipartFile file) throws IOException {
//        File fileMetadata = new File();
//        fileMetadata.setName(file.getOriginalFilename());
//        fileMetadata.setParents(Collections.singletonList(FOLDER_ID));
//
//        FileContent mediaContent = new FileContent(file.getContentType(), convertMultiPartToFile(file));
//        File uploadedFile = driveService.files().create(fileMetadata, mediaContent)
//                .setFields("id")
//                .execute();
//        return uploadedFile.getId();
//    }
//
//    public String updateFileById(String fileId, MultipartFile file) throws IOException {
//        java.io.File tempFile = convertMultiPartToFile(file);
//        File fileMetadata = new File();
//        fileMetadata.setName(file.getOriginalFilename());
//
//        FileContent mediaContent = new FileContent(file.getContentType(), tempFile);
//        File updatedFile = driveService.files()
//                .update(fileId, fileMetadata, mediaContent)
//                .setFields("id, name, modifiedTime")
//                .execute();
//
//        tempFile.delete();
//        return updatedFile.getId();
//    }
//
//    public void removeFileById(String fileId) throws IOException {
//        driveService.files().delete(fileId).execute();
//    }
//
//    private java.io.File convertMultiPartToFile(MultipartFile file) throws IOException {
//        java.io.File convFile = new java.io.File(file.getOriginalFilename());
//        try (FileOutputStream fos = new FileOutputStream(convFile)) {
//            fos.write(file.getBytes());
//        }
//        return convFile;
//    }
//
//    public List<File> listFiles() throws IOException {
//        FileList result = driveService.files().list()
//                .setQ("'" + FOLDER_ID + "' in parents and mimeType contains 'image/'")
//                .setPageSize(20)
//                .setFields("nextPageToken, files(id, name, mimeType, webViewLink, webContentLink, thumbnailLink)")
//                .execute();
//        return result.getFiles();
//    }
//}