package com.yysp.ecandroid.task;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.config.WeChatUIConst;
import com.yysp.ecandroid.data.TestBeautifyModel;
import com.yysp.ecandroid.framework.distribute.BaseFriendTask;
import com.yysp.ecandroid.framework.distribute.IStrategy;
import com.yysp.ecandroid.framework.util.ContactUtil;
import com.yysp.ecandroid.framework.util.PerformClickUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/9/11 0011.
 * by penny
 * 通讯录加友
 */

public class AddContactTask extends BaseFriendTask implements IStrategy {

    private ArrayList<TestBeautifyModel> mArrayList;

    @Override
    public void running() {

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void running(ArrayList pArrayList) {
        mArrayList = pArrayList;
        startImportContacts();
    }

    /**
     * 导入通讯录
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void startImportContacts() {
//        importContact();
        addFrirnd();
    }

    /**
     * 开始导入通讯录
     */
    private void importContact() {
        int lSize = mArrayList.size();
        for (int i = 0; i < lSize; i++) {
            ContactUtil.addContact(mContext,
                    mArrayList.get(i).getPhone(),
                    mArrayList.get(i).getCompany());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void jumpContactsPage() {
        if (currentPageIsChanged(WeChatUIConst.AddMoreFriendsUI)) {
            AccessibilityNodeInfo phoneContacts = findText(WeChatUIConst.phone_contact);
            if (!nodeIsEmpty(phoneContacts)) {
                PerformClickUtils.performClick(phoneContacts);
                sleep();
                jumpAddFriendsPage();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void jumpAddFriendsPage() {
        if (currentPageIsChanged(WeChatUIConst.AddMoreFriendsByOtherWayUI)) {
            AccessibilityNodeInfo addPhoneText = findText(WeChatUIConst.add_phone_contact);
            PerformClickUtils.performClick(addPhoneText);
            sleep(10000);
            jumpMobilePage();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void jumpMobilePage() {
        if (currentPageIsChanged(WeChatUIConst.MobileFriendUI)) {
            List<AccessibilityNodeInfo> add = findId(WeChatUIConst.add_bt);
            if (listNotEmpty(add)) {
                for (AccessibilityNodeInfo i :
                        add) {
                    PerformClickUtils.performClick(i);
                    sleep();
                    boolean lApplyPage = jumpApplyPage();
                    if (!lApplyPage) {
                        sleep(1000);
                        continue;
                    }
                }

                //循环结束 滑动整屏
                List<AccessibilityNodeInfo> list_contact = findId(WeChatUIConst.contact_list);
                if (listNotEmpty(list_contact)) {
                    scroll(list_contact.get(0));
                    sleep();
                    jumpMobilePage();
                }

            }
        } else {
            PerformClickUtils.performBack(mSuper);
            sleep();
            jumpAddFriendsPage();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private boolean jumpApplyPage() {
        String lSubstring = null;
        if (currentPageIsChanged(WeChatUIConst.SayHi)) {// in sayHi page
            //获取提交好友申请文本
            List<AccessibilityNodeInfo> userInfo = findId(WeChatUIConst.cache_in_phone);
            if (listNotEmpty(userInfo)) {
                for (AccessibilityNodeInfo i :
                        userInfo) {
                    String text = i.getText().toString();
                    if (!TextUtils.isEmpty(text)) {
                        lSubstring = text.substring(text.indexOf("“"), text.indexOf("”"));
                    }
                }
            }
            //设置好友申请文本
            List<AccessibilityNodeInfo> desc = findId(WeChatUIConst.description);
            if (listNotEmpty(desc)) {
                setText(desc, lSubstring + "你们店团购顾客");
                sleep();
                send();
            }
            return true;
        } else {//no sayHi page
            if (currentPageIsChanged(WeChatUIConst.ContactInfoUI)) {
                PerformClickUtils.performBack(mSuper);
                sleep();
                return true;
            } else {
                return false;
            }
        }
    }


    /**
     * 发送好友请求
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    protected void send() {
        AccessibilityNodeInfo send = findText(WeChatUIConst.send);
        if (!nodeIsEmpty(send)) {
            PerformClickUtils.performClick(send);
            sleep();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onSucceed(String m) {
        if (m.equals(WeChatUIConst.add_)) {
            jumpAddFriendPage();
        } else if (m.equals(WeChatUIConst.jump_add_friend_text)) {
            jumpContactsPage();
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
