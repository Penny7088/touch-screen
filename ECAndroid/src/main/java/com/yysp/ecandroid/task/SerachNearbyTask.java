package com.yysp.ecandroid.task;

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
 * Created on 2017/8/24 0024.
 * by penny
 * 搜索附近好友任务
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class SerachNearbyTask extends SuperTask implements IStrategy {

    @Override
    public void running(ArrayList pArrayList) {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void running() {
        Logger.d("SearchNearbyTask", "=========================");
        start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void start() {
        AccessibilityNodeInfo discover = findText(WeChatUIConst.discover);
        if (!nodeIsEmpty(discover)) {
            PerformClickUtils.performClick(discover);
            sleep();
            jumpDiscoverPage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void jumpDiscoverPage() {
        AccessibilityNodeInfo nearbyPerson = findText(WeChatUIConst.nearby_person);
        if (!nodeIsEmpty(nearbyPerson)) {
            PerformClickUtils.performClick(nearbyPerson);
            sleep();
            PerformClickUtils.findDialogAndClick(mSuper, "确定", "取消");
            sleep(10000);
            jumpNearbyPage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void jumpNearbyPage() {
        if (currentPageIsChanged(WeChatUIConst.NearbyFriendsUI)) {
//                scroll(listView.get(0));
            List<AccessibilityNodeInfo> nearby_item = findId(WeChatUIConst.nearby_item);
            if (listNotEmpty(nearby_item)) {
                for (AccessibilityNodeInfo i :
                        nearby_item) {
                    PerformClickUtils.performClick(i);
                    sleep();
                    boolean isNearbyPage = jumpNearbyDetailPage();
                    if (!isNearbyPage) {
                        sleep(1000);
                        continue;
                    }
                }

                List<AccessibilityNodeInfo> listView = findId(WeChatUIConst.nearby_listview);
                if (listNotEmpty(listView)) {
                    scroll(listView.get(0));
                    sleep();
                    jumpNearbyPage();
                }

            } else {
                PerformClickUtils.performBack(mSuper);
                jumpDiscoverPage();
            }
        } else {
            PerformClickUtils.performBack(mSuper);
            sleep();
            jumpDiscoverPage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private boolean jumpNearbyDetailPage() {
        if (currentPageIsChanged(WeChatUIConst.ContactInfoUI)) {
            AccessibilityNodeInfo sayHi = findText(WeChatUIConst.say_hi);
            if (nodeIsEmpty(sayHi)) {
                PerformClickUtils.performClick(sayHi);
                sleep();
                jumpSayHiPage();
            }
            return true;
        } else {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void jumpSayHiPage() {
        if (currentPageIsChanged(WeChatUIConst.say_hi_edit_ui)) {
            List<AccessibilityNodeInfo> sayHi = findId(WeChatUIConst.say_hi_edit);
            if (listNotEmpty(sayHi)) {
                setText(sayHi, "你好");
                sleep();
                send();
            }
        }
    }

    private void send() {
        AccessibilityNodeInfo send = findText(WeChatUIConst.send);
        if (nodeIsEmpty(send)) {
            PerformClickUtils.performClick(send);
            sleep(10000);
            if (currentPageIsChanged(WeChatUIConst.ContactInfoUI)) {
                sleep();
                PerformClickUtils.performBack(mSuper);
            }
        }
    }


}
