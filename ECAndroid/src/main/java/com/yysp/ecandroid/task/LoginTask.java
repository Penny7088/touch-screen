package com.yysp.ecandroid.task;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.config.WeChatUIConst;
import com.yysp.ecandroid.config.PackageConst;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.framework.distribute.IStrategy;
import com.yysp.ecandroid.framework.distribute.SuperTask;
import com.yysp.ecandroid.framework.net.BaseSubscriber;
import com.yysp.ecandroid.framework.net.Request;
import com.yysp.ecandroid.framework.util.FileUtils;
import com.yysp.ecandroid.framework.util.Logger;
import com.yysp.ecandroid.framework.util.MobileUtil;
import com.yysp.ecandroid.framework.util.PerformClickUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/8/24 0024.
 * by penny
 * 登录任务
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class LoginTask extends SuperTask implements IStrategy {

    long time = 1000;
    private String mSmsCode;

    public static String mobile = "13554413655";

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void running() {
        findDir();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void findDir() {
        String weChatDir = FileUtils.findDir();
        if (!TextUtils.isEmpty(weChatDir)) {
            if (weChatDir.equals(PackageConst.APP)) {//有
                boolean isDelete = FileUtils.deleteDir(weChatDir);
                if (isDelete) {
                    sleep(1000);
                    String lMkdir = FileUtils.mkdir();
                    FileUtils.permissionAs(lMkdir);
                    PerformClickUtils.launcherWeChat(mContext);
                    sleep(10000);
                    loginButton();
                }
            }
        }
    }

    @Override
    public void running(ArrayList pArrayList) {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void loginButton() {
        if (rootViewNotNull()) {
            List<AccessibilityNodeInfo> lNodeInfos = findId(WeChatUIConst.login);
            for (AccessibilityNodeInfo i :
                    lNodeInfos) {
                if (i.getClassName().equals(PackageConst.BUTTON)) {
                    PerformClickUtils.performClick(i);
                    sleep(1000);
                    jumpToLoginPage();
                }
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    /**
     * 跳入的登录界面
     */
    private void jumpToLoginPage() {
        if (currentPageIsChanged(WeChatUIConst.MobileInputUI)) {
            if (MobileUtil.isMobileNum(mobile)) {//是否是手机号或微信号
                isMobile();
            } else {
                isWeChatAccount();
            }
        } else {
            jumpToLoginPage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void isWeChatAccount() {
        AccessibilityNodeInfo account = findText(WeChatUIConst.wechat_account);
        if (nodeIsEmpty(account)) {
            PerformClickUtils.performClick(account);
            sleep();
            inputWechatAccount();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void inputWechatAccount() {
        if (currentPageIsChanged(WeChatUIConst.login_ui)) {
            List<AccessibilityNodeInfo> accountAndPasswd = findId(WeChatUIConst.input_serach_text);
            if (listNotEmpty(accountAndPasswd) && accountAndPasswd.size() == 2) {
//                setText();
            }
        } else {
            inputWechatAccount();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void isMobile() {
        input(WeChatUIConst.login_edit_phone, mobile);
        input(WeChatUIConst.login_edit_password, "moon1977");
        enter();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void input(String id, String text) {
        sleep();
        if (rootViewNotNull()) {
            List<AccessibilityNodeInfo> lNodeInfos = findId(id);
            if (lNodeInfos != null && lNodeInfos.size() != 0) {
                setText(lNodeInfos, text);
            }
        } else {
            Logger.d("rootViewNotNull", "=========rootView is null=======");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void next() {
        sleep();
        if (rootViewNotNull()) {
            List<AccessibilityNodeInfo> next = findId(WeChatUIConst.login_next_button);
            for (AccessibilityNodeInfo i :
                    next) {
                if (i.getClassName().equals(PackageConst.BUTTON)) {
                    PerformClickUtils.performClick(i);
                    break;
                }
            }
            inputPassWord();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void inputPassWord() {
        if (rootViewNotNull() && currentPageIsChanged(WeChatUIConst.MobileInputUI)) {
            List<AccessibilityNodeInfo> password = findId(WeChatUIConst.login_button);
            Logger.d("LoginTask", "==========" + password.size());
            if (listNotEmpty(password)) {
                AccessibilityNodeInfo lAccessibilityNodeInfo = password.get(1);
                setText(lAccessibilityNodeInfo, "yysp123456789");
                enter();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    /**
     * 登录
     */
    private void enter() {
        List<AccessibilityNodeInfo> enter = findId(WeChatUIConst.login_button);
        if (listNotEmpty(enter)) {
            for (AccessibilityNodeInfo i :
                    enter) {
                PerformClickUtils.performClick(i);
                sleep();
                securityAccount();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void securityAccount() {
        if (currentPageIsChanged(WeChatUIConst.SecurityAccountIntroUI)) {
            List<AccessibilityNodeInfo> securityText = findId(WeChatUIConst.security_account_text);
            if (listNotEmpty(securityText)) {
                PerformClickUtils.performClick(securityText.get(0));
                sleep();
                inputSecurityCode();
            }
        } else {
            Logger.d("LoginTask", "===没到此界面====SecurityAccountIntroUI=====");
            time = time + 1000;
            sleep(time);
            if (time == 100 * 100) {
                showScanWebView();
            } else {
                securityAccount();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void inputSecurityCode() {
        if (currentPageIsChanged(WeChatUIConst.SecurityAccountVerifyUI)) {
            List<AccessibilityNodeInfo> inputEdit = findId(WeChatUIConst.input_securty);
            if (listNotEmpty(inputEdit)) {
                //TODO 这里开始请求
                sleep();
                requestCode();
            }
        } else {
            inputSecurityCode();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void requestCode() {
        Request.getAuthCode(1, mobile).subscribe(new BaseSubscriber<DisBean>() {
            @Override
            public void onError(Throwable e) {
                requestCode();
            }

            @Override
            public void onNext(DisBean pS) {
                if (pS != null && pS.getData().getSmsCode() != null) {
                    Logger.d("Request", ":" + pS.getData().getSmsCode());
                    mSmsCode = pS.getData().getSmsCode();
                    setAuthCode();
                } else {
                    sleep();
                    requestCode();
//                    PerformClickUtils.findDialogAndClick(mSuper,WeChatUIConst.dialog_login_text)
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void setAuthCode() {
        List<AccessibilityNodeInfo> inputEdit = findId(WeChatUIConst.input_securty);
        if (listNotEmpty(inputEdit)) {
            setText(inputEdit, mSmsCode);
            sleep(1000);
            setAuthCodeFinish();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void setAuthCodeFinish() {
        List<AccessibilityNodeInfo> next = findId(WeChatUIConst.next_id);
        if (listNotEmpty(next)) {
            PerformClickUtils.performClick(next.get(0));
            sleep(10000);
            showScanWebView();
        }
    }

    private void showScanWebView() {
        if (currentPageIsChanged(WeChatUIConst.scan_code_webbiew)) {
            notifyUserScan();
        } else {
            showScanWebView();
        }
//            boolean isShowDialog = PerformClickUtils.
//                    findDialogAndClick(mSuper, WeChatUIConst.dialog_login_comfirm, WeChatUIConst.dialog_login_text);
//            boolean isShowMatching = PerformClickUtils.
//                    findDialogAndClick(mSuper,  WeChatUIConst.dialog_matching_yes,WeChatUIConst.dialog_matching_contact);
//            if (isShowDialog) {
//                //TODO 这里需要调用接口
//                notifyUserScan();
//            } else if (isShowMatching) {
//                //TODO 登录的任务结束
//
//            }
//
//        } else {
//
//            showScanWebView();
//        }

    }

    private void notifyUserScan() {
        Request.notifyUserScan(2, mobile).subscribe(new BaseSubscriber<DisBean>() {
            @Override
            public void onError(Throwable e) {
                notifyUserScan();
            }

            @Override
            public void onNext(DisBean pDisBean) {
                if (pDisBean.getCode() == 200) {
                    //结束了
                    Logger.d("LoginTask", "=============task-is-end==========");

                }
            }
        });
    }


}
