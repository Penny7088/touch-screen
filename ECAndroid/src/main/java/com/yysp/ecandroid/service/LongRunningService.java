package com.yysp.ecandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.config.JKSystem;
import com.jkframework.debug.JKLog;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.config.ECSdCardPath;
import com.yysp.ecandroid.data.bean.EcPostBean;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.receiver.AlarmReceiver;
import com.yysp.ecandroid.util.OthoerUtil;
import com.yysp.ecandroid.util.PerformClickUtils;
import com.yysp.ecandroid.view.activity.ECTaskActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;


/**
 * Created by Administrator on 2017/4/15.
 */

public class LongRunningService extends Service {

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
                //todo 执行定时任务
                doTask(ECSdCardPath.Task_Finish_TXT);
            }
        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 5 * 1000; // 这是定时的时间
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);

    }


    //执行监控任务是否完成
    private void doTask(String filePath) {
        if (JKFile.IsSDCardAvailable()) {
            if (JKFile.IsExists(ECSdCardPath.SD_CARD_PATH)) {
                readFileStatus(filePath);
            } else {
                //不存在则创建path文件夹
                JKFile.CreateDir(ECSdCardPath.SD_CARD_PATH);
                readFileStatus(filePath);
            }
        }
    }

    private void readFileStatus(String filePath) {
        //读取文件检测任务完成状态
        String task_status = JKFile.ReadFile(filePath);
        JKLog.i("RT", "task_status:" + task_status);
        int taskType = JKPreferences.GetSharePersistentInt("taskType");
        if (task_status.equals(ECConfig.success)) {
            switch (taskType) {
                case MyPushIntentService.ContactAddFriendTpye:
                    doOfScript();
                    break;
                case MyPushIntentService.CreatGroupType:
                    ECTaskResultResponse response = new ECTaskResultResponse();
                    response.setDeviceAlias(JKSystem.GetGUID());//TODO
                    response.setStatus(ECConfig.TASK_FINISH);
                    String id = String.valueOf(MyPushIntentService.CreatGroupType);
                    response.setTaskId(id);
                    doOfScriptCreatGroup(response);
                    break;
                case MyPushIntentService.GetGroupPeoPleNum:
                    //脚本完成任务
                    JKFile.WriteFile(ECSdCardPath.Task_Finish_TXT, "");
                    JKPreferences.SaveSharePersistent("doTasking", true);
                    break;
                case MyPushIntentService.DetectionTask:
                    //反馈检测账号是否合格
                    String task = JKFile.ReadFile(ECSdCardPath.DETECTION_TASK_Finish_TXT);
                    JKLog.i("RT", "task_task:" + task);
                    if (task.equals(ECConfig.Detetion_Success)) {
                        //TODO: 2017/5/9 检测成功
                        JKFile.WriteFile(ECSdCardPath.Task_Finish_TXT, "");
                        OthoerUtil.doOfTaskEnd();
                    }
                    break;
                case MyPushIntentService.GetWxUserInfo:
                    JKPreferences.SaveSharePersistent("doTasking", true);
                    OthoerUtil.launcherWx(this);
                    break;
            }
        } else if (task_status.equals(ECConfig.fail)) {
            String failReason = JKFile.ReadFile(ECSdCardPath.Task_Fail_TXT);
            // TODO: 2017/5/10 上报脚本执行失败原因并清除
            JKLog.i("RT", "task_fail:" + failReason);
            JKFile.WriteFile(ECSdCardPath.Task_Fail_TXT, "");
            OthoerUtil.doOfTaskEnd();
        } else {
            JKLog.i("RT", "task:" + "任务执行状态:没任务/正在执行");
        }
    }

    private void doOfScriptCreatGroup(ECTaskResultResponse resultResponse) {
        ECNetSend.taskStatus(resultResponse).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<EcPostBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(EcPostBean ecPostBean) {
                if (ecPostBean.getCode() == 200) {
                    JKLog.i("RT", "task_finish" + MyPushIntentService.CreatGroupType);
                }
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private void doOfScript() {
        //脚本完成任务
        JKFile.WriteFile(ECSdCardPath.Task_Finish_TXT, "");
        //app接着完成任务
        JKPreferences.SaveSharePersistent("doTasking", true);
        OthoerUtil.launcherWx(this);
    }
}
