package com.stone.sqldemo;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.github.commonlib.utils.LogUtils;

public class App extends Application {
    private static final String TAG = "App";

    @Override
    public void onCreate() {
        Log.d(TAG, "attachBaseContext: " + getApplicationContext());

        super.onCreate();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.d(TAG, "attachBaseContext: 11"+ base);
        LogUtils.init(this);
    }
}
