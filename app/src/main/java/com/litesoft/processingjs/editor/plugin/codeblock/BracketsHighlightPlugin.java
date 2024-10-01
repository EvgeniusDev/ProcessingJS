package com.litesoft.processingjs.editor.plugin.codeblock;

import android.graphics.Color;
import android.text.Spanned;

import com.litesoft.processingjs.editor.plugin.base.Plugin;
import com.litesoft.processingjs.editor.theme.ITheme;
import com.litesoft.processingjs.editor.util.BracketHighlightSpan;

public class BracketsHighlightPlugin extends Plugin {
    private char[] delimiters = {'{', '[', '(', '<', '}', ']', ')', '>'};

    private BracketHighlightSpan openDelimiterSpan;
    private BracketHighlightSpan closedDelimiterSpan;
    
    @Override
    public void attach() {
        super.attach();
        
        var theme = editor.getTheme();
        openDelimiterSpan = new BracketHighlightSpan(theme.colorPairBrackets);
        closedDelimiterSpan = new BracketHighlightSpan(theme.colorPairBrackets);
    }
    
    
    @Override
    public void onSelectionChanged(int start, int end) {
        if (start != end) {
            return;
        }
        
        editor.getText().removeSpan(openDelimiterSpan);
        editor.getText().removeSpan(closedDelimiterSpan);
        
        if (start > 0 && start <= editor.getText().length()) {
            char c1 = editor.getText().charAt(start-1);
            
            for (int i=0; i<delimiters.length; i++) {
                if (delimiters[i] == c1) {
                    int half = delimiters.length/2;
                    boolean open = i <= half - 1;
                    char c2 = delimiters[(i + half) % delimiters.length];
                    
                    int k = start;
                    
                    if (open) {
                        int nob = 1;
                        
                        while (k < editor.getText().length()) {
                            if (editor.getText().charAt(k) == c2) {
                                nob--;
                            }
                            
                            if (editor.getText().charAt(k) == c1) {
                                nob++;
                            }
                            
                            if (nob == 0) {
                                showBracket(start-1, k);
                                break;
                            }
                            
                            k++;
                        }
                    }
                    else {
                        int ncb = 1;
                        k -= 2;
                        
                        while (k >= 0) {
                            if (editor.getText().charAt(k) == c2) {
                                ncb--;
                            }
                            
                            if (editor.getText().charAt(k) == c1) {
                                ncb++;
                            }
                            
                            if (ncb == 0) {
                                showBracket(k, start-1);
                                break;
                            }
                            
                            k--;
                        }
                    }
                }
            }
        }
    }
    
    private void showBracket(int i, int j) {
        editor.getText().setSpan(openDelimiterSpan, i, i+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        editor.getText().setSpan(closedDelimiterSpan, j, j+1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    @Override
    public void onThemeChanged(ITheme theme) {
        openDelimiterSpan = new BracketHighlightSpan(theme.colorPairBrackets);
        closedDelimiterSpan = new BracketHighlightSpan(theme.colorPairBrackets);
    }
}
