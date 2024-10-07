package com.litesoft.processingjs.utils.validator.filename;

import com.litesoft.processingjs.utils.validator.Validator;

import java.io.File;
import java.util.List;

public class FileExistsValidator implements Validator {
    private List<File> other;
    
    public FileExistsValidator(List<File> other) {
        this.other = other;
    }
    
    @Override
    public String validate(String input) {
        for (File file : other) {
            String fileName = file.getName();
            fileName = fileName.substring(0, fileName.lastIndexOf("."));
            
            if (input.equals(fileName)) {
                return "Файл уже существует";
            }
        }
        
        return "";
    }
}