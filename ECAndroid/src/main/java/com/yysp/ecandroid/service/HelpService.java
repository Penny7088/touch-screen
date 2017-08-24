package com.yysp.ecandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;

import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.task.distribute.TaskFactory;
import com.yysp.ecandroid.util.ContactUtil;
import com.yysp.ecandroid.util.Logger;

@RequiresApi(api = Build.VERSION_CODES.DONUT)
public class HelpService extends AccessibilityService {



    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (ContactUtil.mHelpServic == null){
            ContactUtil.mHelpServic = this;
        }
        if (!mActivityListenerThread.isAlive()) {
            mActivityListenerThread.start();
        }
        int event_type = event.getEventType();
        switch (event_type) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                ContactUtil.ActivityName = event.getClassName().toString();
                Logger.i("RT", "task_activity:" + ContactUtil.ActivityName + "  taskType:" + ContactUtil.taskType + "/" + ContactUtil.isTasking);
                ECConfig.WaitCount = 0;
                TaskFactory.createTask(501,this).running();
                if (!ContactUtil.isTasking && !ContactUtil.TaskId.equals("")){
//                    isTasking = true;
//                    taskType = JKPreferences.GetSharePersistentInt("taskType");
//                    Gson gson = new Gson();
//                    TaskBean = gson.fromJson(JKPreferences.GetSharePersistentString("pushData"),DisGetTaskBean.class);
//                    StarWx();
                }
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
