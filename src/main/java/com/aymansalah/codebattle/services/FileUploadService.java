package com.aymansalah.codebattle.services;

import com.aymansalah.codebattle.util.judge.Helper;
import com.aymansalah.codebattle.util.judge.SampleTest;
import com.aymansalah.codebattle.util.judge.checkers.Checker;
import com.aymansalah.codebattle.util.judge.checkers.LineChecker;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
public class FileUploadService {
    private static final String UPLOAD_DIRECTORY = System.getProperty("user.dir") + "/src/main/resources/static/uploads";
    private static final String PHOTOS_DIRECTORY = UPLOAD_DIRECTORY + "/photos/";
    private static final String PROBLEMS_IO_DIRECTORY = UPLOAD_DIRECTORY + "/problems/";
    private static final String SUBMISSIONS_DIRECTORY = UPLOAD_DIRECTORY + "/submissions/";
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
            Path path = Paths.get(problemDirectory + ioFiles[i].getOriginalFilename());
            Files.write(path, ioFiles[i].getBytes());
        }
    }

    private static String getPersonalizedFileNameForUsername(String username, MultipartFile file) {return username + getFileExtension(file.getOriginalFilename());}
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

    public boolean saveSubmittedOutput(long problemId, String username, MultipartFile file) {
        String submissionDirectory = SUBMISSIONS_DIRECTORY + username + "/" + problemId + "/";
        createDirectoriesIfNotExists(submissionDirectory);
        Path path = Paths.get(submissionDirectory + file.getOriginalFilename());
        try {
            Files.write(path, file.getBytes());
            File judgeTestFile = getMainTestOutputFileForProblemId(problemId);
            if(null == judgeTestFile)
                return false;
            Checker checker = new LineChecker();
            boolean result = checker.compare(judgeTestFile, new File(path.toString()));
            Files.deleteIfExists(path);
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
