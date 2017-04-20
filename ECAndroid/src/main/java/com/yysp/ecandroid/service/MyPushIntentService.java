package com.yysp.ecandroid.service;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityManager;

import com.google.gson.Gson;
import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.control.JKToast;
import com.jkframework.debug.JKLog;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;
import com.yysp.ecandroid.config.ECSdCardPath;
import com.yysp.ecandroid.data.bean.EcContactBean;
import com.yysp.ecandroid.util.ContactToAddUtil;
import com.yysp.ecandroid.util.PerformClickUtils;
import com.yysp.ecandroid.view.activity.ECTaskActivity;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Administrator on 2017/4/17.
 */

public class MyPushIntentService extends UmengMessageService {
    @Override
    public void onMessage(Context context, Intent intent) {
        String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        try {
            UMessage msg = new UMessage(new JSONObject(message));
            //添加联系人到通讯录
//            AddToContact(context, msg.text);
//            JKPreferences.SaveSharePersistent("doTasking", true);
            JKLog.i("RT", "push:" + msg.custom);
            startTask(msg.custom);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void startTask(String msg) {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            JKToast.Show("找到检测辅助功能，然后开启服务即可", 0);
        } else {
            //开启任务
            if (msg.equals("开始")) {
                JKPreferences.SaveSharePersistent("doTasking", true);
                Intent intent = new Intent();
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
                startActivity(intent);
            }
        }

    }

    /*
    通过服务端发送的json将手机号加入通讯录
     */
    private void AddToContact(Context context, String custom) {
        if (custom != null && TextUtils.isEmpty(custom)) {
            Gson gson = new Gson();
            List<String> list = gson.fromJson(custom, EcContactBean.class).getTargetMobiles();
            for (String phone : list) {
                ContactToAddUtil.addContact(context, phone);
            }
        }
    }
}
