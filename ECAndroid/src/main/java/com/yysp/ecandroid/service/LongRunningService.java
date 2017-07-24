package com.yysp.ecandroid.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.accessibility.AccessibilityManager;

import com.google.gson.Gson;
import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.control.JKToast;
import com.jkframework.debug.JKLog;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.config.ECSdCardPath;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.bean.DisGetTaskBean;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.util.ContactUtil;
import com.yysp.ecandroid.util.OthoerUtil;
import com.yysp.ecandroid.view.activity.ECTaskActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.yysp.ecandroid.config.ECConfig.AliasName;


/**
 * Created by Administrator on 2017/4/15.
 */

public class LongRunningService extends Service {
    public final static int SearchAddFriendType = 501;
    public final static int ContactGetFriendInfo = 502;
    public final static int GetWxUserInfo = 503;//不走脚本
    public final static int CreatGroupType = 504;
    public final static int CreatGroupTypeBySmallWx = 505;
    public final static int FindGroupJoinPeo = 506;
    public final static int GetCreatGroupInfo = 507;
    public final static int ChatWithFriend = 508;
    public final static int LookFriendCircle = 509;
    public final static int VoiceWithFriend = 510;
    public final static int VideoWithFriend = 511;
    public final static int Forwarding = 512;//转发
    public final static int SendFriendCircle = 513;
    public final static int Clicklike = 514;
    public final static int Comment = 515;
    public final static int DetectionTask = 516;
    public final static int FriendNumInfo = 517;
    public final static int AgressAddInGroupMsg = 518;
    public final static int GoOutGroup = 519;
    public final static int KickOutGroup = 520;
    public final static int DeleFriend = 521;
    public final static int AgressAddFriend = 525;
    public final static int NeedContactAddFriend = 527;
    public final static int ViewMessage = 532;
    public final static int ViewFriendNews = 533;
    public final static int AddFriendFromGroup = 534;
    public final static int CleanSystemFile = 535;
    public final static int SearchGroupAndGetPeoInfo = 536;
    public final static int PickingUpBottles = 537;
    public final static int ThrowTheBottle = 538;
    public final static int AddNearPeople = 539;

    List<DisGetTaskBean.DataBean.TargetAccountsBean> list;
    Gson gson;

