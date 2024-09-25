package com.litesoft.processingjs.project.files;

import java.io.File;
import java.io.Serializable;

public abstract class AbstractFile implements Serializable {
    private File file;
    
    public AbstractFile(File file) {
        this.file = file;
    }
    
    public String getPath() {
        return file.getPath();
    }
    
    public String getName() {
        return file.getName();
    }
    
    public File getBaseFile() {
        return file;
    }
}