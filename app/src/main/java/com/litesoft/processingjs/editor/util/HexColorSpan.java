package com.litesoft.processingjs.editor.util;

import android.graphics.Paint;
import android.graphics.Canvas;

public class HexColorSpan {
    private float left;
    private float top;
    private float right;
    private float bottom;
    
    private int color;
    private int corner;
    private int padding;
    private Paint paint;
    
    public HexColorSpan(float left, float top, float right, float bottom, int color) {
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
        this.color = color;
        this.corner = 16;
        this.padding = 8;
        
        paint = new Paint();
        paint.setColor(color);
        paint.setAntiAlias(true);
    }
    
    public void draw(Canvas canvas) {
        canvas.drawRoundRect(left, top + padding, right, bottom - padding, corner, corner, paint);
    }
}
