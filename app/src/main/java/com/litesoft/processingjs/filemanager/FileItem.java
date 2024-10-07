package com.litesoft.processingjs.filemanager;

import java.io.File;

public class FileItem {
    private File file;
    
    public FileItem(File file) {
        this.file = file;
    }
    
    public File getFile() {
        return file;
    }
}