package com.aymansalah.codebattle.services;

import com.aymansalah.codebattle.util.judge.Helper;
import com.aymansalah.codebattle.util.judge.SampleTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class FileUploadService {
    private static final String BASE_DIRECTORY = System.getProperty("user.dir") + "/src/main/";
    private static final String PHOTOS_DIRECTORY = BASE_DIRECTORY + "/resources/static/uploads/photos/";
    private static final String PROBLEMS_IO_DIRECTORY = BASE_DIRECTORY + "/webapp/WEB-INF/uploads/problems/";

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

    public void saveIOFilesForProblemId(long problemId, MultipartFile[] ioFiles) throws IOException {
        final String problemDirectory = PROBLEMS_IO_DIRECTORY + problemId + "/";
        createDirectoriesIfNotExists(problemDirectory);
        for(int i = 0; i < ioFiles.length; i++) {
            Path path = Paths.get(problemDirectory + unifyIOFileName(ioFiles[i].getOriginalFilename()));
            Files.write(path, ioFiles[i].getBytes());
        }
    }

    private static String getPersonalizedFileNameForUsername(String username, MultipartFile file) {
        return username + getFileExtension(file.getOriginalFilename());
    }
    private static void createDirectoriesIfNotExists(String path) {
        new File(path).mkdirs();
    }
    private static String getFileExtension(String fileName) {
        return fileName.substring(fileName.lastIndexOf('.'));
    }

    public List<File> getIOFileForProblemId(long problemId) {
        return listFilesInDirectory(PROBLEMS_IO_DIRECTORY + problemId + "/");
    }

    public File getMainTestOutputFileForProblemId(long problemId) {
        List<File> allFiles = getIOFileForProblemId(problemId);
        for(int i = 0; i < allFiles.size(); i++)
            if(!Helper.fileNameStartsWith(allFiles.get(i).getName(), "sample")) {
                if(Helper.isOutputFile(allFiles.get(i).getName()))
                    return allFiles.get(i);
            }
        return null;
    }

    public List<SampleTest> getSamplesForProblemId(long problemId) {
        List<File> sampleFiles = getSampleFiles(listFilesInDirectory(PROBLEMS_IO_DIRECTORY + problemId + "/"));
        List<SampleTest> samples = new ArrayList<>();
        HashMap<String, File> inputFilesHash = new HashMap<>();
        for(int i = 0; i < sampleFiles.size(); i++) {
            if (Helper.isInputFile(sampleFiles.get(i).getName()))
                inputFilesHash.put(Helper.getFileNameWithoutExt(sampleFiles.get(i).getName()), sampleFiles.get(i));
        }
        for(int i = 0, j = 0; i < sampleFiles.size(); i++) {
            if(Helper.isOutputFile(sampleFiles.get(i).getName())) {
                String input = Helper.getFileContent(inputFilesHash.get(Helper.getFileNameWithoutExt(sampleFiles.get(i).getName())));
                String output = Helper.getFileContent(sampleFiles.get(i));
                samples.add(new SampleTest(input, output));
            }
        }
        return samples;
    }

    private List<File> getSampleFiles(List<File> ioFiles) {
        List<File> samples = new ArrayList<>();
        for(int i = 0; i < ioFiles.size(); i++) {
            if(Helper.fileNameStartsWith(ioFiles.get(i).getName(), "sample"))
                samples.add(ioFiles.get(i));
        }
        return samples;
    }

    private static List<File> listFilesInDirectory(String directory) {
        return new ArrayList<File>(Arrays.asList(new File(directory).listFiles()));
    }

    public void updateProblemFiles(long problemId, MultipartFile[] ioFiles) {
        final String problemDirectory = PROBLEMS_IO_DIRECTORY + problemId + "/";

        clearDirectory(problemDirectory);

        createDirectoriesIfNotExists(problemDirectory);
        for(int i = 0; i < ioFiles.length; i++) {
            Path path = Paths.get(problemDirectory + unifyIOFileName(ioFiles[i].getOriginalFilename()));
            try {
                Files.write(path, ioFiles[i].getBytes());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void clearDirectory(String problemDirectory) {
        File dir = new File(problemDirectory);
        if(!dir.exists())
            return;
        String[] oldFiles = dir.list();
        for(String name : oldFiles) {
            File curFile = new File(dir.getPath(), name);
            curFile.delete();
        }
    }

    private String unifyIOFileName(String s) {
        if(Helper.isInputFile(s)) {
            if(Helper.fileNameStartsWith(s, "sample"))
                return "sample.in";
            else
                return "1.in";
        } else {
            if(Helper.fileNameStartsWith(s, "sample"))
                return "sample.out";
            else
                return "1.out";
        }
    }
}
