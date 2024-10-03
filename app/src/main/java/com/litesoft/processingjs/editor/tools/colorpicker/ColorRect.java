package com.litesoft.processingjs.editor.tools.colorpicker;

import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Color;
import android.util.TypedValue;
import android.graphics.PointF;
import android.view.MotionEvent;

public class ColorRect extends View {
    private GradientDrawable colorGradient;
    private GradientDrawable shadeGradient;
    
    private Paint thumbPaint;
    private float thumbRadius;
    private PointF thumbPosition;
    private int thumbColor;
    
    private float hue;
    private float saturation;
    private float value;
    
    private OnChangeColorListener listener;
    
    public ColorRect(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        colorGradient = new GradientDrawable();
        colorGradient.setColors(new int[] {0xFFFFFFFF, 0xFFFF0000});
        colorGradient.setOrientation(GradientDrawable.Orientation.LEFT_RIGHT);
        
        shadeGradient = new GradientDrawable();
        shadeGradient.setColors(new int[] {0x00000000, 0xFF000000});
        shadeGradient.setOrientation(GradientDrawable.Orientation.TOP_BOTTOM);
        
        thumbPaint = new Paint();
        thumbPaint.setColor(Color.WHITE);
        thumbPaint.setStyle(Paint.Style.STROKE);
        thumbPaint.setStrokeWidth(8);
        thumbPaint.setAntiAlias(true);
        
        thumbRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 4, context.getResources().getDisplayMetrics());
        thumbPosition = new PointF(0, 0);
        thumbColor = Color.WHITE;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        
        colorGradient.setBounds(0, 0, w, h);
        shadeGradient.setBounds(0, 0, w, h);
    }
    

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_MOVE: {
                thumbPosition.x = clamp(event.getX(), 0, getWidth()-1);
                thumbPosition.y = clamp(event.getY(), 0, getHeight()-1);
                updateColorWithThumb();
                invalidate();
            }
        }
        
        return true;
    }

    
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        
        colorGradient.draw(canvas);
        shadeGradient.draw(canvas);
        
        canvas.drawCircle(thumbPosition.x, thumbPosition.y, thumbRadius, thumbPaint);
    }
    
    
    public void setInitialColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hue = hsv[0];
        saturation = hsv[1];
        value = hsv[2];
        updateGradient();
        
        post(new Runnable() {
            @Override
            public void run() {
                updateThumbWithColor();
                invalidate();
            }
        });
    }
    
    
    public void setHue(float hue) {
        this.hue = hue;
        updateGradient();
        invalidate();
        listener.onChange(hsvToColor());
    }
    
    
    public void setOnChangeColorListener(OnChangeColorListener listener) {
        this.listener = listener;
    }
    
    private float clamp(float value, float min, float max) {
        return Math.min(Math.max(value, min), max);
    }
    
    
    private void updateThumbWithColor() {
        thumbPosition.x = getWidth() * saturation;
        thumbPosition.y = getHeight() * (1f-value);
        updateThumbColor();
    }
    
    
    private void updateColorWithThumb() {
        saturation = (float) thumbPosition.x / getWidth();
        value = 1f - ((float) thumbPosition.y / getHeight());
        updateThumbColor();
        listener.onChange(hsvToColor());
    }
    
    
    private void updateThumbColor() {
        thumbColor = value < 0.5f ? Color.WHITE : Color.BLACK;
        thumbPaint.setColor(thumbColor);
    }
    
    
    private void updateGradient() {
        float hsv[] = {hue, 1, 1};
        colorGradient.setColors(new int[] {Color.WHITE, Color.HSVToColor(hsv)});
    }
    
    
    private int hsvToColor() {
        float[] hsv = {hue, saturation, value};
        return Color.HSVToColor(hsv);
    }
    
    
    public interface OnChangeColorListener {
        public void onChange(int color);
    }
}