package com.yysp.ecandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.util.ContactUtil;


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
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000 * ECConfig.hbTimer);
                    String tsId = ContactUtil.TaskId;
//                    final Gson gson = new Gson();"  AliasName: " + AliasName);
//                    ECRequest.searchToDoJobByDevice(tsId, AliasName)
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
//                    Logger.i(TAG, "tsId:" + tsId +
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    });
}
