package com.jkframework.animation.action;

import android.os.Handler;
import android.os.Message;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;

public class JKAnimationSleep extends JKAnimationOne{

	/**计时器*/
	private Handler hlHandler = new Handler();
	/**动画开始的时间*/
	private long lStartTime;
	/**动画剩余的时间*/
	private int nRemainTime;	
	
	/**
	 * 回调函数默认执行
	 */
	public void FinishAnimation() {
				
	}

	/**
	 * 构造函数,注意延迟选项默认是不被复写的
	 * @param nSleep 等待时间
	 */
	public JKAnimationSleep(int nSleep)
	{
		nAnimationTime = nSleep;
		bAutoCancel = false;
		/*固定步骤*/
		InitFilter();		
	}
	
	@Override
	public void InitFilter() {
		a_tFilterList.add("Sleep");
	}

	@Override
	public void AddFilter() {
		a_tTypeList.add("Sleep");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if (!bAutoCancel || hmList.get("Sleep")) //取消动画
		{
			AddFilter();
			return 1;
		}			
		else
			return 2;
	}

	@Override
	public void StartAnimation(HashMap<String, Boolean> hmList, Boolean jkvbTmp) {
		/*间隔时间进行查补误差*/
		nRemainTime = (int) (lFinishTime - System.currentTimeMillis());
		if (nRemainTime < 0)	//防止动画不执行
			nRemainTime = 1;
		if (nAnimationTime <= 0)
			nRemainTime = 0;
		PlayAnimation(hmList, nRemainTime);
	}

	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		if (bAutoCancel && !hmList.get("Sleep") && CheckStatus("Sleep"))	//取消动画
			hlHandler.removeMessages(0);	
	}

	@Override
	public void StopAnimation() {
		if (CheckStatus("Sleep"))
		{
			hlHandler.removeMessages(0);	
		}
	}

	@Override
	public void PauseAnimation() {
		if (CheckStatus("Sleep"))
		{
			hlHandler.removeMessages(0);
			nRemainTime = nRemainTime - (int) (System.currentTimeMillis() - lStartTime);
		}
	}

	@Override
	public void RestartAnimation(HashMap<String, Boolean> hmList) {
		PlayAnimation(hmList,nRemainTime);
	}	
	
	/**
	 * 播放动画
	 * @param hmList 过滤列表
	 * @param nTime 持续时间
	 */
	private void PlayAnimation(HashMap<String, Boolean> hmList,int nTime)
	{
		lStartTime = System.currentTimeMillis();
		if (!bAutoCancel || hmList.get("Sleep") && nRemainTime>0)
		{
			hlHandler = new Handler() {
				public void dispatchMessage(Message msg) {
					a_tTypeList.remove("Sleep");
					CheckStatus();
				}
			};
			hlHandler.sendEmptyMessageDelayed(0, nTime);
		}
		else
		{
			a_tTypeList.remove("Sleep");
			CheckStatus();
		}
	}

}