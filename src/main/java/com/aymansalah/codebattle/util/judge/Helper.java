package com.aymansalah.codebattle.util.judge;

import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;

public class Helper {

    public static boolean isInputFile(MultipartFile file) {
        if(null == file)
            return false;
        return getFileExtension(file).equalsIgnoreCase("in");
    }

    public static boolean isOutputFile(MultipartFile file) {
        if(null == file)
            return false;
        return getFileExtension(file).equalsIgnoreCase("out");
    }

    public static String getFileExtension(MultipartFile file) {
        String[] temp = file.getOriginalFilename().split("\\.");
        return temp.length == 0 ? null : temp[temp.length - 1];
    }

    public static double sizeInMB(long size) {
        return (double) size / 1024.0 / 1024.0;
    }

    public static boolean isImageType(String fileType) {
        return fileType.contains("image");
    }

    public static String getFileNameWithoutExt(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        int lastIdx = fileName.length() - 1;
        while(lastIdx >= 0 && fileName.charAt(lastIdx) != '.')
            lastIdx--;
        return fileName.substring(0, lastIdx);
    }

    public static boolean fileNameStartsWith(MultipartFile file, String prefix) {
        if(null == file || null == prefix)
            return false;
        return file.getOriginalFilename().startsWith(prefix);
    }


    public static boolean hasDifferentExtensions(MultipartFile[] ioFiles) {
        for(int i = 0; i < ioFiles.length; i++) {
            String ext = getFileExtension(ioFiles[i]);
            if(null == ext || !(ext.equalsIgnoreCase("in") || ext.equalsIgnoreCase("out")))
                return true;
        }
        return false;
    }

    public static boolean inputNameMatchesOutput(MultipartFile[] ioFiles) {
        HashSet<String> inputHash = new HashSet<>();
        HashSet<String> outputHash = new HashSet<>();
        for(int i = 0; i < ioFiles.length; i++) {
            String fileName = getFileNameWithoutExt(ioFiles[i]);
            if(isInputFile(ioFiles[i]))
                inputHash.add(fileName);
            else if(isOutputFile(ioFiles[i]))
                outputHash.add(fileName);
        }
        return inputHash.size() == outputHash.size() && inputHash.containsAll(outputHash);
    }



    public static int countSampleFiles(MultipartFile[] ioFiles) {
        int count = 0;
        for(int i = 0; i < ioFiles.length; i++)
            count += fileNameStartsWith(ioFiles[i], "sample") ? 1 : 0;
        return count;
    }

    public static int countFilesWithExtension(MultipartFile[] ioFiles, String ext) {
        int count = 0;
        for(int i = 0; i < ioFiles.length; i++) {
            String fileExt = getFileExtension(ioFiles[i]);
            count += null != fileExt && fileExt.equalsIgnoreCase(ext) ? 1 : 0;
        }
        return count;
    }

}
