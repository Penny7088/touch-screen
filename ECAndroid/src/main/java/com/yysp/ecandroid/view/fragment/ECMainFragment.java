package com.yysp.ecandroid.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jakewharton.rxbinding2.view.RxView;
import com.jkframework.control.JKTextView;
import com.yysp.ecandroid.R;
import com.yysp.ecandroid.view.ECBaseFragment;
import com.yysp.ecandroid.view.activity.ECLoginDemoActivity_;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

@EFragment(R.layout.ecandroid_mainfragment)
public class ECMainFragment extends ECBaseFragment {

    @ViewById(R.id.jktvLoginDemo)
    JKTextView jktvLoginDemo;
    /**
     * 界面初始化
     */
    private boolean bInit = false;
    /**
     * 回收初始化
     */
    private boolean bRecycle = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            bInit = savedInstanceState.getBoolean("Init", false);
        }
        return null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("Init", bInit);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (bRecycle) {
            bRecycle = false;

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @AfterViews
    void InitData() {
        /*设置登录demo点击事件*/
        RxView.clicks(jktvLoginDemo)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        StartActivity(ECLoginDemoActivity_.class);
                    }
                });


        if (!bInit) {
            bInit = true;
        } else {
            bRecycle = true;
        }
    }

}
