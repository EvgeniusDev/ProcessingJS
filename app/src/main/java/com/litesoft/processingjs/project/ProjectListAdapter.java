package com.litesoft.processingjs.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.litesoft.processingjs.databinding.ItemProjectBinding;
import com.litesoft.processingjs.project.files.ProjectFile;
import java.util.List;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.Holder> {
    private LayoutInflater inflater;
    private List<ProjectFile> projects;
    private ItemTouchListener listener;
    
    public ProjectListAdapter(Context context, ItemTouchListener listener) {
        this.inflater = LayoutInflater.from(context);
        this.listener = listener;
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
        holder.root.setOnClickListener(v -> listener.onClick(pos));
        holder.title.setText(project.getName());
    }
    
    public interface ItemTouchListener {
        public void onClick(int pos);
    }
}
