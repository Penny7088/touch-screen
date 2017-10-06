package com.yysp.ecandroid.task;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.config.WeChatUIConst;
import com.yysp.ecandroid.data.bean.ContactBean;
import com.yysp.ecandroid.framework.distribute.BaseFriendTask;
import com.yysp.ecandroid.framework.distribute.IStrategy;
import com.yysp.ecandroid.framework.util.Logger;
import com.yysp.ecandroid.framework.util.PerformClickUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 2017/9/20 0020.
 * by penny
 * 识别通讯录
 */
public class ContactDiscernTask extends BaseFriendTask implements IStrategy {


    private ArrayList<ContactBean> mContactBeanArrayList;

    @Override
    public void running(ArrayList pArrayList) {
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void running() {
        mContactBeanArrayList = new ArrayList<>();
        start();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void start() {
        addFrirnd();
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

    @Override
    public void onFailure(String m) {

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
                for (int i = 0; i < add.size(); i++) {
                    //TODO 这里需要手机通讯录的数据
                    collectData(i);
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

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    private void collectData(int pI) {
        List<AccessibilityNodeInfo> nameList = findId(WeChatUIConst.textId);
        List<AccessibilityNodeInfo> weChatNameList = findId(WeChatUIConst.WeChat_name);
        if (listNotEmpty(nameList) && listNotEmpty(weChatNameList)) {
            String name = getText(nameList.get(pI));
            String weChatName = getText(weChatNameList.get(pI));
            Logger.d("ContactDiscernTask", "name:" + name + "=====" + "weChatName" + weChatName);
            ContactBean lBean = new ContactBean();
            ArrayList<String> lArrayList = new ArrayList<>();
            lArrayList.add(name);
            lArrayList.add(weChatName);
            lBean.setTargetAccounts(lArrayList);
            mContactBeanArrayList.add(lBean);
        }
    }

}
