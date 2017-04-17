package com.yysp.ecandroid.application;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.jkframework.debug.JKLog;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.yysp.ecandroid.BuildConfig;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.config.ECVersion;
import com.jkframework.config.JKVersion;
import com.jkframework.debug.JKDebug;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

public class ECApplication extends Application {

    /**内存监听对象*/
    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        ECApplication application = (ECApplication) context.getApplicationContext();
        return application.refWatcher;
    }

	@Override
	public void onCreate()
	{
		super.onCreate();
        JKDebug.Init(BuildConfig.DEBUG ? 1 : 0,getApplicationContext(), ECConfig.MESSAGE_CENTER);

        /*内存泄漏检测*/
        refWatcher = BuildConfig.DEBUG ? LeakCanary.install(this) : RefWatcher.DISABLED;

        /*初始化*/
		boolean bInit = JKVersion.CheckVersion();
		if (bInit)
		{
			ECVersion.UpdateOldVersion();
			JKVersion.SaveVersion();
		}

        /*腾讯bugly*/
//        if (!BuildConfig.DEBUG)
//            QQBugly.GetInstance();
         /* 友盟推送sdk*/
        PushAgent mPushAgent = PushAgent.getInstance(this);
        mPushAgent.register(new IUmengRegisterCallback() {

            @Override
            public void onSuccess(String deviceToken) {
                //注册成功会返回device token
                JKLog.i("token:"+deviceToken);
            }

            @Override
            public void onFailure(String s, String s1) {

            }
        });
	}

    @Override
    protected void attachBaseContext(final Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }
}
