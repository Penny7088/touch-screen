package com.yysp.ecandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.debug.JKLog;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.config.ECSdCardPath;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.util.OthoerUtil;
import com.yysp.ecandroid.util.PerformClickUtils;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.yysp.ecandroid.config.ECConfig.AliasName;
import static com.yysp.ecandroid.service.MyPushIntentService.SendFriendCircle;


/**
 * Created by Administrator on 2017/4/15.
 */

public class LongRunningService extends Service {

    ECTaskResultResponse response;
    String TAG = "RT";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        return super.onStartCommand(intent, flags, startId);

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
                        response.setAmount(3);//未知异常
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
}
