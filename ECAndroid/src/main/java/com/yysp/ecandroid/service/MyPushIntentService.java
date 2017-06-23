package com.yysp.ecandroid.service;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import com.google.gson.Gson;
import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.control.JKToast;
import com.jkframework.debug.JKLog;
import com.umeng.message.UmengMessageService;
import com.umeng.message.entity.UMessage;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.config.ECSdCardPath;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.bean.DisPushBean;
import com.yysp.ecandroid.data.bean.EcContactBean;
import com.yysp.ecandroid.data.bean.DisGetTaskBean;
import com.yysp.ecandroid.data.bean.EcGetTaskBean;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.util.ContactUtil;
import com.yysp.ecandroid.util.OthoerUtil;
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
import static com.yysp.ecandroid.config.ECConfig.AliasName;

/**
 * Created by Administrator on 2017/4/17.
 */

public class MyPushIntentService extends UmengMessageService {
    String TAG = "saas-api_";
    //taskType
    public final static int SearchAddFriendType = 501;
    public final static int ContactGetFriendInfo = 502;
    public final static int GetWxUserInfo = 503;//不走脚本
    public final static int CreatGroupType = 504;
    public final static int CreatGroupTypeBySmallWx = 505;
    public final static int GetGroupPeoPleNum = 506;
    public final static int GetCreatGroupInfo = 507;
    public final static int ChatWithFriend = 508;
    public final static int LookFriendCircle = 509;
    public final static int VoiceWithFriend = 510;
    public final static int VideoWithFriend = 511;
    public final static int Forwarding = 512;//转发
    public final static int SendFriendCircle = 513;
    public final static int Clicklike = 514;
    public final static int Comment = 515;
    public final static int DetectionTask = 516;
    public final static int FriendNumInfo = 517;
    public final static int AgressAddInGroupMsg = 518;
    public final static int GoOutGroup = 519;
    public final static int KickOutGroup = 520;
    public final static int DeleFriend = 521;
    public final static int AgressAddFriend = 525;
    public final static int NeedContactAddFriend = 527;
    public final static int ViewMessage = 532;
    public final static int ViewFriendNews = 533;
    public final static int AddFriendFromGroup = 534;

    @Override
    public void onMessage(Context context, Intent intent) {
        String message = intent.getStringExtra(AgooConstants.MESSAGE_BODY);
        try {
            UMessage msg = new UMessage(new JSONObject(message));
            JKLog.i(TAG, "task_push:" + msg.text);
            startTask(msg.text);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void startTask(final String msg) {
        //开启任务
        final Gson gson = new Gson();
        DisPushBean disPushBean = gson.fromJson(msg, DisPushBean.class);
        JKLog.i(TAG, "taskId:" + disPushBean.getTaskId());
        JKPreferences.SaveSharePersistent("taskId", disPushBean.getTaskId());
        //等于0请求任务
        ECNetSend.taskApply(disPushBean.getTaskId(), AliasName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(
                new Observer<DisGetTaskBean>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DisGetTaskBean disGetTaskBean) {
                        JKLog.i(TAG, "taskBean:" + disGetTaskBean.getMsg());
                        if (disGetTaskBean.getData() != null) {
                            JKLog.i(TAG, "dis:" + disGetTaskBean.getData().getTaskID() + "'*'" + disGetTaskBean.getData().getTaskType());
                            if (disGetTaskBean.getData() != null) {
                                WindowManager wm = (WindowManager) getSystemService(MyPushIntentService.this.WINDOW_SERVICE);
                                wm.setTpDisable(0);//关闭屏幕触摸
                                JKPreferences.SaveSharePersistent("taskType", disGetTaskBean.getData().getTaskType());
                                String jsonStr = gson.toJson(disGetTaskBean.getData());
                                doTaskWithId(disGetTaskBean.getData().getTaskType(), jsonStr);

                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        JKLog.i(TAG, "task_APPLY:" + e.getMessage());
                        OthoerUtil.AddErrorMsgUtil(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                    }
                }
        );

    }


    private void doTaskWithId(int taskType, String content) {
        JKLog.i(TAG, "data:" + content);
        JKPreferences.SaveSharePersistent("pushData", content);//备份
        Intent intent;
        //判断辅助服务是否开启
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            JKToast.Show("找到空容器辅助功能，然后开启服务即可", 0);
        } else {
            intent = new Intent();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
            startActivity(intent);
            //TODO
            doTypeTask(taskType, content);
        }
    }

    private void doTypeTask(int taskType, final String content) {
        switch (taskType) {
            case SearchAddFriendType:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case ContactGetFriendInfo:
                new Thread() {
                    @Override
                    public void run() {
                        AddToContact(MyPushIntentService.this, content);
                    }
                }.start();
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case GetWxUserInfo:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case CreatGroupType:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case CreatGroupTypeBySmallWx:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case GetGroupPeoPleNum:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case GetCreatGroupInfo:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case ChatWithFriend:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case LookFriendCircle:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case VoiceWithFriend:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case VideoWithFriend:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case Forwarding:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case SendFriendCircle:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case Clicklike:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case Comment:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case DetectionTask:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case FriendNumInfo:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case AgressAddInGroupMsg:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case GoOutGroup:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case KickOutGroup:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case DeleFriend:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case AgressAddFriend:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case NeedContactAddFriend:
                new Thread() {
                    @Override
                    public void run() {
                        AddToContact(MyPushIntentService.this, content);
                    }
                }.start();
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case ViewMessage:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case ViewFriendNews:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;
            case AddFriendFromGroup:
                JKFile.WriteFile(ECSdCardPath.Task_List_TXT, content);
                break;

        }

    }


    /*
    通过服务端发送的json将手机号加入通讯录
     */
    private void AddToContact(Context context, String custom) {
        Gson gson = new Gson();
        List<DisGetTaskBean.DataBean.TargetAccountsBean> list = gson.fromJson(custom, DisGetTaskBean.DataBean.class).getTargetAccounts();
        List<String> phoneList = new ArrayList<>();
        JKLog.i(TAG, "phone_size:" + list.size());
        for (int i = 0; i < list.size(); i++) {
            ContactUtil.addContact(context, list.get(i).getMobile());
            JKLog.i(TAG, "phone:" + list.get(i).getMobile());
            phoneList.add(list.get(i).getMobile());
        }
        JKPreferences.SaveSharePersistent("phoneList", (ArrayList<String>) phoneList);
    }
}
