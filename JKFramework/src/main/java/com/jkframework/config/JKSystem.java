package com.jkframework.config;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.PendingIntent;
import android.app.PendingIntent.CanceledException;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Debug;
import android.os.Environment;
import android.os.Looper;
import android.provider.Settings;
import android.provider.Settings.SettingNotFoundException;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;

import com.jkframework.activity.JKBaseActivity;
import com.jkframework.algorithm.JKAnalysis;
import com.jkframework.algorithm.JKConvert;
import com.jkframework.algorithm.JKFile;
import com.jkframework.bean.JKDeviceType;
import com.jkframework.callback.JKBatteryLinstener;
import com.jkframework.debug.JKDebug;
import com.jkframework.debug.JKLog;
import com.jkframework.manager.JKActivityManager;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.Locale;

import cn.trinea.android.common.util.PackageUtils;
import cn.trinea.android.common.util.ShellUtils;


public class JKSystem {
    /**
     * 照相机对象
     */
    private static Camera cmCamera = null;
    /**
     * Http回调监听
     */
    private static JKBatteryLinstener mBattery;

    /**
     * 获取屏幕可显示宽高(包括状态栏,不包括虚拟按钮)
     *
     * @param nOrientation 横屏为0,竖屏为1
     * @return 返回x为宽y为高
     */
    public static Point GetScreenOrientation(int nOrientation) {
        int nWidth = JKDebug.hContext.getResources().getDisplayMetrics().widthPixels;
        int nHeight = JKDebug.hContext.getResources().getDisplayMetrics().heightPixels;
        Point ptScreen = new Point();
        if (0 == nOrientation) {
            ptScreen.x = Math.max(nWidth, nHeight);
            ptScreen.y = Math.min(nWidth, nHeight);
        } else {
            ptScreen.x = Math.min(nWidth, nHeight);
            ptScreen.y = Math.max(nWidth, nHeight);
        }
        return ptScreen;
    }

    /**
     * 获取布局正确的宽高
     *
     * @param vgFrame      布局对象
     * @param nOrientation 横屏为0,竖屏为1
     * @return 返回x为宽y为高
     */
    public static Point GetLayoutOrientation(ViewGroup vgFrame, int nOrientation) {
        int nWidth = vgFrame.getWidth();
        int nHeight = vgFrame.getHeight();
        Point ptScreen = new Point();
        if (0 == nOrientation) {
            ptScreen.x = Math.max(nWidth, nHeight);
            ptScreen.y = Math.min(nWidth, nHeight);
        } else {
            ptScreen.x = Math.min(nWidth, nHeight);
            ptScreen.y = Math.max(nWidth, nHeight);
        }
        return ptScreen;
    }

    /**
     * 获取测量布局宽高
     *
     * @param child 布局对象
     * @return 返回x为宽y为高
     */
    public static Point GetMeasureView(LinearLayout child) {
        ViewGroup.LayoutParams p = child.getLayoutParams();
        if (p == null) {
            p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0, p.width);
        int lpHeight = p.height;
        int childHeightSpec;
        if (lpHeight > 0) {
            childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
        } else {
            childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
        }
        child.measure(childWidthSpec, childHeightSpec);
        Point ptPoint = new Point();
        ptPoint.x = child.getMeasuredWidth();
        ptPoint.y = child.getMeasuredHeight();
        return ptPoint;
    }

    /**
     * 获取屏幕密度
     *
     * @return 屏幕密度
     */
    public static float GetDensity() {
        return JKDebug.hContext.getResources().getDisplayMetrics().density;
    }

    /**
     * 获取字体缩放比例
     *
     * @return 字体缩放比例
     */
    public static float GetFontSize() {
        return JKDebug.hContext.getResources().getConfiguration().fontScale;
    }

    /**
     * 设置默认的字体大小缩放设置
     */
    public static void SetDefaultFontSize() {
        Resources res = JKDebug.hContext.getResources();
        Configuration config = new Configuration();
        config.setToDefaults();
        res.updateConfiguration(config, res.getDisplayMetrics());
    }

