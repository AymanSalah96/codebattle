package com.aymansalah.codebattle.util.judge;

import com.aymansalah.codebattle.models.User;
import com.aymansalah.codebattle.util.NullAwareBeanUtilsBeanAndIgnoreIdProperty;
import org.apache.commons.beanutils.BeanUtilsBean;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;

public class Helper {

    public static boolean isInputFile(String fileName) {
        if(null == fileName)
            return false;
        return getFileExtension(fileName).equalsIgnoreCase("in");
    }

    public static boolean isOutputFile(String fileName) {
        if(null == fileName)
            return false;
        return getFileExtension(fileName).equalsIgnoreCase("out");
    }

    public static String getFileExtension(String fileName) {
        String[] temp = fileName.split("\\.");
        return temp.length == 0 ? null : temp[temp.length - 1];
    }

    public static double sizeInMB(long size) {
        return (double) size / 1024.0 / 1024.0;
    }

    public static boolean isImageType(String fileType) {
        return fileType.contains("image");
    }

    public static String getFileNameWithoutExt(String fileName) {
        int lastIdx = fileName.length() - 1;
        while(lastIdx >= 0 && fileName.charAt(lastIdx) != '.')
            lastIdx--;
        return fileName.substring(0, lastIdx);
    }

    public static boolean fileNameStartsWith(String fileName, String prefix) {
        if(null == fileName || null == prefix)
            return false;
        return fileName.startsWith(prefix);
    }


    public static boolean hasDifferentExtensions(MultipartFile[] ioFiles) {
        for(int i = 0; i < ioFiles.length; i++) {
            String ext = getFileExtension(ioFiles[i].getOriginalFilename());
            if(null == ext || !(ext.equalsIgnoreCase("in") || ext.equalsIgnoreCase("out")))
                return true;
        }
        return false;
    }

    public static boolean inputNameMatchesOutput(MultipartFile[] ioFiles) {
        HashSet<String> inputHash = new HashSet<>();
        HashSet<String> outputHash = new HashSet<>();
        for(int i = 0; i < ioFiles.length; i++) {
            String fileName = getFileNameWithoutExt(ioFiles[i].getOriginalFilename());
            if(isInputFile(ioFiles[i].getOriginalFilename()))
                inputHash.add(fileName);
            else if(isOutputFile(ioFiles[i].getOriginalFilename()))
                outputHash.add(fileName);
        }
        return inputHash.size() == outputHash.size() && inputHash.containsAll(outputHash);
    }



    public static int countSampleFiles(MultipartFile[] ioFiles) {
        int count = 0;
        for(int i = 0; i < ioFiles.length; i++)
            count += fileNameStartsWith(ioFiles[i].getOriginalFilename(), "sample") ? 1 : 0;
        return count;
    }

    public static int countFilesWithExtension(MultipartFile[] ioFiles, String ext) {
        int count = 0;
        for(int i = 0; i < ioFiles.length; i++) {
            String fileExt = getFileExtension(ioFiles[i].getOriginalFilename());
            count += null != fileExt && fileExt.equalsIgnoreCase(ext) ? 1 : 0;
        }
        return count;
    }

    public static String getFileContent(File file) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while(null != (line = reader.readLine())) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    public static Object replaceNotNullProperties(Object target, Object source) {
        BeanUtilsBean notNull = new NullAwareBeanUtilsBeanAndIgnoreIdProperty();
        try {
            notNull.copyProperties(target, source);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return target;
    }
}
