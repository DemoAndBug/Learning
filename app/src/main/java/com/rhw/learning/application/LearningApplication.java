package com.rhw.learning.application;

import android.app.Application;
import android.content.Context;

import cn.jpush.android.api.JPushInterface;

/**
 * Author:renhongwei
 * Date:2017/11/23 on 14:51
 * 1、整个程序的入口 2、初始化工作内容  3、为整个应用的其他模块提供上下文
 */
public class LearningApplication extends Application {

    protected static LearningApplication instance;

    public static LearningApplication getInstance() {
        return instance;
    }

    public static Context getContext() {
        return getInstance().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        initJpush(this);
    }

    /**
     * 初始化Jpush
     * @param context
     */
    private void   initJpush(Context  context){
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
