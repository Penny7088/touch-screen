package com.yysp.ecandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;

import com.yysp.ecandroid.ECTaskActivity;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.config.PackageConst;
import com.yysp.ecandroid.data.TestBeautifyModel;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.data.test;
import com.yysp.ecandroid.framework.distribute.TaskFactory;
import com.yysp.ecandroid.framework.util.ContactUtil;
import com.yysp.ecandroid.framework.util.FileUtils;
import com.yysp.ecandroid.framework.util.Logger;
import com.yysp.ecandroid.framework.util.PerformClickUtils;

import junit.framework.Test;

import java.util.ArrayList;


/**
 * Created by Administrator on 2017/4/15.
 */

public class LongRunningService extends Service {

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
        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void run() {
            while (true) {
                try {
//                    Thread.sleep(1000 * ECConfig.hbTimer);
//                    String tsId = ContactUtil.TaskId;


                    //TODO 从这里开始进行任务的分发
                    //TODO test START
                    if (ECTaskActivity.isOpen) {

                        //登录
//                        String lDirTencent = FileUtils.findDir();
//                        if (lDirTencent != null) {
//                            if (lDirTencent.equals(PackageConst.APP)) { //有这个文件夹
//                                boolean lDeleteDir = FileUtils.deleteDir(lDirTencent);
//                                if (lDeleteDir) {
//                                    Thread.sleep(1000);
//                                    String lMkdir = FileUtils.mkdir();
//                                    FileUtils.permissionAs(lMkdir);
//                                    PerformClickUtils.launcherWeChat(LongRunningService.this);
//                                    Thread.sleep(2000);
//                                    TaskFactory.createTask(500).running();
//                                }
//                            }
//                        } else {
//                            PerformClickUtils.launcherWeChat(LongRunningService.this);
//                            Thread.sleep(2000);
//                            TaskFactory.createTask(500).running();
//                        }

                        //加友
//                        String lJson = test.getJson(LongRunningService.this, "beautify.json");
//                        ArrayList<TestBeautifyModel> lBeautifyModels = test.parseData(lJson);
//                        Logger.d("onResume", "================" + lBeautifyModels.size());
//                        if (lBeautifyModels != null && lBeautifyModels.size() != 0) {
                            ECTaskActivity.isOpen = false;
                            //导入通讯录
//                            for (int i = 0; i < lBeautifyModels.size(); i++) {
//                                ContactUtil.addContact(LongRunningService.this,
//                                            lBeautifyModels.get(i).getPhone(),
//                                            lBeautifyModels.get(i).getCompany());
//                            }
                            Thread.sleep(1000);
                            PerformClickUtils.launcherWeChat(LongRunningService.this);
                            Thread.sleep(10000);
                            TaskFactory.createTask(502).running();
//                            TaskFactory.createTask(502).running(lBeautifyModels);
//                        }
                    }
                    //TODO test END

                    if (!ContactUtil.isTasking && !ContactUtil.TaskId.equals("")) {
//                    isTasking = true;
//                    taskType = JKPreferences.GetSharePersistentInt("taskType");
//                    Gson gson = new Gson();
//                    TaskBean = gson.fromJson(JKPreferences.GetSharePersistentString("pushData"),DisGetTaskBean.class);
//                    StarWx();
                    }
//                    final Gson gson = new Gson();"  AliasName: " + AliasName);
//                    Request.searchToDoJobByDevice(tsId, "")
//                            .subscribe(new BaseSubscriber<DisGetTaskBean>() {
//                                @Override
//                                public void onNext(DisGetTaskBean disGetTaskBean) {
//                                    if (disGetTaskBean.getData() != null && disGetTaskBean.getData().size() > 0 && disGetTaskBean.getData().get(0) != null) {
//                                        Logger.i(TAG, "disGetTaskBean:" + disGetTaskBean + "  gethbTimer: " + disGetTaskBean.getData().get(0).gethbTimer());
//                                        if (disGetTaskBean.getData().get(0).gethbTimer() > 0) {
//                                            ECConfig.hbTimer = disGetTaskBean.getData().get(0).gethbTimer();
//                                            if (disGetTaskBean.getData().get(0).getTaskId() != null && !disGetTaskBean.getData().get(0).getTaskId().equals("")) {
//                                                disGetTaskBean.setTaskIndex(0);
//                                                Logger.i(TAG, "dis:" + disGetTaskBean.getData().get(disGetTaskBean.getTaskIndex()).getTaskId() + "'*'" + disGetTaskBean.getData().get(disGetTaskBean.getTaskIndex()).getTaskType());
//                                                ECConfig.CloseScreenOrder(LongRunningService.this);
//
//                                                for (int i = 0 ; i < disGetTaskBean.getData().size(); i++){
//                                                }
//                                            } else {
//                                            }
//                                        }
//                                    } else{
//
//                                    }
//                                }
//                            });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
}
