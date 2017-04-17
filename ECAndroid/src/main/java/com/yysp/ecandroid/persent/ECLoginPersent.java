package com.yysp.ecandroid.persent;

import com.yysp.ecandroid.data.response.ECLoginResponse;
import com.yysp.ecandroid.data.model.ECLoginModel;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.view.ui.ILoginView;
import com.jakewharton.retrofit2.adapter.rxjava2.HttpException;
import com.jkframework.control.JKToast;

import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.rx_cache2.RxCacheException;

/**
 * Created by 2016/7/5.
 */
public class ECLoginPersent {

    private ILoginView mView;
    private ECLoginModel mModel;

    public ECLoginPersent(ILoginView view) {
        mView = view;
        mModel = new ECLoginModel();
    }

    public void LoadModel(ECLoginModel mModel) {
        this.mModel = mModel;
        mView.SetAccount(mModel.tAccount);
        mView.SetPassword(mModel.tPassword);
        mView.SetResult(mModel.tResult);
    }

    public ECLoginModel SaveModel() {
        mModel.tAccount = mView.GetAccount();
        mModel.tPassword = mView.GetPassword();
        mModel.tResult = mView.GetResult();
        return mModel;
    }

    public void LoadData() {
        LoadModel(mModel);
    }

    public void Login() {
        mView.LockScreen("正在登录...");
        ECNetSend.Login(mView.GetAccount(),mView.GetPassword())
                .subscribeOn(Schedulers.io())       //指定上个subscribe所在线程
                .observeOn(AndroidSchedulers.mainThread())  //指定subscribe回调时线程
                .subscribe(new Observer<ECLoginResponse>() {

                    @Override
                    public void onError(Throwable e) {
                        mView.UnlockScreen();
                        if (e instanceof RxCacheException)
                            e = e.getCause();
                        if (e instanceof HttpException) {
                            JKToast.Show("网络异常",1);
                        } else if (e instanceof UnknownHostException) {
                            JKToast.Show("网络异常",1);
                        } else {
                            JKToast.Show("数据异常",1);
                        }
                    }

                    @Override
                    public void onComplete() {
                        mView.UnlockScreen();
                    }

                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(ECLoginResponse ECLoginResponse) {
                        //mView.SetResult(Optional.fromNullable(afLoginAnalyze.result.avatar).or(""));
                    }
                });
                        //if (!osLogin.isUnsubscribed())
                        //osLogin.unsubscribe();    取消操作
    }
}