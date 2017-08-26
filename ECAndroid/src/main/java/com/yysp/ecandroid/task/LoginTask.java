package com.yysp.ecandroid.task;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.config.EcWeiXinUI;
import com.yysp.ecandroid.config.PackageConst;
import com.yysp.ecandroid.task.distribute.IStrategy;
import com.yysp.ecandroid.task.distribute.SuperTask;
import com.yysp.ecandroid.util.FileUtils;
import com.yysp.ecandroid.util.Logger;
import com.yysp.ecandroid.util.PerformClickUtils;

import java.io.File;
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
        loginButton();


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void loginButton() {
        if (rootViewNotNull()) {
            List<AccessibilityNodeInfo> lNodeInfos = findId(EcWeiXinUI.login);
            for (AccessibilityNodeInfo i :
                    lNodeInfos) {
                if (i.getClassName().equals(PackageConst.BUTTON)) {
                    Logger.d("LoginTask", "=====BUTTON=======");
                    PerformClickUtils.performClick(i);
                    input(EcWeiXinUI.login_edit_phone, "14548514946");
                    input(EcWeiXinUI.login_edit_password, "123456789yysp");
                    enter();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void input(String id, String text) {
        sleep(1000);
        if (rootViewNotNull()) {
            List<AccessibilityNodeInfo> lNodeInfos = findId(id);
            if (lNodeInfos != null && lNodeInfos.size() != 0) {
                setText(lNodeInfos, text);
            }
        } else {
            Logger.d("LoginTask", "=========input=======");
        }
    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//    private void next() {
//        sleep(500);
//        if (rootViewNotNull()) {
//            List<AccessibilityNodeInfo> next = findId(EcWeiXinUI.login_next_button);
//            for (AccessibilityNodeInfo i :
//                    next) {
//                if (i.getClassName().equals(PackageConst.BUTTON)) {
//                    PerformClickUtils.performClick(i);
//                    break;
//                }
//            }
//            inputPassWord();
//        }
//    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//    private void inputPassWord() {
//        if (rootViewNotNull()) {
//            List<AccessibilityNodeInfo> password = findId(EcWeiXinUI.login_button);
//            Logger.d("LoginTask", "==========" + password.size());
//            if (listNotEmpty(password)) {
//                AccessibilityNodeInfo lAccessibilityNodeInfo = password.get(1);
//                setText(lAccessibilityNodeInfo, "123456789yysp");
//                enter();
//            }
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void enter() {
        if (rootViewNotNull()) {
            List<AccessibilityNodeInfo> enter = findId(EcWeiXinUI.login_button);
            for (AccessibilityNodeInfo i :
                    enter) {
                PerformClickUtils.performClick(i);
            }
        }
    }

}
