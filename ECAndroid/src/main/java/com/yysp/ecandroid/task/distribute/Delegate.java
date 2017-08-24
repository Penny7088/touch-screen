package com.yysp.ecandroid.task.distribute;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.application.App;
import com.yysp.ecandroid.service.HelpService;

import java.util.List;

/**
 * Created on 2017/8/24 0024.
 * by penny
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class Delegate implements IDelegate {

    private Delegate(){}

    private static final class Singleton {
        private static final Delegate INSTANCE = new Delegate();
    }

    public static Delegate getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public AccessibilityNodeInfo getRootView(HelpService pSuper) {
        AccessibilityNodeInfo lNodeInfo = pSuper.getRootInActiveWindow();
        if (lNodeInfo != null) {
            return lNodeInfo;
        } else {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public List<AccessibilityNodeInfo> findId(HelpService pSuper, String id) {
        AccessibilityNodeInfo lRootView = getRootView(pSuper);
        if (lRootView != null) {
            List<AccessibilityNodeInfo> nodeInfosByViewId = lRootView.findAccessibilityNodeInfosByViewId(id);
            return nodeInfosByViewId;
        } else {
            return null;
        }
    }

    @Override
    public AccessibilityNodeInfo findText(HelpService pSuper, String text) {
        AccessibilityNodeInfo nodeInfo = null;
        AccessibilityNodeInfo lRootView = getRootView(pSuper);
        if (lRootView != null) {
            List<AccessibilityNodeInfo> lInfosByText = lRootView.findAccessibilityNodeInfosByText(text);
            if (lInfosByText != null && lInfosByText.size() != 0) {
                for (AccessibilityNodeInfo info : lInfosByText) {
                    if (info.getText().toString().equals(text)) {
                        nodeInfo = info;
                        break;
                    }
                }
                return nodeInfo;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void sleep(long time) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException pE) {
            pE.printStackTrace();
        }
    }


}