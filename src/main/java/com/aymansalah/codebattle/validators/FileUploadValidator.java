package com.aymansalah.codebattle.validators;

import com.aymansalah.codebattle.util.judge.Helper;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FileUploadValidator {

    public static List<String> validateImage(MultipartFile imageFile) {
        List<String> errors = new ArrayList<>();
        if(!Helper.isImageType(imageFile.getContentType()))
            errors.add("You can only upload an image");
        else if(Helper.sizeInMB(imageFile.getSize()) > 1)
            errors.add("Image size cannot exceed 1 MB");
        return errors;
    }

    public static List<String> validateProblemIOFiles(MultipartFile[] ioFiles) {
        List<String> errors = new ArrayList<>();
        if(ioFiles.length != 4) {
            errors.add("4 files expected, one sample and one test file");
            return errors;
        }
        if(Helper.hasDifferentExtensions(ioFiles)) {
            errors.add("'.in' and '.out' files only supported");
            return errors;
        }
        int inCount = Helper.countFilesWithExtension(ioFiles, "in");
        int outCount = Helper.countFilesWithExtension(ioFiles, "out");
        if(inCount == 0 || inCount != outCount) {
            errors.add("Input files count must match output files count");
            errors.add("Make sure that input files ends with '.in'");
            errors.add("Make sure that output files ends with '.out'");
            return errors;
        }
        int samplesCount = Helper.countSampleFiles(ioFiles);
        if (samplesCount < 1) {
            errors.add("Must exists at least one sample file");
            errors.add("Make sure that sample files begins with 'sample'");
            return errors;
        }
        if (samplesCount == ioFiles.length) {
            errors.add("All files detected as samples");
            errors.add("Must exist at least one test file");
            return errors;
        }
        int notSamplesCount = ioFiles.length - samplesCount;
        if(!Helper.inputNameMatchesOutput(ioFiles)) {
            errors.add("Input files names not matches output files names");
            errors.add("Make sure that each input file has the same name of it's output file");
            return errors;
        }
        if(notSamplesCount != 2) {
            errors.add("Only one main test file needed, more than one detected");
            return errors;
        }
        return errors;
    }

    public static List<String> validateSubmitOutputFile(MultipartFile file) {
        List<String> errors = new ArrayList<>();
        errors.add("Wrong Answer");
        System.out.println(file.getContentType());
        return errors;
    }
}
