package com.yysp.ecandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.app.Instrumentation;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jkframework.config.JKPreferences;
import com.jkframework.config.JKSystem;
import com.jkframework.debug.JKLog;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.data.bean.EcPostBean;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.util.ContactUtil;
import com.yysp.ecandroid.util.OthoerUtil;
import com.yysp.ecandroid.util.PerformClickUtils;
import com.yysp.ecandroid.view.activity.ECTaskActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

@RequiresApi(api = Build.VERSION_CODES.DONUT)
public class HelpService extends AccessibilityService {
    String TAG = "RT";
    int count = 0;
    int page = 1;
    int taskType;

    int fromType = 1;

    //控件id
    String vx_name_id = "com.tencent.mm:id/lg";
    String wx_user_id = "com.tencent.mm:id/i_";
    String no_more_people_id = "com.tencent.mm:id/ad0";
    String gender_id = "com.tencent.mm:id/adt";
    String ares_id = "android:id/summary";
    String textId = "com.tencent.mm:id/axz";
    String groupNameId = "com.tencent.mm:id/hx";
    String add_to_contact = "com.tencent.mm:id/adi";
    String back_id = "com.tencent.mm:id/gw";
    String new_friend = "com.tencent.mm:id/avo";
    String groupChat = "com.tencent.mm:id/hm";
    String groupNum = "android:id/text1";
    List<ECTaskResultResponse.TaskResultBean> infoList = new ArrayList<>();

    //任务是否进行开关
    public boolean isTasking;
    ECTaskResultResponse.TaskResultBean wxUserBean;
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
    //群聊界面
    public static final String ChatroomInfoUI = "com.tencent.mm.plugin.chatroom.ui.ChatroomInfoUI";
    //桌面
    public static final String HomeLauncherUI = "com.freeme.home.Launcher";


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int event_type = event.getEventType();
        switch (event_type) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
//                isTasking = true;
//                taskType = MyPushIntentService.getCreatGroupInfo;
                isTasking = JKPreferences.GetSharePersistentBoolean("doTasking");
                taskType = JKPreferences.GetSharePersistentInt("taskType");

