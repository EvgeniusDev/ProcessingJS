package com.litesoft.processingjs.editor.tools.colorpicker;

import android.widget.SeekBar;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.Color;

public class HueSlider extends SeekBar {
    private GradientDrawable thumb;
    private OnHueChangeListener listener;
    
    public HueSlider(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        LayerDrawable layer = (LayerDrawable) getProgressDrawable();
        GradientDrawable gd = (GradientDrawable) layer.getDrawable(0);
        gd.setColors(new int[] {Color.RED, Color.YELLOW, Color.GREEN, Color.CYAN, Color.BLUE, Color.MAGENTA, Color.RED});
        
        LayerDrawable thumbLayer = (LayerDrawable) getThumb();
        thumb = (GradientDrawable) thumbLayer.getDrawable(2);
        thumb.setColor(Color.RED);
        
        setMax(359);
        
        setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
            
            @Override
            public void onProgressChanged(SeekBar p1, int p2, boolean user) {
                if (user) {
                    updateThumbColor();
                    listener.onChange(getHue());
                } else {
                    updateThumbColor();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar p1) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar p1) {
            }
        });
    }
    
    public void setInitialColor(int color) {
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        setProgress((int) hsv[0]);
    }
    
    private void updateThumbColor() {
        float[] hsv = new float[3];
        hsv[1] = 1f;
        hsv[2] = 1f;
        hsv[0] = getHue();
        thumb.setColor(Color.HSVToColor(hsv));
        thumb.invalidateSelf();
    }
    
    private float getHue() {
        return getProgress();
    }
    
    public void setOnHueChangeListener(OnHueChangeListener listener) {
        this.listener = listener;
    }
    
    public interface OnHueChangeListener {
        public void onChange(float hue);
    }
}