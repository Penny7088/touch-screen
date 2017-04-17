package com.jkframework.manager;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.jkframework.algorithm.JKPicture;
import com.jkframework.bean.JKNotificationData;
import com.jkframework.debug.JKDebug;




public class JKNotificationManager
{
	/**通知栏单例对象*/
	private static JKNotificationManager jknfNotify = null;
	/**通知栏管理者*/
	private static NotificationManager mNotificationManager;
	
	/**推送起始个数*/
	private int nNum = 1;
	/**推送个数最大限制*/
	private int nMaxNum = 50;
	
	/**
	 * 获取单例对象
	 * @return  通知栏对象
	 */
	public static JKNotificationManager GetInstance()
	{
		if (jknfNotify == null)
		{
			synchronized (JKNotificationManager.class)
			{
				if (jknfNotify == null) {
					jknfNotify = new JKNotificationManager();
					mNotificationManager = (NotificationManager)JKDebug.hContext.getSystemService(Context.NOTIFICATION_SERVICE);
				}
			}
		}
		return jknfNotify;
	}
	
	/**
	 * 设置最大推送个数
	 * @param nNum 最大推送个数(不允许超过50)
	 */
	public void SetMaxNum(int nNum)
	{
		nMaxNum = Math.min(nNum,50);
	}
	
	/**
	 * 启动文本通知
	 * @param intent 点击通知的intent事件
	 * @param nResSmallIcon 标题栏图标(48*48)
	 * @param nResLargeIcon 通知栏图标
	 * @param tTips 通知的提示内容
	 * @param tTitle 通知的标题
	 * @param tContent 通知的内容
	 * @param bSound 是否使用通知音效
	 * @param bVibrate 是否使用通知震动
	 * @param bLED 是否使用通知LED灯
	 * @param bAutoCancel 是否点击后消失
	 * @return 通知信息对象返回
	 */
	@TargetApi(16)
	public JKNotificationData MakeNotification(Intent intent,int nResSmallIcon,int nResLargeIcon,String tTips,String tTitle,String tContent,boolean bSound,boolean bVibrate,boolean bLED,boolean bAutoCancel)
	{		
		JKNotificationData jknfTmp = new JKNotificationData();
		int nDefault = 0;
		if (bSound)
			nDefault |= Notification.DEFAULT_SOUND;
		if (bVibrate)
			nDefault |= Notification.DEFAULT_VIBRATE;
		if (bLED)
			nDefault |= Notification.DEFAULT_LIGHTS;
		
		NotificationCompat.Builder bdBuilder = new NotificationCompat.Builder(JKDebug.hContext)
		 .setTicker(tTips)
		 .setSmallIcon(nResSmallIcon)
         .setLargeIcon(JKPicture.LoadBitmap(nResLargeIcon))
         .setWhen(System.currentTimeMillis())
         .setAutoCancel(bAutoCancel)  
         .setContentTitle(tTitle)
         .setContentText(tContent)
         .setDefaults(nDefault);    
		if (intent != null)
		{
            PendingIntent contentIntent = PendingIntent.getActivity(JKDebug.hContext,0,intent,0);
            bdBuilder.setContentIntent(contentIntent);
		}
		jknfTmp.setNotifi(bdBuilder.build());
		
		return jknfTmp;
	}
	
