package com.litesoft.processingjs.editor.util;

import android.text.style.BackgroundColorSpan;
import android.text.TextPaint;

public class BracketHighlightSpan extends BackgroundColorSpan {
    
    public BracketHighlightSpan(int color) {
        super(color);
    }

    @Override
    public void updateDrawState(TextPaint textPaint) {
        super.updateDrawState(textPaint);
        textPaint.setFakeBoldText(true);
    }
}
