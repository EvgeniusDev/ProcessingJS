package com.litesoft.processingjs.ui.modalmenu;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.litesoft.processingjs.R;

public class MenuItemHolder extends RecyclerView.ViewHolder {
    public View root;
    public ImageView icon;
    public TextView title;
    
    public MenuItemHolder(View root) {
        super(root);
        this.root = root;
        this.icon = root.findViewById(R.id.icon);
        this.title = root.findViewById(R.id.title);
    }
}