package com.litesoft.processingjs.explorer;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.litesoft.processingjs.R;

public class FileIconProvider {
    private Context context;
    private Map<String, Drawable> baseIcons;
    private Drawable folderIcon;
    private Drawable folderOpenIcon;
    
    public FileIconProvider(Context context) {
        this.context = context;
        this.baseIcons = new HashMap<>();
        
        baseIcons.put(".js", context.getDrawable(R.drawable.ic_language_javascript));
        baseIcons.put(".html", context.getDrawable(R.drawable.ic_language_html5));
        baseIcons.put(".css", context.getDrawable(R.drawable.ic_language_css3));
        baseIcons.put(".cfg", context.getDrawable(R.drawable.ic_information_outline));
        
        folderIcon = context.getDrawable(R.drawable.ic_folder);
        folderOpenIcon = context.getDrawable(R.drawable.ic_folder_open);
    }
    
    public Drawable getIconForElement(ExplorerElement element) {
        File file = element.getFile();
        
        if (file == null) {
            return folderOpenIcon;
        }
        
        if (file.isDirectory()) {
            return folderIcon;
        }
        
        for (String format : baseIcons.keySet()) {
            if (file.getName().endsWith(format)) {
                return baseIcons.get(format);
            }
        }
        
        if (file.getName().endsWith(".png") || file.getName().endsWith(".jpg")) {
            return new BitmapDrawable(BitmapFactory.decodeFile(file.getPath()));
        }
        
        return null;
    }
}