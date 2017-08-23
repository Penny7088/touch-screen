package com.yysp.ecandroid.service;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.jkframework.config.JKPreferences;
import com.jkframework.debug.JKLog;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.data.bean.DisGetTaskBean;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.net.ECNetSend;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static com.yysp.ecandroid.config.ECConfig.AliasName;


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
                                    if (disGetTaskBean.getData() != null && disGetTaskBean.getData().size() > 0 && disGetTaskBean.getData().get(0) != null) {
                                        JKLog.i(TAG, "disGetTaskBean:" + disGetTaskBean + "  gethbTimer: " + disGetTaskBean.getData().get(0).gethbTimer());
                                        if (disGetTaskBean.getData().get(0).gethbTimer() > 0) {
                                            ECConfig.hbTimer = disGetTaskBean.getData().get(0).gethbTimer();
                                            if (disGetTaskBean.getData().get(0).getTaskId() != null && !disGetTaskBean.getData().get(0).getTaskId().equals("")) {
                                                disGetTaskBean.setTaskIndex(0);
                                                JKLog.i(TAG, "dis:" + disGetTaskBean.getData().get(disGetTaskBean.getTaskIndex()).getTaskId() + "'*'" + disGetTaskBean.getData().get(disGetTaskBean.getTaskIndex()).getTaskType());
                                                ECConfig.CloseScreenOrder(LongRunningService.this);

                                                for (int i = 0 ; i < disGetTaskBean.getData().size(); i++){
                                                }
                                            } else {
                                            }
                                        }
                                    } else{

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
}
