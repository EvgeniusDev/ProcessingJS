package com.litesoft.processingjs.ui.modalmenu;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.litesoft.processingjs.databinding.ModalMenuItemBinding;
import java.util.List;

public class MenuAdapter extends RecyclerView.Adapter<MenuItemHolder> {
    private LayoutInflater inflater;
    private List<MenuItem> items;
    private OnItemClickListener listener;
    
    public MenuAdapter(Context context, List<MenuItem> items) {
        this.inflater = LayoutInflater.from(context);
        this.items = items;
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public MenuItemHolder onCreateViewHolder(ViewGroup parent, int type) {
        return new MenuItemHolder(ModalMenuItemBinding.inflate(inflater, parent, false).getRoot());
    }

    @Override
    public void onBindViewHolder(MenuItemHolder holder, int pos) {
        var item = items.get(pos);
        holder.icon.setImageDrawable(item.getIcon());
        holder.title.setText(item.getTitle());
        holder.root.setOnClickListener(v -> listener.onClick(item.getItemId()));
    }
    
    public void setOnClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
    
    public interface OnItemClickListener {
        public void onClick(int id);
    }
}
