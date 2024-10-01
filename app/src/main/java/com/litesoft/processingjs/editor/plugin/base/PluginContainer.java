package com.litesoft.processingjs.editor.plugin.base;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.litesoft.processingjs.editor.widget.CodeEditor;

public class PluginContainer {
    public static final String TAG = "PluginContainer";
    
    private final CodeEditor editor;
    private final List<Plugin> plugins = new ArrayList<>();
    
    public PluginContainer(CodeEditor editor) {
        this.editor = editor;
    }
    
    public void attachPlugin(Class<? extends Plugin> clazz) {
        try {
            Plugin plugin = clazz.getConstructor().newInstance();
            plugins.add(plugin);
            plugin.editor = editor;
            plugin.attach();
            Log.d(TAG, plugin.getName() + " has been attached");
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void detachPlugin(Class<? extends Plugin> clazz) {
        Plugin plugin = getPlugin(clazz);
        plugin.detach();
        Log.d(TAG, plugin.getName() + " has been detached");
    }


    public Plugin getPlugin(Class<? extends Plugin> clazz) {
        for (Plugin plugin : plugins) {
            if (plugin.getClass() == clazz) {
                return plugin;
            }
        }

        throw new IllegalArgumentException("Not found plugin " + clazz.getSimpleName());
    }
    
    
    public List<Plugin> getPlugins() {
        return plugins;
    }
}
