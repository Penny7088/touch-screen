package com.yysp.ecandroid.view.activity;


import android.os.Bundle;

import com.jkframework.debug.JKDebug;
import com.yysp.ecandroid.R;
import com.yysp.ecandroid.view.ECBaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;

@EActivity(R.layout.ecandroid_loadingactivity)
public class ECLoadingActivity extends ECBaseActivity {

    //等待Logo判断
    private boolean bWait = false;
    //登录判断
    private boolean bLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        JKDebug.Activation();
        super.onCreate(savedInstanceState);
//        JKSystem.SetStatus(false,true);
    }

    @Override
    public void finish() {
        super.finish();
    }

    @Override
    public void onResume() {
        super.onResume();
        CheckLoadingFinish();
    }

    @AfterViews
    void InitData() {
        CheckUpdate();

        WaitLogo();
        Login();
    }

    /**
     * 检查更新
     */
    private void CheckUpdate() {

    }

    @UiThread(delay = 2000)
    void WaitLogo() {
        bWait = true;
        CheckLoadingFinish();
    }

    void Login() {
        bLogin = true;
        CheckLoadingFinish();
    }

    @UiThread
    void CheckLoadingFinish() {
        if (IsBackground())
            return;
        if (!bLogin || !bWait) {
            return;
        }

        StartActivity(ECMainActivity_.class);
        finish();
    }
}
