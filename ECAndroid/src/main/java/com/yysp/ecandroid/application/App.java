package com.yysp.ecandroid.application;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.StringRes;

import com.yysp.ecandroid.framework.crashMonitor.CrashMonitor;

import java.util.Random;


/**
 * 应用程序对象.
 *
 * @author penny
 */
public class App {

    /**
     * 主线程的Handler对象 *
     */
    private Handler mHandler = new Handler(Looper.getMainLooper());
    //    private  AppManager appManager;
    private Context appContext;

    private App() {
    }

    /**
     * 得到App的单例对象
     *
     * @return
     */
    public static App getInstance() {
        return Singleton.INSTANCE;
    }

    /**
     * 当前是否是Debug模式
     *
     * @return
     */
    public boolean isDebug() {
//        return BuildConfig.DEBUG;
        return true;
    }

    /**
     * 得到全局上下文对象
     *
     * @return
     */
    public Context getContext() {
        return ECApplication.getContext();
    }

    public Application getApplication() {
        return ECApplication.getApplication();
    }

    /**
     * 得到全局Handler对象
     *
     * @return
     */
    public Handler getHandler() {
        return mHandler;
    }

    /**
     * 得到全局资源类
     *
     * @return
     */

    public Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 得到String
     *
     * @param stringId stringId
     * @return String
     */
    public String getString(@StringRes int stringId) {
        return getResources().getString(stringId);
    }

    private Random mRandom = new Random();

    /**
     * 全局随机数生成器
     *
     * @return
     */
    public Random getRandom() {
        return mRandom;
    }

    // 记录是否已经初始化
    private boolean isInit = false;

    /**
     * 初始化Application需要的组件
     */
    public void initApplication(Context context) {
        //crash
        CrashMonitor.init(context,isDebug());
    }


    private static final class Singleton {
        private static final App INSTANCE = new App();
    }

    //版本名
    public String getVersionName() {
        return getPackageInfo().versionName;
    }

    //版本号
    public int getVersionCode() {
        return getPackageInfo().versionCode;
    }

    private PackageInfo getPackageInfo() {
        PackageInfo pi = null;
        try {
            PackageManager pm = ECApplication.getApplication().getPackageManager();
            pi = pm.getPackageInfo(ECApplication.getApplication().getPackageName(),
                    PackageManager.GET_CONFIGURATIONS);
            return pi;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pi;
    }

}
