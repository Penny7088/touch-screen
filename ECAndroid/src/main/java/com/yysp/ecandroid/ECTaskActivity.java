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
    private AccessibilityManager mAccessibilityManager;
    public static boolean isOpen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ecandroid_taskacticity);
        if (savedInstanceState != null) {
            bInit = savedInstanceState.getBoolean("Init", false);
        }
        initData();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
        outState.putBoolean("Init", bInit);
    }

    void initData() {
        if (!bInit) {
            bInit = true;
        }
        ECConfig.OpenScreenOrder(this);
        mAccessibilityManager = (AccessibilityManager) getSystemService(ACCESSIBILITY_SERVICE);
        if (!mAccessibilityManager.isEnabled()) {
            ContactUtil.isTasking = false;
//            ContactUtil.AddErrorMsgUtil(AliasName + "   辅助未打开");
            new MaterialDialog.Builder(this)
                    .content("请打开空容器辅助功能!")
                    .positiveText("确定")
                    .onPositive(new MaterialDialog.SingleButtonCallback() {
                @Override
                public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                    dialog.dismiss();
                }
            }).show();

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Logger.d("onResume","================");
        isOpen = mAccessibilityManager.isEnabled();
//        if(mAccessibilityManager.isEnabled()){
//
//        }
    }
}
