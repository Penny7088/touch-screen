package com.yysp.ecandroid.framework.distribute;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.service.HelpService;
import com.yysp.ecandroid.framework.util.Logger;

import java.util.List;

/**
 * Created on 2017/8/24 0024.
 * by penny
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class SuperTask {

    protected static HelpService mSuper;
    private static AccessibilityEvent mEvent;

    protected SuperTask() {

    }

    protected AccessibilityNodeInfo getRootView() {
        return Delegate.getInstance().getRootView(mSuper);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected List<AccessibilityNodeInfo> findId(String id) {
        return Delegate.getInstance().findId(mSuper, id);
    }

    protected AccessibilityNodeInfo findText(String text) {
        return Delegate.getInstance().findText(mSuper, text);
    }

    protected void sleep(long time) {
        Delegate.getInstance().sleep(time);
    }

    protected void setText(List<AccessibilityNodeInfo> pNodeInfos, String text) {
        Delegate.getInstance().setText(mSuper, pNodeInfos, text);
    }

    protected void setText(AccessibilityNodeInfo pNodeInfos, String text) {
        Delegate.getInstance().setText(mSuper, pNodeInfos, text);
    }

//    protected void setPassWord(List<AccessibilityNodeInfo> pPassword, String pS) {
//        Delegate.getInstance().setPassWord(mSuper, pPassword, pS);
//    }

//    protected boolean isInput(List<AccessibilityNodeInfo> pNodeInfos, String pText) {
//        return Delegate.getInstance().isInput(mSuper, pNodeInfos, pText);
//    }

    public static void initService(HelpService pHelpService, AccessibilityEvent pEvent) {
        Logger.d("super", "====initService====");
        mSuper = pHelpService;
        mEvent = pEvent;
    }

    protected boolean listNotEmpty(List<AccessibilityNodeInfo> pList) {
        if (pList != null && pList.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean notEmpty(AccessibilityNodeInfo node) {
        if (node != null) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean rootViewNotNull() {
        AccessibilityNodeInfo lRootView = getRootView();
        if (lRootView != null) {
            return true;
        } else {
            return false;
        }
    }

    protected boolean contentIsChanged() {
        if (mEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            String packageName = mEvent.getPackageName().toString();
            Logger.d("mEvent", "=======packageName:" + packageName);
            return true;
        } else {
            return false;
        }
    }
}
