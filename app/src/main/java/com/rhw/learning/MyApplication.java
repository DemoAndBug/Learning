package com.rhw.learning;

import android.app.Application;
import android.content.Context;

/**
 * Author:renhongwei
 * Date:2017/11/23 on 14:51
 */
public class MyApplication extends Application {

    protected static MyApplication instance;

    public static MyApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return getInstance().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

}
