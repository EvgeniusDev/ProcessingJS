package com.litesoft.processingjs.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.litesoft.processingjs.databinding.ItemProjectBinding;
import com.litesoft.processingjs.project.files.ProjectFile;
import com.litesoft.processingjs.R;
import java.util.List;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.Holder> {
    private Context context;
    private LayoutInflater inflater;
    private List<ProjectFile> projects;
    private OnItemClickListener itemClickListener;
    private OnItemMenuClickListener itemMenuClickListener;
    
    public ProjectListAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    
    public void setData(List<ProjectFile> data) {
        projects = data;
    }
    
    public class Holder extends RecyclerView.ViewHolder {
        View root;
        TextView title;
        
        public Holder(ItemProjectBinding binding) {
            super(binding.getRoot());
            root = binding.getRoot();
            title = binding.title;
        }
    }

    @Override
    public int getItemCount() {
        return projects == null ? 0 : projects.size();
    }

    @Override
    public ProjectListAdapter.Holder onCreateViewHolder(ViewGroup parent, int type) {
        return new Holder(ItemProjectBinding.inflate(inflater, parent, false));
    }
    
    @Override
    public void onBindViewHolder(ProjectListAdapter.Holder holder, int pos) {
        var project = projects.get(pos);
        holder.title.setText(project.getName());
        holder.root.setOnClickListener(v -> itemClickListener.onClick(pos));
        holder.root.setOnLongClickListener(v -> {
            showMenu(pos, holder.root);
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
    
    public void setOnItemClickListener(OnItemClickListener listener) {
        itemClickListener = listener;
    }
    
    public void setOnItemMenuClickListener(OnItemMenuClickListener listener) {
        itemMenuClickListener = listener;
    }
    
    public interface OnItemClickListener {
        public void onClick(int pos);
    }
    
    public interface OnItemMenuClickListener {
        public void onItemClick(int itemId, int pos);
    }
}
