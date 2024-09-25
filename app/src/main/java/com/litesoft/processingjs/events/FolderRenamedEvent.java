package com.litesoft.processingjs.events;

import java.io.File;

public class FolderRenamedEvent extends Event {
    public File child;
    public String oldParentPath;
    
    public FolderRenamedEvent(File child, String oldPath) {
        this.child = child;
        this.oldParentPath = oldPath;
    }
}