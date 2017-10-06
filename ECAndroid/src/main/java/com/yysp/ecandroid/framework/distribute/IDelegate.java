package com.yysp.ecandroid.framework.distribute;

import android.accessibilityservice.AccessibilityService;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.service.HelpService;

import java.util.List;

/**
 * Created on 2017/8/24 0024.
 * by penny
 */

public interface IDelegate {

    AccessibilityNodeInfo getRootView(AccessibilityService pSuper);

    List<AccessibilityNodeInfo> findId(AccessibilityService pSuper, String id);

    AccessibilityNodeInfo findText(AccessibilityService pSuper, String text);

    void setText(AccessibilityService pSuper, List<AccessibilityNodeInfo> pNodeInfos, String text);

    void setText(AccessibilityService pSuper, AccessibilityNodeInfo pNodeInfos, String text);

    void scroll(AccessibilityNodeInfo pNodeInfos);

    String getText(AccessibilityNodeInfo pNodeInfo);

    void sleep(long time);

    boolean isScroll(AccessibilityEvent pAccessibilityEvent);

    boolean currentPageIsChanged(String currentPage, String pageName);

}
