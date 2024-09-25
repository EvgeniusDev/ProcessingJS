package com.litesoft.processingjs.project.files;

import com.litesoft.processingjs.editor.CodeEditorFragment;
import java.io.File;
import com.litesoft.processingjs.utils.FileUtil;

public class TextFile extends AbstractFile {
    private CodeEditorFragment fragment;
    
    public TextFile(File file) {
        super(file);
        fragment = new CodeEditorFragment(this);
    }
    
    public CodeEditorFragment getFragment() {
        return fragment;
    }
    
    public String readFile() {
        File file = getBaseFile();
        return FileUtil.readFile(file);
    }
}