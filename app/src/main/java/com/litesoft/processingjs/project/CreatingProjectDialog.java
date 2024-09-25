package com.litesoft.processingjs.project;

import android.content.Context;
import android.view.LayoutInflater;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.litesoft.processingjs.databinding.DialogTextinputBinding;
import com.litesoft.processingjs.utils.SimpleTextWatcher;
import com.litesoft.processingjs.project.files.ProjectFile;
import java.util.List;

public class CreatingProjectDialog {
    private Context context;
    private List<ProjectFile> existingProjects;
    private Listener listener;
    
    private TextInputLayout inputLayout;
    private TextInputEditText editText;
    private AlertDialog dialog;
    
    public CreatingProjectDialog(Context context, List<ProjectFile> existingProjects, Listener listener) {
        this.context = context;
        this.existingProjects = existingProjects;
        this.listener = listener;
        
        var binding = DialogTextinputBinding.inflate(LayoutInflater.from(context));
        inputLayout = binding.inputLayout;
        editText = binding.editText;
        
        inputLayout.setHint("Название");
        editText.setText("sketch");
        
        dialog = new MaterialAlertDialogBuilder(context)
        .setTitle("Новый проект")
        .setView(binding.getRoot())
        .setNegativeButton("Отмена", null)
        .setPositiveButton("Создать", (d, w) -> listener.onClickCreate(editText.getText().toString()))
        .create();
        
            
        var inputListener = new SimpleTextWatcher(editText, text -> {
            if (text.isEmpty()) {
                setError("Введите название!");
                return;
            }
                
            if (text.contains("/")) {
                setError("В названии не должно быть разделителя!");
                return;
            }
                
            for (ProjectFile project : existingProjects) {
                if (project.getName().equals(text)) {
                    setError("Название уже занято!");
                    return;
                }
            }
                
            setError("");
        });
        
        dialog.show();
    }
    
    private void setError(String msg) {
        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(msg.isEmpty());
        inputLayout.setErrorEnabled(!msg.isEmpty());
        inputLayout.setError(msg);
    }
    
    public interface Listener {
        public void onClickCreate(String name);
    }
}
