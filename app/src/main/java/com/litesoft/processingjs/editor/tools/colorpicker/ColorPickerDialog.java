package com.litesoft.processingjs.editor.tools.colorpicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.res.ColorStateList;
import android.view.LayoutInflater;
import android.view.View;
import androidx.appcompat.app.AlertDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.litesoft.processingjs.databinding.DialogColorpickerBinding;

public class ColorPickerDialog {
    private Context context;
    private DialogColorpickerBinding binding;
    private AlertDialog dialog;
    
    private ColorRect colorRect;
    private HueSlider hueSlider;
    private View colorPreview;
    private int currentColor;
    
    public ColorPickerDialog(Context context) {
        this.context = context;
        
        binding = DialogColorpickerBinding.inflate(LayoutInflater.from(context));
        colorRect = binding.colorRect;
        hueSlider = binding.hueSlider;
        colorPreview = binding.colorPreview;
        
        colorRect.setOnChangeColorListener(color -> {
            currentColor = color;
            colorPreview.setBackgroundTintList(ColorStateList.valueOf(color));
        });
        
        hueSlider.setOnHueChangeListener(hue -> {
            colorRect.setHue(hue);
        });
        
        dialog = new MaterialAlertDialogBuilder(context)
        .setTitle("Выбор цвета")
        .setView(binding.getRoot())
        .setNegativeButton("Назад", null)
        .setPositiveButton("Копировать", (d, w) -> {
            copy();
        })
        .create();
        dialog.show();
    }
    
    private void copy() {
        ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("hex", "\"#" + Integer.toHexString(currentColor) + "\"");
        clipboard.setPrimaryClip(clip);
    }
}