package com.litesoft.processingjs.editor.fragment;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import java.util.List;

public class CodeFragmentAdapter extends FragmentStateAdapter {
    private List<CodeEditorFragment> fragments;
    private long baseId = 0;
    
    public CodeFragmentAdapter(FragmentManager manager, Lifecycle lifecycle) {
        super(manager, lifecycle);
    }
    
    public void setFragments(List<CodeEditorFragment> fragments) {
        this.fragments = fragments;
    }
    
    @Override
    public Fragment createFragment(int pos) {
        return fragments.get(pos);
    }
    
    @Override
    public int getItemCount() {
        return fragments.size();
    }
    
    @Override
    public long getItemId(int pos) {
        return baseId + pos;
    }
    
    public void notifyOnItemChanged(int pos) {
        baseId = getItemCount() + pos;
    }
}