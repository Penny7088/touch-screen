package com.jkframework.control;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Build.VERSION;


public class JKProgressDialog extends ProgressDialog
{	
	
	/**Activity对象*/
	private Activity MainActivity;
	
	/**
	 * 构造函数
	 * @param MainActivity 上下文引用
	 */
	public JKProgressDialog(Activity MainActivity) {		
		super(MainActivity);
		this.MainActivity = MainActivity;
	    setCancelable(false);
	}	
	
	@Override
	@TargetApi(17)
	public void show()
	{
		if (MainActivity!=null && !MainActivity.isFinishing() && !isShowing())
		{
			if (VERSION.SDK_INT >= 17)
			{
                try {
                    if (!MainActivity.isDestroyed())
                        super.show();
                }
				catch (Exception e)
                {
                    super.show();
                }
			}
			else
				super.show();		
		}
	}
	
	/**
	 * 对话框关闭
	 */
	@Override
	@TargetApi(17)
	public void dismiss()
	{
		if (MainActivity!=null && !MainActivity.isFinishing() && isShowing())
		{
			if (VERSION.SDK_INT >= 17)
			{
                try {
                    if (!MainActivity.isDestroyed())
                        super.dismiss();
                }
                catch (Exception e)
                {
                    super.dismiss();
                }
			}
			else
				super.dismiss();
		}
			
	}
}