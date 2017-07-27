package com.yysp.ecandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Rect;
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
    String wx_name = "com.tencent.mm:id/mh";
    String wx_user_id = "com.tencent.mm:id/ig";
    String no_more_people_id = "com.tencent.mm:id/ae8";
    String gender_id = "com.tencent.mm:id/aeu";
    String groupJoinPeoInfo = "com.tencent.mm:id/im";
    String ares_id = "android:id/summary";
    String textId = "com.tencent.mm:id/aze";
    String groupName = "com.tencent.mm:id/aab";
    String new_friend = "com.tencent.mm:id/ax5";
    String groupNum = "com.tencent.mm:id/gp";
    String noReadId = "com.tencent.mm:id/ia";//未读消息小红点
    String agreeText = "我通过了你的朋友验证请求，现在我们可以开始聊天了";
    String userHeadImgId = "com.tencent.mm:id/ik";
    String isEndNoReadId = "com.tencent.mm:id/buf";
    String addBt = "com.tencent.mm:id/aej";


    List<ECTaskResultResponse.TaskResultBean> infoList = new ArrayList<>();
    List<ECTaskResultResponse.TaskResultBean.ChatVo> chatList;
    ECTaskResultResponse response;

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
    //弹出框
    public static final String DialogUI = "com.tencent.mm.ui.base.h";
    //详情界面
    public static final String ContactInfoUI = "com.tencent.mm.plugin.profile.ui.ContactInfoUI";
    public static final String GroupMsgUI = "com.tencent.mm.ui.chatting.En_5b8fbb1e";
    //升级页
    public static final String AppUpdaterUI = "com.tencent.mm.sandbox.updater.AppUpdaterUI";
    public static int CountType;
    public static String ActivityName = "";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!mActivityListenerThread.isAlive()) {
            mActivityListenerThread.start();
        }
        int event_type = event.getEventType();
        switch (event_type) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:

                isTasking = JKPreferences.GetSharePersistentBoolean("doTasking");
                taskType = JKPreferences.GetSharePersistentInt("taskType");
                PerformClickUtils.WaitCount = 0;
                ActivityName = event.getClassName().toString();
                JKLog.i("RT", "task_activity:" + ActivityName);
                JKLog.i("RT", "do_task:" + taskType + "/" + isTasking);
                //对应页面对应事件
                if (isTasking) {
                    switch (ActivityName) {
                        case AppUpdaterUI:
                            sleepAndClickText(2000, "取消");
                            break;
                        case LauncherUI://启动页面
                            switch (taskType) {
                                case MyPushIntentService.ContactGetFriendInfo:
                                    sleepAndClickId(1000, new_friend);
                                    break;
                                case MyPushIntentService.GetWxUserInfo:
                                    if (fromType == 1) {
                                        sleepAndClickText(1000, "通讯录");
                                        fromType = 0;
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                try {
                                                    getWxUserInfo();
                                                } catch (InterruptedException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                        }.start();
                                    }
                                    break;
                                case MyPushIntentService.FriendNumInfo:
                                    sleepAndClickText(1000, "通讯录");
                                    String text = PerformClickUtils.geyTextById(this, no_more_people_id);
                                    if (!text.equals("")) {
                                        String friendNum = PerformClickUtils.getNumFromInfo(text);
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
                                        int num = Integer.parseInt(friendNum);
                                        ECTaskResultResponse response = new ECTaskResultResponse();
                                        if (num >= 3) {
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
                                case MyPushIntentService.ViewMessage:
                                    new Thread() {
                                        @Override
                                        public void run() {
                                            if (fromType == 1) {
                                                doViewMessageTask(getRootInActiveWindow());
                                            }
                                        }
                                    }.start();
                                    break;

                            }
                            break;
                        case GroupMsgUI:
                            switch (taskType) {
                                case MyPushIntentService.FindGroupJoinPeo:
                                    String nums = PerformClickUtils.getNumFromInfo(PerformClickUtils.geyTextById(this, groupNum));
                                    JKLog.i("RT", "task_506:" + PerformClickUtils.geyTextById(this, groupNum) + ":nums:" + nums);
                                    AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                                    List<AccessibilityNodeInfo> nList = nodeInfo.findAccessibilityNodeInfosByViewId(groupJoinPeoInfo);
                                    for (int i = 0; i < nList.size(); i++) {
                                        String info = nList.get(i).getText().toString();
                                        String res = PerformClickUtils.getSubUtilSimple(info, "你邀请(.*?)加入了群聊");
                                        JKLog.i("RT", "task_506" + res);
                                        ECTaskResultResponse.TaskResultBean taskResultBean = new ECTaskResultResponse.TaskResultBean();
                                        taskResultBean.setNickname(res);
                                        infoList.add(taskResultBean);
                                    }
                                    ECTaskResultResponse response = new ECTaskResultResponse();
                                    response.setTaskResult(infoList);
                                    response.setAmount(Integer.parseInt(nums));
                                    response.setStatus(ECConfig.TASK_FINISH);
                                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                    response.setDeviceAlias(AliasName);
                                    doOfTaskEnd(response);
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
                                case MyPushIntentService.GetCreatGroupInfo:
                                    //群识别,滑动
                                    try {
                                        Thread.sleep(2000);
                                        if (fromTypeGroupPeoPleInfo == 1) {
                                            fromType = 1;
                                            fromTypeGroupPeoPleInfo = 0;
                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    goToGroupPeoPleInfo();
                                                }
                                            }.start();
                                        }
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    break;
                                case MyPushIntentService.AddFriendFromGroup:
                                    //群加友
                                    try {
                                        Thread.sleep(2000);
                                        if (fromTypeGroupPeoPleInfo == 1) {
                                            fromType = 1;
                                            fromTypeGroupPeoPleInfo = 0;
                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    addGroupFriend();
                                                }
                                            }.start();
                                        }
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    break;

                            }
                            break;
                        case SeeRoomMemberUI:
                            final AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
                            switch (taskType) {
                                case MyPushIntentService.GetCreatGroupInfo:
                                    if (fromType == 1) {
                                        fromType = 0;
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                getInfo(nodeInfo);
                                            }
                                        }.start();
                                    }
                                    break;
                                case MyPushIntentService.AddFriendFromGroup:
                                    if (fromType == 1) {
                                        fromType = 0;
                                        new Thread() {
                                            @Override
                                            public void run() {
                                                swipeAddFriend(nodeInfo);
                                            }
                                        }.start();
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
                            try {
                                Thread.sleep(5000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            switch (taskType) {

                                case MyPushIntentService.ContactGetFriendInfo:
                                    if (addFromType == 1) {
                                        try {
                                            Thread.sleep(2000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }
                                        if (isNeedSwipe) {
                                            AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
                                            PerformClickUtils.performSwipe(accessibilityNodeInfo);

                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    delewithListView();
                                                }
                                            }.start();
                                        } else {

                                            new Thread() {
                                                @Override
                                                public void run() {
                                                    delewithListView();
                                                }
                                            }.start();
                                        }
                                    }
                            }
                            break;
                        case DialogUI:
                            if (!PerformClickUtils.findText(this,"更新").equals("")){
                                sleepAndClickText(2000, "取消");
                            }else {
                                PerformClickUtils.performBack(this);
                            }
                            break;
                        case ContactInfoUI:
                            switch (taskType) {

                                case MyPushIntentService.SearchAddFriendType:
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    JKLog.i("RT", "task_501:" + PerformClickUtils.geyTextById(this, wx_name));
                                    wxUserBean = new ECTaskResultResponse.TaskResultBean();

                                    wxUserBean.setMobile(PerformClickUtils.geyTextById(this, wx_name));

                                    if (!PerformClickUtils.geyTextById(this, vx_name_id).equals("")) {
                                        wxUserBean.setAccount(PerformClickUtils.geyTextById(this, vx_name_id));
                                    } else {
                                        wxUserBean.setAccount(PerformClickUtils.geyTextById(this, vx_remark));
                                    }

                                    wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
                                    wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
                                    infoList.add(wxUserBean);

                                    response = new ECTaskResultResponse();
                                    response.setStatus(ECConfig.TASK_FINISH);
                                    response.setAmount(1);
                                    response.setDeviceAlias(AliasName);
                                    response.setTaskResult(infoList);
                                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                    doOfTaskEnd(response);
                                    break;
                                case MyPushIntentService.ViewFriendNews:
                                    JKLog.i("RT", "task_533:" + PerformClickUtils.geyTextById(this, vx_name_id));
                                    wxUserBean = new ECTaskResultResponse.TaskResultBean();
                                    String vx = PerformClickUtils.geyTextById(this, vx_name_id);
                                    if (!vx.equals("")) {
                                        String x[] = vx.split(":");
                                        if (x.length > 1) {
                                            wxUserBean.setAccount(x[1]);
                                        }
                                    }

                                    wxUserBean.setMobile(PerformClickUtils.geyTextById(this, wx_name));
                                    wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
                                    wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
                                    infoList.add(wxUserBean);

                                    response = new ECTaskResultResponse();
                                    response.setStatus(ECConfig.TASK_FINISH);
                                    response.setDeviceAlias(AliasName);
                                    response.setTaskResult(infoList);
                                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                    doOfTaskEnd(response);
                                    break;

                                case MyPushIntentService.SearchGroupAndGetPeoInfo:
                                    try {
                                        Thread.sleep(3000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    JKLog.i("RT", "task_536:" + PerformClickUtils.geyTextById(this, wx_name));
                                    wxUserBean = new ECTaskResultResponse.TaskResultBean();
                                    wxUserBean.setNickname(PerformClickUtils.geyTextById(this, wx_name));
                                    wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
                                    wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
                                    infoList.add(wxUserBean);

                                    response = new ECTaskResultResponse();
                                    response.setStatus(ECConfig.TASK_FINISH);
                                    response.setDeviceAlias(AliasName);
                                    response.setTaskResult(infoList);
                                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                    doOfTaskEnd(response);
                                    break;
                            }
                            break;
                        case HomeLauncherUI:
                            Intent intent;
                            switch (taskType) {
                                case MyPushIntentService.SearchAddFriendType:
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    intent = new Intent();
                                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                    intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
                                    startActivity(intent);
                                    break;
                                case MyPushIntentService.GetWxUserInfo:
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                    fromType = 1;
                                    intent = new Intent();
                                    intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                    intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
                                    startActivity(intent);
                                    break;
                                case MyPushIntentService.ViewMessage:
                                    try {
                                        Thread.sleep(1000);
                                        fromType = 1;
                                        Thread.sleep(1000);
                                        intent = new Intent();
                                        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                                        intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
                                        startActivity(intent);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }

                                    break;
                            }
                            break;

                    }
                }else{
                    switch (ActivityName) {
                        case AppUpdaterUI:
                            sleepAndClickText(2000, "取消");
                            break;
                        case DialogUI:
                            if (!PerformClickUtils.findText(this,"更新").equals("")){
                                sleepAndClickText(2000, "取消");
                            }
                            break;
                    }
                }

        }
    }

    /*
     寻找未读消息
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void doViewMessageTask(AccessibilityNodeInfo nodeInfo) {
        fromType = 0;
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(noReadId);
            JKLog.i("RT", "task_532:" + list.size());
            List<AccessibilityNodeInfo> noRead = nodeInfo.findAccessibilityNodeInfosByViewId(isEndNoReadId);
            JKLog.i("RT", "task_532:" + noRead.size());
            if (noRead.size() != 0) {
                if (list.size() != 0) {
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < list.size(); i++) {
                        sleepAndClickId(2000, noReadId);
                        try {
                            Thread.sleep(2000);
                            String agree_msg = PerformClickUtils.findText(this, agreeText);
                            if (!agree_msg.equals("")) {
                                //同意加好友,头像,聊天记录
                                List<AccessibilityNodeInfo> headList = nodeInfo.findAccessibilityNodeInfosByViewId(userHeadImgId);
                                List<AccessibilityNodeInfo> nList = nodeInfo.findAccessibilityNodeInfosByViewId(groupJoinPeoInfo);
                                chatList = new ArrayList<>();
                                wxUserBean = new ECTaskResultResponse.TaskResultBean();
                                if (nList.size() != 0) {
                                    int Offset = 0;
                                    for (int j = 0; j < headList.size(); j++) {
                                        if (j + Offset < nList.size()){
                                            Rect headRect = headList.get(j).getBoundsInScreen();
                                            Rect ListRect = nList.get(j + Offset).getBoundsInScreen();
                                            while (headRect.top != ListRect.top) {
                                                Offset++;
                                                if(j + Offset < nList.size()) {
                                                    headRect = headList.get(j).getBoundsInScreen();
                                                    ListRect = nList.get(j + Offset).getBoundsInScreen();
                                                }else
                                                {
                                                    break;
                                                }
                                            }

                                            if(j + Offset < nList.size()){
                                                String info = nList.get(j + Offset).getText().toString();
                                                String name = headList.get(j).getContentDescription().toString();
                                                ECTaskResultResponse.TaskResultBean.ChatVo chatVo = new ECTaskResultResponse.TaskResultBean.ChatVo();
                                                chatVo.setName(name);
                                                chatVo.setContent(info);
                                                chatList.add(chatVo);

                                                JKLog.i(TAG, "task_532_chat:" + name + "/" + info);
                                            }
                                        }
                                    }
                                }

                                Thread.sleep(5000);
                                if (headList.size() != 0) {
                                    PerformClickUtils.performClick(headList.get(0));
                                    Thread.sleep(3000);

                                    if (!PerformClickUtils.geyTextById(this, vx_name_id).equals("")) {
                                        wxUserBean.setAccount(PerformClickUtils.geyTextById(this, vx_name_id));
                                    } else {
                                        wxUserBean.setAccount(PerformClickUtils.geyTextById(this, vx_remark));
                                    }

                                    wxUserBean.setNickname(PerformClickUtils.geyTextById(this, wx_name));
                                    wxUserBean.setArea(PerformClickUtils.geyTextById(this, ares_id));
                                    wxUserBean.setSex(PerformClickUtils.getContentDescriptionById(this, gender_id));
                                    wxUserBean.setChatList(chatList);
                                    infoList.add(wxUserBean);

                                    Thread.sleep(2000);
                                    PerformClickUtils.performBack(this);
                                    Thread.sleep(2000);
                                    PerformClickUtils.performBack(this);
                                }
                            } else {
                                JKLog.i("RT", "task_532:无同意好友");
                                Thread.sleep(2000);
                                PerformClickUtils.performBack(this);
                            }
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //TODO 打印下
                    AccessibilityNodeInfo nodeInfos = getRootInActiveWindow();
                    swipeAndHome(nodeInfos);
                } else {
                    fromType = 1;
                    JKLog.i("RT", "task_532:滑动到底部了!");
                    postResult();
                }

            } else {
                if (infoList.size() != 0) {
                    JKLog.i("RT", "task_532:读完未读消息了!!!");
                    postResult();
                } else {
                    JKLog.i("RT", "task_532:没有未读消息!!!");
                    postResult();
                }

            }
        }


    }

    private void postResult() {

        ECTaskResultResponse response = new ECTaskResultResponse();
        response.setStatus(ECConfig.TASK_FINISH);
        response.setDeviceAlias(AliasName);
        response.setTaskResult(infoList);
        response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
        doOfTaskEnd(response);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void swipeAndHome(AccessibilityNodeInfo nodeInfo) {
        try {
            Thread.sleep(2000);
            PerformClickUtils.performSwipe(nodeInfo);
            Thread.sleep(2000);
            PerformClickUtils.performHome(this);
        } catch (InterruptedException e) {
            e.printStackTrace();
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
            if (!lastName.equals(list.get(list.size() - 1).getText().toString())) {
                lastName = list.get(list.size() - 1).getText().toString();
                for (int i = 0; i < list.size(); i++) {
                    //获取微信名
                    Thread.sleep(1000);
                    //反馈消息
                    wxUserBean = new ECTaskResultResponse.TaskResultBean();
                    wxUserBean.setNickname(list.get(i).getText().toString());
                    JKLog.i(TAG, "task_507:" + list.get(i).getText().toString());
                    infoList.add(wxUserBean);
                    Thread.sleep(2000);
                }
                PerformClickUtils.performClick(list.get(0));
                Thread.sleep(2000);
                PerformClickUtils.performBack(this);
                Thread.sleep(1000);
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
        }


    }

    /**
     * 获取群组中成员信息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void addGroupFriend() {
        try {
            Thread.sleep(1000 * 20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        String titleId = "android:id/title";
        String groupNameId = "com.tencent.mm:id/ce7";
        if (PerformClickUtils.getContentDescriptionById(this, titleId).equals("分隔栏")) {
            //不需要滑动
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(groupNameId);
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    //获取微信名
                    sleepAndClickText(1000, list.get(i).getText().toString());
                    try {
                        Thread.sleep(3000);
                        sleepAndClickId(1000, addBt);
                        sleepAndClickId(1000, "com.tencent.mm:id/gl");
                        Thread.sleep(2000);
                        PerformClickUtils.performBack(this);
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                ECTaskResultResponse response = new ECTaskResultResponse();
                response.setStatus(ECConfig.TASK_FINISH);
                response.setDeviceAlias(AliasName);
                response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                doOfTaskEnd(response);
                getFromType = -1;
            }
        } else if (PerformClickUtils.geyTextById(this, titleId).equals("")) {
            //需要滑动
            PerformClickUtils.performSwipe(nodeInfo);
            try {
                Thread.sleep(1000);
                if (PerformClickUtils.geyTextById(this, titleId).equals("查看全部群成员")) {
                    Thread.sleep(1000);
                    PerformClickUtils.findViewIdAndClick(this, titleId);
                    getFromType = -1;
                } else if (PerformClickUtils.getContentDescriptionById(this, titleId).equals("分隔栏")) {

                    PerformClickUtils.performSwipeBack(nodeInfo);
                    Thread.sleep(1000);
                    nodeInfo = getRootInActiveWindow();
                    List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(groupNameId);
                    for (int i = 0; i < list.size(); i++) {
                        //获取微信名
                        sleepAndClickText(1000, list.get(i).getText().toString());
                        try {
                            Thread.sleep(3000);
                            sleepAndClickId(1000, addBt);
                            Thread.sleep(2000);
                            sleepAndClickText(1000, "发送");
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
                    if (list.size() != 0) {
                        for (int i = 0; i < list.size(); i++) {
                            //获取微信名
                            sleepAndClickText(1000, list.get(i).getText().toString());
                            try {
                                Thread.sleep(3000);
                                sleepAndClickId(1000, addBt);
                                Thread.sleep(2000);
                                sleepAndClickText(1000, "发送");
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
                        response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                        doOfTaskEnd(response);
                        getFromType = -1;
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 获取群组中成员信息
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void goToGroupPeoPleInfo() {
        try {
            Thread.sleep(1000 * 20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        String titleId = "android:id/title";
        String groupNameId = "com.tencent.mm:id/ce7";
        if (PerformClickUtils.getContentDescriptionById(this, titleId).equals("分隔栏")) {
            //不需要滑动
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(groupNameId);
            if (list.size() != 0) {
                for (int i = 0; i < list.size(); i++) {
                    //获取微信名
                    wxUserBean = new ECTaskResultResponse.TaskResultBean();
                    wxUserBean.setNickname(list.get(i).getText().toString());
                    infoList.add(wxUserBean);

                }
                sleepAndClickText(1000, list.get(0).getText().toString());
                try {
                    Thread.sleep(1500);
                    PerformClickUtils.performBack(this);
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                ECTaskResultResponse response = new ECTaskResultResponse();
                response.setStatus(ECConfig.TASK_FINISH);
                response.setDeviceAlias(AliasName);
                response.setTaskResult(infoList);
                response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                doOfTaskEnd(response);
                getFromType = -1;
            }
        } else if (PerformClickUtils.geyTextById(this, titleId).equals("")) {
            //需要滑动
            PerformClickUtils.performSwipe(nodeInfo);
            try {
                Thread.sleep(1000);
                if (PerformClickUtils.geyTextById(this, titleId).equals("查看全部群成员")) {
                    Thread.sleep(1000);
                    PerformClickUtils.findViewIdAndClick(this, titleId);
                    getFromType = -1;
                } else if (PerformClickUtils.getContentDescriptionById(this, titleId).equals("分隔栏")) {

                    PerformClickUtils.performSwipeBack(nodeInfo);
                    Thread.sleep(1000);
                    nodeInfo = getRootInActiveWindow();
                    List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(groupNameId);
                    if (list.size() != 0) {
                        for (int i = 0; i < list.size(); i++) {
                            //获取微信名
                            wxUserBean = new ECTaskResultResponse.TaskResultBean();
                            wxUserBean.setNickname(list.get(i).getText().toString());
                            infoList.add(wxUserBean);
                        }
                        sleepAndClickText(1000, list.get(0).getText().toString());
                        try {
                            Thread.sleep(1500);
                            PerformClickUtils.performBack(this);
                            Thread.sleep(1500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        PerformClickUtils.performSwipe(nodeInfo);
                        Thread.sleep(1000);
                    }

                    nodeInfo = getRootInActiveWindow();

                    list = nodeInfo.findAccessibilityNodeInfosByViewId(groupNameId);
                    if (list.size() != 0) {
                        for (int i = 0; i < list.size(); i++) {
                            //获取微信名
                            wxUserBean = new ECTaskResultResponse.TaskResultBean();
                            JKLog.i(TAG, "507_no_" + list.get(i).getText().toString());
                            wxUserBean.setNickname(list.get(i).getText().toString());
                            infoList.add(wxUserBean);

                        }
                        sleepAndClickText(1000, list.get(0).getText().toString());
                        try {
                            Thread.sleep(2000);
                            PerformClickUtils.performBack(this);
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        ECTaskResultResponse response = new ECTaskResultResponse();
                        response.setStatus(ECConfig.TASK_FINISH);
                        response.setDeviceAlias(AliasName);
                        response.setTaskResult(infoList);
                        response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                        doOfTaskEnd(response);
                        getFromType = -1;
                    }

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getInfo(AccessibilityNodeInfo nodeInfo) {
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
     * 滑动群
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void swipeAddFriend(AccessibilityNodeInfo nodeInfo) {
        if (getFromType == 1) {
            //滑动
            PerformClickUtils.performSwipeUpOfGridView(nodeInfo);
            try {
                Thread.sleep(3000);
                clickAddGroup(nodeInfo);
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
                swipeAddFriend(nodeInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        } else if (getFromType == 0) {
            try {
                Thread.sleep(3000);
                clickAddGroup(nodeInfo);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }


    /**
     * 点击群组人员进入信息界面
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void clickAddGroup(AccessibilityNodeInfo nodeInfo) throws InterruptedException {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(groupName);
        if (list.size() != 0) {
            JKLog.i(TAG, "534_last_name:" + list.get(list.size() - 1).getText().toString());
            if (!lastName.equals(list.get(list.size() - 1).getText().toString())) {
                lastName = list.get(list.size() - 1).getText().toString();
                for (int i = 0; i < list.size(); i++) {
                    //获取微信名
                    Thread.sleep(1000);
                    PerformClickUtils.performClick(list.get(i));
                    Thread.sleep(1000);
                    //反馈消息
                    try {
                        Thread.sleep(3000);
                        sleepAndClickId(1000, addBt);
                        Thread.sleep(4000);
                        sleepAndClickId(1000, "com.tencent.mm:id/gl");
                        Thread.sleep(4000);
                        PerformClickUtils.performBack(this);
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
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
        }
    }


    /**
     * 拿到用户信息
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void getWxUserInfo() throws InterruptedException {
        String text = PerformClickUtils.geyTextById(this, no_more_people_id);
        JKLog.i(TAG, "task_503:" + text);
        AccessibilityNodeInfo nodeInfo = getRootInActiveWindow();
        if (nodeInfo != null) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(wx_user_id);

            if (list.size() != 0) {
                int j;
                if (page == 1) {
                    j = 0;
                } else {
                    j = 1;
                }
                for (int i = j; i < list.size(); i++) {
                    //获取微信名
                    wxUserBean = new ECTaskResultResponse.TaskResultBean();
                    String user = list.get(i).getText().toString();
                    JKLog.i(TAG, "task_item:" + user);
                    if (!user.equals("微信团队") && !user.equals("文件传输助手")) {
                        wxUserBean.setNickname(user);
                        infoList.add(wxUserBean);
                        Thread.sleep(2000);
                    }
                }
                page++;
            }
            Thread.sleep(2000);
            PerformClickUtils.performSwipe(nodeInfo);
            Thread.sleep(5000);
            if (!text.equals("")) {
                JKLog.i(TAG, "task_503:滑到底部了" + infoList.size());
                //任务执行完善后工作
                ECTaskResultResponse response = new ECTaskResultResponse();
                response.setStatus(ECConfig.TASK_FINISH);
                response.setDeviceAlias(AliasName);
                response.setTaskResult(infoList);
                response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                doOfTaskEnd(response);
            } else {
                //重复调用判断
                getWxUserInfo();
            }
        } else {
            ECTaskResultResponse response = new ECTaskResultResponse();
            response.setStatus(ECConfig.TASK_Fail);
            response.setDeviceAlias(AliasName);
            response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
            doOfTaskEnd(response);
        }
    }

    @Override
    public void onInterrupt() {

    }

    //处理通讯录加好友listview
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void delewithListView() {
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        addFromType = 0;
        AccessibilityNodeInfo accessibilityNodeInfo = getRootInActiveWindow();
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(textId);
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
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    wxUserBean = new ECTaskResultResponse.TaskResultBean();
                    wxUserBean.setMobile(nodeInfoList.get(i).getText().toString());
                    JKLog.i(TAG, "task_contanct:" + nodeInfoList.get(i).getText().toString());
                    infoList.add(wxUserBean);
                }
                sleepAndClickText(2000, nodeInfoList.get(0).getText().toString());
                PerformClickUtils.performBack(this);
                page++;
                isNeedSwipe = true;
                addFromType = 1;
            } else {
                isNeedSwipe = false;
                addFromType = 1;
                new Thread() {
                    @Override
                    public void run() {
                        OthoerUtil.deleContanct(HelpService.this);//删除通讯录
                    }
                }.start();
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
        } else {
            if (CountType >= 2) {
                isNeedSwipe = false;
                addFromType = 1;
                new Thread() {
                    @Override
                    public void run() {
                        OthoerUtil.deleContanct(HelpService.this);//删除通讯录
                    }
                }.start();

                ECTaskResultResponse response = new ECTaskResultResponse();
                response.setStatus(ECConfig.TASK_Fail);
                response.setDeviceAlias(AliasName);
                response.setReason("通讯录无人");
                response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                doOfTaskEnd(response);
            } else {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                PerformClickUtils.performBack(this);
                CountType++;
                isNeedSwipe = false;
                addFromType = 1;
            }

        }


    }


    private void doOfTaskEnd(ECTaskResultResponse resultResponse) {


        ECNetSend.taskStatus(resultResponse, this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DisBean disBean) {
                PerformClickUtils.performHome(HelpService.this);//任务完成进入home
                OthoerUtil.doOfTaskEnd();
                clearList();
            }


            @Override
            public void onError(Throwable e) {
                OthoerUtil.doOfTaskEnd();
                OthoerUtil.AddErrorMsgUtil("taskStatus" + e.getMessage());
                clearList();
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

    /**
     * 清理list
     */
    private void clearList() {
        if (infoList != null) {
            infoList.clear();
        }
        if (chatList != null) {
            chatList.clear();
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

    protected Thread mActivityListenerThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    if (ActivityName.indexOf("com.tencent.mm") != -1) {
                        PerformClickUtils.WaitCount++;
                    }
                    if (PerformClickUtils.WaitCount > 60) {
                        PerformClickUtils.WaitCount = 0;
                        ECTaskResultResponse response = new ECTaskResultResponse();
                        response.setStatus(ECConfig.TASK_Fail);
                        response.setDeviceAlias(AliasName);
                        response.setReason("在该界面卡死：" + ActivityName);
                        response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                        doOfTaskEnd(response);
                    }
                    Thread.sleep(10000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    });
}
