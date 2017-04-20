package com.yysp.ecandroid.view.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.accessibility.AccessibilityManager;

import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.control.JKEditText;
import com.jkframework.control.JKTextView;
import com.jkframework.control.JKToast;
import com.yysp.ecandroid.R;
import com.yysp.ecandroid.service.LongRunningService;
import com.yysp.ecandroid.util.ContactToAddUtil;
import com.yysp.ecandroid.view.ECBaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Administrator on 2017/4/15.
 */
@EActivity(R.layout.ecandroid_taskacticity)
public class ECTaskActivity extends ECBaseActivity {
    /**
     * 页面初始化
     */
    private boolean bInit = false;

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

        tv_deviceId.setText(JKPreferences.GetSharePersistentString("deviceToken"));
    }


    @Click(R.id.bt_writeToFile)
    void writeToFile() {

    }

    @Click(R.id.bt_task)
    void task() {
        Intent intent = new Intent(this, LongRunningService.class);
        startService(intent);
    }

    @Click(R.id.bt_start)
    void start() {
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            JKToast.Show("找到检测辅助功能，然后开启服务即可", 0);
        } else {
            //开启任务
            JKPreferences.SaveSharePersistent("doTasking",true);
            Intent intent = new Intent();
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName(MM, LauncherUI);
            startActivity(intent);
        }


    }


}
