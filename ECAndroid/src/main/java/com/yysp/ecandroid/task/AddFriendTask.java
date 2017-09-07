package com.yysp.ecandroid.task;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.config.EcWeiXinUI;
import com.yysp.ecandroid.data.TestBeautifyModel;
import com.yysp.ecandroid.framework.crashMonitor.ui.utils.PackageUtils;
import com.yysp.ecandroid.framework.distribute.IStrategy;
import com.yysp.ecandroid.framework.distribute.SuperTask;
import com.yysp.ecandroid.framework.util.Logger;
import com.yysp.ecandroid.framework.util.PerformClickUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/8/24 0024.
 * by penny
 * 添加好友任务
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class AddFriendTask extends SuperTask implements IStrategy {

    private ArrayList<TestBeautifyModel> mArrayList;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void running() {
        Logger.d("AddFriendTask", "=========================");
        addFrirnd();
        sleep(1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void running(ArrayList pArrayList) {
        Logger.d("AddFriendTask", "=============pArrayList============");
        mArrayList = pArrayList;
        addFrirnd();
        sleep(1000);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void addFrirnd() {
        List<AccessibilityNodeInfo> addNode = findId(EcWeiXinUI.add_);
        if (addNode != null && addNode.size() != 0) {
            for (AccessibilityNodeInfo o :
                    addNode) {
                PerformClickUtils.performClick(o);
                sleep(500);
                jumpAddFriend();
            }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void jumpAddFriend() {
        AccessibilityNodeInfo hiNode = findText(EcWeiXinUI.jump_add_friend_text);
        if (hiNode != null) {
            PerformClickUtils.performClick(hiNode);
            sleep(5000);
            startAddFriendPage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void startAddFriendPage() {
        AccessibilityNodeInfo text = findText(EcWeiXinUI.jump_add_page_text);
        PerformClickUtils.findTextAndClick(mSuper,EcWeiXinUI.jump_add_page_text);
        sleep(1000);
        PerformClickUtils.findTextAndClick(mSuper,EcWeiXinUI.jump_add_page_text);
        sleep(1000);
        PerformClickUtils.findTextAndClick(mSuper,EcWeiXinUI.jump_add_page_text);
//        List<AccessibilityNodeInfo> lInfoList = findId(EcWeiXinUI.jump_add_page_id);
//        if (listNotEmpty(lInfoList)) {
//            for (AccessibilityNodeInfo i :
//                    lInfoList) {
//                String text = i.getChild(0).getText().toString();
//                Logger.d("AddFriendTask", "==text=:" + text);
//                if (text.equals(EcWeiXinUI.jump_add_page_text)) {
//                    click(i);
//                PerformClickUtils.performClick(i);
//                sleep(1000);
//                    Logger.d("AddFriendTask", "==text=:");
//                break;
//            }
//        }
//                inputNum();
//            }
//        }
    }

    private void click(AccessibilityNodeInfo i) {
        if (i.isClickable()) {
            i.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            Logger.d("AddFriendTask", "===click:" + i.toString());
        } else {
            click(i.getParent());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void inputNum() {
        List<AccessibilityNodeInfo> serach = findId(EcWeiXinUI.input_serach_text);
        if (listNotEmpty(serach)) {
            String lPhone = mArrayList.get(0).getPhone();
            Logger.d("AddFriendTask:", lPhone);
            Logger.d("AddFriendTask:", serach.size() + "");
            for (int i = 0; i < serach.size(); i++) {
                AccessibilityNodeInfo lNodeInfo = serach.get(i);
//                Logger.d("AddFriendTask:",lNodeInfo);
            }
//            for (AccessibilityNodeInfo i :
//                    serach) {
//                String text = i.getText().toString();
//                String className = i.getClassName().toString();
//                Logger.d("AddFriendTask:", "className:" + className + "=====" + "text:" + text);
//
//            }
//            setText(serach, lPhone);
//            for (AccessibilityNodeInfo i :
//                    serach) {
//                setText(i, mArrayList.get(0).getPhone());
//
//            }
        }
    }
}
