package com.litesoft.processingjs.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;

public class FileUtil {
    
    public static void createFile(String path) {
        try {
            File file = new File(path);
            file.createNewFile();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String readFile(File file) {
        StringBuilder sb = new StringBuilder();
        FileReader fr = null;
        
        try {
            fr = new FileReader(file);

            char[] buff = new char[1024];
            int length = 0;

            while ((length = fr.read(buff)) > 0) {
                sb.append(new String(buff, 0, length));
            }
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        
        return sb.toString();
    }
    
    public static void writeFile(File file, String str) {
        try {
            FileWriter writer = new FileWriter(file, false);
            writer.write(str);
            writer.flush();
            writer.close();
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }

        File[] fileArr = file.listFiles();

        if (fileArr != null) {
            for (File subFile : fileArr) {
                if (subFile.isDirectory()) {
                    deleteFile(subFile.getAbsoluteFile());
                }

                if (subFile.isFile()) {
                    subFile.delete();
                }
            }
        }

        file.delete();
    }
}