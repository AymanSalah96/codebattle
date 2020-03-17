package com.aymansalah.codebattle.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class FileUploadService {
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";
    private static final String PHOTOS_DIRECTORY = UPLOAD_DIRECTORY + "/photos/";

    @Autowired
    private UserService userService;

    public boolean savePhotoForUsername(MultipartFile imageFile, String username) throws IOException {
        if(null == imageFile)
            return false;
        createDirectoriesIfNotExists(PHOTOS_DIRECTORY);
        String photoName = getPersonalizedFileNameForUsername(username, imageFile);
        Path path = Paths.get(PHOTOS_DIRECTORY + photoName);
        Files.write(path, imageFile.getBytes());
        userService.savePhotoForUsername(username, photoName);
        return true;
    }

    private static String getPersonalizedFileNameForUsername(String username, MultipartFile file) {return username + getFileExtension(file.getOriginalFilename());}
    private static void createDirectoriesIfNotExists(String path) {
        new File(path).mkdirs();
    }
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }
}
