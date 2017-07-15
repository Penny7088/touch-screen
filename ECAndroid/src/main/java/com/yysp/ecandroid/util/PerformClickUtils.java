package com.yysp.ecandroid.util;

import android.accessibilityservice.AccessibilityService;
import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.jkframework.debug.JKLog;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2017/4/18.
 */

public class PerformClickUtils {

    public static int WaitCount = 0;
    /**
     * 在当前页面查找文字内容并点击
     *
     * @param text
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void findTextAndClick(AccessibilityService accessibilityService, String text) {

        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }
        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            JKLog.i("RT", "findTextAndClick + nodeInfoList = " + nodeInfoList);
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    performClick(nodeInfo);
                    break;
                }
            }
        }
    }


    /**
     * 检查viewId进行点击
     *
     * @param accessibilityService
     * @param id
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void findViewIdAndClick(AccessibilityService accessibilityService, String id) {

        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null) {
                    performClick(nodeInfo);
                    break;
                }
            }
        }
    }


    /**
     * 在当前页面查找对话框文字内容并点击
     *
     * @param text1 默认点击text1
     * @param text2
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void findDialogAndClick(AccessibilityService accessibilityService, String text1, String text2) {

        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return;
        }

        List<AccessibilityNodeInfo> dialogWait = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text1);
        List<AccessibilityNodeInfo> dialogConfirm = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text2);
        if (!dialogWait.isEmpty() && !dialogConfirm.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : dialogWait) {
                if (nodeInfo != null && text1.equals(nodeInfo.getText())) {
                    performClick(nodeInfo);
                    break;
                }
            }
        }
    }

    //模拟点击事件
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
    }

    //模拟返回事件
    public static void performBack(AccessibilityService service) {
        if (service == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
        }
    }

    //模拟Home事件
    public static void performHome(AccessibilityService service) {
        if (service == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
            JKLog.i("RT", "task_run:home");
        }
    }



    /**
     * text
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static String geyTextById(AccessibilityService accessibilityService, String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return "";
        }

        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && nodeInfo.getText() != null) {

                    return nodeInfo.getText().toString();
                } else {
                    return "";
                }
            }
        }
        return "";
    }


    /*
    ContentDescription
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static String getContentDescriptionById(AccessibilityService accessibilityService, String id) {
        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return "";
        }

        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByViewId(id);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && nodeInfo.getContentDescription() != null) {
                    return nodeInfo.getContentDescription().toString();
                }
            }
        }

        return "";
    }  //正则匹配数字

    public static String getNumFromInfo(String numInfo) {
        Pattern pattern = Pattern.compile("[^0-9]");
        Matcher matcher = pattern.matcher(numInfo);
        String num = matcher.replaceAll("");
        return num;
    }


    public static String getSubUtilSimple(String soap, String rgex) {
        Pattern pattern = Pattern.compile(rgex);// 匹配的模式
        Matcher m = pattern.matcher(soap);
        while (m.find()) {
            return m.group(1);
        }
        return "";
    }

    /**
     * 在当前页面查找文字内容
     *
     * @param text
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static String findText(AccessibilityService accessibilityService, String text) {

        AccessibilityNodeInfo accessibilityNodeInfo = accessibilityService.getRootInActiveWindow();
        if (accessibilityNodeInfo == null) {
            return "";
        }

        List<AccessibilityNodeInfo> nodeInfoList = accessibilityNodeInfo.findAccessibilityNodeInfosByText(text);
        if (nodeInfoList != null && !nodeInfoList.isEmpty()) {
            for (AccessibilityNodeInfo nodeInfo : nodeInfoList) {
                if (nodeInfo != null && nodeInfo.getText() != null && text.equals(nodeInfo.getText().toString())) {
                    return nodeInfo.getText().toString();
                }
            }
        }
        return "";
    }


    //模拟滑动事件
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void performSwipe(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.getChildCount() > 0 && nodeInfo.getChild(0) != null) {
            for (int i = 0; i < nodeInfo.getChild(0).getChildCount(); i++) {
                if (nodeInfo.getChild(0).getChild(i).getClassName() != null) {
                    if (nodeInfo.getChild(0).getChild(i).getClassName().equals("android.widget.ListView")) {
                        AccessibilityNodeInfo node_lsv = nodeInfo.getChild(0).getChild(i);
                        node_lsv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        WaitCount = 0;
                    }
                }
            }
        }

    }

    public static void performSwipeBack(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.getChildCount() > 0 && nodeInfo.getChild(0) != null) {
            for (int i = 0; i < nodeInfo.getChild(0).getChildCount(); i++) {
                if (nodeInfo.getChild(0).getChild(i).getClassName().equals("android.widget.ListView")) {
                    AccessibilityNodeInfo node_lsv = nodeInfo.getChild(0).getChild(i);
                    node_lsv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                    WaitCount = 0;
                }
            }
        }
    }


    //模拟向上滑动事件
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void performSwipeUpOfGridView(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.getChildCount() > 0 && nodeInfo.getChild(0) != null) {
            for (int i = 0; i < nodeInfo.getChild(0).getChildCount(); i++) {
                if (nodeInfo.getChild(0).getChild(i).getClassName().equals("android.widget.GridView")) {
                    AccessibilityNodeInfo node_lsv = nodeInfo.getChild(0).getChild(i);
                    node_lsv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                    WaitCount = 0;
                }
            }
        }
    }

    //模拟向下滑动事件
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    public static void performSwipeDownOfGridView(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.getChildCount() > 0 && nodeInfo.getChild(0) != null) {
            for (int i = 0; i < nodeInfo.getChild(0).getChildCount(); i++) {
                if (nodeInfo.getChild(0).getChild(i).getClassName().equals("android.widget.GridView")) {
                    AccessibilityNodeInfo node_lsv = nodeInfo.getChild(0).getChild(i);
                    node_lsv.performAction(AccessibilityNodeInfo.ACTION_SCROLL_BACKWARD);
                    WaitCount = 0;
                }
            }
        }
    }


}