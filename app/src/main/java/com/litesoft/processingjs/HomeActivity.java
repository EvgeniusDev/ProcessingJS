package com.litesoft.processingjs;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.litesoft.processingjs.databinding.ActivityHomeBinding;

import com.litesoft.processingjs.databinding.DialogTextinputBinding;
import com.litesoft.processingjs.project.CreatingProjectDialog;
import com.litesoft.processingjs.project.files.ProjectFile;
import com.litesoft.processingjs.project.ProjectListAdapter;

import com.litesoft.processingjs.project.ProjectModifier;
import com.litesoft.processingjs.utils.DeleteFileDialog;
import com.litesoft.processingjs.utils.FileNameInputValidator;
import com.litesoft.processingjs.utils.FileUtil;
import com.litesoft.processingjs.utils.RecyclerSpaceDecorator;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {
    public static final String PROJECTS_FOLDER = "/P5Projects/";
    
    private ActivityHomeBinding binding;
    
    private List<ProjectFile> projectFiles;
    private ProjectListAdapter projectListAdapter;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowCompat.setDecorFitsSystemWindows(getWindow(), true);
        
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        projectFiles = new ArrayList<>();
        projectListAdapter = new ProjectListAdapter(this);
        projectListAdapter.setOnItemClickListener(pos -> openProject(projectFiles.get(pos)));
        projectListAdapter.setOnItemMenuClickListener((itemId, pos) -> onProjectMenuClick(itemId, pos));
        
        binding.projects.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        binding.projects.setAdapter(projectListAdapter);
        binding.projects.addItemDecoration(new RecyclerSpaceDecorator((int) getResources().getDimension(R.dimen.recycler_space)));
        
        binding.fab.setOnClickListener(v -> beginNewProject());
        
        if (!hasPermissions()) {
            new MaterialAlertDialogBuilder(this)
            .setTitle("Требуется разрешение")
            .setMessage("Для работы приложения, требуется предоставить разрешение на чтение и запись файлов")
            .setNegativeButton("Выход", (d, w) -> finishAffinity())
            .setPositiveButton("Предоставить", (d, w) -> requestPermissions(101))
            .setCancelable(false)
            .create()
            .show();
        }
    }
    
    private boolean hasPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        } else {
            return true;
        }
    }

    private void requestPermissions(int requestCode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", getPackageName())));
                startActivityForResult(intent, requestCode);
            } catch (Exception e) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, requestCode);
            }
        } else {
            requestPermissions(new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, requestCode);
        }
    }
    
    @Override
    protected void onActivityResult(int arg0, int arg1, Intent arg2) {
        super.onActivityResult(arg0, arg1, arg2);
        loadProjects();
    }
    
    @Override
    protected void onResume() {
        super.onResume();
        loadProjects();
    }
    
    private void loadProjects() {
        if (!hasPermissions()) {
            return;
        }
        
        projectFiles.clear();
            
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + PROJECTS_FOLDER);
        
        if (folder.exists() && folder.listFiles() != null) {
            for (File file : folder.listFiles()) {
                var project = new ProjectFile(file);
                projectFiles.add(project);
            }
        }
        
        projectListAdapter.setData(projectFiles);
        projectListAdapter.notifyDataSetChanged();
    }
    
    private void onProjectMenuClick(int itemId, int pos) {
        var project = projectFiles.get(pos);
        
        if (itemId == R.id.menu_rename_file) {
            renameProject(project);
        } else if (itemId == R.id.menu_delete_file) {
            deleteProject(project);
        }
    }
    
    private void renameProject(ProjectFile project) {
        var binding = DialogTextinputBinding.inflate(getLayoutInflater());
        var inputLayout = binding.inputLayout;
        var editText = binding.editText;
        
        inputLayout.setHint("Введите название");
        editText.setText(project.getName());
        
        var dialog = new MaterialAlertDialogBuilder(this)
        .setTitle("Переименовать файл")
        .setView(binding.getRoot())
        .setNegativeButton("Отмена", null)
        .setPositiveButton("Ок", (d, w) -> {
            continueRenaming(editText.getText().toString(), project);
        })
        .create();
        
        dialog.show();
        
        var files = new ArrayList<File>();
        
        for (ProjectFile p : projectFiles) {
            files.add(p.getBaseFile());
        }
        
        var validator = new FileNameInputValidator(editText, files, project.getBaseFile(), msg -> {
            inputLayout.setErrorEnabled(msg.isEmpty());
            inputLayout.setError(msg);
            dialog.getButton(dialog.BUTTON_POSITIVE).setEnabled(msg.isEmpty());
        });
        
        validator.validate();
    }
    
    private void continueRenaming(String name, ProjectFile project) {
        File oldFile = project.getBaseFile();
        File newFile = new File(oldFile.getParent(), name);
        oldFile.renameTo(newFile);
        project.setFile(newFile);
        projectListAdapter.notifyDataSetChanged();
    }
    
    private void deleteProject(ProjectFile project) {
        var deleteDialog = new DeleteFileDialog(this, () -> {
            FileUtil.deleteFile(project.getBaseFile());
            projectFiles.remove(project);
            projectListAdapter.notifyDataSetChanged();
        });
    }
    
    private void beginNewProject() {
        var dialog = new CreatingProjectDialog(this, projectFiles, name -> {
            createProject(name);
        });
    }
    
    private void createProject(String name) {
        File file = new File(Environment.getExternalStorageDirectory() + PROJECTS_FOLDER + name);
        var project = new ProjectFile(file);
        
        ProjectModifier modifier = new ProjectModifier(this);
        modifier.setProject(project);
        modifier.applyFirstSetup();
        
        openProject(project);
    }
    
    private void openProject(ProjectFile project) {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra(MainActivity.EXTRA_PROJECT, project);
        startActivity(intent);
    }
}
