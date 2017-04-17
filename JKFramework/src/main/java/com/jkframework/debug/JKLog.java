package com.jkframework.debug;

import android.util.Log;

import com.jkframework.algorithm.JKDate;
import com.jkframework.algorithm.JKFile;

public class JKLog {

	/**错误信息默认地址*/
	static public String ERROR_PATH = JKFile.GetPublicPath() + "/JKError/Error.txt";
	
	/**默认log标签*/
	private static String tDebugtTag = "JKDebug";
	
	/**
	 * debug内容
	 * @param tMsg log内容
	 */
	public static void d(String tMsg){
		if (JKDebug.nDebug != 0)
		{
			if (tMsg == null)
				Log.d(tDebugtTag, "null");
			else
				Log.d(tDebugtTag, tMsg);
		}
	}
	
	/**
	 * debug内容
	 * @param tTag debug标签
	 * @param tMsg log内容
	 */
	public static void d(String tTag, String tMsg){
		if (JKDebug.nDebug != 0)
		{
			if (tMsg == null)
				Log.d(tTag, "null");
			else
				Log.d(tTag, tMsg);
		}
	}
	
	/**
	 * info内容
	 * @param tMsg log内容
	 */
	public static void i(String tMsg){
		if (JKDebug.nDebug != 0)
		{
			if (tMsg == null)
				Log.i(tDebugtTag, "null");
			else
				Log.i(tDebugtTag, tMsg);
		}
	}
	
	/**
	 * info内容
	 * @param tTag info标签
	 * @param tMsg log内容
	 */
	public static void i(String tTag, String tMsg){
		if (JKDebug.nDebug != 0)
		{
			if (tMsg == null)
				Log.i(tTag, "null");
			else
				Log.i(tTag, tMsg);
		}
	}
	
	/**
	 * warning内容
	 * @param tMsg log内容
	 */
	public static void w(String tMsg){
		if (JKDebug.nDebug != 0)
		{
			if (tMsg == null)
				Log.w(tDebugtTag, "null");
			else
				Log.w(tDebugtTag, tMsg);
		}
	}
	
	/**
	 * warning内容
	 * @param tTag warning标签
	 * @param tMsg log内容
	 */
	public static void w(String tTag, String tMsg){
		if (JKDebug.nDebug != 0)
		{
			if (tMsg == null)
				Log.w(tTag, "null");
			else
				Log.w(tTag, tMsg);
		}
	}
	
	/**
	 * error内容
	 * @param tMsg log内容
	 */
	public static void e(String tMsg){
		if (JKDebug.nDebug != 0)
		{
			if (tMsg == null)
				Log.e(tDebugtTag, "null");
			else
				Log.e(tDebugtTag, tMsg);
		}
	}
	
	/**
	 * error内容
	 * @param tTag error标签
	 * @param tMsg log内容
	 */
	public static void e(String tTag, String tMsg){
		if (JKDebug.nDebug != 0)
		{
			if (tMsg == null)
				Log.e(tTag, "null");
			else
				Log.e(tTag, tMsg);
		}
	}
	
	/**
	 * verbose内容
	 * @param tMsg log内容
	 */
	public static void v(String tMsg){
		if (JKDebug.nDebug != 0)
		{
			if (tMsg == null)
				Log.v(tDebugtTag, "null");
			else
				Log.v(tDebugtTag, tMsg);
		}
	}
	
	/**
	 * verbose内容
	 * @param tTag verbose标签
	 * @param tMsg log内容
	 */
	public static void v(String tTag, String tMsg){
		if (JKDebug.nDebug != 0)
		{
			if (tMsg == null)
				Log.v(tTag, "null");
			else
				Log.v(tTag, tMsg);
		}
	}
	
	/**
	 * 打印消息错误log
	 * @param tMessage 错误内容
	 */
	public static synchronized void ErrorLog(String tMessage)
	{
		if (JKDebug.nDebug != 0)
		{
			JKFile.CreateDir(ERROR_PATH);
	        if (!JKFile.IsExists(ERROR_PATH))
	            JKFile.WriteFile(ERROR_PATH, "");
	        JKFile.AppendFile(ERROR_PATH, JKDate.GetFullDate(false) + ": " + GetFunctionPos() + "  " + tMessage + "\r\n");
		}
	}
	
	/**
	 * 获取当前除库文件外函数位置
	 * @return 包名+类名+行数
	 */
	public static String GetFunctionPos()
	{
		StackTraceElement[] a_steFunction = Thread.currentThread().getStackTrace();
		for (int i=2; i<a_steFunction.length; ++i)
		{
			if (a_steFunction[i].getFileName() != null)
			{
				if (a_steFunction[i].getFileName().indexOf("JK") != 0) {
					if (a_steFunction[i].getClassName() != null && a_steFunction[i].getMethodName() != null)
						return a_steFunction[i].getClassName() + "." + a_steFunction[i].getMethodName() + "[" + a_steFunction[i].getLineNumber() + "]";
					else
						return "";
				}
			}
		}
		return "";
	}
}
