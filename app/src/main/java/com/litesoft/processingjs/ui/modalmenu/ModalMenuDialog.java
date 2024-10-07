package com.litesoft.processingjs.ui.modalmenu;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.litesoft.processingjs.databinding.ModalMenuBinding;
import java.util.ArrayList;
import java.util.List;

public class ModalMenuDialog extends BottomSheetDialogFragment {
    private int menuId;
    private List<MenuItem> items;
    private MenuAdapter adapter;
    private RecyclerView recycler;
    private ModalMenuBinding binding;
    private OnItemClickListener listener;
    
    public ModalMenuDialog(int menuId, OnItemClickListener listener) {
        this.menuId = menuId;
        this.listener = listener;
        
        items = new ArrayList<>();
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle args) {
        binding = ModalMenuBinding.inflate(inflater, parent, false);
        return binding.getRoot();
    }
    
    @Override
    public void onViewCreated(View view, Bundle args) {
        super.onViewCreated(view, args);
        
        PopupMenu popup = new PopupMenu(getContext(), view);
        popup.inflate(menuId);
        
        var menu = popup.getMenu();
        
        for (int i=0; i<menu.size(); i++) {
            items.add(menu.getItem(i));
        }
        
        recycler = binding.menuRecycler;
        recycler.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        adapter = new MenuAdapter(getContext(), items);
        adapter.setOnClickListener(id -> listener.onClick(id));
        recycler.setAdapter(adapter);
    }
    
    public interface OnItemClickListener {
        public void onClick(int itemId);
    }
}