    ECTaskResultResponse response;
    String TAG = "RT";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public LongRunningService() {
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        readFileStatus(ECSdCardPath.Task_Finish_TXT);
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        if (!mIMThread.isAlive()){
            mIMThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        if(mIMThread != null){
            mIMThread.stop();
            mIMThread.destroy();
            mIMThread = null;
        }
        super.onDestroy();
    }


    //执行监控任务是否完成


    private void readFileStatus(String filePath) {
        //读取文件检测任务完成状态

        String task_status = JKFile.ReadFile(filePath);
        int taskType = JKPreferences.GetSharePersistentInt("taskType");
        response = new ECTaskResultResponse();
        if (task_status.equals(ECConfig.success)) {
            //clear
            JKFile.WriteFile(ECSdCardPath.Task_Finish_TXT, "");
            switch (taskType) {
                case MyPushIntentService.SearchAddFriendType:
                    String result = JKFile.ReadFile(ECSdCardPath.ResultTxt);
                    switch (result) {
                        case "搜索不到":
                            response.setAmount(0);
                            postTaskFinish(response);
                            break;
                        case "已经是好友":
                            doOfScript();
                            break;
                        default:
                            response.setAmount(1);
                            postTaskFinish(response);
                            break;
                    }
                    break;
                case MyPushIntentService.ContactGetFriendInfo:
                    HelpService.addFromType = 1;
                    HelpService.CountType = 1;
                    HelpService.lastName = "";
                    doOfScript();
                    break;
                case MyPushIntentService.GetWxUserInfo:
                    HelpService.fromType = 1;
                    doOfScript();
                    break;
                case MyPushIntentService.CreatGroupType:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.CreatGroupTypeBySmallWx:
                    //小号直接拉群
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.FindGroupJoinPeo:
                    doOfScript();
                    break;
                case MyPushIntentService.GetCreatGroupInfo:
                    HelpService.fromTypeGroupPeoPleInfo = 1;
                    HelpService.lastName = "";
                    doOfScript();
                    break;
                case MyPushIntentService.ChatWithFriend:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.LookFriendCircle:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.VoiceWithFriend:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.VideoWithFriend:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.Forwarding:
                    postTaskFinish(response);
                    break;
                case SendFriendCircle:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.Clicklike:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.Comment:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.DetectionTask:
                    //反馈检测账号是否合格
                    doOfScript();
                    break;
                case MyPushIntentService.FriendNumInfo:
                    doOfScript();
                    break;
                case MyPushIntentService.AgressAddInGroupMsg:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.GoOutGroup:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.KickOutGroup:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.DeleFriend:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.AgressAddFriend:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.NeedContactAddFriend:
                    new Thread() {
                        @Override
                        public void run() {
                            OthoerUtil.deleContanct(LongRunningService.this);
                        }
                    }.start();

                    postTaskFinish(response);
                    break;
                case MyPushIntentService.ViewMessage:
                    HelpService.fromType = 1;
                    doOfScript();
                    break;
                case MyPushIntentService.ViewFriendNews:
                    doOfScript();
                    break;
                case MyPushIntentService.AddFriendFromGroup:
                    doOfScript();
                    break;
                case MyPushIntentService.CleanSystemFile:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.PickingUpBottles:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.ThrowTheBottle:
                    postTaskFinish(response);
                    break;
                case MyPushIntentService.AddNearPeople:
                    postTaskFinish(response);
                    break;

            }
        } else if (task_status.equals(ECConfig.fail)) {
            String failReason = JKFile.ReadFile(ECSdCardPath.Task_Fail_TXT);
            if (failReason.equals("登录失败")) {
                response.setLoginFail(true);
                //登录失败具体结果
                String result = JKFile.ReadFile(ECSdCardPath.ResultTxt);
                switch (result) {
                    case "1":
                        response.setAmount(1);//密码错误
                        break;
                    case "2":
                        response.setAmount(2);//封号
                        break;
                    case "3":
                        response.setAmount(3);//界面卡死
                        break;
                    case "4":
                        response.setAmount(4);//未知异常
                        break;
                    default:
                        response.setAmount(0);
                        break;
                }
            } else {
                response.setLoginFail(false);
            }
            JKLog.i("RT", "task_fail:" + failReason);
            JKFile.WriteFile(ECSdCardPath.Task_Finish_TXT, "");
            switch (taskType) {
                case MyPushIntentService.SearchAddFriendType:
                    switch (failReason) {
                        case "搜索频繁":
                            postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                            break;
                        case "被搜索好友异常":
                            postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                            break;
                        default:
                            postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                            break;
                    }
                    break;
                case MyPushIntentService.ContactGetFriendInfo:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.GetWxUserInfo:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.CreatGroupType:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.CreatGroupTypeBySmallWx:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.FindGroupJoinPeo:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.GetCreatGroupInfo:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.ChatWithFriend:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.LookFriendCircle:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.VoiceWithFriend:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.VideoWithFriend:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.Forwarding:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.SendFriendCircle:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.Clicklike:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.Comment:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.DetectionTask:
                    String detectionTaskTxt = JKFile.ReadFile(ECSdCardPath.DETECTION_TASK_Finish_TXT);
                    postTaskFailReason(response, detectionTaskTxt, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.FriendNumInfo:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.AgressAddInGroupMsg:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.GoOutGroup:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.KickOutGroup:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.DeleFriend:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.AgressAddFriend:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.NeedContactAddFriend:
                    new Thread() {
                        @Override
                        public void run() {
                            OthoerUtil.deleContanct(LongRunningService.this);
                        }
                    }.start();
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.ViewMessage:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.ViewFriendNews:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.AddFriendFromGroup:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.CleanSystemFile:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.SearchGroupAndGetPeoInfo:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.PickingUpBottles:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.ThrowTheBottle:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;
                case MyPushIntentService.AddNearPeople:
                    postTaskFailReason(response, failReason, ECConfig.TASK_Fail);
                    break;


            }

        } else {
            JKLog.i("saas_api_", "task:" + "任务执行状态:没任务/正在执行");
        }
    }

    /**
     * 任务失败
     */
    private void postTaskFailReason(ECTaskResultResponse response, String reason, int status) {
        JKPreferences.RemoveSharePersistent("taskType");//删除type以免轮休两次
        response.setStatus(status);
        response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
        response.setDeviceAlias(AliasName);
        response.setReason(reason);
        ECNetSend.taskStatus(response, this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DisBean disBean) {
                JKLog.i(TAG, disBean.getMsg() + "/" + disBean.getCode());
                OthoerUtil.doOfTaskEnd();
            }

            @Override
            public void onError(Throwable e) {
                OthoerUtil.AddErrorMsgUtil("postTaskFailReason:taskStatus:" + e.getMessage());
                OthoerUtil.doOfTaskEnd();
            }

            @Override
            public void onComplete() {

            }

        });

    }

