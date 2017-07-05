package com.yysp.ecandroid.service;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityManager;

import com.google.gson.Gson;
import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.control.JKToast;
import com.jkframework.debug.JKLog;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.config.ECSdCardPath;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.bean.DisPushBean;
import com.yysp.ecandroid.data.bean.DisGetTaskBean;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.util.ContactUtil;
import com.yysp.ecandroid.util.OthoerUtil;
import com.yysp.ecandroid.view.activity.ECTaskActivity;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.yysp.ecandroid.config.ECConfig.AliasName;

/**
 * Created by Administrator on 2017/4/17.
 */

public class MyPushIntentService extends UmengMessageService {
    String TAG = "RT";
    //taskType
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

    @Override
    public void onMessage(Context context, Intent intent) {
        String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        try {
            final UMessage msg = new UMessage(new JSONObject(message));
            JKLog.i(TAG, "task_push:" + msg.text);
            new Thread() {
                @Override
                public void run() {
                    startTask(msg.text);
                }
            }.start();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startTask(final String msg) {
        //开启任务
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            final Gson gson = new Gson();
            DisPushBean disPushBean = gson.fromJson(msg, DisPushBean.class);
            JKLog.i(TAG, "辅助未打开 " + "taskId:" + disPushBean.getTaskId());
            ECTaskResultResponse response = new ECTaskResultResponse();
            response.setStatus(ECConfig.TASK_Fail);
            response.setTaskId(disPushBean.getTaskId());
            response.setDeviceAlias(AliasName);

            doSomeThing(response);
        } else {
            final Gson gson = new Gson();
            DisPushBean disPushBean = gson.fromJson(msg, DisPushBean.class);
            JKLog.i(TAG, "taskId:" + disPushBean.getTaskId());
            String tsId = JKPreferences.GetSharePersistentString("taskId");
            if (!tsId.equals(disPushBean.getTaskId())) {
                JKPreferences.SaveSharePersistent("taskId", disPushBean.getTaskId());
                //等于0请求任务
                ECNetSend.taskApply(disPushBean.getTaskId(), AliasName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                        new Observer<DisGetTaskBean>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(DisGetTaskBean disGetTaskBean) {
                                JKLog.i(TAG, "taskBean:" + disGetTaskBean.getData() + "code:" + disGetTaskBean.getCode());
                                if (disGetTaskBean.getData() != null && !disGetTaskBean.getData().getTaskId().equals("")) {
                                    JKLog.i(TAG, "dis:" + disGetTaskBean.getData().getTaskId() + "'*'" + disGetTaskBean.getData().getTaskType());
                                    JKLog.i(TAG, "task_data" + disGetTaskBean.getData());
//                                    ECConfig.CloseScreenOrder(MyPushIntentService.this);
                                    JKPreferences.SaveSharePersistent("taskType", disGetTaskBean.getData().getTaskType());
                                    String jsonStr = gson.toJson(disGetTaskBean.getData());
                                    doTaskWithId(disGetTaskBean.getData().getTaskType(), jsonStr);

                                } else {
                                    JKLog.i(TAG, "disGetTaskBean.getData() or disGetTaskBean.getData().getTaskID() is null");
                                    ECTaskResultResponse response = new ECTaskResultResponse();
                                    response.setStatus(ECConfig.TASK_Fail);
                                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                    response.setDeviceAlias(AliasName);
                                    doSomeThing(response);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                JKLog.i(TAG, "task_APPLY:" + e.getMessage());
                                ECTaskResultResponse response = new ECTaskResultResponse();
                                response.setStatus(ECConfig.TASK_Fail);
                                response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                response.setDeviceAlias(AliasName);
                                doSomeThing(response);

                            }

                            @Override
                            public void onComplete() {
                            }
                        }
                );
            } else {
                JKLog.i(TAG, "task_重复的taskId");
            }
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
                JKLog.i(TAG, "task" + disBean.getCode() + "/." +
                        disBean.getMsg());
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
                            AddToContact(MyPushIntentService.this, content);
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
                            AddToContact(MyPushIntentService.this, content);
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


    /*
    通过服务端发送的json将手机号加入通讯录
     */
    private void AddToContact(Context context, String custom) {
        Gson gson = new Gson();
        List<DisGetTaskBean.DataBean.TargetAccountsBean> list = gson.fromJson(custom, DisGetTaskBean.DataBean.class).getTargetAccounts();
        List<String> phoneList = new ArrayList<>();
        JKLog.i(TAG, "phone_size:" + list.size());
        for (int i = 0; i < list.size(); i++) {
            ContactUtil.addContact(context, list.get(i).getMobile());
            JKLog.i(TAG, "phone:" + list.get(i).getMobile());
            phoneList.add(list.get(i).getMobile());
        }
        JKPreferences.SaveSharePersistent("phoneList", (ArrayList<String>) phoneList);
    }
}
