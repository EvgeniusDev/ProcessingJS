package com.litesoft.processingjs.project;

import android.content.Context;
import android.content.res.AssetManager;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.litesoft.processingjs.project.files.ProjectFile;

public class ProjectModifier {
    private Context context;
    private ProjectFile projectFile;
    
    public ProjectModifier(Context context) {
        this.context = context;
    }
    
    public void setProject(ProjectFile projectFile) {
        this.projectFile = projectFile;
    }
    
    public void applyFirstSetup() {
        File file = projectFile.getBaseFile();
        file.mkdirs();
        
        File assets = new File(file.getAbsolutePath(), "assets");
        assets.mkdir();
        
        File scripts = new File(file.getAbsolutePath(), "scripts");
        scripts.mkdir();
        
        File libs = new File(file.getAbsolutePath(), "libs");
        libs.mkdir();
        
        copyFileFromAssets("template/index.html", new File(file, "index.html").getPath());
        copyFileFromAssets("template/style.css", new File(file, "style.css").getPath());
        copyFileFromAssets("template/p5.min.js", new File(libs, "p5.min.js").getPath());
        copyFileFromAssets("template/sketch.js", new File(scripts, "sketch.js").getPath());
        copyFileFromAssets("template/config.json", new File(file, "config.cfg").getPath());
    }
    
    private void copyFileFromAssets(String assetFileName, String destinationFilePath) {
        AssetManager assetManager = context.getAssets();

        try (InputStream inputStream = assetManager.open(assetFileName);
            FileOutputStream outputStream = new FileOutputStream(destinationFilePath)) {
         
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
