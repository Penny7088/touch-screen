package com.yysp.ecandroid.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.yysp.ecandroid.service.LongRunningService;
import com.yysp.ecandroid.util.ContactUtil;


public class ECApplication extends Application {

    /**
     * 内存监听对象
     */


    @Override
    public void onCreate() {
        super.onCreate();
        ContactUtil.isTasking = false;

        Intent intent = new Intent(this, LongRunningService.class);
        startService(intent);

    }

    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
