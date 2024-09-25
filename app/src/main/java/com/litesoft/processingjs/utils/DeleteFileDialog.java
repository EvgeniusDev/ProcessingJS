package com.litesoft.processingjs.utils;

import android.content.Context;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

public class DeleteFileDialog {
    
    public DeleteFileDialog(Context context, OnYesClickListener listener) {
        new MaterialAlertDialogBuilder(context)
        .setTitle("Удаление файла")
        .setMessage("Вы уверены, что хотите удалить файл?")
        .setNegativeButton("Нет", null)
        .setPositiveButton("Да", (d, w) -> {
            listener.onYesClick();
        })
        .create()
        .show();
    }
    
    public interface OnYesClickListener {
        public void onYesClick();
    }
}