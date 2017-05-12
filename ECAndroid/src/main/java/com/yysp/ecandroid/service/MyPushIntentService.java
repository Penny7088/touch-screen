package com.yysp.ecandroid.service;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityManager;

import com.google.gson.Gson;
import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.config.JKSystem;
import com.jkframework.control.JKToast;
import com.jkframework.debug.JKLog;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;
import com.yysp.ecandroid.config.ECSdCardPath;
import com.yysp.ecandroid.data.bean.EcContactBean;
import com.yysp.ecandroid.data.bean.EcGetTaskBean;
import com.yysp.ecandroid.data.bean.EcPostBean;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.util.ContactUtil;
import com.yysp.ecandroid.view.activity.ECTaskActivity;

import org.android.agoo.common.AgooConstants;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Administrator on 2017/4/17.
 */

public class MyPushIntentService extends UmengMessageService {

    //taskType
    public final static int SearchAddFriendType = 501;
    public final static int ContactAddFriendTpye = 502;
    public final static int CreatGroupType = 504;
    public final static int GetGroupPeoPleNum = 506;
    public final static int getCreatGroupInfo = 508;
    public final static int DetectionTask = 507;
    public final static int GetWxUserInfo = 509;
    //任务是否在执行
    boolean isrunning=false;

    @Override
    public void onMessage(Context context, Intent intent) {
        String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        try {
            UMessage msg = new UMessage(new JSONObject(message));
            JKLog.i("RT", "tasks:" + msg.custom);
            startTask(msg.custom);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void startTask(String msg) {
        //开启任务
        Gson gson = new Gson();
        EcGetTaskBean ecGetTaskBean = gson.fromJson(msg, EcGetTaskBean.class);
        JKPreferences.SaveSharePersistent("taskType", ecGetTaskBean.getTaskType());
        //标记任务是否在运行,运行则不写入文件
        if (ecGetTaskBean.getTaskType() == 0) {
            ECNetSend.taskApply(ecGetTaskBean.getTaskType(), JKSystem.GetGUID()).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                    new Observer<EcPostBean>() {
                        @Override
                        public void onSubscribe(Disposable d) {

                        }

                        @Override
                        public void onNext(EcPostBean ecPostBean) {
                            JKLog.i("RT", "tasks:" + ecPostBean.getCode());
                        }

                        @Override
                        public void onError(Throwable e) {

                        }

                        @Override
                        public void onComplete() {

                        }
                    }
            );
        } else {
            doTaskWithId(ecGetTaskBean.getTaskType(), msg);


        }

    }

    private void doTaskWithId(int taskType, String content) {
        //判断辅助服务是否开启
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            JKToast.Show("找到空容器辅助功能，然后开启服务即可", 0);
        } else {

            isrunning = JKPreferences.GetSharePersistentBoolean("isRunning");
            JKLog.i("RT", "task_running:"+isrunning);
            if (isrunning) {
                JKLog.i("RT", "task_running:任务正在执行不能写入文件");
            } else {
                Intent intent = new Intent();
                intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
                intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
                startActivity(intent);
                //判断任务是否在执行
                //收到推送表示开启任务
                JKPreferences.SaveSharePersistent("doTasking", true);
                JKPreferences.SaveSharePersistent("isRunning", true);
                switch (taskType) {
                    case ContactAddFriendTpye:
                        AddToContact(this, content);
                        JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                        break;
                    case SearchAddFriendType:
                        JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                        break;
                    case CreatGroupType:
                        JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                        break;
                    case GetGroupPeoPleNum:
                        JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                        break;
                    case DetectionTask:
                        JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                        break;
                    case getCreatGroupInfo:
                        break;
                }
            }
        }
        {
            //任务在执行
        }

    }


    /*
    通过服务端发送的json将手机号加入通讯录
     */
    private void AddToContact(Context context, String custom) {
        Gson gson = new Gson();
        List<String> list = gson.fromJson(custom, EcContactBean.class).getTargetMobiles();
        for (String phone : list) {
            ContactUtil.addContact(context, phone);
        }
        JKPreferences.SaveSharePersistent("phoneList", (ArrayList<String>) list);
    }
}
