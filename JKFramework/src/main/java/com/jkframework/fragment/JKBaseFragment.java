package com.jkframework.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jkframework.activity.JKBaseActivity;
import com.jkframework.config.JKSystem;

/**
 * Created by 2016/4/6.
 */
public class JKBaseFragment extends Fragment {

    /**Frangment是否运行中*/
    private boolean bRun = false;


    /**
     * Activity是否正常运作中
     * @return true表示正常运作中
     */
    public boolean IsRun()
    {
        return bRun;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View vView = super.onCreateView(inflater,container,savedInstanceState);
        bRun = true;
        return vView;
    }

    @Override
    public void onResume() {
        super.onResume();
        bRun = true;
    }

    /**
     * 获取Framgment所在Activity
     * @return  所在Activity
     */
    public JKBaseActivity GetActivity()
    {
        if (super.getActivity() != null)
            return (JKBaseActivity) super.getActivity();
        else {
            return JKSystem.GetCurrentActivity();
        }
    }

    /**
     * 打开Activity
     * @param cls 界面class对象
     */
    public synchronized void StartActivity(Class<?> cls)
    {
        if (bRun) {
            bRun = false;
            Intent itIntent = new Intent(getContext(),cls);
            super.startActivity(itIntent);
        }
    }

    /**
     * 打开Activity
     * @param cls 界面class对象
     * @param itIntent Intent参数对象
     */
    public synchronized  void StartActivity(Class<?> cls, Intent itIntent)
    {
        if (bRun) {
            bRun = false;
            itIntent.setClass(getContext(), cls);
            super.startActivity(itIntent);
        }
    }

    /**
     * 关闭Activity
     */
    public synchronized void finish()
    {
        JKBaseActivity CurrentActivity = GetActivity();
        if (CurrentActivity != null)
            CurrentActivity.finish();
    }

    /**
     * 设置Toolbar
     * @param toolbar Toolbar对象
     */
    public void setSupportActionBar(@Nullable Toolbar toolbar)
    {
        JKBaseActivity CurrentActivity = GetActivity();
        if (CurrentActivity != null)
            CurrentActivity.setSupportActionBar(toolbar);
    }

    /**
     * 锁屏对话框
     * @param tMessage 锁屏提示信息
     */
    public void LockScreen(String tMessage)
    {
        JKBaseActivity CurrentActivity = GetActivity();
        if (CurrentActivity != null)
            CurrentActivity.LockScreen(tMessage);
    }

    /**
     * 解除锁屏对话框
     */
    public void UnlockScreen()
    {
        JKBaseActivity CurrentActivity = GetActivity();
        if (CurrentActivity != null)
            CurrentActivity.UnlockScreen();
    }

    /**
     * 打开Activity并接受返回
     * @param itIntent Intent参数对象
     * @param nRequestCode 返回值索引
     */
    public synchronized void StartActivityForResult(Intent itIntent,int nRequestCode)
    {
        if (bRun)
        {
            bRun = false;
            super.startActivityForResult(itIntent,nRequestCode);
        }
    }

    /**
     * 打开Activity并接受返回
     * @param cls 界面class对象
     * @param nRequestCode 返回值索引
     */
    public synchronized void StartActivityForResult(Class<?> cls,int nRequestCode)
    {
        if (bRun)
        {
            bRun = false;
            Intent itIntent = new Intent(getContext(),cls);
            super.startActivityForResult(itIntent,nRequestCode);
        }
    }

    /**
     * 打开Activity并接受返回
     * @param cls 界面class对象
     * @param itIntent Intent参数对象
     * @param nRequestCode 返回值索引
     */
    public synchronized void StartActivityForResult(Class<?> cls,Intent itIntent,int nRequestCode)
    {
        if (bRun) {
            bRun = false;
            itIntent.setClass(getContext(), cls);
            super.startActivityForResult(itIntent, nRequestCode);
        }
    }
}
