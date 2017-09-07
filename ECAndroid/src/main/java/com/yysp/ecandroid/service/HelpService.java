package com.yysp.ecandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;

import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.framework.distribute.SuperTask;
import com.yysp.ecandroid.framework.util.ContactUtil;
import com.yysp.ecandroid.framework.util.Logger;

@RequiresApi(api = Build.VERSION_CODES.DONUT)
public class HelpService extends AccessibilityService {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        ContactUtil.mHelpServic = this;
        SuperTask.initService(this, event, this.getApplicationContext());
        if (!mActivityListenerThread.isAlive()) {
            mActivityListenerThread.start();
        }
        int event_type = event.getEventType();
        switch (event_type) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                ContactUtil.ActivityName = event.getClassName().toString();
//                mChange.contentChanged(this);
                Logger.i("HelpService", "task_activity:" + ContactUtil.ActivityName + "  taskType:" + ContactUtil.taskType + "/" + ContactUtil.isTasking);
                ECConfig.WaitCount = 0;
        }
    }

    @Override
    public void onInterrupt() {

    }


    protected Thread mActivityListenerThread = new Thread(new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
//                    if (!JKPreferences.GetSharePersistentString("taskId").equals("")) {
//                        ECConfig.WaitCount++;
//                    }
                    if (ECConfig.WaitCount > 60) {
                        ECConfig.WaitCount = 0;
                        ECTaskResultResponse response = new ECTaskResultResponse();
                        response.setStatus(ECConfig.TASK_Fail);
//                        response.setDeviceAlias(AliasName);
                        response.setReason("在该界面卡死：" + ContactUtil.ActivityName);
                        response.setTaskId(ContactUtil.TaskId);
                        ContactUtil.doOfTaskEnd(response, HelpService.this);
                    }
                    Thread.sleep(10000);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }
    });
}
