package com.yysp.ecandroid.application;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.support.multidex.MultiDex;

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
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.response.AddErrorMsgResponse;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.service.LongRunningService;
import com.yysp.ecandroid.service.MyPushIntentService;
import com.yysp.ecandroid.util.OthoerUtil;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.yysp.ecandroid.config.ECConfig.AliasName;

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
        OthoerUtil.CreatNeedFile();

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

        mPushAgent.addAlias(AliasName, ECConfig.AliasType, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean b, String s) {
                JKLog.i("RT", "uid:" + AliasName);
                postUid(AliasName, AliasName);
            }
        });


        //自定义消息处理
        mPushAgent.setPushIntentServiceClass(MyPushIntentService.class);
        Intent intent = new Intent(this, LongRunningService.class);
        startService(intent);

    }


    private void postUid(String uid, String machineCode) {
        ECNetSend.signUid(uid, machineCode).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DisBean disSignBean) {
                JKLog.i("saas-api_", "sign_do" + disSignBean.getCode() + "/" + disSignBean.getMsg());
            }

            @Override
            public void onError(Throwable e) {
                JKLog.i("saas-api_", "sign_do_erro:" + e.getMessage() + "/" + e.getLocalizedMessage() + "*" + e.toString());
                OthoerUtil.AddErrorMsgUtil(e.getMessage());
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
