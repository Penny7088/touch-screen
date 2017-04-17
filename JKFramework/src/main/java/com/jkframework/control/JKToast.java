package com.jkframework.control;


import android.widget.Toast;

import com.jkframework.debug.JKDebug;



public class JKToast
{	
	private static Toast taInstance = null;
	
	
	/**
	 * Toast提示
	 * @param tMessage 提示内容
	 * @param nType 提示时间长短,0为短时间,1为长时间
	 */
	public static void Show(String tMessage,int nType)
	{
		if (taInstance != null)
		{
			taInstance.setText(tMessage);
			taInstance.setDuration(nType == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
			taInstance.show();
			return;
		}
		taInstance = Toast.makeText(JKDebug.hContext, tMessage == null ? "" : tMessage, nType == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
		taInstance.show();
	}
	
	/**
	 * Toast提示
	 * @param nResourceMessage 提示内容
	 * @param nType 提示时间长短,0为短时间,1为长时间
	 */
	public static void Show(int nResourceMessage,int nType)
	{
		if (taInstance != null)
		{
			taInstance.setText(nResourceMessage);
			taInstance.setDuration(nType == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
			taInstance.show();
			return;
		}
		taInstance = Toast.makeText(JKDebug.hContext, nResourceMessage, nType == 0 ? Toast.LENGTH_SHORT : Toast.LENGTH_LONG);
		taInstance.show();
	}
}