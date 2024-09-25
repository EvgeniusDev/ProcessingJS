package com.litesoft.processingjs.explorer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.litesoft.processingjs.GlobalEventBus;
import com.litesoft.processingjs.databinding.FragmentFileExplorerBinding;
import com.litesoft.processingjs.R;
import com.litesoft.processingjs.events.FileDeletedEvent;
import com.litesoft.processingjs.project.files.ProjectFile;
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
                renameFile(elements.get(pos));
            } else if (id == R.id.menu_delete_file) {
                deleteFile(elements.get(pos));
            }
        });
        
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
    
    private void renameFile(ExplorerElement element) {
        
    }
    
    private void deleteFile(ExplorerElement element) {
        new MaterialAlertDialogBuilder(getContext())
        .setTitle("Удаление файла")
        .setMessage("Вы уверены, что хотите удалить файл?")
        .setNegativeButton("Нет", null)
        .setPositiveButton("Да", (d, w) -> {
            elements.remove(element);
            element.getFile().delete();
            updateElements();
            GlobalEventBus.post(new FileDeletedEvent(element.getFile()));
        })
        .create()
        .show();
    }
    
    public interface OnFileClickListener {
        public void onClick(File file);
    }
}