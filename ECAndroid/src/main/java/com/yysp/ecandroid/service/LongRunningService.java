package com.yysp.ecandroid.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.view.accessibility.AccessibilityManager;

import com.google.gson.Gson;
import com.jkframework.config.JKPreferences;
import com.jkframework.control.JKToast;
import com.jkframework.debug.JKLog;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.config.ECTaskType;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.bean.DisGetTaskBean;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.util.ContactUtil;
import com.yysp.ecandroid.util.OthoerUtil;

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
    List<DisGetTaskBean.DataBean.TargetAccountsBean> list;
    Gson gson;

    ECTaskResultResponse response;
    String TAG = "RT";
    CountDownTimer countDownTimer;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public LongRunningService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(final Intent intent, int flags, final int startId) {
        if (!mIMThread.isAlive()) {
            mIMThread.start();
        }
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        if (mIMThread != null) {
            mIMThread.stop();
            mIMThread.destroy();
            mIMThread = null;
        }
        super.onDestroy();
    }

    protected Thread mIMThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000 * ECConfig.hbTimer);
                    String tsId = JKPreferences.GetSharePersistentString("taskId");
                    final Gson gson = new Gson();
                    JKLog.i(TAG, "tsId:" + tsId + "  AliasName: " + AliasName);
                    ECNetSend.searchToDoJobByDevice(tsId, AliasName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                            new Observer<DisGetTaskBean>() {
                                @Override
                                public void onSubscribe(Disposable d) {

                                }

                                @Override
                                public void onNext(DisGetTaskBean disGetTaskBean) {
                                    if (disGetTaskBean.getData() != null) {
                                        JKLog.i(TAG, "disGetTaskBean:" + disGetTaskBean + "  gethbTimer: " + disGetTaskBean.getData().gethbTimer());
                                        if (disGetTaskBean.getData().gethbTimer() > 0) {
                                            ECConfig.hbTimer = disGetTaskBean.getData().gethbTimer();
                                            if (disGetTaskBean.getData().getTaskId() != null && !disGetTaskBean.getData().getTaskId().equals("")) {
                                                JKLog.i(TAG, "dis:" + disGetTaskBean.getData().getTaskId() + "'*'" + disGetTaskBean.getData().getTaskType());
                                                ECConfig.CloseScreenOrder(LongRunningService.this);

                                                JKPreferences.SaveSharePersistent("taskId", disGetTaskBean.getData().getTaskId());
                                                JKPreferences.SaveSharePersistent("taskType", disGetTaskBean.getData().getTaskType());

                                                String jsonStr = gson.toJson(disGetTaskBean.getData());
                                                doTaskWithId(disGetTaskBean.getData().getTaskType(), jsonStr, disGetTaskBean.getData().getTimeOut());

                                            }
                                        }
                                    }
                                }

                                @Override
                                public void onError(Throwable e) {
                                }

                                @Override
                                public void onComplete() {
                                }
                            }
                    );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });

    private void doTaskWithId(int taskType, String content, int timeOut) {
        JKLog.i(TAG, "data:" + content);
        if (taskType != 500) {
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
                doTypeTask(taskType, content);
                JKLog.i("RT", "task_timer:" + content);
            }
        }else{
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
            case ECTaskType.BreakTask:
                HelpService.isTasking = false;
                response = new ECTaskResultResponse();
                response.setStatus(ECConfig.TASK_FINISH);
                response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                response.setDeviceAlias(AliasName);
                ECNetSend.taskStatus(response, this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DisBean disBean) {
                        OthoerUtil.doOfTaskEnd();
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
                break;
            case ECTaskType.SearchAddFriendType:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.ContactGetFriendInfo:
                //TODO 清空一次通讯录
                gson = new Gson();
                list = gson.fromJson(content, DisGetTaskBean.DataBean.class).getTargetAccounts();
                if (list.size() != 0) {
                    try {
                        Thread.sleep(20 * 1000);
                        ContactUtil.clearContact(LongRunningService.this);
                        Thread.sleep(30 * 1000);
                        AddToContact(LongRunningService.this, content);
                        Thread.sleep(20 * 1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    HelpService.isTasking = true;
                    OthoerUtil.launcherWx(this);
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
            case ECTaskType.GetWxUserInfo:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.CreatGroupType:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.CreatGroupTypeBySmallWx:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.FindGroupJoinPeo:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.GetCreatGroupInfo:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.ChatWithFriend:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.LookFriendCircle:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.VoiceWithFriend:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.VideoWithFriend:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.Forwarding:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.SendFriendCircle:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.Clicklike:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.Comment:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.DetectionTask:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.FriendNumInfo:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.AgressAddInGroupMsg:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.GoOutGroup:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.KickOutGroup:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.DeleFriend:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.AgressAddFriend:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.NeedContactAddFriend:
                gson = new Gson();
                list = gson.fromJson(content, DisGetTaskBean.DataBean.class).getTargetAccounts();
                if (list.size() != 0) {
                    HelpService.isTasking = true;
                    OthoerUtil.launcherWx(this);
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
            case ECTaskType.ViewMessage:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.ViewFriendNews:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.AddFriendFromGroup:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.CleanSystemFile:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.SearchGroupAndGetPeoInfo:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.PickingUpBottles:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.ThrowTheBottle:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;
            case ECTaskType.AddNearPeople:
                HelpService.isTasking = true;
                OthoerUtil.launcherWx(this);
                break;

        }

    }
}
