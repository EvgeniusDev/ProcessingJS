package com.litesoft.processingjs.explorer;

import java.io.File;

public class ExplorerElement {
    private File file;
    
    public ExplorerElement(File file) {
        this.file = file;
    }
    
    public File getFile() {
        return file;
    }
}