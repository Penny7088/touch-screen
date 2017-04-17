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
import com.yysp.ecandroid.receiver.AlarmReceiver;


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
    public int onStartCommand(Intent intent, int flags, final int startId) {
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
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return super.onStartCommand(intent, flags, startId);
    }


    //执行监控任务是否完成
    private void doTask() {
        String path = "/storage/emulated/legacy/KRQ/";
        String filePath = path + "task.txt";
        if (JKFile.IsSDCardAvailable()) {
            if (JKFile.IsExists(path)) {
                //读取文件检测任务完成状态
                String task_status = JKFile.ReadFile(filePath);
                JKLog.d(task_status);
                if (task_status.equals("完成")) {
                    JKLog.d("读取文件状态:" + task_status);
                    JKFile.WriteFile(filePath,"");
                } else {
                    JKLog.d("读取文件状态:" + task_status);
                }
            } else {
                //不存在则创建path文件夹
                JKFile.CreateDir(path);
            }
        }


    }


}
