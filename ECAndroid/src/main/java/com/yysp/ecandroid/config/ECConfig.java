package com.yysp.ecandroid.config;


import android.content.Context;
import android.telephony.TelephonyManager;


public class ECConfig {
    //任务结束
    public final static int TASK_FINISH = 604;
    //任务失败
    public final static int TASK_Fail = 605;
    //搜索频繁
    public final static int TASK_searMoreErro = 606;
    //成功
    public final static String success = "成功";
    //失败
    public final static String fail = "失败";

    //别名
//    public final static String AliasName = JKSystem.GetGUID(TelephonyManager.PHONE_TYPE_GSM);

    //心跳时间
    public static int hbTimer = 30;

    //卡死超时时间
    public static int WaitCount = 0;

    public final static void CloseScreenOrder(Context context) {//关闭屏幕触摸
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        wm.setTpDisable(0);
    }

    public final static void OpenScreenOrder(Context context) {//打开屏幕触摸
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        wm.setTpDisable(1);
    }
}
