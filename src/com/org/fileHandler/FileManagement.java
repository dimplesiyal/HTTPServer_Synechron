package com.org.fileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;

public class FileManagement {

    public static boolean createDirectories(String dir){
        File file = new File(dir);
        return file.mkdirs();
    }

    public static void writeToFile(FileInputStream fis, OutputStream os)
            throws Exception {
        try {
            int n;
            while ((n = fis.read()) != -1) {
                os.write(n);
            }
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (os != null) {
                os.close();
            }
        }
        System.out.println("File Copied");

    }
}
