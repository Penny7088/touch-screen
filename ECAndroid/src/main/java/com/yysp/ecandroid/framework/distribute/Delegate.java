package com.yysp.ecandroid.framework.distribute;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.config.PackageConst;
import com.yysp.ecandroid.framework.util.Logger;
import com.yysp.ecandroid.service.HelpService;

import java.util.List;

/**
 * Created on 2017/8/24 0024.
 * by penny
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class Delegate implements IDelegate {

    /**
     * 是否输入成功
     */
    private boolean mInput;
    private HelpService mService;

    private Delegate() {
    }

    private static final class Singleton {
        private static final Delegate INSTANCE = new Delegate();
    }

    public static Delegate getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public AccessibilityNodeInfo getRootView(AccessibilityService pSuper) {
        AccessibilityNodeInfo lNodeInfo = pSuper.getRootInActiveWindow();
        if (lNodeInfo != null) {
            return lNodeInfo;
        } else {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public List<AccessibilityNodeInfo> findId(AccessibilityService pSuper, String id) {
        AccessibilityNodeInfo lRootView = getRootView(pSuper);
        if (lRootView != null) {
            List<AccessibilityNodeInfo> nodeInfosByViewId = lRootView.findAccessibilityNodeInfosByViewId(id);
            return nodeInfosByViewId;
        } else {
            return null;
        }
    }

    @Override
    public AccessibilityNodeInfo findText(AccessibilityService pSuper, String text) {
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
    public void setText(AccessibilityService pSuper, List<AccessibilityNodeInfo> pNodeInfos, String text) {
        AccessibilityNodeInfo lRootView = getRootView(pSuper);
        if (lRootView != null) {
            Logger.d("AddFriendTask", "开始输入.....");
            for (AccessibilityNodeInfo i :
                    pNodeInfos) {
                if (i.getClassName().equals(PackageConst.EDIT_TEXT)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Bundle arguments = new Bundle();
                        arguments.putCharSequence(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
                        i.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                        Logger.d("AddFriendTask", "输入成功" + text);
                    }
                }
            }
        }
    }

    @Override
    public void setText(AccessibilityService pSuper, AccessibilityNodeInfo pNodeInfos, String text) {
        AccessibilityNodeInfo lRootView = getRootView(pSuper);
        if (lRootView != null) {
            if (pNodeInfos.getClassName().equals(PackageConst.EDIT_TEXT)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
                    lRootView.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                }
            }
        }
    }

    @Override
    public void scroll(AccessibilityNodeInfo pNodeInfos) {
        if (pNodeInfos.isScrollable()) {
            pNodeInfos.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        } else {
            scroll(pNodeInfos.getParent());
        }
    }

    @Override
    public String getText(AccessibilityNodeInfo pNodeInfo) {
        return pNodeInfo.getText().toString();
    }

    @Override
    public void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException pE) {
            pE.printStackTrace();
        }
    }

    @Override
    public boolean isScroll(AccessibilityEvent pAccessibilityEvent) {
        int lEventType = pAccessibilityEvent.getEventType();
        Logger.d("Delegate", ":" + lEventType);
        if (lEventType == AccessibilityEvent.TYPE_VIEW_SCROLLED) {
            Logger.d("Delegate", "yes scroll");
            return true;
        } else {
            Logger.d("Delegate", "no scroll");
            return false;
        }
    }

    @Override
    public boolean currentPageIsChanged(String currentPage, String pageName) {
        if (currentPage.equals(pageName)) {
            return true;
        } else {
            return false;
        }
    }

}