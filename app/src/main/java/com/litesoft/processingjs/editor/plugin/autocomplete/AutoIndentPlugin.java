package com.litesoft.processingjs.editor.plugin.autocomplete;

import com.litesoft.processingjs.editor.plugin.base.Plugin;

import android.text.Editable;

public class AutoIndentPlugin extends Plugin {
    private String indent = "    ";
    private String oldText;

    @Override
    public void onBeforeTextChanged(String text) {
        oldText = text;
    }

    @Override
    public void onAfterTextChanged(Editable editable) {
        String text = editable.toString();

        if (text.length() == oldText.length() + 1 && editor.getSelectionStart() > 0) {
            char pressedChar = text.charAt(editor.getSelectionStart() - 1);

            //нажат enter
            if (pressedChar == '\n') {
                int lastLineNum = editor.getCurrentLine() - 1;

                //Get the indentation of the previous line
                String[] lines = editor.getText().toString().split("\n");
                String lastLine = "";
                String lastIndent = "";

                //Calculate the indentation of the previous line
                if (lines.length > 0) {
                    lastLine = lines[Math.min(lastLineNum, lines.length - 1)];

                    for (int i = 0; i < lastLine.length(); i ++) {
                        if (lastLine.charAt(i) == ' ') {
                            lastIndent += ' ';
                        }
                        else {
                            break;
                        }
                    }
                }

                //Determine the last character of the previous line (not counting whitespace)
                char lastChar = ' ';
                String trimmedLastLine = lastLine.trim();
                if (trimmedLastLine.length() > 0) {
                    lastChar = trimmedLastLine.charAt(trimmedLastLine.length() - 1);
                }

                //Automatically indent
                if (lastChar == '{') {

                    //Automatically increase the indent if this is a new code block
                    editor.getText().insert(editor.getSelectionStart(), lastIndent + indent);

                    //Automatically press enter again so that everything lines up nicely.. This is incredibly hacky...
                    //Also make sure that the user has enabled curly brace insertion
                    if (editor.getText().length() > editor.getSelectionStart() && editor.getText().charAt(editor.getSelectionStart()) == '}') {
                        //Add a newline (the extra space is so that we don't recursively detect a newline; adding at least two characters at once sidesteps this possibility)
                        editor.getText().insert(editor.getSelectionStart(), "\n" + lastIndent + " ");
                        //Move the cursor back (hacky...)
                        editor.setSelection(editor.getSelectionStart() - (lastIndent.length() + 2));
                        //Remove the extra space (see above)
                        editor.getText().replace(editor.getSelectionStart() + 1, editor.getSelectionStart() + 2, "");
                    }
                } else {
                    //Regular indentation
                    editor.getText().insert(editor.getSelectionStart(), lastIndent);
                }
            }
        }
    }
}
