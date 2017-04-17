package com.yysp.ecandroid.view.activity;

import android.content.Intent;
import android.os.Bundle;

import com.yysp.ecandroid.R;
import com.yysp.ecandroid.view.ECBaseActivity;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;


@EActivity(R.layout.ecandroid_mainactivity)
public class ECMainActivity extends ECBaseActivity {

    /**页面初始化*/
    private boolean bInit = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            bInit = savedInstanceState.getBoolean("Init", false);
        }
	}

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("Init", bInit);
//        outState.putParcelable("Backup", mPresenter.SaveBackup());
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
//        mPresenter.LoadBackup((XTTHomeModel) savedInstanceState.getParcelable("Backup"));
    }

    @Override
    public void onNewIntent(Intent intent)
    {
        super.onNewIntent(intent);
//        mPresenter.LoadData();
    }

    @AfterViews
    void InitData()
    {
        if (!bInit)
        {
            bInit = true;
        }
    }
}
