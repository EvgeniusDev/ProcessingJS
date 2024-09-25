package com.litesoft.processingjs.editor;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.CallSuper;
import androidx.annotation.MainThread;
import androidx.fragment.app.Fragment;

import com.litesoft.processingjs.databinding.FragmentCodeEditorBinding;
import com.litesoft.processingjs.editor.CodeEditorView;
import com.litesoft.processingjs.project.files.TextFile;
import com.litesoft.processingjs.R;

public class CodeEditorFragment extends Fragment {
    private CodeEditorView editor;
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
        editor.post(()-> {
            var container = view.findViewById(R.id.code_container);
            int pad = container.getPaddingLeft()*2;
            editor.setMinWidth(container.getWidth()-pad);
            editor.setMinHeight(container.getHeight()-pad);
        });
        
        editor.openFile(textFile);
    }
    
    public CodeEditorView getEditor() {
        return editor;
    }
    
    public TextFile getFile() {
        return textFile;
    }
}