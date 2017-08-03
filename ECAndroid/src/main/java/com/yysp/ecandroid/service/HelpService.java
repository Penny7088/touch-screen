package com.yysp.ecandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;

import com.jkframework.config.JKPreferences;
import com.jkframework.debug.JKLog;
import com.yysp.ecandroid.config.ECConfig;
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

@RequiresApi(api = Build.VERSION_CODES.DONUT)
public class HelpService extends AccessibilityService {
    String TAG = "RT";
    int taskType;

    //任务是否进行开关
    public static boolean isTasking;

    public static String ActivityName = "";

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (!mActivityListenerThread.isAlive()) {
            mActivityListenerThread.start();
        }
        int event_type = event.getEventType();
        switch (event_type) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                ActivityName = event.getClassName().toString();
                JKLog.i("RT", "task_activity:" + ActivityName + "  taskType:" + taskType + "/" + isTasking);
                ECConfig.WaitCount = 0;
                if (isTasking){
                    taskType = JKPreferences.GetSharePersistentInt("taskType");
                }
        }
    }

    @Override
    public void onInterrupt() {

    }

    /*
     登陆任务微信号
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void StarWx(){

    }


    private void doOfTaskEnd(ECTaskResultResponse resultResponse) {


        ECNetSend.taskStatus(resultResponse, this).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DisBean disBean) {
                PerformClickUtils.performHome(HelpService.this);//任务完成进入home
                OthoerUtil.doOfTaskEnd();
            }


            @Override
            public void onError(Throwable e) {
                PerformClickUtils.performHome(HelpService.this);//任务完成进入home
                OthoerUtil.doOfTaskEnd();
                OthoerUtil.AddErrorMsgUtil("taskStatus" + e.getMessage());
            }

            @Override
            public void onComplete() {
            }
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sleepAndClickText(long time, String text) {
        try {
            Thread.sleep(time);
            PerformClickUtils.findTextAndClick(this, text);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void sleepAndClickId(long time, String text) {

        try {
            Thread.sleep(time);
            PerformClickUtils.findViewIdAndClick(this, text);
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    protected Thread mActivityListenerThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    JKLog.i(TAG, "taskId:" + JKPreferences.GetSharePersistentString("taskId") + "   WaitCount = " + ECConfig.WaitCount);
                    if (!JKPreferences.GetSharePersistentString("taskId").equals("")) {
                        ECConfig.WaitCount++;
                    }
                    if (ECConfig.WaitCount > 60) {
                        ECConfig.WaitCount = 0;
                        ECTaskResultResponse response = new ECTaskResultResponse();
                        response.setStatus(ECConfig.TASK_Fail);
                        response.setDeviceAlias(AliasName);
                        response.setReason("在该界面卡死：" + ActivityName);
                        response.setTaskId(JKPreferences.GetSharePersistentString("taskId"));
                        doOfTaskEnd(response);
                    }
                    Thread.sleep(10000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    });
}
