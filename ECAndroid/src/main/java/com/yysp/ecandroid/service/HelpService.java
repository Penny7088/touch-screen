package com.yysp.ecandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jkframework.config.JKPreferences;
import com.jkframework.debug.JKLog;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.net.ECNetSend;
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
import static com.yysp.ecandroid.config.ECConfig.AliasName;

@RequiresApi(api = Build.VERSION_CODES.DONUT)
public class HelpService extends AccessibilityService {
    String TAG = "RT";
    public static String lastName = "";
    int page = 1;
    int taskType;

    public static int fromTypeGroupPeoPleInfo = 1;
    public static int fromType = 1;
    public static int addFromType = 1;
    int getFromType = -1;
    boolean isNeedSwipe = false;

    //控件id
    String vx_name_id = "com.tencent.mm:id/aeq";
    String vx_remark = "com.tencent.mm:id/aze";
    String wx_user_id = "com.tencent.mm:id/ig";
    String no_more_people_id = "com.tencent.mm:id/ae8";
    String gender_id = "com.tencent.mm:id/aeu";
    String ares_id = "android:id/summary";
    String textId = "com.tencent.mm:id/aze";
    String groupName = "com.tencent.mm:id/aab";
    String new_friend = "com.tencent.mm:id/ax5";
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
    public static final String HomeLauncherUI = "com.android.launcher3.Launcher";
    //聊天成员
    public static final String SeeRoomMemberUI = "com.tencent.mm.plugin.chatroom.ui.SeeRoomMemberUI";
    //选择联系人
    public static final String SelectContactUI = "com.tencent.mm.ui.contact.SelectContactUI";
    //
    public static final String BindMContactIntroUI = "com.tencent.mm.ui.bindmobile.BindMContactIntroUI";


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int event_type = event.getEventType();
        switch (event_type) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:

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
                                case MyPushIntentService.ContactGetFriendInfo:
                                    sleepAndClickId(1000, new_friend);
                                    break;
                                case MyPushIntentService.GetWxUserInfo:
                                    if (fromType == 1) {
                                        try {
                                            sleepAndClickText(1000, "通讯录");
                                            fromType = 0;
                                            getWxUserInfo();
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                    break;
                                case MyPushIntentService.FriendNumInfo:
                                    sleepAndClickText(1000, "通讯录");
                                    String text = PerformClickUtils.geyTextById(this, no_more_people_id);
                                    if (!text.equals("")) {
                                        String friendNum = PerformClickUtils.getNumFromInfo(text);
                                        JKLog.i(TAG, "task_friendNum:" + friendNum);

                                        ECTaskResultResponse response = new ECTaskResultResponse();
                                        response.setStatus(ECConfig.TASK_FINISH);
                                        response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                        response.setDeviceAlias(AliasName);
                                        response.setAmount(Integer.parseInt(friendNum));
                                        doOfTaskEnd(response);
                                    }
                                    break;
                                case MyPushIntentService.DetectionTask:
                                    sleepAndClickText(1000, "通讯录");
                                    String nums = PerformClickUtils.geyTextById(this, no_more_people_id);
                                    if (!nums.equals("")) {
                                        String friendNum = PerformClickUtils.getNumFromInfo(nums);
                                        JKLog.i(TAG, "task_friendNum:" + friendNum);
                                        int num=Integer.parseInt(friendNum);
                                        ECTaskResultResponse response = new ECTaskResultResponse();
                                        if (num>=3){
                                            response.setStatus(ECConfig.TASK_FINISH);
                                            response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                            response.setDeviceAlias(AliasName);
                                            doOfTaskEnd(response);
                                        } else {
                                            response.setStatus(ECConfig.TASK_Fail);
                                            response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                            response.setDeviceAlias(AliasName);
                                            response.setReason("好友数量不够");
                                            doOfTaskEnd(response);
                                        }
                                    }
                                    break;
                            }
                            break;
                        case FMessageConversationUI://新的朋友
                            switch (taskType) {
                                case MyPushIntentService.ContactGetFriendInfo:
                                    sleepAndClickText(1000, "添加朋友");
                                    break;
                            }
                            break;
                        case AddMoreFriendsUI://手机联系人
                            switch (taskType) {
                                case MyPushIntentService.ContactGetFriendInfo:
                                    sleepAndClickText(1000, "手机联系人");
                                    break;
                            }
                            break;
                        case ChatroomInfoUI:
                            switch (taskType) {
                                case MyPushIntentService.GetGroupPeoPleNum:
                                    String nums = PerformClickUtils.getNumFromInfo(PerformClickUtils.geyTextById(this, groupNum));
                                    JKLog.i(TAG, "task_nums:" + nums);
                                    ECTaskResultResponse response = new ECTaskResultResponse();
                                    response.setStatus(ECConfig.TASK_FINISH);
                                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                    response.setDeviceAlias(AliasName);
                                    response.setAmount(Integer.parseInt(nums));
                                    doOfTaskEnd(response);
                                    break;
                                case MyPushIntentService.GetCreatGroupInfo:
                                    //群识别,滑动
                                    try {
                                        Thread.sleep(2000);
                                        if(fromTypeGroupPeoPleInfo == 1){
                                            fromType = 1;
                                            fromTypeGroupPeoPleInfo = 0;
                                            goToGroupPeoPleInfo();
                                        }
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    break;
                            }
                            break;
                        case SeeRoomMemberUI:
                            switch (taskType) {
                                case MyPushIntentService.GetCreatGroupInfo:
                                    AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                                    if (fromType == 1) {
                                        fromType = 0;
                                        getInfo(nodeInfo);
                                    }
                                    break;
                            }
                            break;
                        case SelectContactUI:
                            switch (taskType) {
                                case MyPushIntentService.GetCreatGroupInfo:
                                    isTasking = false;
                                    fromType = 0;
                                    break;
                            }
                            break;
                        case AddMoreFriendsByOtherWayUI:
                            switch (taskType) {
                                case MyPushIntentService.ContactGetFriendInfo:
                                    sleepAndClickText(2000, "添加手机联系人");
                                    break;
                            }
                            break;
                        case BindMContactIntroUI:
                            switch (taskType) {
                                case MyPushIntentService.ContactGetFriendInfo:
                                    ECTaskResultResponse response = new ECTaskResultResponse();
                                    response.setStatus(ECConfig.TASK_FINISH);
                                    response.setDeviceAlias(AliasName);
                                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                    response.setReason("未绑定手机号");
                                    doOfTaskEnd(response);
                                    break;
                            }
                            break;
                        case MobileFriendUI:
                            switch (taskType) {
                                case MyPushIntentService.ContactGetFriendInfo:
                                    if (addFromType == 1) {
                                        if (isNeedSwipe) {
                                            AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
                                            PerformClickUtils.performSwipe(accessibilityNodeInfo);
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            delewithListView();
                                        } else {
                                            try {
                                                Thread.sleep(2000);
                                            } catch (InterruptedException e) {
                                                e.printStackTrace();
                                            }
                                            delewithListView();

                                        }
                                    }
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
                                    fromType = 1;
                                    Intent intent = new Intent();
                                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                    intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
                                    startActivity(intent);
                                    break;
                            }
                            break;

                    }
                }

        }
    }

    /**
     * 点击群组人员进入信息界面
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void clickGetInfo(AccessibilityNodeInfo nodeInfo) throws InterruptedException {
        JKLog.i(TAG, "507_task:");
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(groupName);
        if (list.size() != 0) {
            JKLog.i(TAG, "507_last_name:" + list.get(list.size() - 1).getText().toString());
            if (!lastName.equals(list.get(list.size() - 1).getText().toString())) {
                lastName = list.get(list.size() - 1).getText().toString();
                for (int i = 0; i < list.size(); i++) {
                    //获取微信名
                    Thread.sleep(1000);
                    PerformClickUtils.performClick(list.get(i));
                    Thread.sleep(1000);
                    //反馈消息
                    wxUserBean = new ECTaskResultResponse.TaskResultBean();
                    wxUserBean.setNickname(PerformClickUtils.geyTextById(this, "com.tencent.mm:id/mh"));
                    wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
                    wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
                    infoList.add(wxUserBean);
                    Thread.sleep(2000);
                    PerformClickUtils.performBack(this);
                    Thread.sleep(1000);
                }
                getFromType = 1;
                fromType = 1;
            } else {

                ECTaskResultResponse response = new ECTaskResultResponse();
                //任务执行完善后工作
                response.setStatus(ECConfig.TASK_FINISH);
                response.setDeviceAlias(AliasName);
                response.setTaskResult(infoList);
                response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                doOfTaskEnd(response);
                fromType = 0;
            }
            Log.i(TAG, "task_s:" + list.size() + "/" + lastName);
        }


    }

    /**
     * 获取群组中成员信息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void goToGroupPeoPleInfo() {
        try {
            Thread.sleep(1000*20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        String titleId = "android:id/title";
        String groupNameId = "com.tencent.mm:id/ce7";
        JKLog.i(TAG, "507_pf***:" + PerformClickUtils.getContentDescriptionById(this, titleId));
        if (PerformClickUtils.getContentDescriptionById(this, titleId).equals("分隔栏")) {
            //不需要滑动
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(groupNameId);
            for (int i = 0; i < list.size(); i++) {
                //获取微信名
                sleepAndClickText(1000, list.get(i).getText().toString());
                wxUserBean = new ECTaskResultResponse.TaskResultBean();
                JKLog.i(TAG, "507_no_" + list.get(i).getText().toString());
                wxUserBean.setNickname(PerformClickUtils.geyTextById(this, vx_name_id));
                wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
                wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
                infoList.add(wxUserBean);
                try {
                    Thread.sleep(2000);
                    PerformClickUtils.performBack(this);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            ECTaskResultResponse response = new ECTaskResultResponse();
            response.setStatus(ECConfig.TASK_FINISH);
            response.setDeviceAlias(AliasName);
            response.setTaskResult(infoList);
            response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
            doOfTaskEnd(response);
            getFromType = -1;
        } else if (PerformClickUtils.geyTextById(this, titleId).equals("")) {
            //需要滑动
            PerformClickUtils.performSwipe(nodeInfo);
            try {
                Thread.sleep(1000);
                if (PerformClickUtils.geyTextById(this, titleId).equals("查看全部群成员")) {
                    Thread.sleep(1000);
                    PerformClickUtils.findViewIdAndClick(this, titleId);
                    getFromType = -1;
                }else if (PerformClickUtils.getContentDescriptionById(this, titleId).equals("分隔栏")) {

                    PerformClickUtils.performSwipeBack(nodeInfo);
                    Thread.sleep(1000);
                    nodeInfo = getRootInActiveWindow();
                    List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(groupNameId);
                    for (int i = 0; i < list.size(); i++) {
                        //获取微信名
                        sleepAndClickText(1000, list.get(i).getText().toString());
                        wxUserBean = new ECTaskResultResponse.TaskResultBean();
                        JKLog.i(TAG, "507_no_" + list.get(i).getText().toString());
                        wxUserBean.setNickname(PerformClickUtils.geyTextById(this, vx_name_id));
                        wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
                        wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
                        infoList.add(wxUserBean);
                        try {
                            Thread.sleep(2000);
                            PerformClickUtils.performBack(this);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    PerformClickUtils.performSwipe(nodeInfo);
                    Thread.sleep(1000);
                    nodeInfo = getRootInActiveWindow();
                    list = nodeInfo.findAccessibilityNodeInfosByViewId(groupNameId);
                    for (int i = 0; i < list.size(); i++) {
                        //获取微信名
                        sleepAndClickText(1000, list.get(i).getText().toString());
                        wxUserBean = new ECTaskResultResponse.TaskResultBean();
                        JKLog.i(TAG, "507_no_" + list.get(i).getText().toString());
                        wxUserBean.setNickname(PerformClickUtils.geyTextById(this, vx_name_id));
                        wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
                        wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
                        infoList.add(wxUserBean);
                        try {
                            Thread.sleep(2000);
                            PerformClickUtils.performBack(this);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    ECTaskResultResponse response = new ECTaskResultResponse();
                    response.setStatus(ECConfig.TASK_FINISH);
                    response.setDeviceAlias(AliasName);
                    response.setTaskResult(infoList);
                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                    doOfTaskEnd(response);
                    getFromType = -1;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getInfo(AccessibilityNodeInfo nodeInfo) {
        JKLog.i(TAG, "task_type" + getFromType);
        if (getFromType == 1) {
            //滑动
            PerformClickUtils.performSwipeUpOfGridView(nodeInfo);
            try {
                Thread.sleep(3000);
                clickGetInfo(nodeInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (getFromType == -1) {
            //向上滑动
            try {
                PerformClickUtils.performSwipeDownOfGridView(nodeInfo);
                Thread.sleep(2000);
                PerformClickUtils.findViewIdAndClick(this, groupName);
                Thread.sleep(2000);
                PerformClickUtils.performBack(this);
                getFromType = 0;
                getInfo(nodeInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else if (getFromType == 0) {
            try {
                Thread.sleep(3000);
                clickGetInfo(nodeInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 拿到用户信息
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getWxUserInfo() throws InterruptedException {
        String text = PerformClickUtils.geyTextById(this, no_more_people_id);

        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(wx_user_id);
        for (int i = 0; i < list.size(); i++) {
            //获取微信名
            wxUserBean = new ECTaskResultResponse.TaskResultBean();
            String user = list.get(i).getText().toString();
            JKLog.i(TAG, "item:" + user);
            sleepAndClickText(1000, user);

            wxUserBean.setRemark(list.get(i).getText().toString());
            wxUserBean.setNickname(PerformClickUtils.geyTextById(this, vx_name_id));
            wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
            wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
            infoList.add(wxUserBean);
            Thread.sleep(500);
            PerformClickUtils.performBack(this);
        }

        Thread.sleep(1000);
        PerformClickUtils.performSwipe(nodeInfo);
        Thread.sleep(1000);
        if (!text.equals("")) {
            JKLog.i(TAG, "task_:滑到底部了" + infoList.size());
            page = 1;
            //任务执行完善后工作
            ECTaskResultResponse response = new ECTaskResultResponse();
            response.setStatus(ECConfig.TASK_FINISH);
            response.setDeviceAlias(AliasName);
            response.setTaskResult(infoList);
            response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
            doOfTaskEnd(response);
        } else {
            Thread.sleep(1000);
            JKLog.i(TAG, "-------" + "performHome" + "-------");
            PerformClickUtils.performHome(this);
        }
    }

    @Override
    public void onInterrupt() {

    }


    //处理通讯录加好友listview
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void delewithListView() {
        addFromType = 0;
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(textId);
        JKLog.i(TAG, "502_size:" + nodeInfoList.size());
        if (nodeInfoList.size() != 0) {
            if (!lastName.equals(nodeInfoList.get(nodeInfoList.size() - 1).getText().toString())) {
                lastName = nodeInfoList.get(nodeInfoList.size() - 1).getText().toString();
                int j;
                if (page == 1) {
                    j = 0;
                } else {
                    j = 1;
                }

                for (int i = j; i < nodeInfoList.size(); i++) {
                    //获取微信名
                    sleepAndClickText(1000, nodeInfoList.get(i).getText().toString());
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    wxUserBean = new ECTaskResultResponse.TaskResultBean();
                    wxUserBean.setRemark(PerformClickUtils.geyTextById(this, vx_remark));
                    wxUserBean.setNickname(PerformClickUtils.geyTextById(this, vx_name_id));
                    wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
                    wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
                    infoList.add(wxUserBean);
                    try {
                        Thread.sleep(2000);
                        PerformClickUtils.performBack(this);
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                JKLog.i("RT", "502,is:" + isNeedSwipe);
                page++;
                isNeedSwipe = true;
                addFromType = 1;
            } else {
                isNeedSwipe = false;
                addFromType = 1;
                JKLog.i(TAG, "502:滑动通讯录底部了!");
                OthoerUtil.deleContanct(this);//删除通讯录
                ECTaskResultResponse response = new ECTaskResultResponse();
                response.setStatus(ECConfig.TASK_FINISH);
                response.setDeviceAlias(AliasName);
                response.setTaskResult(infoList);
                response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                doOfTaskEnd(response);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }


    private void doOfTaskEnd(ECTaskResultResponse resultResponse) {
        ECNetSend.taskStatus(resultResponse).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DisBean disBean) {
                JKLog.i(TAG, "taskStatus:" + disBean.getCode() + "/" + disBean.getMsg());
                if (disBean.getCode() == 200) {
                    JKLog.i(TAG, "item_taskStatus:success");
                }else {
                    OthoerUtil.AddErrorMsgUtil(disBean.getMsg());
                }
                OthoerUtil.doOfTaskEnd();
                infoList.clear();

            }

            @Override
            public void onError(Throwable e) {
                JKLog.i(TAG, "erro:" + e.getMessage());
                OthoerUtil.doOfTaskEnd();
                OthoerUtil.AddErrorMsgUtil(e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
