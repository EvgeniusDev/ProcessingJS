package com.litesoft.processingjs.editor.util;

import android.text.style.BackgroundColorSpan;
import android.text.TextPaint;

public class BracketHighlightSpan extends BackgroundColorSpan {
    private int textColor;
    
    public BracketHighlightSpan(int color, int textColor) {
        super(color);
        this.textColor = textColor;
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setFakeBoldText(true);
        textPaint.setColor(textColor);
    }
}
