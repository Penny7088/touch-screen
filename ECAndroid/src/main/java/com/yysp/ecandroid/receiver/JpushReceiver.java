package com.yysp.ecandroid.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
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
import com.yysp.ecandroid.data.bean.DisPushBean;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.service.MyPushIntentService;
import com.yysp.ecandroid.util.ContactUtil;
import com.yysp.ecandroid.util.OthoerUtil;
import com.yysp.ecandroid.view.activity.ECTaskActivity;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Context.ACCESSIBILITY_SERVICE;
import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.yysp.ecandroid.config.ECConfig.AddFriendFromGroup;
import static com.yysp.ecandroid.config.ECConfig.AddNearPeople;
import static com.yysp.ecandroid.config.ECConfig.AgressAddFriend;
import static com.yysp.ecandroid.config.ECConfig.AgressAddInGroupMsg;
import static com.yysp.ecandroid.config.ECConfig.AliasName;
import static com.yysp.ecandroid.config.ECConfig.ChatWithFriend;
import static com.yysp.ecandroid.config.ECConfig.CleanSystemFile;
import static com.yysp.ecandroid.config.ECConfig.Clicklike;
import static com.yysp.ecandroid.config.ECConfig.Comment;
import static com.yysp.ecandroid.config.ECConfig.ContactGetFriendInfo;
import static com.yysp.ecandroid.config.ECConfig.CreatGroupType;
import static com.yysp.ecandroid.config.ECConfig.CreatGroupTypeBySmallWx;
import static com.yysp.ecandroid.config.ECConfig.DeleFriend;
import static com.yysp.ecandroid.config.ECConfig.DetectionTask;
import static com.yysp.ecandroid.config.ECConfig.FindGroupJoinPeo;
import static com.yysp.ecandroid.config.ECConfig.Forwarding;
import static com.yysp.ecandroid.config.ECConfig.FriendNumInfo;
import static com.yysp.ecandroid.config.ECConfig.GetCreatGroupInfo;
import static com.yysp.ecandroid.config.ECConfig.GetWxUserInfo;
import static com.yysp.ecandroid.config.ECConfig.GoOutGroup;
import static com.yysp.ecandroid.config.ECConfig.KickOutGroup;
import static com.yysp.ecandroid.config.ECConfig.LookFriendCircle;
import static com.yysp.ecandroid.config.ECConfig.NeedContactAddFriend;
import static com.yysp.ecandroid.config.ECConfig.PickingUpBottles;
import static com.yysp.ecandroid.config.ECConfig.SearchAddFriendType;
import static com.yysp.ecandroid.config.ECConfig.SearchGroupAndGetPeoInfo;
import static com.yysp.ecandroid.config.ECConfig.SendFriendCircle;
import static com.yysp.ecandroid.config.ECConfig.ThrowTheBottle;
import static com.yysp.ecandroid.config.ECConfig.VideoWithFriend;
import static com.yysp.ecandroid.config.ECConfig.ViewFriendNews;
import static com.yysp.ecandroid.config.ECConfig.ViewMessage;
import static com.yysp.ecandroid.config.ECConfig.VoiceWithFriend;


public class JpushReceiver extends BroadcastReceiver {
    List<DisGetTaskBean.DataBean.TargetAccountsBean> list;
    Gson gson;
    String TAG = "RT";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            startTask(message, context);
        }
    }

    private void startTask(final String msg, final Context context) {
        //开启任务
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            final Gson gson = new Gson();
            DisPushBean disPushBean = gson.fromJson(msg, DisPushBean.class);
            ECTaskResultResponse response = new ECTaskResultResponse();
            response.setStatus(ECConfig.TASK_Fail);
            response.setTaskId(disPushBean.getTaskId());
            response.setDeviceAlias(AliasName);

            doSomeThing(response, context);
        } else {
            final Gson gson = new Gson();
            DisPushBean disPushBean = gson.fromJson(msg, DisPushBean.class);
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
                                if (disGetTaskBean.getData() != null && disGetTaskBean.getData().getTaskId() != null) {
                                    if (!disGetTaskBean.getData().getTaskId().equals("")) {
                                        JKLog.i(TAG, "dis:" + disGetTaskBean.getData().getTaskId() + "'*'" + disGetTaskBean.getData().getTaskType());
                                        ECConfig.CloseScreenOrder(context);
                                        JKPreferences.SaveSharePersistent("taskType", disGetTaskBean.getData().getTaskType());
                                        String jsonStr = gson.toJson(disGetTaskBean.getData());
                                        doTaskWithId(disGetTaskBean.getData().getTaskType(), jsonStr, context);
                                    } else {
                                        ECTaskResultResponse response = new ECTaskResultResponse();
                                        response.setStatus(ECConfig.TASK_Fail);
                                        response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                        response.setDeviceAlias(AliasName);
                                        doSomeThing(response, context);
                                    }
                                } else {
                                    ECTaskResultResponse response = new ECTaskResultResponse();
                                    response.setStatus(ECConfig.TASK_Fail);
                                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                    response.setDeviceAlias(AliasName);
                                    doSomeThing(response, context);
                                }
                            }

                            @Override
                            public void onError(Throwable e) {
                                ECTaskResultResponse response = new ECTaskResultResponse();
                                response.setStatus(ECConfig.TASK_Fail);
                                response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                                response.setDeviceAlias(AliasName);
                                doSomeThing(response, context);

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

    private void doSomeThing(ECTaskResultResponse resultResponse, Context context) {
        OthoerUtil.doOfTaskEnd();
        ECNetSend.taskStatus(resultResponse, context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
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


    private void doTaskWithId(int taskType, String content, Context context) {
        JKLog.i(TAG, "data:" + content);
        JKPreferences.SaveSharePersistent("pushData", content);//备份
        Intent intent;
        //判断辅助服务是否开启
        AccessibilityManager accessibilityManager = (AccessibilityManager) context.getSystemService(ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            JKToast.Show("找到空容器辅助功能，然后开启服务即可", 0);
        } else {
            intent = new Intent();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
            context.startActivity(intent);

            doTypeTask(taskType, content, context);

        }
    }

    private void doTypeTask(int taskType, final String content, final Context context) {
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
                            AddToContact(context, content);
                        }
                    }.start();
                    JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                } else {
                    ECTaskResultResponse response = new ECTaskResultResponse();
                    response.setReason("下发任务无数据");
                    response.setStatus(ECConfig.TASK_Fail);
                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                    response.setDeviceAlias(AliasName);
                    ECNetSend.taskStatus(response, context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
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
                            AddToContact(context, content);
                        }
                    }.start();
                    JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                } else {
                    ECTaskResultResponse response = new ECTaskResultResponse();
                    response.setReason("下发任务无数据");
                    response.setStatus(ECConfig.TASK_Fail);
                    response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                    response.setDeviceAlias(AliasName);
                    ECNetSend.taskStatus(response, context).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
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
            if (ContactUtil.addContact(context, list.get(i).getMobile())) {
                JKLog.i(TAG, "phone:" + list.get(i).getMobile());
                phoneList.add(list.get(i).getMobile());
            }

        }
        JKPreferences.SaveSharePersistent("phoneList", (ArrayList<String>) phoneList);
    }

}
