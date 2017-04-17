package com.jkframework.animation.action;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;

import com.jkframework.animation.JKAnimationOne;
import com.jkframework.debug.JKDebug;
import com.jkframework.debug.JKLog;

import java.util.HashMap;

public class JKAnimationMusic extends JKAnimationOne{

	/**音乐播放器对象*/
	private MediaPlayer mpMediaPlayer = null;	
	/**音乐播放路径*/
	private String tPlayPath;	
	/**音乐附加的Activity*/
	private Context hMain;
	/**音乐播放整形状态指针*/
	private Integer jkvnSound;
	/**音乐播放布尔状态指针*/
	private Boolean jkvbSound;
	/**音乐播放计时器*/
	private Handler hlHandler = new Handler();
	/**音乐剩余播放的时间*/
	private int nRemainTime;
	/**音乐加载状态(1为SD卡上路径,2为assets路径)*/
	private int nLoadType;
	/**音乐开始播放的时间*/
	private long lStartTime;
	
	/**
	 * 构造函数,注意声音选项默认是不被复写的
	 * @param hContext  播放音乐的Activity
	 * @param tPath  播放音乐的路径
	 * @param jkvnTmp  声音是否静音整形状态指针
	 * @param jkvbTmp  声音是否静音布尔状态指针 
	 * @param bCover  声音是否覆盖
	 * @param nType  音乐类型(1为SD卡上路径,2为raw路径)
	 */
	public JKAnimationMusic(Context hContext,String tPath,Integer jkvnTmp,Boolean jkvbTmp,boolean bCover,int nType)
	{
		hMain = hContext;
		tPlayPath = tPath;
		jkvnSound = jkvnTmp;	
		jkvbSound = jkvbTmp;
		bAutoCancel = bCover;
		nLoadType = nType;
		/*固定步骤*/
		InitFilter();
	}
	
	/**
	 * 回调函数默认执行
	 */
	public void FinishAnimation() {
		
	}
	
	@Override
	public void InitFilter() {
		a_tFilterList.add("Music");
	}
	
	@Override
	public void AddFilter() {
		a_tTypeList.add("Music");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if (!bAutoCancel || hmList.get("Music") && !bAbandon)	//取消动画
		{
			AddFilter();
			if (3 == nLoadType)
			{
				
				mpMediaPlayer = new MediaPlayer();
				try {
					AssetFileDescriptor fileDescriptor = JKDebug.hContext.getAssets().openFd(tPlayPath);
					mpMediaPlayer.setDataSource(fileDescriptor.getFileDescriptor(),fileDescriptor.getStartOffset(), fileDescriptor.getLength());
					mpMediaPlayer.prepare();
				} catch (Exception e) {
					JKLog.ErrorLog("音频文件不存在:" + tPlayPath + ".原因为" + e.getMessage());
					mpMediaPlayer = null;				
				}	
				
			}
			else if (1 == nLoadType)
			{
				mpMediaPlayer = new MediaPlayer();
				try {
					mpMediaPlayer.setDataSource(tPlayPath);
					mpMediaPlayer.prepare();
				} catch (Exception e) {
					JKLog.ErrorLog("音频文件不存在:" + tPlayPath + ".原因为" + e.getMessage());
					mpMediaPlayer = null;				
				}		
			}
			else if (2 == nLoadType)
			{
				int nId = JKDebug.hContext.getResources().getIdentifier(tPlayPath.substring(0,tPlayPath.indexOf(".")), "raw", JKDebug.hContext.getPackageName());
				mpMediaPlayer = MediaPlayer.create(hMain, nId);
			}
			if (mpMediaPlayer == null)
			{
				bAbandon = true;
				return 2;
			}
			nAnimationTime = mpMediaPlayer.getDuration() + 50;
			return 1;
		}
		else {
			return 2;
		}
	}

	@Override
	public void StartAnimation(HashMap<String, Boolean> hmList, Boolean jkvbTmp) {
		nRemainTime = nAnimationTime;
		lStartTime = System.currentTimeMillis();
		if (mpMediaPlayer != null && (!bAutoCancel || hmList.get("Music")))
		{			
			mpMediaPlayer.setOnVideoSizeChangedListener(null);
			mpMediaPlayer.setOnPreparedListener(null);
			
			/*播放音乐*/		
			if (jkvnSound > 0 || !jkvbSound)	//静音
				mpMediaPlayer.setVolume(0, 0);		
			
			mpMediaPlayer.start();		
			
			hlHandler = new Handler() {	
				public void dispatchMessage(Message msg) {					
					mpMediaPlayer.stop();
					mpMediaPlayer.release();
					a_tTypeList.remove("Music");
					CheckStatus();
				}
			};
			hlHandler.sendEmptyMessageDelayed(0, nAnimationTime);
		}
		else
		{	
			a_tTypeList.remove("Music");
			CheckStatus();
		}
	}

	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		if (bAutoCancel && !hmList.get("Music") && CheckStatus("Music"))	//取消动画
		{
			hlHandler.removeMessages(0);
			mpMediaPlayer.stop();
			mpMediaPlayer.release();			
			a_tTypeList.remove("Music");
			CheckStatus();
		}
	}

	@Override
	public void StopAnimation() {
		hlHandler.removeMessages(0);
		if (mpMediaPlayer != null)
		{
			mpMediaPlayer.stop();	
			mpMediaPlayer.release();
		}
	}

	@Override
	public void PauseAnimation() {
		hlHandler.removeMessages(0);
		nRemainTime = nRemainTime - (int) (System.currentTimeMillis() - lStartTime);
		if (mpMediaPlayer != null)
			mpMediaPlayer.pause();			
	}

	@Override
	public void RestartAnimation(HashMap<String, Boolean> hmList) {
		if (mpMediaPlayer == null)
			return;
		mpMediaPlayer.start();
		lStartTime = System.currentTimeMillis();
		hlHandler = new Handler() {
			public void dispatchMessage(Message msg) {
				mpMediaPlayer.stop();
				mpMediaPlayer.release();
				a_tTypeList.remove("Music");
				CheckStatus();
			}
		};
		hlHandler.sendEmptyMessageDelayed(0, nRemainTime);
	}	
	
	@Override
	public void EnableSound()
	{
		if (0 == jkvnSound && jkvbSound)	//恢复声音
			mpMediaPlayer.setVolume(1, 1);
	}
	
	@Override
	public void DisableSound()
	{
		mpMediaPlayer.setVolume(0, 0);
	}
}