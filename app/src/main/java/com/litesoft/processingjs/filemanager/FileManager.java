package com.litesoft.processingjs.filemanager;

import com.litesoft.processingjs.project.files.ProjectFile;
import java.util.ArrayList;
import java.util.List;

public class FileManager {
    private ProjectFile project;
    private List<FileItem> items;
    
    public FileManager(ProjectFile project) {
        this.project = project;
        items = new ArrayList<>();
    }
}