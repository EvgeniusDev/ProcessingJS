package com.litesoft.processingjs.editor.plugin.syntaxhighlight;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.Spanned;

import com.litesoft.processingjs.editor.plugin.base.Plugin;
import com.litesoft.processingjs.editor.theme.ITheme;
import com.litesoft.processingjs.editor.util.HexColorSpan;
import com.litesoft.processingjs.editor.util.SyntaxHighlightSpan;
import com.litesoft.processingjs.editor.lang.SyntaxHighlightResult;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

public class SyntaxHighlightPlugin extends Plugin {
    private ITheme theme;
    
    private List<HexColorSpan> hexColorSpans;
    private List<SyntaxHighlightResult> results;
    
    private SyntaxUpdaterTask syntaxUpdater;
    
    @Override
    public void attach() {
        super.attach();
        
        hexColorSpans = new ArrayList<>();
        results = new ArrayList<>();
        theme = editor.getTheme();
    }

    
    @Override
    public void onThemeChanged(ITheme theme) {
        super.onThemeChanged(theme);
        this.theme = theme;
    }

    
    @Override
    public void onTextInserted() {
        super.onTextInserted();
        syntaxHiglight();
    }
    
    
    @Override
    public void onAfterTextChanged(Editable editable) {
        super.onAfterTextChanged(editable);
        syntaxHiglight();
    }

    
    @Override
    public void onScrollChanged() {
        super.onScrollChanged();
        //updateSyntaxHighlight();
    }

    
    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);
        updateSyntaxHighlight();
    }
    
    
    private void syntaxHiglight() {
        if (syntaxUpdater != null) {
            syntaxUpdater.cancel();
            syntaxUpdater = null;
        }
        
        syntaxUpdater = new SyntaxUpdaterTask() {
            @Override
            public void onSuccess(List<SyntaxHighlightResult> result) {
                results.clear();
                results.addAll(result);
                updateSyntaxHighlight();
            }
        };

        var editable = editor.getText();
        syntaxUpdater.exexute(editable.toString());
    }
    
    
    private void updateSyntaxHighlight() {
        if (editor.getLayout() == null) return;
        
        Editable editable = editor.getText();
        hexColorSpans.clear();
        
        for (SyntaxHighlightSpan span : editable.getSpans(0, editable.length(), SyntaxHighlightSpan.class)) {
            editable.removeSpan(span);
        }
        
        int lineStart = editor.getLayout().getLineStart(editor.getTopVisibleLine());
        int lineEnd = editor.getLayout().getLineEnd(editor.getBottomVisibleLine());
        
        for (SyntaxHighlightResult result : results) {
            boolean isInText = result.start >= 0 && result.end <= editor.getText().length();
            boolean isValid = result.start <= result.end;
            boolean isVisible = (result.start >= lineStart && result.start <= lineEnd) || (result.start <= lineEnd && result.end >= lineStart);
            
            /*if (isInText && isValid && isVisible) {
                int start = result.start < lineStart ? lineStart : result.start;
                int end = result.end > lineEnd ? lineEnd : result.end;
                */
                int start = result.start;
                int end = result.end;
            
                switch (result.type) {
                    case LANG_CONST:
                    case NUMBER: {
                        editable.setSpan(new SyntaxHighlightSpan(theme.colorNumbers, Typeface.NORMAL), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        continue;
                    }
                    
                    case OPERATOR: {
                        editable.setSpan(new SyntaxHighlightSpan(theme.colorOperators, Typeface.NORMAL), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        continue;
                    }
                    
                    case KEYWORD: {
                        editable.setSpan(new SyntaxHighlightSpan(theme.colorKeywords, Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        continue;
                    }
                    
                    case TYPE: {
                        editable.setSpan(new SyntaxHighlightSpan(theme.colorTypes, Typeface.BOLD), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        continue;
                    }
                
                    case FUNCTION: {
                        editable.setSpan(new SyntaxHighlightSpan(theme.colorFunctions, Typeface.NORMAL), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        continue;
                    }
                    
                    case STRING: {
                        editable.setSpan(new SyntaxHighlightSpan(theme.colorStrings, Typeface.NORMAL), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        continue;
                    }
                    
                    case STRING_HEX: {
                        createHexColorSpan(result, editable, start, end);
                        continue;
                    }
                    
                    case SINGLE_COMMENT:
                    case BLOCK_COMMENT: {
                        editable.setSpan(new SyntaxHighlightSpan(theme.colorComments, Typeface.NORMAL), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        continue;
                    }
               // }
            }
        }
    }
    
    
    private void createHexColorSpan(SyntaxHighlightResult result, Editable editable, int start, int end) {
        if (!Pattern.matches("\"#([a-fA-F\\d]{6}|[a-fA-F\\d]{8})\"", editable.toString().substring(start, end))) {
            return;
        }
        
        String hex = editable.toString().substring(result.start+1, result.end-1);
        int color = Color.parseColor(hex);
        int textColor = Color.luminance(color) < 0.5f ? Color.WHITE : Color.BLACK;

        float[] startPos = editor.getCharPosition(result.start);
        float[] endPos = editor.getCharPosition(result.end);
        HexColorSpan span = new HexColorSpan(startPos[0], startPos[1], endPos[0], endPos[1] + editor.getLineHeight(), color);
        hexColorSpans.add(span);
        
        editable.setSpan(new SyntaxHighlightSpan(textColor, Typeface.NORMAL), result.start, result.end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

    
    @Override
    public void onDrawBehind(Canvas canvas) {
        super.onDrawBehind(canvas);
        
        for (HexColorSpan span : hexColorSpans) {
            span.draw(canvas);
        }
    }
}
