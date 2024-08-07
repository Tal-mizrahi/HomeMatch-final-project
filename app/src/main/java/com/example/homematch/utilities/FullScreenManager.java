package com.example.homematch.utilities;

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

public class FullScreenManager {

    private static Context context;
    private static volatile FullScreenManager instance;

    private FullScreenManager(Context context) {
        this.context = context;
    }


    public static void init(Context context) {
        if (instance == null) {
            instance = new FullScreenManager(context);
        }
    }

    public static FullScreenManager getInstance() {
        return instance;
    }

    public void fullScreen(Window window){
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = window.getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

}


