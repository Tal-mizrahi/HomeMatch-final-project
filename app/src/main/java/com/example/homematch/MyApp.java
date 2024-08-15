package com.example.homematch;

import android.app.Application;

import com.example.homematch.Utilities.FullScreenManager;
import com.example.homematch.Utilities.MyDbDataManager;
import com.example.homematch.Utilities.MyDbStorageManager;
import com.example.homematch.Utilities.MyDbUserManager;
import com.example.homematch.Utilities.ShowDetailDialogManager;

public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FullScreenManager.init(this);
        MyDbDataManager.init(this);
        MyDbUserManager.init(this);
        MyDbStorageManager.init(this);
        ShowDetailDialogManager.init(this);

    }
}