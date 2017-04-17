package com.yysp.ecandroid.view.ui;

/**
 * Created by 2016/7/5.
 */
public interface ILoginView extends IBaseView{

    ///设置界面
    void SetAccount(String tText);
    void SetPassword(String tText);
    void SetResult(String tText);

    ///从界面上读取属性
    String GetAccount();
    String GetPassword();
    String GetResult();
}

