package com.jkframework.debug;

import java.io.PrintWriter;
import java.io.StringWriter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Looper;

import com.jkframework.activity.JKBaseActivity;
import com.jkframework.algorithm.JKAnalysis;
import com.jkframework.bean.JKReflectData;
import com.jkframework.config.JKSystem;
import com.jkframework.activity.JKExceptionActivity;
import com.jkframework.control.JKMessagebox;
import com.jkframework.control.JKToast;
import com.jkframework.manager.JKActivityManager;






public class JKException implements java.lang.Thread.UncaughtExceptionHandler {
	
	/**退出发起时间*/
	private static long lExitTime = 0;

    /**反射函数类*/
	private String tReflectClass;

	/**
	 * 构造函数
	 * @param tReflectTmp 反射函数类
	 */
	public JKException(String tReflectTmp) {
		tReflectClass = tReflectTmp;
	}
	
	/**
	 * 退出程序
	 */
	public static void ExitProgram()
	{		
		JKActivityManager.Exit();
		//System.exit(0);	 	
		//Process.killProcess(Process.myPid());	
	}
	
	/**
	 * 二次退出程序
	 * @param nInterval 二次确认退出的间隔时间
	 * @param tToast 退出提示语
	 */
	public static void SecondExitProgram(int nInterval,String tToast)
	{		
		if (System.currentTimeMillis() - lExitTime <= nInterval)
		{
			ExitProgram();
		}
		else {
			lExitTime = System.currentTimeMillis();
			JKToast.Show(tToast, 1);
		}
	}
	
	/**
	 * 消息反射函数
	 * @param MainActivity 显示结果的activity
	 * @param tReflect 反射类
	 * @param tFunction 反射函数
	 */
	public static void DoingReflect(Activity MainActivity,String tReflect,String tFunction)
	{
		Class<?>[] a_ClassList;
		Object[] a_ObjectList;
		if (tFunction.contains("(")) //有参数
		{
			String tParameter = JKAnalysis.GetMiddleString(tFunction, "(", ")");
			tFunction = tFunction.substring(0,tFunction.indexOf("("));
			String[] a_tParameter = tParameter.split(",");		
			a_ClassList = new Class[a_tParameter.length];
			for (int i=0; i<a_tParameter.length; ++i)
				a_ClassList[i] = String.class;
			
			a_ObjectList = new Object[a_tParameter.length];
            System.arraycopy(a_tParameter, 0, a_ObjectList, 0, a_tParameter.length);
		}
		else {
			a_ClassList = new Class[]{};
			a_ObjectList = new Object[]{};
		}
		
		JKReflectData jkrrTmp = JKReflect.Reflect(null, tReflect, tFunction, a_ClassList, a_ObjectList);
		switch (jkrrTmp.GetSuccess())
		{
			case 0:
			{
				JKMessagebox.Messagebox(MainActivity,(String) jkrrTmp.GetObject(), "确定");
				break;
			}
			case 1:
			{
				JKMessagebox.Messagebox(MainActivity, "消息平台处理类不存在", "确定");
				break;
			}
			case 2:
			{
				JKMessagebox.Messagebox(MainActivity, "无效的消息", "确定");
				break;
			}
			case 3:
			{
				JKMessagebox.Messagebox(MainActivity, "未知错误", "确定");
				break;
			}
			case 4:
			{
				JKMessagebox.Messagebox(MainActivity, "反射函数出现崩溃代码", "确定");
				break;
			}
		}		
	}

	@SuppressLint("InflateParams") @Override
	public synchronized void uncaughtException(Thread thread, Throwable exception) {
		final StringWriter stackTrace = new StringWriter();
		exception.printStackTrace(new PrintWriter(stackTrace));					                       	
		/*崩溃信息*/
        StringBuilder tReportText = new StringBuilder();
		tReportText.append("Model:").append(Build.MODEL).append("\r\n");
        tReportText.append("Device:").append(Build.DEVICE).append("\r\n");
        tReportText.append("Product:").append(Build.PRODUCT).append("\r\n");
        tReportText.append("Manufacturer:").append(Build.MANUFACTURER).append("\r\n");
        tReportText.append("Version:").append(Build.VERSION.RELEASE).append("\r\n");
        tReportText.append("CodeVersion:").append(JKSystem.GetVersionString()).append("(").append(JKSystem.GetVersionCode()).append(")").append("\r\n\r\n");
        tReportText.append(stackTrace.toString());	
        JKLog.e(tReportText.toString());		    
		
		Intent intent = new Intent();
		intent.putExtra("ReportText", tReportText.toString());
		intent.putExtra("Class", tReflectClass);
		JKBaseActivity CurrentActivity = JKSystem.GetCurrentActivity();
		if (CurrentActivity != null)
			CurrentActivity.StartActivity(JKExceptionActivity.class,intent);
		try {		//锁死对话
			Looper.getMainLooper();
			Looper.loop();
		}
		catch(RuntimeException ignored)
		{
		}
	}
}