	/**
	 * 启动下载通知
     * @param intent 点击通知的intent事件
	 * @param nResSmallIcon 标题栏图标(48*48)
	 * @param nResLargeIcon 通知栏图标
	 * @param tTips 通知的提示内容
	 * @param tTitle 通知的标题
	 * @param nMax 进度条最大进度
	 * @param nCurrent 当前进度
	 * @param bSound 是否使用通知音效
	 * @param bVibrate 是否使用通知震动
	 * @param bLED 是否使用通知LED灯
	 * @return 通知信息对象返回
	 */
	@TargetApi(16)
	public JKNotificationData MakeNotification(Intent intent,int nResSmallIcon,int nResLargeIcon,String tTips,String tTitle,int nMax,int nCurrent,boolean bSound,boolean bVibrate,boolean bLED)
	{		
		JKNotificationData jknfTmp = new JKNotificationData();
		int nDefault = 0;
		if (bSound)
			nDefault |= Notification.DEFAULT_SOUND;
		if (bVibrate)
			nDefault |= Notification.DEFAULT_VIBRATE;
		if (bLED)
			nDefault |= Notification.DEFAULT_LIGHTS;
		
		NotificationCompat.Builder bdBuilder = new NotificationCompat.Builder(JKDebug.hContext)
		 .setTicker(tTips)
		 .setSmallIcon(nResSmallIcon)
		 .setLargeIcon(JKPicture.LoadBitmap(nResLargeIcon))
		 .setWhen(System.currentTimeMillis())
		 .setAutoCancel(false)
		 .setContentTitle(tTitle)
		 .setProgress(nMax, nCurrent, false)
		 .setDefaults(nDefault);
        if (intent != null)
        {
            PendingIntent contentIntent = PendingIntent.getActivity(JKDebug.hContext,0,intent,0);
            bdBuilder.setContentIntent(contentIntent);
        }
		jknfTmp.setNotifi(bdBuilder.build());
		
		return jknfTmp;
	}
	
	/**
	 * 启动自定义界面通知
     * @param intent 点击通知的intent事件
	 * @param nResSmallIcon 标题栏图标(48*48)
	 * @param nResLargeIcon 通知栏图标
	 * @param tTips 通知的提示内容
	 * @param rvViews 通知的界面对象
	 * @param bSound 是否使用通知音效
	 * @param bVibrate 是否使用通知震动
	 * @param bLED 是否使用通知LED灯
	 * @return 通知信息对象返回
	 */
	@TargetApi(16)
	public JKNotificationData MakeNotification(Intent intent,int nResSmallIcon,int nResLargeIcon,String tTips,RemoteViews rvViews,boolean bSound,boolean bVibrate,boolean bLED)
	{
		JKNotificationData jknfTmp = new JKNotificationData();
		int nDefault = 0;
		if (bSound)
			nDefault |= Notification.DEFAULT_SOUND;
		if (bVibrate)
			nDefault |= Notification.DEFAULT_VIBRATE;
		if (bLED)
			nDefault |= Notification.DEFAULT_LIGHTS;
		
		NotificationCompat.Builder bdBuilder = new NotificationCompat.Builder(JKDebug.hContext)
			 .setTicker(tTips)
             .setSmallIcon(nResSmallIcon)
             .setLargeIcon(JKPicture.LoadBitmap(nResLargeIcon))
             .setWhen(System.currentTimeMillis())
             .setAutoCancel(false)  
             .setDefaults(nDefault)  
             .setContent(rvViews);
        if (intent != null)
        {
            PendingIntent contentIntent = PendingIntent.getActivity(JKDebug.hContext,0,intent,0);
            bdBuilder.setContentIntent(contentIntent);
        }
		jknfTmp.setNotifi(bdBuilder.build());
		
		return jknfTmp;
	}
	
	/**
	 * 开始通知
	 * @param jknfTmp 通知对象
	 * @return 通知对象的ID
	 */
	public int StartNotification(JKNotificationData jknfTmp)
	{
		jknfTmp.setID(nNum);
		mNotificationManager.notify(nNum,jknfTmp.getNotifi());		
		if (nNum - nMaxNum > 0)	//清除下限
			mNotificationManager.cancel(nNum - nMaxNum);
		nNum++;
		return jknfTmp.getID();
	}
	
	/**
	 * 更新通知
	 * @param jknfNotifi 通知对象
	 */
	public void UpdateNotification(JKNotificationData jknfNotifi)
	{
		mNotificationManager.notify(jknfNotifi.getID(), jknfNotifi.getNotifi());
	}
	
	/**
	 * 清除指定通知
	 * @param jknfNotifi 通知对象
	 */
	public void CancelNotification(JKNotificationData jknfNotifi)
	{
		mNotificationManager.cancel(jknfNotifi.getID());
	}
	
	/**
	 * 清除所有通知
	 */
	public void CancelNotification()
	{
		mNotificationManager.cancelAll();
	}
}