    /**
     * 设置弱显示虚拟按钮
     */
    public static void SetLowVirtualButton() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null) {
            WindowManager.LayoutParams params = CurrentActivity.getWindow().getAttributes();
            params.systemUiVisibility = View.SYSTEM_UI_FLAG_LOW_PROFILE;
            CurrentActivity.getWindow().setAttributes(params);
        }
    }

    /**
     * 获取留黑边最佳适配比例
     *
     * @param nScreenWidth  屏幕宽度
     * @param nScreenHeight 屏幕高度
     * @param nTargetWidth  布局宽度
     * @param nTargetHeight 布局高度
     * @return 返回最佳比例
     */
    public static float GetBestScale(int nScreenWidth, int nScreenHeight, int nTargetWidth, int nTargetHeight) {
        float fScaleX = (float) nScreenWidth / nTargetWidth;
        float fScaleY = (float) nScreenHeight / nTargetHeight;
        float fScale;
        if (fScaleX < fScaleY)
            fScale = fScaleX;
        else
            fScale = fScaleY;
        return fScale;
    }

    /**
     * 获取手机当前状态栏高度
     *
     * @return > 0 success; <= 0 fail
     */
    public static int GetCurrentStatusBarHeight() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null) {
            WindowManager.LayoutParams attrs = CurrentActivity.getWindow().getAttributes();
            if ((attrs.flags & WindowManager.LayoutParams.FLAG_FULLSCREEN) == WindowManager.LayoutParams.FLAG_FULLSCREEN) {
                return 0;
            } else {
                return GetStatusBarHeight();
            }
        }
        return 0;
    }

    /**
     * 获取手机当前虚拟按钮高度
     *
     * @return > 0 success; <= 0 fail
     */
    public static int GetCurrentNavigationBarHeight() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null) {
            SystemBarTintManager tintManager = new SystemBarTintManager(CurrentActivity);
            if (tintManager.getConfig().hasNavigtionBar())
                return tintManager.getConfig().getNavigationBarHeight();
            else
                return 0;
        }
        return 0;
    }

    /**
     * 获取手机状态栏高度
     *
     * @return > 0 success; <= 0 fail
     */
    public static int GetStatusBarHeight() {
        int nStatusHeight = 0;
        Rect rRect = new Rect();
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null) {
            CurrentActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(rRect);
            nStatusHeight = rRect.top;
        }
        if (0 == nStatusHeight) {
            Class<?> cClass;
            try {
                cClass = Class.forName("com.android.internal.R$dimen");
                Object oObject = cClass.newInstance();
                int nI5 = Integer.parseInt(cClass.getField("status_bar_height").get(oObject).toString());
                nStatusHeight = JKDebug.hContext.getResources().getDimensionPixelSize(nI5);
            } catch (Exception e) {
                SystemBarTintManager tintManager;
                if (CurrentActivity != null) {
                    tintManager = new SystemBarTintManager(CurrentActivity);
                    return tintManager.getConfig().getStatusBarHeight();
                }
            }
        }
        return nStatusHeight;
    }

    /**
     * 获取标题栏高度
     *
     * @return 标题栏高度
     */
    public static int GetTitleBarHeight() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null)
            return CurrentActivity.getWindow().findViewById(Window.ID_ANDROID_CONTENT).getTop();
        return 0;
    }

    /**
     * 获取手机虚拟按钮高度
     *
     * @return > 0 success; <= 0 fail
     */
    public static int GetNavigationBarHeight() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null) {
            SystemBarTintManager tintManager = new SystemBarTintManager(CurrentActivity);
            return tintManager.getConfig().getNavigationBarHeight();
        }
        return 0;
    }

    /**
     * 判断设备是否为平板
     *
     * @return 平板返回true
     */
    public static boolean IsTable() {
        DisplayMetrics myMetrics = new DisplayMetrics();
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null)
            CurrentActivity.getWindowManager().getDefaultDisplay().getMetrics(myMetrics);
        final int dpi = (int) (myMetrics.density * 160);
        final int x = myMetrics.widthPixels;
        final int y = myMetrics.heightPixels;
        boolean bLargSize = (x * x + y * y >= 42 * dpi * dpi);
        try {
            TelephonyManager mgr = (TelephonyManager) JKDebug.hContext.getSystemService(Context.TELEPHONY_SERVICE);
            return bLargSize && (TelephonyManager.SIM_STATE_READY != mgr
                    .getSimState());
        } catch (Exception e) {
            e.printStackTrace();
            return bLargSize;
        }
    }

    /**
     * 判断屏幕是否为自动亮度调解
     *
     * @return 当前为自动亮度调解返回true
     */
    public static boolean IsAutomicBrightness() {
        boolean bAutomicBrightness = false;
        try {
            bAutomicBrightness = Settings.System.getInt(JKDebug.hContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (SettingNotFoundException e) {
            JKLog.ErrorLog("判断屏幕是否为自动亮度调解失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
        return bAutomicBrightness;
    }

    /**
     * 设置屏幕亮度类型
     *
     * @param bAuto true时为自动调解,否则为手工调解
     */
    public static void SetBrightnessType(boolean bAuto) {
        if (bAuto) {
            Settings.System.putInt(JKDebug.hContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
        } else {
            Settings.System.putInt(JKDebug.hContext.getContentResolver(),
                    Settings.System.SCREEN_BRIGHTNESS_MODE,
                    Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
        }
    }

    /**
     * 设置屏幕亮度
     *
     * @param fPercent 屏幕亮度百分比(0~100)
     */
    public static void SetBrightness(float fPercent) {
        if (IsAutomicBrightness())
            SetBrightnessType(false);
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null) {
            final WindowManager.LayoutParams attrs = CurrentActivity.getWindow().getAttributes();
            attrs.screenBrightness = fPercent / 100.0f;
            CurrentActivity.getWindow().setAttributes(attrs);
        }
    }

    /**
     * 获取当前屏幕亮度
     *
     * @return 屏幕当前亮度百分比, 失败为-1
     */
    public static float GetBrightness() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null) {
            float fBrightness = CurrentActivity.getWindow().getAttributes().screenBrightness;
            if (fBrightness < 0) {
                try {
                    fBrightness = android.provider.Settings.System.getInt(
                            CurrentActivity.getContentResolver(), Settings.System.SCREEN_BRIGHTNESS);
                } catch (SettingNotFoundException e) {
                    JKLog.ErrorLog("获取当前屏幕亮度失败.原因为" + e.getMessage());
                    e.printStackTrace();
                    return -1;
                }
                return fBrightness / (float) 255 * 100;
            }
            return fBrightness * 100;
        } else
            return -1;
    }

    /**
     * 获取Wifi状态
     *
     * @return 当前Wifi状态返回true
     */
    public static boolean IsWifi() {
        WifiManager wifiManager = (WifiManager) JKDebug.hContext.getSystemService(Context.WIFI_SERVICE);
        if (wifiManager != null) {
            int nWifiState = wifiManager.getWifiState();
            return nWifiState == WifiManager.WIFI_STATE_ENABLED;
        }
        return false;
    }

    /**
     * 获取当前网络状态
     *
     * @return 当前有网状态返回true
     */
    public static boolean IsNetwork() {
        ConnectivityManager cwjManager = (ConnectivityManager) JKDebug.hContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cwjManager == null)
            return false;
        NetworkInfo info = cwjManager.getActiveNetworkInfo();
        return !(info == null || !info.isAvailable());
    }

    /**
     * 获取当前GPS状态
     *
     * @return 当前有GPS状态返回true
     */
    public static boolean IsGPS() {
        LocationManager locationManager = (LocationManager) JKDebug.hContext.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        return gps || network;

    }

    /**
     * 设置GPS状态
     *
     * @param bOpen true为打开,false为关闭
     * @param bAuto 是否自动设置GPS(false会弹出页面)
     */
    public static void SetGPS(boolean bOpen, boolean bAuto) {
        if (bAuto) {
            Intent GPSIntent = new Intent();
            GPSIntent.setClassName("com.android.settings",
                    "com.android.settings.widget.SettingsAppWidgetProvider");
            GPSIntent.addCategory("android.intent.category.ALTERNATIVE");
            GPSIntent.setData(Uri.parse("custom:3"));
            try {
                PendingIntent.getBroadcast(JKDebug.hContext, 0, GPSIntent, bOpen ? 0 : Intent.FILL_IN_ACTION).send();
            } catch (CanceledException e) {
                e.printStackTrace();
            }
        } else {
            Intent intent = new Intent();
            intent.setAction(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            try {
                JKDebug.hContext.startActivity(intent);
            } catch (ActivityNotFoundException ex) {
                // The Android SDK doc says that the location settings activity
                // may not be found. In that case show the general settings.

                // General settings activity
                intent.setAction(Settings.ACTION_SETTINGS);
                try {
                    JKDebug.hContext.startActivity(intent);
                } catch (Exception e) {
                    JKLog.ErrorLog("打开GPS设置菜单失败.原因为" + e.getMessage());
                }
            }
        }
    }

    /**
     * 设置屏幕旋转方向
     *
     * @param nType 0为竖屏,1为横屏
     */
    public static void SetScreenOrientation(int nType) {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null) {
            if (nType == 0) {
                if (CurrentActivity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                    CurrentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            } else {
                if (CurrentActivity.getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                    CurrentActivity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            }
        }
    }

    /**
     * 保持屏幕常亮
     */
    public static void KeepScreen() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null)
            CurrentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    /**
     * 屏幕全屏
     */
    public static void FullScreen() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null)
            CurrentActivity.requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        SetStatus(false, false);
    }

    /**
     * 设置状态栏状态
     *
     * @param bShow      状态栏是否显示
     * @param bAnimation 是否有动画
     */
    public static void SetStatus(boolean bShow, boolean bAnimation) {
        if (bShow) {
            if (JKDeviceType.Instance() != JKDeviceType.EKEN_M001 && JKDeviceType.Instance() != JKDeviceType.PAN_DIGITAL) {
                JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
                if (CurrentActivity != null)
                    CurrentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            }
            if (bAnimation) {
                if (JKDeviceType.Instance() != JKDeviceType.KINDLE_FIRE_1ST_GENERATION) {
                    JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
                    if (CurrentActivity != null)
                        CurrentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                }
            }
        } else {
            if (JKDeviceType.Instance() != JKDeviceType.EKEN_M001 && JKDeviceType.Instance() != JKDeviceType.PAN_DIGITAL) {
                JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
                if (CurrentActivity != null) {
                    CurrentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
            }
            if (bAnimation) {
                if (JKDeviceType.Instance() != JKDeviceType.KINDLE_FIRE_1ST_GENERATION) {
                    JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
                    if (CurrentActivity != null) {
                        CurrentActivity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    }
                }
            }
        }
    }

    /**
     * 设置状态栏颜色
     *
     * @param nColor 状态栏颜色
     */
    @TargetApi(19)
    public static void SetStatusBarColor(int nColor) {
        if (Build.VERSION.SDK_INT >= 19) {
            JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
            if (CurrentActivity != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    Window win = CurrentActivity.getWindow();
                    WindowManager.LayoutParams winParams = win.getAttributes();
                    final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                    winParams.flags &= ~bits;
                    CurrentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    CurrentActivity.getWindow().setStatusBarColor(nColor);
                }
                SystemBarTintManager tintManager = new SystemBarTintManager(CurrentActivity);
                tintManager.setStatusBarTintEnabled(true);
                tintManager.setStatusBarAlpha(1);
                tintManager.setStatusBarTintColor(nColor);
            }
        }
    }

    /**
     * 设置虚拟按钮颜色
     *
     * @param nColor 虚拟按钮颜色
     */
    @TargetApi(19)
    public static void SetNavigationBarColor(int nColor) {
        if (Build.VERSION.SDK_INT >= 19) {
            JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
            if (CurrentActivity != null) {
                if (Build.VERSION.SDK_INT >= 21) {
                    Window win = CurrentActivity.getWindow();
                    WindowManager.LayoutParams winParams = win.getAttributes();
                    final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                    winParams.flags &= ~bits;
                    CurrentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    CurrentActivity.getWindow().setNavigationBarColor(nColor);
                }
                SystemBarTintManager tintManager = new SystemBarTintManager(CurrentActivity);
                tintManager.setNavigationBarTintEnabled(true);
                tintManager.setNavigationBarAlpha(1);
                tintManager.setNavigationBarTintColor(nColor);
            }
        }
    }

    /**
     * 获取android设备唯一标示
     *
     * @return android唯一标示字符串
     */
    public static String GetGUID() {
        String tAndroidImei = GetAndroidImei();
//        String tAndroidCode = GetAndroidCode()
//        return JKEncryption.MD5_32(tAndroidImei + tAndroidCode);
        return tAndroidImei;
    }

    /**
     * 获取android 机器码
     *
     * @return 获取失败返回"",否则返回机器码
     */
    @SuppressLint("HardwareIds")
    public static String GetAndroidCode() {
        try {
            String tBack = android.provider.Settings.Secure.getString(JKDebug.hContext.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);//手机序列号(机器码)
            if (tBack == null)
                tBack = "";
            return tBack;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取android Imei号
     *
     * @return 获取失败返回"",否则返回Imei号
     */
    @SuppressLint("HardwareIds")
    public static String GetAndroidImei() {
        try {
            TelephonyManager telephonyManager = (TelephonyManager) JKDebug.hContext.getSystemService(Context.TELEPHONY_SERVICE);
            String tBack = telephonyManager.getDeviceId();
            if (tBack == null)
                tBack = "";
            return tBack;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取Mac地址
     *
     * @return Wlan Mac地址
     */
    @SuppressLint("HardwareIds")
    public static String GetMac() {
        try {
            WifiManager wifi = (WifiManager) JKDebug.hContext.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            return JKAnalysis.toUpper(info.getMacAddress()); //获取mac地址
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 设置程序当前语言
     *
     * @param lcLanguage 语言对象
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static void SetLanguage(Locale lcLanguage) {
        Resources resources = JKDebug.hContext.getApplicationContext().getResources();
        Configuration config = resources.getConfiguration();//获得设置对象
        DisplayMetrics dm = resources.getDisplayMetrics();//获得屏幕参数：主要是分辨率，像素等。
        if (GetSDKVersion() >= 17)
            config.setLocale(lcLanguage);
        else
            config.locale = lcLanguage;
        resources.updateConfiguration(config, dm);
    }

    /**
     * 判断SD卡是否可以使用
     *
     * @return 可以使用返回true
     */
    public static boolean IsSDcardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取当前应用的内部版本号
     *
     * @return 应用的内部版本号, 获取失败返回0
     */
    public static int GetVersionCode() {
        return PackageUtils.getAppVersionCode(JKDebug.hContext);
    }

    /**
     * 获取当前应用的外部版本号
     *
     * @return 应用的外部版本号, 获取失败返回""
     */
    public static String GetVersionString() {
        try {
            return JKDebug.hContext.getPackageManager().getPackageInfo(GetPackageName(), 0).versionName;
        } catch (NameNotFoundException e) {
            JKLog.ErrorLog("获取当前应用的外部版本号失败.原因为" + e.getMessage());
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 获取当前应用的包名
     *
     * @return 应用的内部版本号, 获取失败返回-1
     */
    public static String GetPackageName() {
        return JKDebug.hContext.getPackageName();
    }

    /**
     * 获取当前应用的程序名
     *
     * @return 应用程序名
     */
    public static String GetApplicationName() {
        PackageManager packageManager;
        ApplicationInfo applicationInfo;
        try {
            packageManager = JKDebug.hContext.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(GetPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
        return packageManager.getApplicationLabel(applicationInfo).toString();
    }

    /**
     * 获取当前应用的icon资源
     *
     * @return icon资源
     */
    public static Drawable GetApplicationIcon() {
        PackageManager packageManager;
        ApplicationInfo applicationInfo;
        try {
            packageManager = JKDebug.hContext.getPackageManager();
            applicationInfo = packageManager.getApplicationInfo(GetPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            return null;
        }
        return packageManager.getApplicationIcon(applicationInfo);
    }

    /**
     * 判断程序是否为正常的apk安装包
     *
     * @param tPath 程序SD卡路径地址
     * @return true表示正常
     */
    public static boolean IsPackage(String tPath) {
        PackageManager pmManager = JKDebug.hContext.getPackageManager();
        PackageInfo piInfo = pmManager.getPackageArchiveInfo(tPath, PackageManager.GET_ACTIVITIES);
        return piInfo != null;
    }

    /**
     * 安装apk包
     *
     * @param tPath  apk包路径地址
     * @param bQuiet 是否静默安装
     * @return true表示正常安装
     */
    public static boolean InstallPackage(String tPath, boolean bQuiet) {
        String tRun = "chmod 777 " + tPath;
        Runtime tiRun = Runtime.getRuntime();
        try {
            tiRun.exec(tRun);
        } catch (IOException e) {
            JKLog.ErrorLog("修改权限失败.原因为" + e.getMessage());
            e.printStackTrace();
        }
        if (bQuiet)
            return PackageUtils.installSilent(JKDebug.hContext, tPath) == 1;
        else
            return PackageUtils.installNormal(JKDebug.hContext, tPath);
    }

    /**
     * 卸载apk包
     *
     * @param tPackage apk包名
     * @param bQuiet   是否静默安装
     * @return true表示正常卸载
     */
    public static boolean UninstallPackage(String tPackage, boolean bQuiet) {
        if (bQuiet)
            return PackageUtils.uninstallSilent(JKDebug.hContext, tPackage) == 1;
        else
            return PackageUtils.uninstallNormal(JKDebug.hContext, tPackage);
    }

    /**
     * 获取手机电量百分比
     *
     * @return 手机电量
     */
    public static void GetBatteryPer(JKBatteryLinstener l) {
        mBattery = l;
        //注册广播接受者java代码
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        //注册receiver
        JKDebug.hContext.registerReceiver(batteryReceiver, intentFilter);
    }

    /**
     * 检测手机是否Root
     *
     * @return Root返回true, 否则返回false
     */
    public static boolean IsRoot() {
        return ShellUtils.checkRootPermission();
    }

    /**
     * 获取IP地址
     *
     * @return 返回IP地址字符串, 若没有网络返回""
     */
    public static String GetLocalIP() {
        if (IsNetwork()) {
            if (IsWifi()) {
                WifiManager wifiManager = (WifiManager) JKDebug.hContext.getSystemService(Context.WIFI_SERVICE);
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                return ((ipAddress & 0xff) + "." + (ipAddress >> 8 & 0xff) + "."
                        + (ipAddress >> 16 & 0xff) + "." + (ipAddress >> 24 & 0xff));
            } else {
                try {
                    for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                        NetworkInterface intf = en.nextElement();
                        for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                            InetAddress inetAddress = enumIpAddr.nextElement();
                            if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                                return inetAddress.getHostAddress();
                            }
                        }
                    }
                } catch (SocketException ex) {
                    JKLog.ErrorLog("获取GPRS的IP地址失败.失败原因为:" + ex.getMessage());
                }
                return "";
            }
        } else {
            return "";
        }
    }

    /**
     * 打开软键盘
     *
     * @param vView 打开软键盘的视图
     */
    public static void OpenKeyboard(View vView) {
        InputMethodManager imm = (InputMethodManager) JKDebug.hContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(vView, InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }

    /**
     * 关闭软键盘
     *
     * @param vView 关闭软键盘的视图
     */
    public static void CloseKeyboard(View vView) {
        InputMethodManager imm = (InputMethodManager) JKDebug.hContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(vView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 关闭软键盘
     */
    public static void CloseKeyboard() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null) {
            View vView = CurrentActivity.getCurrentFocus();
            if (vView != null) {
                InputMethodManager imm = (InputMethodManager) JKDebug.hContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(vView.getApplicationWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 获取手机号
     *
     * @return 返回手机号码(没有号码返回空字符串)
     */
    public String GetUserNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) JKDebug.hContext.getSystemService(Context.TELEPHONY_SERVICE);
        String tBack = telephonyManager.getLine1Number();
        if (tBack == null)
            tBack = "";
        return tBack;
    }

    /**
     * 打开手电筒
     */
    public static void OpenFlashLight() {
        if (!JKDebug.hContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return;
        }
//		if (GetSDKVersion()<21) {
        if (null == cmCamera) {
            cmCamera = Camera.open();
        }

        Camera.Parameters parameters = cmCamera.getParameters();
        parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
        cmCamera.setParameters(parameters);
        cmCamera.startPreview();
//		}
//		else {
//            try {
//                //获得CameraManager
//                CameraManager cameraManager = (CameraManager) JKDebug.hContext.getSystemService(Context.CAMERA_SERVICE);
//                //获得属性
//                if (cameraManager.getCameraIdList().length > 0) {
//                    CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(cameraManager.getCameraIdList()[0]);
//                    //支持的STREAM CONFIGURATION
//                    StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
//                    //打开相机
//                    cameraManager.openCamera("0", mCameraDeviceStateCallback, mHandler);
//                }
//            } catch (CameraAccessException e) {
//                e.printStackTrace();
//            }
//		}
    }

    /**
     * 关闭手电筒
     */
    public static void CloseFlashLight() {
        if (cmCamera != null) {
            cmCamera.stopPreview();
            cmCamera.release();
            cmCamera = null;
        }
    }

    /**
     * 禁止程序触发锁屏
     */
    public static void DissmissLockScreen() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null)
            CurrentActivity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
    }

    /**
     * 设置程序休眠时间
     *
     * @param nTime 休眠时间毫秒数
     */
    public static void SetSleepTime(int nTime) {
        Settings.System.putInt(JKDebug.hContext.getContentResolver(), android.provider.Settings.System.SCREEN_OFF_TIMEOUT, nTime);
    }

    /**
     * 获取当前正在运行的Activity
     *
     * @return 当前运行的Activity, 获取失败返回null
     */
    public static JKBaseActivity GetCurrentActivity() {
        for (int i = JKActivityManager.getAllActivity().size() - 1; i >= 0; --i) {
            if (JKActivityManager.getAllActivity().get(i).IsRun())
                return JKActivityManager.getAllActivity().get(i);
        }
        return null;
    }

    /**
     * 获取当前程序进程名
     *
     * @return 如果找不到进程则返回""
     */
    public static String GetProcessName() {
        int nPid = android.os.Process.myPid();
        ActivityManager mActivityManager = (ActivityManager) JKDebug.hContext.
                getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo rapiProcess : mActivityManager
                .getRunningAppProcesses()) {
            if (rapiProcess.pid == nPid) {
                return rapiProcess.processName;
            }
        }
        return "";
    }

    /**
     * 获取当前SDK版本
     *
     * @return SDK整型版本
     */
    public static int GetSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取当前程序是否运行
     *
     * @return true表示运行中
     */
    public static boolean IsRunning() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        return CurrentActivity != null && !CurrentActivity.IsBackground();
    }

    /**
     * 当前程序是否正在显示
     *
     * @return true表示为栈顶程序
     */
    public static boolean IsTopProgress() {
        return PackageUtils.isTopActivity(JKDebug.hContext, GetPackageName());
    }

    /**
     * 返回桌面
     */
    public static void OnHome() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null)
            CurrentActivity.moveTaskToBack(true);
    }

    /**
     * 返回应用
     */
    public static void ReturnApp() {
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null) {
            ActivityManager mActivityManager = (ActivityManager) JKDebug.hContext
                    .getSystemService(Context.ACTIVITY_SERVICE);
            try {
                mActivityManager.moveTaskToFront(CurrentActivity.getTaskId(), 0);
            } catch (Exception ignored) {
                Intent itIntent = JKDebug.hContext.getPackageManager().getLaunchIntentForPackage(JKSystem.GetPackageName());
                if (itIntent != null) {
                    itIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                    JKDebug.hContext.startActivity(itIntent);
                }
            }
        } else {
            Intent itIntent = JKDebug.hContext.getPackageManager().getLaunchIntentForPackage(JKSystem.GetPackageName());
            if (itIntent != null) {
                itIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                JKDebug.hContext.startActivity(itIntent);
            }
        }
    }

    /**
     * 用系统浏览器打开网址
     *
     * @param tUrl 网址地址
     */
    public static void OpenBrowser(String tUrl) {
        Uri uri = Uri.parse(tUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null)
            CurrentActivity.startActivity(intent);
    }

    /**
     * 启动应用
     *
     * @param tPackage 应用包名
     */
    public static boolean OpenApk(String tPackage) {
        PackageManager packageManager = JKDebug.hContext.getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(tPackage);
        if (intent == null) {
            return false;
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
        if (CurrentActivity != null)
            CurrentActivity.startActivity(intent);
        return true;
    }

    /**
     * 清除应用所有数据
     */
    public static void CleanData() {
        JKFile.DeleteFile(JKDebug.hContext.getCacheDir().getAbsolutePath());
        JKFile.DeleteFile(JKDebug.hContext.getFilesDir().getAbsolutePath());
        JKFile.DeleteFile(JKDebug.hContext.getFilesDir().getParent() + "/databases");
        JKFile.DeleteFile(JKDebug.hContext.getFilesDir().getParent() + "/shared_prefs");
        File flFile = JKDebug.hContext.getExternalFilesDir(null);
        if (flFile != null) {
            String tFilePath = flFile.getPath();
            JKFile.DeleteFile(tFilePath);
        }
        flFile = JKDebug.hContext.getExternalCacheDir();
        if (flFile != null) {
            String tFilePath = flFile.getPath();
            JKFile.DeleteFile(tFilePath);
        }
    }

    /**
     * 清除应用缓存
     */
    public static void CleanCache() {
        JKFile.DeleteFile(JKDebug.hContext.getCacheDir().getAbsolutePath());
        File flFile = JKDebug.hContext.getExternalCacheDir();
        if (flFile != null) {
            String tFilePath = flFile.getPath();
            JKFile.DeleteFile(tFilePath);
        }
    }

    /**
     * 获取应用内存允许使用空间(单位字节)
     *
     * @return 获取可用的内存空间
     */
    public static long GetMemorySize() {
        return ((ActivityManager) JKDebug.hContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryClass() * 1024 * 1024;
    }

    /**
     * 获取系统内存总大小(单位字节)
     *
     * @return 获取总内存空间
     */
    @TargetApi(16)
    public static long GetSystemMemorySize() {
        if (VERSION.SDK_INT >= 16) {
            MemoryInfo miInfo = new MemoryInfo();
            ((ActivityManager) JKDebug.hContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(miInfo);
            return (int) miInfo.totalMem;
        } else {
            String str1 = "/proc/meminfo";// 系统内存信息文件
            String str2;
            String[] arrayOfString;
            long initial_memory;
            try {
                FileReader localFileReader = new FileReader(str1);
                BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
                str2 = localBufferedReader.readLine();// 读取meminfo第一行，系统总内存大小
                arrayOfString = str2.split("\\s+");
                if (arrayOfString[2].toUpperCase(Locale.getDefault()).equals("KB")) {
                    initial_memory = JKConvert.toLong(arrayOfString[1]) * 1024;
                } else if (arrayOfString[2].toUpperCase(Locale.getDefault()).equals("MB")) {
                    initial_memory = JKConvert.toLong(arrayOfString[1]) * 1024 * 1024;
                } else if (arrayOfString[2].toUpperCase(Locale.getDefault()).equals("GB")) {
                    initial_memory = JKConvert.toLong(arrayOfString[1]) * 1024 * 1024 * 1024;
                } else if (arrayOfString[2].toUpperCase(Locale.getDefault()).equals("TB")) {
                    initial_memory = JKConvert.toLong(arrayOfString[1]) * 1024 * 1024 * 1024 * 1024;
                } else
                    initial_memory = JKConvert.toLong(arrayOfString[1]);
                localBufferedReader.close();
                return initial_memory;
            } catch (IOException e) {
                return 0;
            }
        }
    }

    /**
     * 获取系统内存剩余使用空间(单位字节)
     *
     * @return 获取剩余的内存空间
     */
    public static long GetRemainSystemMemorySize() {
        MemoryInfo miInfo = new MemoryInfo();
        ((ActivityManager) JKDebug.hContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(miInfo);
        return (int) miInfo.availMem;
    }

    /**
     * 获取系统缓存使用大小(单位字节)
     *
     * @return 系统缓存使用大小
     */
    public static long GetCacheSize() {
        String str1 = "/proc/meminfo";// 系统内存信息文件
        String str2;
        String[] arrayOfString;
        long initial_memory;
        try {
            FileReader localFileReader = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(localFileReader, 8192);
            localBufferedReader.readLine();
            localBufferedReader.readLine();
            localBufferedReader.readLine();
            str2 = localBufferedReader.readLine();// 读取meminfo第4行，缓存大小
            arrayOfString = str2.split("\\s+");
            if (arrayOfString[2].toUpperCase(Locale.getDefault()).equals("KB")) {
                initial_memory = JKConvert.toLong(arrayOfString[1]) * 1024;
            } else if (arrayOfString[2].toUpperCase(Locale.getDefault()).equals("MB")) {
                initial_memory = JKConvert.toLong(arrayOfString[1]) * 1024 * 1024;
            } else if (arrayOfString[2].toUpperCase(Locale.getDefault()).equals("GB")) {
                initial_memory = JKConvert.toLong(arrayOfString[1]) * 1024 * 1024 * 1024;
            } else if (arrayOfString[2].toUpperCase(Locale.getDefault()).equals("TB")) {
                initial_memory = JKConvert.toLong(arrayOfString[1]) * 1024 * 1024 * 1024 * 1024;
            } else
                initial_memory = JKConvert.toLong(arrayOfString[1]);
            localBufferedReader.close();
            return initial_memory;
        } catch (IOException e) {
            return 0;
        }
    }

    /**
     * 获取当前应用内存剩余使用空间(单位字节)
     *
     * @return 获取当前应用的内存空间
     */
    public static long GetRemainMemorySize() {
        ActivityManager mActivityManager = (ActivityManager) JKDebug.hContext.getSystemService(Context.ACTIVITY_SERVICE);
        //获得系统里正在运行的所有进程 
        List<RunningAppProcessInfo> runningAppProcessesList = mActivityManager.getRunningAppProcesses();
        for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcessesList) {
            String processName = runningAppProcessInfo.processName;
            if (processName.equals(GetPackageName())) {
                int[] pids = new int[]{runningAppProcessInfo.pid};
                Debug.MemoryInfo[] memoryInfo = mActivityManager.getProcessMemoryInfo(pids);
                return memoryInfo[0].dalvikPrivateDirty * 1024;
            }
        }
        return 0;
    }

    /**
     * 是否低内存状态
     *
     * @return true表示低内存状态
     */
    public static boolean IsLowMemory() {
        MemoryInfo miInfo = new MemoryInfo();
        ((ActivityManager) JKDebug.hContext.getSystemService(Context.ACTIVITY_SERVICE)).getMemoryInfo(miInfo);
        return miInfo.lowMemory;
    }

    /**
     * 查看是否在主线程
     *
     * @return true表示在主线程
     */
    public static boolean IsMainThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * 查看是否为小米系统
     *
     * @return true表示为小米设备
     */
    public static boolean IsMiuiSystem() {
        return Build.MANUFACTURER.equalsIgnoreCase("Xiaomi");
    }

    /**
     * 判断Cpu架构
     *
     * @return 0为32位系统, 1为64位系统
     */
    public static int GetCpuType() {
        /*判断系统*/
        try {
            Class<?> clazz = Class.forName("android.os.SystemProperties");
            Method get = clazz.getMethod("get", String.class, String.class);
            String tValue = (String) (get.invoke(clazz, "ro.product.cpu.abilist64", ""));
            if (tValue != null && tValue.length() > 0)
                return 1;
        } catch (Exception ignored) {

        }
        /*判断Cpu信息*/
        File cpuInfo = new File("/proc/cpuinfo");
        if (cpuInfo.exists()) {
            InputStream inputStream = null;
            BufferedReader bufferedReader = null;
            try {
                inputStream = new FileInputStream(cpuInfo);
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream), 512);
                String line = bufferedReader.readLine();
                if (line != null && line.length() > 0 && line.toLowerCase(Locale.US).contains("arch64")) {
                    return 1;
                }
            } catch (Throwable ignored) {
            } finally {
                try {
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

                try {
                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        /*判断lib版本*/
        File libcFile = new File("/system/lib64/libc.so");
        if (libcFile.exists()) {
            FileInputStream inputStream = null;
            try {
                inputStream = new FileInputStream(libcFile);
                byte[] tempBuffer = new byte[16];
                int count = inputStream.read(tempBuffer, 0, 16);
                if (count == 16) {
                    if (tempBuffer[4] == 2) {
                        return 1;
                    }
                }
            } catch (Throwable ignored) {
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        return 0;
    }

    /**
     * 文本复制到剪切板
     *
     * @param content 复制的文本内容
     */
    public static void Copy(String content) {
        ClipboardManager cmManager = (ClipboardManager) JKDebug.hContext.getSystemService(Context.CLIPBOARD_SERVICE);
        cmManager.setPrimaryClip(ClipData.newPlainText(null, content));
    }

    /**
     * 文本粘贴剪切板内容
     *
     * @return 粘贴内容
     */
    public static String Pause() {
        ClipboardManager cmManager = (ClipboardManager) JKDebug.hContext.getSystemService(Context.CLIPBOARD_SERVICE);
        if (cmManager.hasPrimaryClip()) {
            return cmManager.getPrimaryClip().getItemAt(0).getText().toString();
        } else
            return "";
    }

    /**
     * 将相关文件通知程序扫描
     *
     * @param tPath 文件路径
     */
    public static void AddToScanfile(String tPath) {
        /*通知文件更新*/
        JKDebug.hContext.sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + tPath)));
    }


    private static BroadcastReceiver batteryReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
                //获取当前电量
                int level = intent.getIntExtra("level", 0);
                //电量的总刻度
                int scale = intent.getIntExtra("scale", 100);
                context.unregisterReceiver(batteryReceiver);
                int nBattery = level * 100 / scale;
                if (mBattery != null)
                    mBattery.Receive(nBattery);
            }
        }
    };


}