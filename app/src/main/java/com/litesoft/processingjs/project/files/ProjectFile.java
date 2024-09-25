package com.litesoft.processingjs.project.files;

import java.io.File;
import java.util.List;

public class ProjectFile extends AbstractFile {
    public ProjectFile(File file) {
        super(file);
    }
    
    public File getFile(String relativePath) {
        return new File(getBaseFile(), relativePath);
    }
}