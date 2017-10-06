package com.yysp.ecandroid.framework.util;

/**
 * Created by zpq on 17-5-15.
 */

public class KConfigInfo {
    /*
     * wechar package name
     */
    public static final String WECHAT_PACKAGE = "com.tencent.mm";
    public static final String WECHAT_USER_LOGIN_INFO = "/data/data/com.tencent.mm/cache/org.chromium.android_webview";
    public static final String WECHAT_LAUNCH = "com.tencent.mm/com.tencent.mm.ui.LauncherUI";
    //框架版本
    public static final String  FRAMEWORK_VERSION = "VersionCode";

    //判断工作完成超时时间(秒)
    public static final int  WAIT_TIMEOUT = 40;

    //判断工作完成情况的间隔(毫秒)
    public static final int INTERVAL_CHECK = 500;

    //等待操作命令多少时间进行工作完成判断(毫秒)
    public static final int WAIT_JOB = 1500;

    //状态机间隔时间(毫秒)
    public static final int WAIT_STATUS = 1000;

    //轮询间隔时间(秒)
    public static final int KEEP_ALIVE = 60;

    //机器类型
    public static final int DEVICE_TYPE = 0;
    //
    public static final int RANDOM_SEED_POS_MIN = -5;
    public static final int RANDOM_SEED_POS_MAX = 5;

    public static final int WECHAT_PAGE_MAIN = 1;
    public static final int WECHAT_PAGE_ADDRESSLIST = 2;
    public static final int WECHAT_PAGE_FIND = 3;
    public static final int WECHAT_PAGE_ME = 4;
}
