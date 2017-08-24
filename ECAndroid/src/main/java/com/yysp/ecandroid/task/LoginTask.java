package com.yysp.ecandroid.task;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.config.EcWeiXinUI;
import com.yysp.ecandroid.config.PackageConst;
import com.yysp.ecandroid.service.HelpService;
import com.yysp.ecandroid.task.distribute.IStrategy;
import com.yysp.ecandroid.task.distribute.SuperTask;
import com.yysp.ecandroid.util.Logger;
import com.yysp.ecandroid.util.PerformClickUtils;

import java.util.List;

/**
 * Created on 2017/8/24 0024.
 * by penny
 * 登录任务
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class LoginTask extends SuperTask implements IStrategy {


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void running() {
        AccessibilityNodeInfo lRootView = getRootView();
        if (lRootView != null) {
            List<AccessibilityNodeInfo> lNodeInfos = findId(EcWeiXinUI.login);
            for (AccessibilityNodeInfo i :
                    lNodeInfos) {
                if (i.getClassName().equals(PackageConst.BUTTON)) {
                    Logger.d("LoginTask","=====BUTTON=======");
                    PerformClickUtils.performClick(i);
                }
            }
        }
    }
}
