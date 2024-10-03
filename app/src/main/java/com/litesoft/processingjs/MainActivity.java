package com.litesoft.processingjs;

import android.content.Intent;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.color.MaterialColors;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.common.eventbus.Subscribe;

import com.litesoft.processingjs.databinding.ActivityMainBinding;
import com.litesoft.processingjs.editor.fragment.CodeEditorFragment;
import com.litesoft.processingjs.editor.fragment.CodeFragmentAdapter;
import com.litesoft.processingjs.editor.tools.colorpicker.ColorPickerDialog;
import com.litesoft.processingjs.events.FileDeletedEvent;
import com.litesoft.processingjs.events.FileRenamedEvent;
import com.litesoft.processingjs.events.FolderRenamedEvent;
import com.litesoft.processingjs.explorer.FileExplorerFragment;
import com.litesoft.processingjs.editor.widget.CodeEditor;
import com.litesoft.processingjs.project.ProjectModifier;
import com.litesoft.processingjs.project.files.ProjectFile;
import com.litesoft.processingjs.project.files.TextFile;
import com.litesoft.processingjs.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_PROJECT = "project";

    private final String[] SUPPORT_FORMATS = {".js", ".html", ".css", ".cfg"};

    private ProjectFile projectFile;

    private ActivityMainBinding binding;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    private TabLayout tabLayout;
    private ViewPager2 codePager;
    private CodeFragmentAdapter adapter;
    private List<CodeEditorFragment> fragments = new ArrayList<>();

    private FileExplorerFragment explorerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GlobalEventBus.register(this);

        projectFile = (ProjectFile) getIntent().getSerializableExtra(EXTRA_PROJECT);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.appBarLayout.setStatusBarForegroundColor(MaterialColors.getColor(binding.appBarLayout, com.google.android.material.R.attr.colorSurface));
        binding.toolbar.setTitle(projectFile.getName());
        ((MenuBuilder) binding.toolbar.getMenu()).setOptionalIconsVisible(true);

        binding.toolbar.setOnMenuItemClickListener(item -> {
            onMenuItemClicked(item.getItemId());
            return true;
        });
        
        
        drawerLayout = binding.drawer;
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, binding.toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();

        
        tabLayout = binding.tablayout;
        codePager = binding.codePager;
        
        adapter = new CodeFragmentAdapter(getSupportFragmentManager(), getLifecycle());
        adapter.setFragments(fragments);
        codePager.setAdapter(adapter);
        
        new TabLayoutMediator(tabLayout, codePager, (tab, position) -> {
            tab.setText(fragments.get(position).getName());
        })
        .attach();
        
        
        explorerFragment = new FileExplorerFragment(projectFile);
        explorerFragment.setOnFileClickListener(file -> openFile(file));

        getSupportFragmentManager()
        .beginTransaction()
        .replace(R.id.file_explorer_layout, explorerFragment)
        .commit();

        
        final View activityRootView = findViewById(android.R.id.content);
        activityRootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                activityRootView.getWindowVisibleDisplayFrame(r);
                int screenHeight = activityRootView.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;

                if (keypadHeight < screenHeight * 0.15) {
                    var fragment = fragments.get(codePager.getCurrentItem());
                    fragment.updateSize();
                }
            }
        });
        
        
        openFile(projectFile.getFile("scripts/sketch.js"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        saveFiles();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.binding = null;
    }
    
    
    private void onMenuItemClicked(int id) {
        if (id == R.id.menu_save) {
            saveFiles();
        }
        else if (id == R.id.menu_set_orientation) {
            setupOrientation();
        } 
        else if (id == R.id.menu_colorpicker) {
            showColorPicker();
        }
        else if (id == R.id.menu_run) {
            saveFiles();
            runSketch();
        }
    }
    
    
    private void showColorPicker() {
        var dialog = new ColorPickerDialog(this);
    }
    
    
    private void setupOrientation() {
        String[] orientationNames = {"Вертикальная", "Горизонтальная"};
        String[] orientationValues = {"portrait", "landscape"};
        
        new MaterialAlertDialogBuilder(this)
        .setTitle("Выберите ориентацию проекта")
        .setSingleChoiceItems(orientationNames, 0, (d, w) -> {
            ProjectModifier modifier = new ProjectModifier(MainActivity.this);
            modifier.setProject(projectFile);
            modifier.setupOrientation(orientationValues, w);
        })
        .create()
        .show();
    }
    
    
    private void runSketch() {
        File index = projectFile.getFile("index.html");
        
        if (index != null && index.exists()) {
            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra(PlayActivity.EXTRA_URL, index.getAbsolutePath());
            intent.putExtra("project", projectFile);
            startActivity(intent);
        }
    }
    
    private void saveFiles() {
        for (CodeEditorFragment fragment : fragments) {
            fragment.saveFile();
        }
    }
    
    private void openFile(File file) {
        if (file.exists() && isTextFile(file)) {
            if (isFileOpened(file)) {
                codePager.setCurrentItem(getFileTabPosition(file));
                drawerLayout.closeDrawer(GravityCompat.START);
                return;
            }

            String name = file.getName();
            TextFile textFile = new TextFile(file);
            
            fragments.add(new CodeEditorFragment(textFile));
            adapter.notifyOnItemChanged(fragments.size()-1);
            adapter.notifyItemInserted(fragments.size()-1);
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    private boolean isTextFile(File file) {
        for (String format : SUPPORT_FORMATS) {
            if (file.getName().endsWith(format)) {
                return true;
            }
        }

        return false;
    }

    private boolean isFileOpened(File file) {
        for (CodeEditorFragment fragment : fragments) {
            TextFile f = fragment.getFile();
            
            if (f.getPath().equals(file.getPath())) {
                return true;
            }
        }

        return false;
    }

    private int getFileTabPosition(File file) {
        for (int i = 0; i < fragments.size(); i++) {
            TextFile f = fragments.get(i).getFile();
            
            if (file.getPath().equals(f.getPath())) {
                return i;
            }
        }

        return 0;
    }
    
    @Subscribe
    public void onFileDeletedEvent(FileDeletedEvent event) {
        for (CodeEditorFragment fragment : fragments) {
            TextFile file = fragment.getFile();
            
            if (file.getName().equals(event.file.getName())) {
                int pos = getFileTabPosition(event.file);
                fragments.remove(pos);
                adapter.notifyOnItemChanged(pos);
                adapter.notifyItemRemoved(pos);
                return;
            }
        }
    }
    
    @Subscribe
    public void onFileRenamedEvent(FileRenamedEvent event) {
        for (CodeEditorFragment fragment : fragments) {
            TextFile file = fragment.getFile();
            
            if (file.getPath().equals(event.oldPath)) {
                int pos = getFileTabPosition(file.getBaseFile());
                file.setFile(event.file);
                tabLayout.getTabAt(pos).setText(event.file.getName());
                return;
            }
        }
    }
    
    @Subscribe
    public void onFolderRenamedEvent(FolderRenamedEvent event) {
        for (CodeEditorFragment fragment : fragments) {
            TextFile file = fragment.getFile();
            
            if (file.getPath().equals(event.oldParentPath)) {
                file.setFile(event.child);
                return;
            }
        }
    }
}
