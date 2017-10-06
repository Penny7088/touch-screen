package com.yysp.ecandroid.framework.distribute;

import android.accessibilityservice.AccessibilityService;
import android.content.Context;
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

    /*服务对象随时更新*/
    protected static AccessibilityService mSuper;
    /*上下文*/
    protected static Context mContext;
    /*事件对象*/
    private static AccessibilityEvent mEvent;
    /*当前page*/
    private static String sCurrentPage;

    private static boolean isChanged;

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

    protected void scroll(AccessibilityNodeInfo pNodeInfo) {
        Delegate.getInstance().scroll(pNodeInfo);
    }

    protected void sleep() {
        Delegate.getInstance().sleep(5000);
    }

    protected void sleep(long time) {
        Delegate.getInstance().sleep(time);
    }

    protected void setText(List<AccessibilityNodeInfo> pNodeInfos, String text) {
        Delegate.getInstance().setText(mSuper, pNodeInfos, text);
    }

    protected String getText(AccessibilityNodeInfo pNodeInfo) {
        if (pNodeInfo == null) return null;
        return Delegate.getInstance().getText(pNodeInfo);
    }

    protected void setText(AccessibilityNodeInfo pNodeInfos, String text) {
        Delegate.getInstance().setText(mSuper, pNodeInfos, text);
    }

    protected boolean currentPageIsChanged(String pageName) {
        return Delegate.getInstance().currentPageIsChanged(sCurrentPage, pageName);
    }

    public static void initService(HelpService pHelpService, AccessibilityEvent pEvent, Context pApplicationContext) {
        Logger.d("super", "====initService====");
        mSuper = pHelpService;
        mEvent = pEvent;
        mContext = pApplicationContext;
    }

    protected boolean listNotEmpty(List<AccessibilityNodeInfo> pList) {
        if (pList != null && pList.size() != 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * node isEmpty
     *
     * @param node
     * @return
     */
    protected boolean nodeIsEmpty(AccessibilityNodeInfo node) {
        if (node == null) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 是否滑动
     *
     * @return 是否滑动
     */
    public boolean isScroll() {
        Logger.d("SuperTask", ":" + mEvent.getEventType());
        return Delegate.getInstance().isScroll(mEvent);
    }

    /**
     * rootView is null?
     *
     * @return
     */
    protected boolean rootViewNotNull() {
        AccessibilityNodeInfo lRootView = getRootView();
        if (lRootView != null) {
            return true;
        } else {
            return false;
        }
    }


    protected Context getContext() {
        return mContext;
    }

    public static void initCurrentPage(String pCurrentPage) {
        sCurrentPage = pCurrentPage;
    }

    public static void initEvent(AccessibilityEvent pEvent) {
        mEvent = pEvent;
    }

    public static void pageIsChange(boolean pIsChanged) {
        isChanged = pIsChanged;
    }
}
