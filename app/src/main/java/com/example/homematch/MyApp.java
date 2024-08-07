package com.example.homematch;

import android.app.Application;

import com.example.homematch.utilities.FullScreenManager;
import com.example.homematch.utilities.MyDbDataManager;
import com.example.homematch.utilities.MyDbStorageManager;
import com.example.homematch.utilities.MyDbUserManager;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FullScreenManager.init(this);
        MyDbDataManager.init(this);
        MyDbUserManager.init(this);
        MyDbStorageManager.init(this);

    }
}