    /**
     * 任务完成
     */
    private void postTaskFinish(ECTaskResultResponse resultResponse) {

        JKPreferences.RemoveSharePersistent("taskType");//删除type以免轮休两次
        resultResponse.setTaskId(JKPreferences.GetSharePersistentString("taskId"));//taskId
        resultResponse.setStatus(ECConfig.TASK_FINISH);//完成状态
        resultResponse.setDeviceAlias(AliasName);//别名
        JKLog.i(TAG, "id:" + JKPreferences.GetSharePersistentString("taskId") + "/" + AliasName);
        ECNetSend.taskStatus(resultResponse, this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DisBean disBean) {
                JKLog.i(TAG, "taskStatus:" + disBean.getCode() + "/" + disBean.getMsg());
                if (disBean.getCode() == 200) {
                    JKLog.i(TAG, TAG + "taskStatus:success");
                } else {
                    OthoerUtil.AddErrorMsgUtil("postTaskFinish:taskStatus:" + disBean.getMsg());
                }
                OthoerUtil.doOfTaskEnd();
            }

            @Override
            public void onError(Throwable e) {
                JKLog.i(TAG, "erro:" + e.getMessage());
                OthoerUtil.AddErrorMsgUtil("postTaskFinish:taskStatus:" + e.getMessage());
                OthoerUtil.doOfTaskEnd();
            }

            @Override
            public void onComplete() {

            }
        });

    }

    /**
     * 脚本执行完成任务APP接着工作
     */
    private void doOfScript() {
        JKFile.WriteFile(ECSdCardPath.Task_Finish_TXT, "");
        JKPreferences.SaveSharePersistent("doTasking", true);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        OthoerUtil.launcherWx(this);
    }

    protected Thread mIMThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000*ECConfig.hbTimer);
                    String tsId = JKPreferences.GetSharePersistentString("taskId");
                    if (tsId.equals("")){
                        final Gson gson = new Gson();
                    ECNetSend.searchToDoJobByDevice(AliasName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                            new Observer<DisGetTaskBean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(DisGetTaskBean disGetTaskBean) {
                                    JKLog.i(TAG, "disGetTaskBean:" + disGetTaskBean + "  gethbTimer: " + disGetTaskBean.getData().gethbTimer());
                                    if (disGetTaskBean.getData() != null && disGetTaskBean.getData().gethbTimer() > 0) {
                                        ECConfig.hbTimer = disGetTaskBean.getData().gethbTimer();
                                        if (disGetTaskBean.getData().getTaskId() != null && !disGetTaskBean.getData().getTaskId().equals("")) {
                                            JKLog.i(TAG, "dis:" + disGetTaskBean.getData().getTaskId() + "'*'" + disGetTaskBean.getData().getTaskType());
                                            ECConfig.CloseScreenOrder(LongRunningService.this);
                                            JKPreferences.SaveSharePersistent("taskId", disGetTaskBean.getData().getTaskId());
                                            JKPreferences.SaveSharePersistent("taskType", disGetTaskBean.getData().getTaskType());
                                            String jsonStr = gson.toJson(disGetTaskBean.getData());
                                            doTaskWithId(disGetTaskBean.getData().getTaskType(), jsonStr);
                                        } else {
//                                            ECTaskResultResponse response = new ECTaskResultResponse();
//                                            response.setStatus(ECConfig.TASK_Fail);
//                                            response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
//                                            response.setDeviceAlias(AliasName);
//                                            doSomeThing(response);
                                        }
                                    } else {
//                                        ECTaskResultResponse response = new ECTaskResultResponse();
//                                        response.setStatus(ECConfig.TASK_Fail);
//                                        response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
//                                        response.setDeviceAlias(AliasName);
//                                        doSomeThing(response);
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
//                                    ECTaskResultResponse response = new ECTaskResultResponse();
//                                    response.setStatus(ECConfig.TASK_Fail);
//                                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
//                                    response.setDeviceAlias(AliasName);
//                                    doSomeThing(response);

                                }

                                @Override
                                public void onComplete() {
                                }
                            }
                    );
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private void doTaskWithId(int taskType, String content) {
        JKLog.i(TAG, "data:" + content);
        JKPreferences.SaveSharePersistent("pushData", content);//备份
        Intent intent;
        //判断辅助服务是否开启
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            JKToast.Show("找到空容器辅助功能，然后开启服务即可", 0);
        } else {
            intent = new Intent();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
            startActivity(intent);

            doTypeTask(taskType, content);

        }
    }

    /*
    通过服务端发送的json将手机号加入通讯录
     */
    private void AddToContact(Context context, String custom) {
        Gson gson = new Gson();
        List<DisGetTaskBean.DataBean.TargetAccountsBean> list = gson.fromJson(custom, DisGetTaskBean.DataBean.class).getTargetAccounts();
        List<String> phoneList = new ArrayList<>();
        JKLog.i(TAG, "phone_size:" + list.size());
        for (int i = 0; i < list.size(); i++) {
            if (ContactUtil.addContact(context, list.get(i).getMobile())) {
                JKLog.i(TAG, "phone:" + list.get(i).getMobile());
                phoneList.add(list.get(i).getMobile());
            }

        }
        JKPreferences.SaveSharePersistent("phoneList", (ArrayList<String>) phoneList);
    }

    private void doTypeTask(int taskType, final String content) {
        switch (taskType) {
            case SearchAddFriendType:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case ContactGetFriendInfo:
                gson = new Gson();
                list = gson.fromJson(content, DisGetTaskBean.DataBean.class).getTargetAccounts();
                if (list.size() != 0) {
                    new Thread() {
                        @Override
                        public void run() {
                            AddToContact(LongRunningService.this, content);
                        }
                    }.start();
                    JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                } else {
                    ECTaskResultResponse response = new ECTaskResultResponse();
                    response.setReason("下发任务无数据");
                    response.setStatus(ECConfig.TASK_Fail);
                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                    response.setDeviceAlias(AliasName);
                    ECNetSend.taskStatus(response, this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(DisBean disBean) {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }

                break;
            case GetWxUserInfo:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case CreatGroupType:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case CreatGroupTypeBySmallWx:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case FindGroupJoinPeo:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case GetCreatGroupInfo:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case ChatWithFriend:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case LookFriendCircle:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case VoiceWithFriend:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case VideoWithFriend:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case Forwarding:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case SendFriendCircle:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case Clicklike:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case Comment:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case DetectionTask:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case FriendNumInfo:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case AgressAddInGroupMsg:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case GoOutGroup:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case KickOutGroup:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case DeleFriend:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case AgressAddFriend:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case NeedContactAddFriend:
                gson = new Gson();
                list = gson.fromJson(content, DisGetTaskBean.DataBean.class).getTargetAccounts();
                if (list.size() != 0) {
                    new Thread() {
                        @Override
                        public void run() {
                            AddToContact(LongRunningService.this, content);
                        }
                    }.start();
                    JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                } else {
                    ECTaskResultResponse response = new ECTaskResultResponse();
                    response.setReason("下发任务无数据");
                    response.setStatus(ECConfig.TASK_Fail);
                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                    response.setDeviceAlias(AliasName);
                    ECNetSend.taskStatus(response, this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(DisBean disBean) {

                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
                }
                break;
            case ViewMessage:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case ViewFriendNews:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case AddFriendFromGroup:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case CleanSystemFile:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case SearchGroupAndGetPeoInfo:
                JKPreferences.SaveSharePersistent("doTasking", true);
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case PickingUpBottles:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case ThrowTheBottle:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case AddNearPeople:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;


        }

    }

    private void doSomeThing(ECTaskResultResponse resultResponse) {
        OthoerUtil.doOfTaskEnd();
        ECNetSend.taskStatus(resultResponse, this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DisBean disBean) {
                if (disBean.getCode() == 200) {
                    JKLog.i(TAG, "item_taskStatus:success");
                } else {
                    OthoerUtil.AddErrorMsgUtil("taskStatus:" + disBean.getMsg());
                }


            }

            @Override
            public void onError(Throwable e) {
                JKLog.i(TAG, "erro:" + e.getMessage());
                OthoerUtil.AddErrorMsgUtil("taskStatus" + e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });

    }
}
