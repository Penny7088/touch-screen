package com.yysp.ecandroid.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.multidex.MultiDex;
import android.support.v7.text.AllCapsTransformationMethod;
import android.text.TextUtils;
import android.widget.Toast;

import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.control.JKToast;
import com.jkframework.debug.JKLog;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengAdHandler;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.Alias;
import com.umeng.message.entity.UMessage;
import com.yysp.ecandroid.BuildConfig;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.config.ECVersion;
import com.jkframework.config.JKVersion;
import com.jkframework.debug.JKDebug;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yysp.ecandroid.service.LongRunningService;
import com.yysp.ecandroid.service.MyPushIntentService;

public class ECApplication extends Application {

    /**
     * 内存监听对象
     */
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        ECApplication application = (ECApplication) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        JKDebug.Init(BuildConfig.DEBUG ? 1 : 0, getApplicationContext(), ECConfig.MESSAGE_CENTER);

        /*内存泄漏检测*/
        refWatcher = BuildConfig.DEBUG ? LeakCanary.install(this) : RefWatcher.DISABLED;

        /*初始化*/
        boolean bInit = JKVersion.CheckVersion();
        if (bInit) {
            ECVersion.UpdateOldVersion();
            JKVersion.SaveVersion();
        }

        /*腾讯bugly*/
//        if (!BuildConfig.DEBUG)
//            QQBugly.GetInstance();

         /* 友盟推送sdk*/
        final PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(final String deviceToken) {
                //注册成功会返回device token
                JKLog.i("token:" + deviceToken);
                mPushAgent.addAlias(deviceToken, "deviceToken", new UTrack.ICallBack() {
                    @Override
                    public void onMessage(boolean b, String s) {
                        JKLog.i("设置别名:" + deviceToken);
                        if (JKPreferences.GetSharePersistentString("deviceToken").equals("") && TextUtils.isEmpty(JKPreferences.GetSharePersistentString("deviceToken"))) {
                            JKPreferences.SaveSharePersistent("deviceToken", deviceToken);
                        }
                    }
                });
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });


        //自定义消息处理
        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);

    }


    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
