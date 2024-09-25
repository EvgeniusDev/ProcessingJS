package com.litesoft.processingjs;

import com.google.common.eventbus.EventBus;

public class GlobalEventBus {
    private static final EventBus eventBus = new EventBus();
    
    public static void register(Object object) {
        eventBus.register(object);
    }
    
    public static void unregister(Object object) {
        eventBus.unregister(object);
    }
    
    public static void post(Object event) {
        eventBus.post(event);
    }
}