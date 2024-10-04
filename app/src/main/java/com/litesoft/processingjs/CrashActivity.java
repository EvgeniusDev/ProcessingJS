package com.litesoft.processingjs;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import androidx.annotation.MainThread;
import androidx.appcompat.app.AppCompatActivity;
import com.litesoft.processingjs.databinding.ActivityCrashBinding;

public class CrashActivity extends AppCompatActivity {
    public static final String EXTRA_CRASH_INFO = "crashInfo";
    private String mLog;

    private ActivityCrashBinding binding;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCrashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        mLog = getIntent().getStringExtra(EXTRA_CRASH_INFO);
        binding.stacktrace.setText(mLog);
        
        binding.fab.setOnClickListener(v -> copy());
    }
    
    @Override
    @MainThread
    public void onBackPressed() {
        super.onBackPressed();
        restart();
    }
    
    private void restart() {
        PackageManager pm = getPackageManager();
        Intent intent = pm.getLaunchIntentForPackage(getPackageName());
        if (intent != null) {
            intent.addFlags(
                Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
            );
            startActivity(intent);
        }
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
    
    private void copy() {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        cm.setPrimaryClip(ClipData.newPlainText(getPackageName(), mLog));
    }
}