package com.litesoft.processingjs.utils;

import android.widget.EditText;
import java.io.File;
import java.util.List;

public class FileNameInputValidator {
    private EditText editText;
    private List<File> otherFiles;
    private Listener listener;
    private File file;
    
    public FileNameInputValidator(EditText editText, List<File> otherFiles, File file, Listener listener) {
        this.editText = editText;
        this.otherFiles = otherFiles;
        this.listener = listener;
        this.file = file;
        
        var watcher = new SimpleTextWatcher(editText, text -> validate());
    }
    
    public void validate() {
        String input = editText.getText().toString();
        
        if (input.isEmpty()) {
            listener.onValidate("Введите название!");
            return;
        }
        
        for (File f : otherFiles) {
            if (file != f && f.getName().equals(input)) {
                listener.onValidate("Имя уже занято!");
                return;
            }
        }
        
        if (input.contains("/")) {
            listener.onValidate("Нельзя использовать разделитель!");
            return;
        }
        
        listener.onValidate("");
    }
    
    public interface Listener {
        public void onValidate(String errorMsg);
    }
}