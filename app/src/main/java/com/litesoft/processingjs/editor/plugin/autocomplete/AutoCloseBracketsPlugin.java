package com.litesoft.processingjs.editor.plugin.autocomplete;

import android.text.Editable;

import java.util.HashMap;
import java.util.Map;

import com.litesoft.processingjs.editor.plugin.base.Plugin;

public class AutoCloseBracketsPlugin extends Plugin {
    private Map<Character, Character> pairs;
    private String oldText;

    @Override
    public void attach() {
        super.attach();
        
        pairs = new HashMap<>();
        pairs.put('(', ')');
        pairs.put('{', '}');
        pairs.put('[', ']');
    }

    
    @Override
    public void onBeforeTextChanged(String text) {
        oldText = text;
    }

    @Override
    public void onAfterTextChanged(Editable editable) {
        String text = editable.toString();

        if (text.length() == oldText.length() + 1 && editor.getSelectionStart() > 0) {
            char pressedChar = text.charAt(editor.getSelectionStart() - 1);

            //авто закрытие скобок
            if (pairs.containsKey(pressedChar)) {
                editor.insertText(pairs.get(pressedChar).toString(), editor.getSelectionStart());
                editor.setSelection(editor.getSelectionStart() - 1);
            }

            //если нажат символ закрывающей скобки
            //и перед курсором такая же скобка
            //нужно перевести курсор за эту скобку
            if (pairs.containsValue(pressedChar) && editor.getSelectionStart() < text.length()) {
                if (pressedChar == text.charAt(editor.getSelectionStart())) {
                    editable.replace(editor.getSelectionStart() - 1, editor.getSelectionStart(), "");
                    editor.setSelection(editor.getSelectionStart() + 1);
                }
            }
            
            //авто закрытие кавычек
            if (pressedChar == '\'' || pressedChar == '\"') {
                editor.insertText(String.valueOf(pressedChar), editor.getSelectionStart());
                editor.setSelection(editor.getSelectionStart() - 1);
            }
        }
    }
}