                String ActivityName = event.getClassName().toString();
                JKLog.i("RT", "task_activity:" + ActivityName);
                JKLog.i("RT", "do_task:" + taskType + "/" + isTasking);
                //对应页面对应事件
                if (isTasking) {
                    switch (ActivityName) {
                        case LauncherUI://启动页面
                            switch (taskType) {
                                case MyPushIntentService.ContactAddFriendTpye:
                                    sleepAndClickId(1000, new_friend);
                                    count++;
                                    break;
                                case MyPushIntentService.GetWxUserInfo:
                                    if (fromType == 1) {
                                        try {

                                            getWxUserInfo();
                                            fromType = 0;
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                            }
                            break;
                        case FMessageConversationUI://新的朋友
                            switch (taskType) {
                                case MyPushIntentService.ContactAddFriendTpye:
                                    sleepAndClickText(1000, "添加朋友");
                                    count++;
                                    break;
                            }
                            break;
                        case AddMoreFriendsUI://手机联系人
                            switch (taskType) {
                                case MyPushIntentService.ContactAddFriendTpye:
                                    sleepAndClickText(1000, "手机联系人");
                                    count++;
                                    break;
                            }
                            break;
                        case ChatroomInfoUI:
                            switch (taskType) {
                                case MyPushIntentService.GetGroupPeoPleNum:
                                    String nums = PerformClickUtils.getNumFromInfo(PerformClickUtils.geyTextById(this, groupNum));
                                    JKLog.i(TAG, "task_nums:" + nums);
                                    // TODO: 识别群人数为多少
                                    OthoerUtil.doOfTaskEnd();
                                    break;
                            }
                            break;
                        case AddMoreFriendsByOtherWayUI:
                            switch (taskType) {
                                case MyPushIntentService.ContactAddFriendTpye:
                                    sleepAndClickText(2000, "添加手机联系人");
                                    count++;
                                    break;
                            }
                            break;
                        case MobileFriendUI:
                            switch (taskType) {
                                case MyPushIntentService.ContactAddFriendTpye:
                                    try {
                                        delewithListView();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    break;
                            }
                            break;
                        case HomeLauncherUI:
                            switch (taskType) {
                                case MyPushIntentService.GetWxUserInfo:
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    Intent intent = new Intent();
                                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                    intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
                                    startActivity(intent);
                                    fromType = 1;
                                    break;
                            }
                            break;

                    }
                }

        }
    }

    /**
     * 拿到用户信息
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getWxUserInfo() throws InterruptedException {
        Thread.sleep(500);
        PerformClickUtils.findTextAndClick(this, "通讯录");
        String text = PerformClickUtils.geyTextById(this, no_more_people_id);

        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(wx_user_id);
        if (page == 1) {
            for (int i = 0; i < list.size(); i++) {
                //获取微信名
                sleepAndClickText(1000, list.get(i).getText().toString());
                //TODO
                Log.i(TAG, "task_name:" + PerformClickUtils.geyTextById(this, vx_name_id));

                wxUserBean = new ECTaskResultResponse.TaskResultBean();
                wxUserBean.setNickname(PerformClickUtils.geyTextById(this, vx_name_id));
                wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
                wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
                infoList.add(wxUserBean);
                PerformClickUtils.performBack(this);
                page++;
            }
        } else {
            for (int i = 1; i < list.size(); i++) {
                //获取微信名
                sleepAndClickText(1000, list.get(i).getText().toString());
                //TODO
                Log.i(TAG, "task_name:" + PerformClickUtils.geyTextById(this, vx_name_id));

                wxUserBean = new ECTaskResultResponse.TaskResultBean();
                wxUserBean.setNickname(PerformClickUtils.geyTextById(this, vx_name_id));
                wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
                wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
                infoList.add(wxUserBean);
                PerformClickUtils.performBack(this);
                page++;
            }
        }

        Thread.sleep(2000);
        PerformClickUtils.performSwipe(nodeInfo);
        Thread.sleep(2000);
        if (!text.equals("")) {
            JKLog.i(TAG, "task_t:滑到底部了" + infoList.size());
            //任务执行完善后工作
            OthoerUtil.doOfTaskEnd();
        }
        PerformClickUtils.performHome(this);

    }

    @Override
    public void onInterrupt() {

    }


    //处理通讯录加好友listview
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void delewithListView() throws InterruptedException {

        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> listview = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(textId);
        List<ECTaskResultResponse.TaskResultBean> list = new ArrayList<>();
        for (int i = 0; i < listview.size(); i++) {
            //获取微信名
            sleepAndClickText(1000, listview.get(i).getText().toString());
            //TODO
            Log.i(TAG, PerformClickUtils.geyTextById(this, vx_name_id));
            Log.i(TAG, PerformClickUtils.geyTextById(this, ares_id));
            Log.i(TAG, PerformClickUtils.getContentDescriptionById(this, gender_id));

            sleepAndClickId(1000, add_to_contact);
            sleepAndClickText(1000, "发送");

            wxUserBean = new ECTaskResultResponse.TaskResultBean();
            wxUserBean.setNickname(PerformClickUtils.geyTextById(this, vx_name_id));
            wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
            wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
            list.add(wxUserBean);
            PerformClickUtils.performBack(this);
            Thread.sleep(1000);
        }
        ECTaskResultResponse response = new ECTaskResultResponse();
        response.setStatus(ECConfig.TASK_FINISH);
        response.setDeviceAlias(JKSystem.GetGUID());
        response.setTaskResult(list);
        response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));


        for (int i = 0; i < listview.size(); i++) {
            sleepAndClickId(1000, add_to_contact);
            sleepAndClickText(1000, "发送");
        }
        doOfTaskEnd(response);

    }


    private void doOfTaskEnd(ECTaskResultResponse response) throws InterruptedException {
        //任务结束善后工作
        OthoerUtil.doOfTaskEnd();
        //任务返回请求
        ECNetSend.taskStatus(response).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<EcPostBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(EcPostBean ecPostBean) {
                if (ecPostBean.getCode() == 200) {
                    JKLog.i("RT", "task:" + "任务返回成功");
                }

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
        //删除通讯录
        ArrayList<String> list = JKPreferences.GetSharePersistentArrayString("phoneList");
        for (String phone : list) {
            try {
                ContactUtil.deleteContact(this, phone);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        Thread.sleep(1000);

        backToCount(count);
        PerformClickUtils.performHome(this);
    }


    //返回键多少次
    private void backToCount(int count) {
        for (int i = 0; i < count; i++) {
            sleepAndClickId(1000, back_id);
        }
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
