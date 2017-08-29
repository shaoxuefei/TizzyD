package com.shanlin.sxf.application;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;

/**
 * Created by Sxf on 2017/8/29.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(getApplicationContext());
    }
}
