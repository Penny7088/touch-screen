package com.yysp.ecandroid.task.distribute;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.service.HelpService;

import java.util.List;

/**
 * Created on 2017/8/24 0024.
 * by penny
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class SuperTask {

    protected static HelpService mSuper;

    protected SuperTask() {

    }

    protected AccessibilityNodeInfo getRootView() {
        return Delegate.getInstance().getRootView(mSuper);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected List<AccessibilityNodeInfo> findId(String id) {
        return Delegate.getInstance().findId(mSuper, id);
    }

    protected AccessibilityNodeInfo findText(String text){
        return Delegate.getInstance().findText(mSuper,text);
    }

    protected void sleep(long time){
        Delegate.getInstance().sleep(time);
    }

    public static void initService(HelpService pHelpService) {
        mSuper = pHelpService;
    }
}
