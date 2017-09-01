package com.yysp.ecandroid.framework.distribute;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.view.accessibility.AccessibilityNodeInfo;

import com.yysp.ecandroid.config.PackageConst;
import com.yysp.ecandroid.service.HelpService;

import java.util.List;

/**
 * Created on 2017/8/24 0024.
 * by penny
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class Delegate implements IDelegate {

    /**
     * 是否输入成功
     */
    private boolean mInput;
    private HelpService mService;

    private Delegate() {
    }

    private static final class Singleton {
        private static final Delegate INSTANCE = new Delegate();
    }

    public static Delegate getInstance() {
        return Singleton.INSTANCE;
    }

    @Override
    public AccessibilityNodeInfo getRootView(HelpService pSuper) {
        AccessibilityNodeInfo lNodeInfo = pSuper.getRootInActiveWindow();
        if (lNodeInfo != null) {
            return lNodeInfo;
        } else {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public List<AccessibilityNodeInfo> findId(HelpService pSuper, String id) {
        AccessibilityNodeInfo lRootView = getRootView(pSuper);
        if (lRootView != null) {
            List<AccessibilityNodeInfo> nodeInfosByViewId = lRootView.findAccessibilityNodeInfosByViewId(id);
            return nodeInfosByViewId;
        } else {
            return null;
        }
    }

    @Override
    public AccessibilityNodeInfo findText(HelpService pSuper, String text) {
        AccessibilityNodeInfo nodeInfo = null;
        AccessibilityNodeInfo lRootView = getRootView(pSuper);
        if (lRootView != null) {
            List<AccessibilityNodeInfo> lInfosByText = lRootView.findAccessibilityNodeInfosByText(text);
            if (lInfosByText != null && lInfosByText.size() != 0) {
                for (AccessibilityNodeInfo info : lInfosByText) {
                    if (info.getText().toString().equals(text)) {
                        nodeInfo = info;
                        break;
                    }
                }
                return nodeInfo;
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Override
    public void setText(HelpService pSuper, List<AccessibilityNodeInfo> pNodeInfos, String text) {
        AccessibilityNodeInfo lRootView = getRootView(pSuper);
        if (lRootView != null) {
            for (AccessibilityNodeInfo i :
                    pNodeInfos) {
                if (i.getClassName().equals(PackageConst.EDIT_TEXT)) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Bundle arguments = new Bundle();
                        arguments.putCharSequence(
                                AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
                        i.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                    }
                }
            }
        }
    }

    @Override
    public void setText(HelpService pSuper, AccessibilityNodeInfo pNodeInfos, String text) {
        AccessibilityNodeInfo lRootView = getRootView(pSuper);
        if (lRootView != null) {
            if (lRootView.getClassName().equals(PackageConst.EDIT_TEXT)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Bundle arguments = new Bundle();
                    arguments.putCharSequence(
                            AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
                    lRootView.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
                }
            }
        }
    }

//    @Override
//    public void setPassWord(HelpService pSuper, List<AccessibilityNodeInfo> pNodeInfos, String text) {
//        AccessibilityNodeInfo lRootView = getRootView(pSuper);
//        if (lRootView != null) {
//            for (AccessibilityNodeInfo i :
//                    pNodeInfos) {
//                if (i.getClassName().equals(PackageConst.EDIT_TEXT)) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
//                        ClipboardManager clipboard = (ClipboardManager) App.getInstance().getContext().getSystemService(Context.CLIPBOARD_SERVICE);
//                        ClipData clip = ClipData.newPlainText("label", text);
//                        clipboard.setPrimaryClip(clip);
//                        i.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
//                        i.performAction(AccessibilityNodeInfo.ACTION_PASTE);
//                    }
//                }
//            }
//        }
//    }

//    @Override
//    public boolean isInput(HelpService pSuper, List<AccessibilityNodeInfo> pNodeInfos, String pText) {
//        sleep(500);
//        AccessibilityNodeInfo lRootView = getRootView(pSuper);
//        if (lRootView != null) {
//            for (AccessibilityNodeInfo i :
//                    pNodeInfos) {
//                if (i.getClassName().equals(PackageConst.EDIT_TEXT)) {
//                    if (i.getText().equals(pText)) {
//                        Logger.d("setText", "已输入==========");
//                        mInput = true;
//                    } else {
//                        Logger.d("setText", "未输入==========");
//                        mInput = false;
//                    }
//                }
//            }
//        }
//        return mInput;
//    }

    @Override
    public void sleep(long time) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException pE) {
            pE.printStackTrace();
        }
    }

}