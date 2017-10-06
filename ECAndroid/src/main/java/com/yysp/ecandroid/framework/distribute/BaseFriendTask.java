package com.yysp.ecandroid.framework.distribute;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.config.WeChatUIConst;
import com.yysp.ecandroid.framework.util.PerformClickUtils;

import java.util.List;

/**
 * Created on 2017/9/12 0012.
 * by penny
 */

public abstract class BaseFriendTask extends SuperTask implements IResult{

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected void addFrirnd() {
        List<AccessibilityNodeInfo> addNode = findId(WeChatUIConst.add_);
        if (addNode != null && addNode.size() != 0) {
            for (AccessibilityNodeInfo o :
                    addNode) {
                PerformClickUtils.performClick(o);
                sleep();
            }
            onSucceed(WeChatUIConst.add_);
        } else {
            onFailure(WeChatUIConst.add_);
        }
    }



    /**
     * 跳转到加友页面
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    protected void jumpAddFriendPage() {
        AccessibilityNodeInfo hiNode = findText(WeChatUIConst.jump_add_friend_text);
        if (hiNode != null) {
            PerformClickUtils.performClick(hiNode);
            sleep();
            onSucceed(WeChatUIConst.jump_add_friend_text);
        } else {
            onFailure(WeChatUIConst.jump_add_friend_text);
        }
    }

}
