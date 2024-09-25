package com.litesoft.processingjs.events;

import java.io.File;

public class FileDeletedEvent extends Event {
    public File file;
    
    public FileDeletedEvent(File file) {
        this.file = file;
    }
}