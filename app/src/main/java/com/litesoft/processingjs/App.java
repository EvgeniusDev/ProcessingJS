package com.litesoft.processingjs;

import android.app.Application;

public class App extends Application {
    static App app;
    
    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
        CrashHandler.init(this);
    }
    
    public static App getInstance() {
        return app;
    }
}