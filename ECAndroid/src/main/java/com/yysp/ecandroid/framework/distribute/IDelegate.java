package com.yysp.ecandroid.framework.distribute;

import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.service.HelpService;

import java.util.List;

/**
 * Created on 2017/8/24 0024.
 * by penny
 */

public interface IDelegate {

    AccessibilityNodeInfo getRootView(HelpService pSuper);

    List<AccessibilityNodeInfo> findId(HelpService pSuper, String id);

    AccessibilityNodeInfo findText(HelpService pSuper, String text);

    void setText(HelpService pSuper, List<AccessibilityNodeInfo> pNodeInfos, String text);

    void setText(HelpService pSuper, AccessibilityNodeInfo pNodeInfos, String text);

//    boolean isInput(HelpService pSuper, List<AccessibilityNodeInfo> pNodeInfos, String pText);

    void sleep(long time);


}
