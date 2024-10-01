package com.litesoft.processingjs.editor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;

import com.litesoft.processingjs.editor.theme.impl.GithubDarkTheme;
import com.litesoft.processingjs.databinding.FragmentCodeEditorBinding;
import com.litesoft.processingjs.editor.widget.CodeEditor;
import com.litesoft.processingjs.project.files.TextFile;
import com.litesoft.processingjs.R;

public class CodeEditorFragment extends Fragment {
    private CodeEditor editor;
    private TextFile textFile;
    
    public CodeEditorFragment(TextFile file) {
        textFile = file;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle bundle) {
        var binding = FragmentCodeEditorBinding.inflate(inflater, parent, false);
        return binding.getRoot();
    }
    
    @Override
    @MainThread
    public void onViewCreated(View view, Bundle args) {
        super.onViewCreated(view, args);
        
        editor = view.findViewById(R.id.editor);
        editor.bindContainer(view.findViewById(R.id.code_container));
        editor.setTheme(new GithubDarkTheme());
        editor.openFile(textFile);
    }
    
    public CodeEditor getEditor() {
        return editor;
    }
    
    public TextFile getFile() {
        return textFile;
    }
}
