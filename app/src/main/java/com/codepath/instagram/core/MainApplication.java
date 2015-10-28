package com.codepath.instagram.core;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

public class MainApplication extends Application {
    private static final String TAG = "MainApplication";
    private static MainApplication instance;

    public static MainApplication sharedApplication() {
        assert(instance != null);
        return instance;
    }

    @Override
    public void onCreate() {
        instance = this;
        Fresco.initialize(this);
        super.onCreate();
    }
}
