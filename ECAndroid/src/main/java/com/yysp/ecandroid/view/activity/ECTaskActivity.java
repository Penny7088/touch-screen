package com.yysp.ecandroid.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.view.accessibility.AccessibilityManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.jkframework.config.JKPreferences;
import com.jkframework.config.JKSystem;
import com.jkframework.control.JKTextView;
import com.jkframework.control.JKToast;
import com.jkframework.debug.JKLog;
import com.yysp.ecandroid.R;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.bean.DisGetTaskBean;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.service.LongRunningService;
import com.yysp.ecandroid.util.ContactUtil;
import com.yysp.ecandroid.util.OthoerUtil;
import com.yysp.ecandroid.view.ECBaseActivity;


import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.yysp.ecandroid.config.ECConfig.AliasName;

/**
 * Created by Administrator on 2017/4/15.
 */
@EActivity(R.layout.ecandroid_taskacticity)
public class ECTaskActivity extends ECBaseActivity {
    /**
     * 页面初始化
     */
    private boolean bInit = false;
    public String TAG="saas-api_";
    public static final String LauncherUI = "com.tencent.mm.ui.LauncherUI";
    public static final String MM = "com.tencent.mm";

    @ViewById(R.id.tv_deviceId)
    JKTextView tv_deviceId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            bInit = savedInstanceState.getBoolean("Init", false);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        outState.putBoolean("Init", bInit);
    }

    @AfterViews
    void InitData() {
        if (!bInit) {
            bInit = true;
        }

        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {

            new MaterialDialog.Builder(this).content("请打开空容器辅助功能!").positiveText("确定").onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                    JKToast.Show("找到空容器APK辅助功能，然后开启服务即可", 0);
                    dialog.dismiss();
                }
            }).show();

        }


    }


    @Click(R.id.bt_writeToFile)
    void writeToFile()  {
        try {
            ContactUtil.deleteContact(this,"18690611244");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Click(R.id.bt_task)
    void task() {
       ContactUtil.addContact(this,"18690611244");
    }

    @Click(R.id.bt_start)
    void start() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            JKToast.Show("找到空容器辅助功能，然后开启服务即可", 0);
        } else {
            Intent intent = new Intent();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
            startActivity(intent);

        }
    }

    @Click(R.id.bt_postId)
    void test() {



            ECTaskResultResponse resultResponse =new ECTaskResultResponse();
            resultResponse.setTaskId("23309523788109");//taskId
            resultResponse.setStatus(ECConfig.TASK_FINISH);//完成状态
            resultResponse.setDeviceAlias(AliasName);//别名
            JKLog.i(TAG, "id:" + JKPreferences.GetSharePersistentString("taskId") + "/" + AliasName);
            ECNetSend.taskStatus(resultResponse).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
                @Override
                public void onSubscribe(Disposable d) {

                }

                @Override
                public void onNext(DisBean disBean) {
                    JKLog.i(TAG, "taskStatus:" + disBean.getCode() + "/" + disBean.getMsg());
                    if (disBean.getCode() == 200) {
                        JKLog.i(TAG, TAG + "taskStatus:success");
                    }
                    OthoerUtil.doOfTaskEnd();
                }

                @Override
                public void onError(Throwable e) {
                    JKLog.i(TAG, "erro:" + e.getMessage());
                }

                @Override
                public void onComplete() {

                }
            });

    }
}
