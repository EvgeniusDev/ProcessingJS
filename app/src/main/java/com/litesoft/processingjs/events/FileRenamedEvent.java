package com.litesoft.processingjs.events;

import java.io.File;

public class FileRenamedEvent extends Event {
    public File file;
    public String oldPath;
    
    public FileRenamedEvent(File file, String oldPath) {
        this.file = file;
        this.oldPath = oldPath;
    }
}