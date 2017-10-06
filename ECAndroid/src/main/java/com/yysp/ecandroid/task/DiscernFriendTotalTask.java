package com.yysp.ecandroid.task;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.config.WeChatUIConst;
import com.yysp.ecandroid.framework.distribute.IStrategy;
import com.yysp.ecandroid.framework.distribute.SuperTask;
import com.yysp.ecandroid.framework.util.Logger;
import com.yysp.ecandroid.framework.util.PerformClickUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/9/22 0022.
 * by penny
 * 识别好友数量
 */

public class DiscernFriendTotalTask extends SuperTask implements IStrategy {

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void running() {
        jumpContactPage();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void jumpContactPage() {
        if (currentPageIsChanged(WeChatUIConst.LauncherUI)) {
            AccessibilityNodeInfo contact = findText(WeChatUIConst.contacts);
            if (!nodeIsEmpty(contact)) {
                PerformClickUtils.performClick(contact);
                sleep();
                click_井();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void click_井() {
        List<AccessibilityNodeInfo> index = findId(WeChatUIConst.indexes);
        if (listNotEmpty(index)) {
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void findContactTotal() {
        List<AccessibilityNodeInfo> total = findId(WeChatUIConst.contact_total);
        if (listNotEmpty(total)) {
            String total_text = getText(total.get(0));
            Logger.d("DiscernFriendTotalTask", "======" + total_text);
            if (total_text.contains("联系人")) {
                Logger.d("DiscernFriendTotalTask", "任务完成");
            }
        } else {
            List<AccessibilityNodeInfo> list = findId(WeChatUIConst.list);
            if (listNotEmpty(list)) {
                scroll(list.get(0));
                sleep(1000);
                findContactTotal();
            }
        }
    }

    @Override
    public void running(ArrayList pArrayList) {
    }
}
