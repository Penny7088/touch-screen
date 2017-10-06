package com.yysp.ecandroid.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;

import com.yysp.ecandroid.config.Config;
import com.yysp.ecandroid.config.WeChatUIConst;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.framework.distribute.SuperTask;
import com.yysp.ecandroid.framework.util.ContactUtil;
import com.yysp.ecandroid.framework.util.Logger;
import com.yysp.ecandroid.framework.util.PerformClickUtils;

@RequiresApi(api = Build.VERSION_CODES.DONUT)
public class HelpService extends AccessibilityService {

    private boolean isChanged = false;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        //TODO 在这里处理弹框问题
        ContactUtil.mHelpServic = this;
        SuperTask.initService(this, event, this.getApplicationContext());
        if (!mActivityListenerThread.isAlive()) {
            mActivityListenerThread.start();
        }
        int event_type = event.getEventType();
        switch (event_type) {
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                isChanged = true;
                SuperTask.pageIsChange(isChanged);
                WeChatUIConst.CURRENT_PAGE = event.getClassName().toString();
                SuperTask.initCurrentPage(WeChatUIConst.CURRENT_PAGE);
//                mChange.contentChanged(this);
                showDialog();
                Logger.i("HelpService", "task_activity:" + WeChatUIConst.CURRENT_PAGE + "  taskType:" + ContactUtil.taskType + "/" + ContactUtil.isTasking);
                Config.WaitCount = 0;
                break;
            case AccessibilityEvent.TYPE_VIEW_SCROLLED:
                Logger.d("HelpService", "======TYPE_VIEW_SCROLLED=========:" + event_type);
                SuperTask.initEvent(event);
                break;
        }

        isChanged = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void showDialog() {
        if (WeChatUIConst.CURRENT_PAGE.equals(WeChatUIConst.MobileFriendUI)) {
            boolean isShowMatching = PerformClickUtils.
                    findDialogAndClick(this,
                            WeChatUIConst.dialog_matching_yes,
                            WeChatUIConst.dialog_matching_contact);

            boolean isShowDialog = PerformClickUtils.
                    findDialogAndClick(this,
                            WeChatUIConst.dialog_login_comfirm,
                            WeChatUIConst.dialog_login_text);
        }

        if (WeChatUIConst.CURRENT_PAGE.equals(WeChatUIConst.SecurityAccountVerifyUI)) {
            boolean authCodeDIalog = PerformClickUtils.
                    findDialogAndClick(this,
                            WeChatUIConst.dialog_login_comfirm,
                            WeChatUIConst.dialog_Authcode_contact);
        }

        if (WeChatUIConst.CURRENT_PAGE.equals(WeChatUIConst.SecurityAccountVerifyUI)) {
            boolean authCodeWrong = PerformClickUtils.
                    findDialogAndClick(this,
                            WeChatUIConst.dialog_login_comfirm,
                            WeChatUIConst.dialog_Authcode_wrong);
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
//                        Config.WaitCount++;
//                    }
                    if (Config.WaitCount > 60) {
                        Config.WaitCount = 0;
                        ECTaskResultResponse response = new ECTaskResultResponse();
                        response.setStatus(Config.TASK_Fail);
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
