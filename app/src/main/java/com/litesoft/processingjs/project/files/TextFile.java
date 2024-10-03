package com.litesoft.processingjs.project.files;

import java.io.File;

import com.litesoft.processingjs.utils.FileUtil;

public class TextFile extends AbstractFile {
    
    public TextFile(File file) {
        super(file);
    }
   
    public String readFile() {
        File file = getBaseFile();
        return FileUtil.readFile(file);
    }
}