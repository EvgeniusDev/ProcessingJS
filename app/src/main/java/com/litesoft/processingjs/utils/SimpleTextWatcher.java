package com.litesoft.processingjs.utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class SimpleTextWatcher implements TextWatcher {
    private Listener listener;
    
    public SimpleTextWatcher(EditText target, Listener listener) {
        target.addTextChangedListener(this);
        this.listener = listener;
    }
    
    @Override
    public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}

     @Override
    public void afterTextChanged(Editable arg0) {}
    
    @Override
    public void onTextChanged(CharSequence seq, int arg1, int arg2, int arg3) {
        listener.onTyped(seq.toString());
    }
    
    public interface Listener {
        public void onTyped(String text);
    }
}