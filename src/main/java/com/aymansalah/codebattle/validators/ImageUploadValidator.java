package com.aymansalah.codebattle.validators;

import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

public class ImageUploadValidator {

    public static List<String> validateImage(MultipartFile imageFile) {
        List<String> errors = new ArrayList<>();
        if(!isImageType(imageFile.getContentType()))
            errors.add("You can only upload an image");
        if(sizeInMB(imageFile.getSize()) > 1)
            errors.add("File size cannot exceed 1 MB");
        return errors;
    }

    private static double sizeInMB(long size) {
        return (double) size / 1024.0 / 1024.0;
    }

    private static boolean isImageType(String fileType) {
        return fileType.contains("image");
    }
}
