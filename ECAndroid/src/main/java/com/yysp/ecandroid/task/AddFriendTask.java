package com.yysp.ecandroid.task;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.config.WeChatUIConst;
import com.yysp.ecandroid.data.TestBeautifyModel;
import com.yysp.ecandroid.data.test;
import com.yysp.ecandroid.framework.distribute.BaseFriendTask;
import com.yysp.ecandroid.framework.distribute.IStrategy;
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
public class AddFriendTask extends BaseFriendTask implements IStrategy {

    public static final String TAG = "AddFriendTask";
    private ArrayList<TestBeautifyModel> mArrayList;
    private TestBeautifyModel mCurrentModel;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void running() {
        Logger.d("AddFriendTask", "=========================");
        addFrirnd();
        sleep();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void running(ArrayList pArrayList) {
        Logger.d("AddFriendTask", "=============running============");
        mArrayList = pArrayList;
        addFrirnd();
    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//    private void addFrirnd() {
//        List<AccessibilityNodeInfo> addNode = findId(WeChatUIConst.add_);
//        if (addNode != null && addNode.size() != 0) {
//            for (AccessibilityNodeInfo o :
//                    addNode) {
//                PerformClickUtils.performClick(o);
//                sleep();
//                jumpAddFriendPage();
//            }
//
//        }
//    }

//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
//    /**
//     * 跳转到加友页面
//     */
//    private void jumpAddFriendPage() {
//        AccessibilityNodeInfo hiNode = findText(WeChatUIConst.jump_add_friend_text);
//        if (hiNode != null) {
//            PerformClickUtils.performClick(hiNode);
//            sleep();
//            startAddFriendPage();
//        }
//    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    /**
     * 跳转到查找用户页面
     */
    private void startAddFriendPage() {
        if (currentPageIsChanged(WeChatUIConst.AddMoreFriendsUI)) {
            Logger.d(TAG, "当前是AddMoreFriends页面");
            AccessibilityNodeInfo text = findText(WeChatUIConst.jump_add_page_text);
            if (text != null) {
            //TODO 这里需要
            }
        } else {
            Logger.d(TAG, "当前不是AddMoreFriends页面");
            PerformClickUtils.performBack(mSuper);
            addFrirnd();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    /**
     * 输入用户号码
     */
    private void jumpInputNumPage() {
        if (currentPageIsChanged(WeChatUIConst.FTSAddFriendUI)) {
            Logger.d(TAG, "当前是输入电话号码页面");
            for (int i = 0; i < mArrayList.size(); i++) {
                List<AccessibilityNodeInfo> serach = findId(WeChatUIConst.input_serach_text);
                if (listNotEmpty(serach)) {
                    String lPhone = mArrayList.get(i).getPhone();
                    boolean lMobileNum = test.isMobileNum(lPhone);
                    if (!lMobileNum) {
                        continue;
                    } else {
                        Logger.d("AddFriendTask:", "inputPhone=======");
                        inputPhone(serach, i, lPhone);
                    }
                }
            }
        } else {
            PerformClickUtils.performBack(mSuper);
            sleep();
            PerformClickUtils.performBack(mSuper);
            sleep();
            startAddFriendPage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void inputPhone(List<AccessibilityNodeInfo> pSerach, int posstion, String pPhone) {
        mCurrentModel = mArrayList.get(posstion);
        if (pPhone == null) return;
        setText(pSerach, pPhone);
        Logger.d("AddFriendTask:", pPhone);
        sleep();
        findSearchAccount();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void findSearchAccount() {
        List<AccessibilityNodeInfo> serach = findId(WeChatUIConst.serach_text_list);
//        AccessibilityNodeInfo userIsExist = findText(WeChatUIConst.user_is_exist);
        if (listNotEmpty(serach)/* && nodeIsEmpty(userIsExist)*/) {
            Logger.d(TAG, "该用户存在...");
            PerformClickUtils.performClick(serach.get(0));
            sleep(8000);
            jumpToAddContactPage();
        } else {
            Logger.d(TAG, "该用户不存在...");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void jumpToAddContactPage() {
        if (currentPageIsChanged(WeChatUIConst.ContactInfoUI)) {
            Logger.d(TAG, "当前是添加好友联系人界面");
            AccessibilityNodeInfo addContact = findText(WeChatUIConst.add_contact);
            if (!nodeIsEmpty(addContact)) {
                PerformClickUtils.performClick(addContact);
                sleep(10000);
                sendApplyFriendPage();
            } else {
                PerformClickUtils.performBack(mSuper);
                sleep();
            }
        } else {
            AccessibilityNodeInfo userIsExist = findText(WeChatUIConst.user_is_exist);
            if (nodeIsEmpty(userIsExist)) {
                return;
            }
//            PerformClickUtils.performBack(mSuper);
//            sleep();
//            findSearchAccount();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    /**
     * 好友申请页面
     */
    private void sendApplyFriendPage() {
        if (currentPageIsChanged(WeChatUIConst.SayHi)) {
            List<AccessibilityNodeInfo> desc = findId(WeChatUIConst.description);
            if (listNotEmpty(desc)) {
                setText(desc, mCurrentModel.getCompany() + "你们店团购顾客");
                sleep(1000);
                send();
            } else {
                sendApplyFriendPage();
            }
        } else {
            PerformClickUtils.performBack(mSuper);
            sleep();
            jumpToAddContactPage();
        }
    }

    /**
     * 发送好友请求
     */
    private void send() {
        AccessibilityNodeInfo send = findText(WeChatUIConst.send);
        if (!nodeIsEmpty(send)) {
            PerformClickUtils.performClick(send);
            sleep();
            PerformClickUtils.performBack(mSuper);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onSucceed(String m) {
        if (m.equals(WeChatUIConst.add_)) {
            jumpAddFriendPage();
        } else if (m.equals(WeChatUIConst.jump_add_friend_text)) {
            startAddFriendPage();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onFailure(String m) {
        if (m.equals(WeChatUIConst.add_)) {
            addFrirnd();
        } else if (m.equals(WeChatUIConst.jump_add_friend_text)) {
            jumpAddFriendPage();
        }
    }
}