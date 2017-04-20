package com.yysp.ecandroid.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.Nullable;

import com.jkframework.algorithm.JKFile;
import com.jkframework.debug.JKLog;
import com.yysp.ecandroid.config.ECSdCardPath;
import com.yysp.ecandroid.receiver.AlarmReceiver;


/**
 * Created by Administrator on 2017/4/15.
 */

public class LongRunningService extends Service {
    private boolean closeAlm = true;

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
                doTask();
            }


        }).start();
        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);
        int time = 5 * 1000; // 这是定时的时间
        long triggerAtTime = SystemClock.elapsedRealtime() + time;
        Intent i = new Intent(this, AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
        //关闭心跳
        if (closeAlm == true) {
            manager.cancel(pi);
            JKLog.i("*****心跳关闭*****");
        } else {
            manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }
        return super.onStartCommand(intent, flags, startId);

    }


    //执行监控任务是否完成
    private void doTask() {
        closeAlm = false;
        if (JKFile.IsSDCardAvailable()) {
            if (JKFile.IsExists(ECSdCardPath.SD_CARD_PATH)) {
                //读取文件检测任务完成状态
                String task_status = JKFile.ReadFile(ECSdCardPath.Task_Finish_TXT);
                if (task_status.equals("完成")) {
                    JKLog.i("读取文件状态:" + task_status);
                    JKFile.WriteFile(ECSdCardPath.Task_Finish_TXT, "");
                    closeAlm = true;
                } else {
                    JKLog.i("任务执行状态:没任务/正在执行");
                }
            } else {
                //不存在则创建path文件夹
                JKFile.CreateDir(ECSdCardPath.SD_CARD_PATH);
            }
        }


    }


}
