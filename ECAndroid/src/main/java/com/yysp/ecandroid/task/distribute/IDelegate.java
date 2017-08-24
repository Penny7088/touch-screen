package com.yysp.ecandroid.task.distribute;

import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.service.HelpService;

import java.util.List;

/**
 * Created on 2017/8/24 0024.
 * by penny
 */

public interface IDelegate {

    AccessibilityNodeInfo getRootView(HelpService pSuper);

    List<AccessibilityNodeInfo> findId(HelpService pSuper,String id);

    AccessibilityNodeInfo findText(HelpService pSuper,String text);

    void sleep(long time);



}
