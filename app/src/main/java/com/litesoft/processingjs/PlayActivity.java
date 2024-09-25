package com.litesoft.processingjs;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import androidx.appcompat.app.AppCompatActivity;
import com.litesoft.processingjs.databinding.ActivityPlayBinding;

public class PlayActivity extends AppCompatActivity {
    public static final String EXTRA_URL = "url";
    
    private ActivityPlayBinding binding;
    private WebView webView;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        binding = ActivityPlayBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        webView = binding.webview;
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        webView.setRendererPriorityPolicy(WebView.RENDERER_PRIORITY_IMPORTANT, true);
        webView.setKeepScreenOn(true);
        webView.setBackgroundColor(Color.BLACK);
        
        webView.getSettings().setAllowFileAccess(true);
        webView.getSettings().setSupportZoom(false);
        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.getSettings().setDomStorageEnabled(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setAllowUniversalAccessFromFileURLs(true);
        
        webView.loadUrl(getIntent().getStringExtra(EXTRA_URL));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        
        webView.clearHistory();
        webView.clearCache(true);
    }
}
