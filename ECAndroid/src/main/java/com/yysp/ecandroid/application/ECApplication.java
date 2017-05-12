package com.yysp.ecandroid.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.text.TextUtils;

import com.jkframework.config.JKPreferences;
import com.jkframework.config.JKSystem;
import com.jkframework.control.JKToast;
import com.jkframework.debug.JKLog;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.yysp.ecandroid.BuildConfig;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.config.ECVersion;
import com.jkframework.config.JKVersion;
import com.jkframework.debug.JKDebug;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;
import com.yysp.ecandroid.data.bean.EcPostBean;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.service.LongRunningService;
import com.yysp.ecandroid.service.MyPushIntentService;

import java.util.HashSet;
import java.util.Set;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
                JKLog.i("RT", "device:" + deviceToken);

            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });

        mPushAgent.addAlias(JKSystem.GetGUID(), ECConfig.AliasType, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {
                JKLog.i("RT", "uid:" + s);
                postUid(s);
            }
        });


        //自定义消息处理
        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
        Intent intent = new Intent(this, LongRunningService.class);
        startService(intent);

        doSomeThing();
    }

    private void doSomeThing() {


    }


    private void postUid(final String uid) {
        ECNetSend.signUid(uid).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<EcPostBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(EcPostBean ecSignBean) {
            }

            @Override
            public void onError(Throwable e) {
                JKLog.i("RT", "tasks:" + e.getMessage());
            }

            @Override
            public void onComplete() {

            }
        });

    }


    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
