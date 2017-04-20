package com.yysp.ecandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.MotionEvent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jkframework.config.JKPreferences;
import com.jkframework.debug.JKLog;
import com.yysp.ecandroid.config.EcWeiXinUI;
import com.yysp.ecandroid.data.bean.ECUserBean;
import com.yysp.ecandroid.data.bean.EcWXUserBean;
import com.yysp.ecandroid.util.PerformClickUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/4/19.
 */

@RequiresApi(api = Build.VERSION_CODES.DONUT)
public class HelpService extends AccessibilityService {
    String TAG = "RT";
    int count = 0;
    public boolean isTasking;
    EcWXUserBean wxUserBean;
    //启动页
    public static final String LauncherUI = "com.tencent.mm.ui.LauncherUI";
    //新的朋友
    public static final String FMessageConversationUI = "com.tencent.mm.plugin.subapp.ui.friend.FMessageConversationUI";
    //添加朋友
    public static final String AddMoreFriendsUI = "com.tencent.mm.plugin.subapp.ui.pluginapp.AddMoreFriendsUI";
    //手机联系人
    public static final String AddMoreFriendsByOtherWayUI = "com.tencent.mm.plugin.subapp.ui.pluginapp.AddMoreFriendsByOtherWayUI";
    //查看手机通讯录
    public static final String MobileFriendUI = "com.tencent.mm.ui.bindmobile.MobileFriendUI";
    //详情资料页面
    public static final String ContactInfoUI = "com.tencent.mm.plugin.profile.ui.ContactInfoUI";


    @TargetApi(Build.VERSION_CODES.KITKAT)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int event_type = event.getEventType();
        switch (event_type) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                isTasking = JKPreferences.GetSharePersistentBoolean("doTasking");
                JKLog.i(TAG, "doTasking:" + isTasking);
                String ActivityName = event.getClassName().toString();
                //是否正在执行任务
                if (isTasking) {
                    //对应页面对应事件
                    switch (ActivityName) {
                        case LauncherUI:
                            isTasking = JKPreferences.GetSharePersistentBoolean("doTasking");
                            if (isTasking) {
                                try {
                                    sleepAndClickText(1000, "新的朋友");
                                    count++;
                                    sleepAndClickText(1000, "添加朋友");
                                    count++;
                                    sleepAndClickText(1000, "手机联系人");
                                    count++;
                                    sleepAndClickText(3000, "添加手机联系人");
                                    count++;
                                    delewithListView(event);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            break;
                    }
                    break;
                }
        }
    }


    @Override
    public void onInterrupt() {

    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void delewithListView(AccessibilityEvent event) throws InterruptedException {
        //查看通讯录界面控件id
        String textId = "com.tencent.mm:id/axz";
        String add_bt_id = "com.tencent.mm:id/ay1";
        String back_id = "com.tencent.mm:id/gw";
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> listview = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(textId);
        List<EcWXUserBean> list = new ArrayList<>();
        for (int i = 0; i < listview.size(); i++) {
            //获取微信名
            sleepAndClickText(1000, listview.get(i).getText().toString());
            //控件id
            String vx_name_id = "com.tencent.mm:id/lg";
            String gender_id = "com.tencent.mm:id/adt";
            String ares_id = "android:id/summary";
            //TODO
            Log.i(TAG, PerformClickUtils.geyTextById(this, vx_name_id));
            Log.i(TAG, PerformClickUtils.geyTextById(this, ares_id));
            Log.i(TAG, PerformClickUtils.getContentDescriptionById(this, gender_id));

            wxUserBean = new EcWXUserBean();
            wxUserBean.setWx_name(PerformClickUtils.geyTextById(this, vx_name_id));
            wxUserBean.setWx_area(PerformClickUtils.geyTextById(this, ares_id));
            wxUserBean.setWx_gender(PerformClickUtils.getContentDescriptionById(this, gender_id));
            list.add(wxUserBean);
            PerformClickUtils.performBack(this);
            Thread.sleep(1000);
        }
        //TODO
        JKLog.i(TAG, "///" + list.size() + "***");
        //添加好友
        List<AccessibilityNodeInfo> addListBt = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(add_bt_id);
        for (int i = 0; i < addListBt.size(); i++) {
            sleepAndClickText(1000, "添加");
            sleepAndClickId(1000, back_id);
        }
        doOfTaskEnd();
//        结束按back回到主页

    }


    private void doOfTaskEnd() throws InterruptedException {
        //删除通讯录
        JKLog.i(TAG, "删除通讯录");
        JKPreferences.RemoveSharePersistent("doTasking");
        for (int i = 0; i < count; i++) {
            PerformClickUtils.performBack(this);
            Thread.sleep(2000);
        }
        Thread.sleep(1000);
        PerformClickUtils.performHome(this);

    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sleepAndClickText(long time, String text) {

        try {
            Thread.sleep(time);
            PerformClickUtils.findTextAndClick(this, text);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void sleepAndClickId(long time, String text) {

        try {
            Thread.sleep(time);
            PerformClickUtils.findViewIdAndClick(this, text);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
