package com.yysp.ecandroid;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.accessibility.AccessibilityManager;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.util.ContactUtil;
import com.yysp.ecandroid.util.Logger;

/**
 * Created by Administrator on 2017/4/15.
 */
public class ECTaskActivity extends AppCompatActivity {
    /**
     * 页面初始化
     */
    private boolean bInit = false;
    public static final String LauncherUI = "com.tencent.mm.ui.LauncherUI";
    public static final String MM = "com.tencent.mm";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecandroid_taskacticity);
        if (savedInstanceState != null) {
            bInit = savedInstanceState.getBoolean("Init", false);
        }
//        JKToast.Show("预发布:" + ECConfig.AliasName, 0);
        initData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        outState.putBoolean("Init", bInit);
    }

    void initData() {
        Logger.d("ECTaskActivity","=========================");
        if (!bInit) {
            bInit = true;
        }
        ECConfig.OpenScreenOrder(this);
        AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!accessibilityManager.isEnabled()) {
            ContactUtil.isTasking = false;
//            ContactUtil.AddErrorMsgUtil(AliasName + "   辅助未打开");
            new MaterialDialog.Builder(this).content("请打开空容器辅助功能!").positiveText("确定").onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }).show();

        }
    }
}
