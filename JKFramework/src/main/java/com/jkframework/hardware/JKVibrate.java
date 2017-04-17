package com.jkframework.hardware;

import android.app.Service;
import android.os.Vibrator;

import com.jkframework.debug.JKDebug;




public class JKVibrate
{
	
	/**
	 * 手机振动
	 * @param nTime 振动时间
	 */
	public static void Vibrate(int nTime)
	{
		Vibrator vibrator = (Vibrator)JKDebug.hContext.getSystemService(Service.VIBRATOR_SERVICE);
		vibrator.vibrate(nTime);
	}
}