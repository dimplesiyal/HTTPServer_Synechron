package com.org.utils;

import com.org.constants.ApplicationConstants;
import com.org.fileHandler.FileManagement;


public class ValidationUtils {

    public static boolean validatePortAndDirectory(int portNumber, String dirName) {
        boolean b = validatePort(portNumber) && vaidateDirectory(dirName);
        return b;
    }

    private static boolean validatePort(int portNumber) {
        if (0 < portNumber && portNumber < 65535) {
            return true;
        }
        return false;
    }

    private static boolean vaidateDirectory(String dirName) {
        if (dirName.length() != -1 && !dirName.isEmpty()) {
            String directoryPath = "C:/" + dirName;
            FileManagement.createDirectories(directoryPath);
            return true;
        }else{
            return false;
        }
    }
}
