package com.jkframework.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

import com.jkframework.config.JKSystem;
import com.jkframework.control.JKProgressDialog;
import com.jkframework.debug.JKDebug;
import com.jkframework.debug.JKException;
import com.jkframework.manager.JKActivityManager;
import com.jkframework.thread.JKManualResetEvent;

import java.util.HashMap;
import java.util.Map;

public abstract class JKBaseActivity extends AppCompatActivity
{
	/**Activity操作锁*/
	private static JKManualResetEvent jkmreLock = new JKManualResetEvent(true);
	
	/**锁屏对话框对象*/
	private AlertDialog adDialog = null;
	/**Activity被废弃*/
	private boolean bAbandon = false;
	/**锁屏对话框文字*/
	private String tMessage = "";
	/**Activity是否运行中*/
	private  boolean bRun = false;
	/**Activity是否锁屏中*/
	private boolean bLock = false;
    /**Activity是否允许手动解除锁屏*/
    private boolean bCancel = false;
    /**Activity是否暂停*/
    private boolean bPause = false;
	/**Activity索引*/
	private int nIndex = -1;
    /**控件点击事件*/
    private HashMap<View,Boolean> h_bClickAble = new HashMap<>();


	/**
	 * Activity操作锁
	 */
	public static void Lock()
	{
		try {
			jkmreLock.WaitOne();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Activity操作解锁
	 */
	public static void Unlock()
	{
		jkmreLock.Set();
	}
	
	/**
	 * 锁屏对话框
	 * @param tMessage 锁屏提示信息
	 */
	public void LockScreen(String tMessage)
	{		
		LockScreen(tMessage, false);
	}

    /**
     * 锁屏对话框
     * @param tMessage 锁屏提示信息
     * @param bCancel 是否取消锁屏
     */
    public void LockScreen(String tMessage,boolean bCancel)
    {
        if (isFinishing())
            return;
        this.bCancel = bCancel;
        if (adDialog.isShowing())
        {
            this.tMessage = tMessage;
            adDialog.setMessage(tMessage);
            adDialog.setCancelable(bCancel);
        }
        else
        {
            if (!bLock)
            {
                LockView((ViewGroup) getWindow().getDecorView());
                bLock = true;
                adDialog = InitProgressDialog();
                this.tMessage = tMessage;
                adDialog.setMessage(tMessage);
                adDialog.setCancelable(bCancel);
                adDialog.show();
            }
        }
    }

	/**
	 * 解除锁屏对话框
	 */
	public void UnlockScreen()
	{
		bLock = false;
		if (adDialog.isShowing())
        {
            adDialog.dismiss();
        }
	}
	
	/**
	 * Activity是否正常运作中
	 * @return true表示正常运作中
	 */
	public boolean IsRun()
	{
		return bRun;
	}
	
	/**
	 * Actvivity状态是否被废弃
	 * @return true表示已废弃
	 */
	public boolean IsAbandon()
	{
		return bAbandon;
	}
	
	/**
	 * 设置是否废弃Activity
	 * @param abandon true表示废弃
	 */
	public void setAbandon(boolean abandon) {
		bAbandon = abandon;
	}
	
	/**
	 * 打开Activity
	 * @param cls 界面class对象
	 */
	public synchronized void StartActivity(Class<?> cls)
	{
		if (bRun)
		{
			bRun = false;
			Intent itIntent = new Intent(this,cls);
			super.startActivity(itIntent);
		}
	}

	/**
	 * 打开Activity
	 * @param cls 界面class对象
	 * @param itIntent Intent参数对象
	 */
	public synchronized void StartActivity(Class<?> cls,Intent itIntent)
	{
		if (bRun)
		{
			bRun = false;
			itIntent.setClass(this,cls);
			super.startActivity(itIntent);
		}
	}

	/**
	 * 打开Activity
	 * @param cls 界面class对象
	 * @param nEnterResource 进入动画
	 * @param nExitResource 退出动画
	 */
	public synchronized void StartActivity(Class<?> cls,int nEnterResource,int nExitResource)
	{
		if (bRun)
		{
			bRun = false;
			Intent itIntent = new Intent(this,cls);
			super.startActivity(itIntent);
	        overridePendingTransition(nEnterResource, nExitResource);
//			ActivityOptionsCompat options = ActivityOptionsCompat.makeCustomAnimation(this,nEnterResource,nExitResource);
//			ActivityCompat.startActivity(this, itIntent, options.toBundle());
		}
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
			Intent itIntent = new Intent(this,cls);
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
		if (bRun)
		{
			bRun = false;
			itIntent.setClass(this,cls);
			super.startActivityForResult(itIntent,nRequestCode);
		}
	}

	/**
	 * 恢复当前Activity
	 */
	public void Restore()
	{
		bRun = true;
		JKActivityManager.getAllActivity().set(nIndex, this);
	}
	
	@Override
	public void finish()
	{
		if (adDialog != null && adDialog.isShowing())
			adDialog.dismiss();
		super.finish();
	}
	
	/**
	 * 动画退出程序
	 */
	public void Finish(int nEnterResource,int nExitResource)
	{
		super.finish();
		overridePendingTransition(nEnterResource, nExitResource);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		bRun = true;
		nIndex = JKActivityManager.getAllActivity().size();
		JKActivityManager.getAllActivity().add(this);
		//消息崩溃处理
		if (JKDebug.nDebug != 0)
			Thread.setDefaultUncaughtExceptionHandler(new JKException(JKDebug.tReflect));
		adDialog = InitProgressDialog();
		JKSystem.SetDefaultFontSize();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (bLock)
		{
			adDialog.dismiss();
		}
		bPause = true;
	}

	@Override
	protected void onNewIntent(Intent intent)
	{
		super.onNewIntent(intent);
		bRun = true;
		if (bAbandon)
		{
			nIndex = JKActivityManager.getAllActivity().size();
			JKActivityManager.getAllActivity().add(this);
			//消息崩溃处理
			if (JKDebug.nDebug != 0)
				Thread.setDefaultUncaughtExceptionHandler(new JKException(JKDebug.tReflect));
			bAbandon = false;
		}
		else {
			JKActivityManager.getAllActivity().set(nIndex, this);		
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (!bAbandon)
		{
			Lock();
			bRun = true;
			if (nIndex < JKActivityManager.getAllActivity().size())
				JKActivityManager.getAllActivity().set(nIndex, this);
			Unlock();
		}
		if (bLock && bPause && !adDialog.isShowing())
		{
			adDialog = InitProgressDialog();
			adDialog.setMessage(tMessage);
            adDialog.setCancelable(bCancel);
			adDialog.show();
		}
		bPause = false;
	}
	
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		if (!bAbandon)
		{
			Lock();
			JKActivityManager.getAllActivity().remove(nIndex);
			//恢复后面的索引序号
			for (int i=nIndex; i<JKActivityManager.getAllActivity().size(); ++i)
			{
				JKActivityManager.getAllActivity().get(i).nIndex--;
			}
			Unlock();
		}
	}

	/**
	 * 程序是否在后台
	 * @return true表示是
	 */
	public boolean IsBackground()
	{
		return bPause;
	}

	/**
	 * 初始化锁屏对话框对象(需要自定义重写此函数)
	 * @return 对话框对象
	 */
	protected AlertDialog InitProgressDialog()
	{
        AlertDialog adTmp =  new JKProgressDialog(this);
        adTmp.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (!bPause)
                {
                    bLock = false;
                }
                UnlockView();
            }
        });
		return adTmp;
	}

    /**
     * 控件点击事件上锁
     * @param vgRoot 控件view
     */
    private void LockView(ViewGroup vgRoot)
    {
        for (int i = 0; i < vgRoot.getChildCount(); i++) {
            View viewchild = vgRoot.getChildAt(i);
            h_bClickAble.put(viewchild,viewchild.isClickable());
            if (viewchild instanceof ViewGroup)
            {
                LockView((ViewGroup) viewchild);
            }
			viewchild.setClickable(false);
        }
    }

    /**
     * 控件点击事件解锁
     */
    private void UnlockView()
    {
        for (Map.Entry<View, Boolean> entry : h_bClickAble.entrySet()) {
            entry.getKey().setClickable(entry.getValue());
        }
    }
}