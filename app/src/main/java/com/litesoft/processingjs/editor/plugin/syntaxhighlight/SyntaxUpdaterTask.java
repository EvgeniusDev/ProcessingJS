package com.litesoft.processingjs.editor.plugin.syntaxhighlight;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.litesoft.processingjs.editor.lang.Lexer;
import com.litesoft.processingjs.editor.lang.SyntaxHighlightResult;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SyntaxUpdaterTask {
    private Handler mainThreadHandler;
    private ExecutorService singleThreadExecutor;
    private Lexer lexer = new Lexer();
    
    public SyntaxUpdaterTask() {
        mainThreadHandler = new Handler(Looper.getMainLooper());
        singleThreadExecutor = Executors.newSingleThreadExecutor();
    }
    
    public void exexute(final String text) {
        singleThreadExecutor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    final List<SyntaxHighlightResult> result = lexer.execute(text);
                    mainThreadHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (!singleThreadExecutor.isShutdown()) {
                                onSuccess(result);
                            }
                        }
                    });
                }
                catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    public void cancel() {
        singleThreadExecutor.shutdown();
    }
    
    public void onSuccess(List<SyntaxHighlightResult> result) {}
}
