package com.yysp.ecandroid.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

import com.jkframework.config.JKVersion;
import com.jkframework.debug.JKDebug;
import com.yysp.ecandroid.BuildConfig;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.config.ECVersion;
import com.yysp.ecandroid.service.LongRunningService;
import com.yysp.ecandroid.util.OthoerUtil;


public class ECApplication extends Application {

    /**
     * 内存监听对象
     */


    @Override
    public void onCreate() {
        super.onCreate();
        JKDebug.Init(BuildConfig.DEBUG ? 1 : 0, getApplicationContext(), ECConfig.MESSAGE_CENTER);
        OthoerUtil.doOfTaskEnd();




        /*初始化*/
        boolean bInit = JKVersion.CheckVersion();
        if (bInit) {
            ECVersion.UpdateOldVersion();
            JKVersion.SaveVersion();
        }

        Intent intent = new Intent(this, LongRunningService.class);
        startService(intent);

    }

    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
