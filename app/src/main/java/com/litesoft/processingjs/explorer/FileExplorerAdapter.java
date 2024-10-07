package com.litesoft.processingjs.explorer;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.color.MaterialColors;
import com.litesoft.processingjs.databinding.ItemFileExplorerBinding;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.litesoft.processingjs.R;

public class FileExplorerAdapter extends RecyclerView.Adapter<FileExplorerAdapter.Holder> {
    public static final int EMPTY_ELEMENT = -1;
    
    private Context context;
    private LayoutInflater inflater;
    private List<ExplorerElement> elements;
    private OnItemClickListener itemClickListener;
    private OnItemMenuClickListener itemMenuClickListener;
    private FileIconProvider iconProvider;
    
    public FileExplorerAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.elements = new ArrayList<>();
        
        iconProvider = new FileIconProvider(context);
    }
    
    public void setElements(List<ExplorerElement> elements) {
        this.elements.clear();
        this.elements.addAll(elements);
        this.elements.add(0, new EmptyElement());
    }
    
    public class Holder extends RecyclerView.ViewHolder {
        View root;
        TextView name;
        ImageView icon;
        ColorStateList color;
        
        public Holder(ItemFileExplorerBinding binding) {
            super(binding.getRoot());
            root = binding.getRoot();
            name = binding.filename;
            icon = binding.icon;
            color = icon.getImageTintList();
        }
    }
    
    @Override
    public int getItemCount() {
        return elements == null ? 0 : elements.size();
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int type) {
        return new Holder(ItemFileExplorerBinding.inflate(inflater, parent, false));
    }
    
    @Override
    public void onBindViewHolder(Holder holder, int pos) {
        var element = elements.get(pos);
        var drawable = iconProvider.getIconForElement(element);
        holder.icon.setImageDrawable(drawable);
        
        if (drawable instanceof BitmapDrawable) {
            holder.icon.setImageTintList(null);
        } else {
            holder.icon.setImageTintList(holder.color);
        }
        
        if (element instanceof EmptyElement) {
            holder.name.setText("...");
            holder.root.setOnClickListener(v -> itemClickListener.onClick(EMPTY_ELEMENT));
            return;
        }
        
        File file = element.getFile();
        holder.name.setText(file.getName());
        holder.root.setOnClickListener(v -> itemClickListener.onClick(pos-1));
        holder.root.setOnLongClickListener(v -> {
            showMenu(pos-1, holder.root);
            return true;
        });
    }
    
    private void showMenu(int pos, View anchor) {
        var menu = new PopupMenu(context, anchor);
        menu.inflate(R.menu.menu_file_explorer);
        menu.setOnMenuItemClickListener(item -> {
            itemMenuClickListener.onItemClick(item.getItemId(), pos);
            return true;
        });
        
        menu.show();
    }
    
    
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }
    
    public void setOnItemMenuClickListener(OnItemMenuClickListener itemMenuClickListener) {
        this.itemMenuClickListener = itemMenuClickListener;
    }
    
    public interface OnItemClickListener {
        public void onClick(int pos);
    }
    
    public interface OnItemMenuClickListener {
        public void onItemClick(int id, int pos);
    }
    
    private class EmptyElement extends ExplorerElement {
        EmptyElement() {
            super(null);
        }
    }
}