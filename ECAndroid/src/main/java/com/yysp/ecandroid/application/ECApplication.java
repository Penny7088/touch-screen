package com.yysp.ecandroid.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.yysp.ecandroid.service.LongRunningService;
import com.yysp.ecandroid.framework.util.ContactUtil;


public class ECApplication extends MultiDexApplication {

    private static ECApplication sApplication;

    private static Context sContext;

    /**
     * 内存监听对象
     */


    @Override
    public void onCreate() {
        super.onCreate();
        ContactUtil.isTasking = false;
        App.getInstance().initApplication(this);
        Intent intent = new Intent(this, LongRunningService.class);
        startService(intent);

    }

    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

    public static Context getContext() {
        return sContext;
    }

    public static Application getApplication() {
        return sApplication;
    }
}
