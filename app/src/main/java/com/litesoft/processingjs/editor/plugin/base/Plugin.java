package com.litesoft.processingjs.editor.plugin.base;

import com.litesoft.processingjs.editor.widget.CodeEditor;

import android.graphics.Canvas;
import android.text.Editable;
import com.litesoft.processingjs.editor.theme.ITheme;

public abstract class Plugin {
    protected CodeEditor editor;
    
    
    public void attach() {}
    
    
    public void detach() {}
    
    
    public void onTextInserted() {}
    
    
    public void onScrollChanged() {}
    
    
    public void onTextChanged(String text) {}
    
    
    public void onBeforeTextChanged(String text) {}
    
    
    public void onAfterTextChanged(Editable editable) {}
    
    
    public void onSelectionChanged(int start, int end) {}
    
    
    public void onSizeChanged(int width, int height) {}
    
    
    public void onDrawBehind(Canvas canvas) {}
    
    
    public void onDrawFront(Canvas canvas) {}
    
    
    public void onThemeChanged(ITheme theme) {}
    
    
    public String getName() {
        return getClass().getSimpleName();
    }
}
