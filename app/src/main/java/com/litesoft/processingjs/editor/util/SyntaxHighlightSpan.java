package com.litesoft.processingjs.editor.util;

import android.text.style.CharacterStyle;
import android.text.TextPaint;
import android.graphics.Typeface;

public class SyntaxHighlightSpan extends CharacterStyle {
    private int color;
    private int style;

    public SyntaxHighlightSpan(int color, int style) {
        this.color = color;
        this.style = style;
    }

    @Override
    public void updateDrawState(TextPaint paint) {
        paint.setFakeBoldText(style == Typeface.BOLD);
        paint.setColor(color);

        if (style == Typeface.ITALIC) {
            paint.setTextSkewX(-0.2f);
        }
    }
}
