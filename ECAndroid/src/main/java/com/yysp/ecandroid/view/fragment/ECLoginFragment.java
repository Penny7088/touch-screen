package com.yysp.ecandroid.view.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yysp.ecandroid.R;
import com.yysp.ecandroid.data.model.ECLoginModel;
import com.yysp.ecandroid.persent.ECLoginPersent;
import com.yysp.ecandroid.view.ECBaseFragment;
import com.yysp.ecandroid.view.ui.ILoginView;
import com.jakewharton.rxbinding2.view.RxView;
import com.jkframework.control.JKEditText;
import com.jkframework.control.JKTextView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.concurrent.TimeUnit;

import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;

@EFragment(R.layout.ecandroid_loginfragment)
public class ECLoginFragment extends ECBaseFragment implements ILoginView {

    @ViewById(R.id.jketAccount)
    JKEditText jketAccount;
    @ViewById(R.id.jketPassword)
    JKEditText jketPassword;
    @ViewById(R.id.jktvReslt)
    JKTextView jktvReslt;
    @ViewById(R.id.jktvLogin)
    JKTextView jktvLogin;
    /**
     * 界面初始化
     */
    private boolean bInit = false;
    /**
     * 回收初始化
     */
    private boolean bRecycle = false;
    /**
     * Presenter对象
     */
    private ECLoginPersent mPresenter;

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
        outState.putParcelable("Backup", mPresenter.SaveModel());
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (bRecycle) {
            bRecycle = false;
            mPresenter.LoadModel((ECLoginModel) savedInstanceState.getParcelable("Backup"));
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @AfterViews
    void InitData() {
        mPresenter = new ECLoginPersent(this);

        /*设置登录点击事件*/
        RxView.clicks(jktvLogin)
                .throttleFirst(500, TimeUnit.MILLISECONDS)
                .subscribe(new Consumer<Object>() {
                    @Override
                    public void accept(@NonNull Object o) throws Exception {
                        mPresenter.Login();
                    }
                });

//        /*设置按钮状态*/
//        Observable<Boolean> bInputAccount = RxTextView.textChanges(jketAccount)
//                .map(new Func1<CharSequence, Boolean>() {
//                    @Override
//                    public Boolean call(CharSequence charSequence) {
//                        return !TextUtils.isEmpty(charSequence);
//                    }
//                });
//        Observable<Boolean> bInputPassword = RxTextView.textChanges(jketPassword)
//                .map(new Func1<CharSequence, Boolean>() {
//                    @Override
//                    public Boolean call(CharSequence charSequence) {
//                        return !TextUtils.isEmpty(charSequence);
//                    }
//                });
//        Observable.combineLatest(bInputAccount, bInputPassword, new Func2<Boolean, Boolean, Boolean>() {
//            @Override
//            public Boolean call(Boolean aBoolean, Boolean aBoolean2) {
//                return aBoolean && aBoolean2;
//            }
//        }).subscribe(new Action1<Boolean>() {
//            @Override
//            public void call(Boolean aBoolean) {
//                jktvLogin.setEnabled(aBoolean);
//            }
//        });

        if (!bInit) {
            bInit = true;
        } else {
            bRecycle = true;
        }
    }

    @Override
    public void SetAccount(String tText) {
        jketAccount.setText(tText);
    }

    @Override
    public void SetPassword(String tText) {
        jketPassword.setText(tText);
    }

    @Override
    public void SetResult(String tText) {
        jktvReslt.setText(tText);
    }

    @Override
    public String GetAccount() {
        return jketAccount.GetRealText();
    }

    @Override
    public String GetPassword() {
        return jketPassword.GetRealText();
    }

    @Override
    public String GetResult() {
        return jktvReslt.GetRealText();
    }
}
