package com.yysp.ecandroid.task;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.config.WeChatUIConst;
import com.yysp.ecandroid.framework.distribute.BaseFriendTask;
import com.yysp.ecandroid.framework.distribute.IStrategy;
import com.yysp.ecandroid.framework.util.Logger;
import com.yysp.ecandroid.framework.util.PerformClickUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/9/20 0020.
 * by penny
 * 建群
 */

public class CreateGounpTask extends BaseFriendTask implements IStrategy {
    @Override
    public void running(ArrayList pArrayList) {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void running() {
        start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void start() {
        addFrirnd();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onSucceed(String m) {
        if (m.equals(WeChatUIConst.add_)) {
            createGroup();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void createGroup() {
        AccessibilityNodeInfo createGroup = findText(WeChatUIConst.create_group);
        if (!nodeIsEmpty(createGroup)) {
            PerformClickUtils.performClick(createGroup);
            sleep();
            jumpToChooseContact();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void jumpToChooseContact() {
        if (currentPageIsChanged(WeChatUIConst.SelectContactUI)) {
            List<AccessibilityNodeInfo> checkBox = findId(WeChatUIConst.check_box);
            List<AccessibilityNodeInfo> list = findId(WeChatUIConst.group_chat_list);
            if (listNotEmpty(checkBox)) {
                for (AccessibilityNodeInfo i :
                        checkBox) {
                    if (!i.isChecked()) {
                        PerformClickUtils.performClick(i);
                        sleep(500);
                    }
                }

                if (listNotEmpty(list)) {
                    sleep(1000);
                    scroll(list.get(0));
                    sleep();
                    if (!isScroll()) {
                        Logger.d("CreateGounpTask", "=====" + "没有滑动任务完成...");
                    } else {
                        jumpToChooseContact();
                    }
                }
            }
        }
    }

    @Override
    public void onFailure(String m) {

    }
}
