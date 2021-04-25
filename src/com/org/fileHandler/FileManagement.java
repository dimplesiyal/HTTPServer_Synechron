package com.org.fileHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Random;

public class FileManagement {

    public String createRandomFileName(){
        Random random = new Random();
        String filename = "//Write_" +   random.nextInt(5000)   + ".txt";
        return filename;
    }

    public boolean createDirectories(String dir){
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
