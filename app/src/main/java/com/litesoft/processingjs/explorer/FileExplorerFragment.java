package com.litesoft.processingjs.explorer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.litesoft.processingjs.GlobalEventBus;
import com.litesoft.processingjs.databinding.DialogTextinputBinding;
import com.litesoft.processingjs.databinding.FragmentFileExplorerBinding;
import com.litesoft.processingjs.R;
import com.litesoft.processingjs.events.FileDeletedEvent;
import com.litesoft.processingjs.events.FileRenamedEvent;
import com.litesoft.processingjs.events.FolderRenamedEvent;
import com.litesoft.processingjs.project.files.ProjectFile;
import com.litesoft.processingjs.utils.FileNameInputValidator;
import com.litesoft.processingjs.utils.FileUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileExplorerFragment extends Fragment {
    private RecyclerView recyclerView;
    private FileExplorerAdapter adapter;
    private ProjectFile projectFile;
    private File rootFolder;
    private File currentFolder;
    private List<ExplorerElement> elements;
    
    private OnFileClickListener fileClickListener;
    
    public FileExplorerFragment(ProjectFile projectFile) {
        this.projectFile = projectFile;
        this.elements = new ArrayList<>();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle args) {
        return FragmentFileExplorerBinding.inflate(inflater, parent, false).getRoot();
    }
    
    @Override
    public void onViewCreated(View view, Bundle args) {
        super.onViewCreated(view, args);
        
        adapter = new FileExplorerAdapter(getContext());
        adapter.setOnItemClickListener(pos -> {
            if (pos == FileExplorerAdapter.EMPTY_ELEMENT) {
                goBack();
            } else {
                onClickElement(elements.get(pos));
            }
        });
        
        adapter.setOnItemMenuClickListener((id, pos) -> {
            if (id == R.id.menu_rename_file) {
                handleRenameFile(elements.get(pos));
            } else if (id == R.id.menu_delete_file) {
                handleDeleteFile(elements.get(pos));
            }
        });
        
        Button btnCreateFile = view.findViewById(R.id.btn_create_file);
        btnCreateFile.setOnClickListener(v -> createFile(true));
        
        Button btnCreateFolder = view.findViewById(R.id.btn_create_folder);
        btnCreateFolder.setOnClickListener(v -> createFile(false));
        
        recyclerView = view.findViewById(R.id.files);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        
        rootFolder = projectFile.getBaseFile();
        currentFolder = rootFolder;
    }
    
    @Override
    @MainThread
    @CallSuper
    public void onResume() {
        super.onResume();
        updateElements();
    }
    
    public void setOnFileClickListener(OnFileClickListener fileClickListener) {
        this.fileClickListener = fileClickListener;
    }
    
    private void goBack() {
        if (!currentFolder.getPath().equals(rootFolder.getPath())) {
            currentFolder = currentFolder.getParentFile();
            updateElements();
        }
    }
    
    private void onClickElement(ExplorerElement element) {
        File file = element.getFile();
        
        if (file.isDirectory()) {
            currentFolder = file;
            updateElements();
            return;
        }
        
        if (fileClickListener != null) {
            fileClickListener.onClick(file);
        }
    }
    
    private void updateElements() {
        elements.clear();
        
        if (currentFolder.exists() && currentFolder.listFiles() != null) {
            for (File file : currentFolder.listFiles()) {
                elements.add(new ExplorerElement(file));
            }
        }
        
        adapter.setElements(elements);
        adapter.notifyDataSetChanged();
    }
    
    private void createFile(boolean isFile) {
        var binding = DialogTextinputBinding.inflate(getLayoutInflater());
        var inputLayout = binding.inputLayout;
        var editText = binding.editText;
        
        inputLayout.setHint("Введите название");
        
        var dialog = new MaterialAlertDialogBuilder(getContext())
        .setTitle(isFile ? "Новый файл" : "Новая папка")
        .setView(binding.getRoot())
        .setNegativeButton("Отмена", null)
        .setPositiveButton("Создать", (d, w) -> {
            String name = editText.getText().toString();
            String path = currentFolder.getPath() + "/" + name;
    
            if (isFile) {
                FileUtil.createFile(path);
            } else {
                File file = new File(path);
                file.mkdir();
            }
                
            updateElements();
        })
        .create();
        
        dialog.show();
        
        var files = getElementsAsFiles();
        
        var validator = new FileNameInputValidator(editText, files, null, msg -> {
            inputLayout.setErrorEnabled(msg.isEmpty());
            inputLayout.setError(msg);
            dialog.getButton(dialog.BUTTON_POSITIVE).setEnabled(msg.isEmpty());
        });
        
        validator.validate();
    }
    
    
    private void handleRenameFile(ExplorerElement element) {
        var binding = DialogTextinputBinding.inflate(getLayoutInflater());
        var inputLayout = binding.inputLayout;
        var editText = binding.editText;
        
        inputLayout.setHint("Введите название");
        editText.setText(element.getFile().getName());
        
        var dialog = new MaterialAlertDialogBuilder(getContext())
        .setTitle("Переименовать файл")
        .setView(binding.getRoot())
        .setNegativeButton("Отмена", null)
        .setPositiveButton("Ок", (d, w) -> {
            continueRenaming(editText.getText().toString(), element);
        })
        .create();
        
        dialog.show();
        
        var files = getElementsAsFiles();
        
        var validator = new FileNameInputValidator(editText, files, element.getFile(), msg -> {
            inputLayout.setErrorEnabled(msg.isEmpty());
            inputLayout.setError(msg);
            dialog.getButton(dialog.BUTTON_POSITIVE).setEnabled(msg.isEmpty());
        });
        
        validator.validate();
    }
    
    private void continueRenaming(String name, ExplorerElement element) {
        File oldFile = element.getFile();
        String oldPath = oldFile.getPath();
        
        File newFile = new File(oldFile.getParent(), name);
        oldFile.renameTo(newFile);
        updateElements();
        
        GlobalEventBus.post(new FileRenamedEvent(newFile, oldPath));
        
        if (newFile.isDirectory()) {
            notifyFilesOnFolderRenamed(newFile, oldPath);
        }
    }
    
    private void notifyFilesOnFolderRenamed(File file, String oldPath) {
        if (file.isFile()) {
            GlobalEventBus.post(new FolderRenamedEvent(file, oldPath));
        }
        
        File[] files = null;
        
        if (file.isDirectory()) {
            files = file.listFiles();
        }
        
        if (files != null) {
            for (File subFile : files) {
                notifyFilesOnFolderRenamed(subFile, oldPath + "/" + subFile.getName());
            }
        }
    }
    
    private void handleDeleteFile(ExplorerElement element) {
        new MaterialAlertDialogBuilder(getContext())
        .setTitle("Удаление файла")
        .setMessage("Вы уверены, что хотите удалить файл?")
        .setNegativeButton("Нет", null)
        .setPositiveButton("Да", (d, w) -> {
            deleteFile(element.getFile());
            elements.remove(element);
            updateElements();
        })
        .create()
        .show();
    }
    
    private void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            GlobalEventBus.post(new FileDeletedEvent(file));
        }
        
        File[] files = null;
        
        if (file.isDirectory()) {
            files = file.listFiles();
        }
        
        if (files != null) {
            for (File subFile : files) {
                deleteFile(subFile);
            }
        }
        
        file.delete();
    }
    
    private List<File> getElementsAsFiles() {
        var files = new ArrayList<File>();
        
        for (ExplorerElement e : elements) {
            files.add(e.getFile());
        }
        
        return files;
    }
    
    public interface OnFileClickListener {
        public void onClick(File file);
    }
}