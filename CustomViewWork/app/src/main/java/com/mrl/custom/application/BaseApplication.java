package com.mrl.custom.application;

import android.app.Application;
import android.content.Context;

public class BaseApplication extends Application {

    private static BaseApplication mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
    }

    public static Context getContext() {
        return BaseApplication.getApplication().getApplicationContext();
    }


    public static BaseApplication getApplication() {
        return mInstance;
    }

}
