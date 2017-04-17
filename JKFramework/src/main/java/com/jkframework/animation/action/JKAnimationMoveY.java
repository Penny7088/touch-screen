package com.jkframework.animation.action;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;




public class JKAnimationMoveY extends JKAnimationOne{
	
	/**起始X坐标*/
	private float fFromY;
	/**目标X坐标*/
	private float fToY;		
	/**表示动作结束坐标是否已知*/
	private boolean bBy = false;
	/**表示动作起始坐标是否已知*/
	private boolean bTo = false;
	/**动画剩余时间*/
	private int nRemainTime;
	/**动画经过时间*/
	private long lRunTime = 0;
	/**自定义动画移动Y*/
	private ObjectAnimator oaMoveY;
	/**自定义执行动画的布局*/
	private View vGroup;	
	/**动画插补器*/
	private TimeInterpolator  tiSet = new LinearInterpolator();
	
	/**
	 * 构造函数
	 * @param vTmp  动画对象
	 * @param fFrom  起始值
	 * @param fTo  目标值
	 * @param nTime  持续时间
	 */
	public JKAnimationMoveY(View vTmp,float fFrom,float fTo,int nTime)
	{	
		vGroup = vTmp;
		fFromY = fFrom;
		fToY = fTo;
		nAnimationTime = nTime;
		/*固定步骤*/
		InitFilter();
	}

	/**
	 * 构造函数
	 * @param vTmp  动画对象
	 * @param fFrom  起始值
	 * @param fTo  目标值
	 * @param nTime  持续时间
	 * @param tiTmp 动画插补器,默认为匀速
	 */
	public JKAnimationMoveY(View vTmp,float fFrom,float fTo,int nTime,TimeInterpolator tiTmp)
	{	
		vGroup = vTmp;
		fFromY = fFrom;
		fToY = fTo;
		nAnimationTime = nTime;
		tiSet = tiTmp;
		/*固定步骤*/
		InitFilter();
	}
	
	/**
	 * 构造函数	
	 * @param vTmp  动画对象
	 * @param fOne  起始/结束值
	 * @param bByto  为true时表示结束值已知,或反之
	 * @param nTime 持续时间
	 */
	public JKAnimationMoveY(View vTmp,float fOne,boolean bByto,int nTime)
	{				
		vGroup = vTmp;
		if (bByto)	//结束值已知
		{
			fFromY = fOne;
			bBy = true;
		}
		else {
			fToY = fOne;
			bTo = true;
		}
		nAnimationTime = nTime;
		/*固定步骤*/
		InitFilter();
	}
	
	@Override
	public void InitFilter() {
		a_tFilterList.add("MoveY");
	}
	
	@Override
	public void AddFilter() {
		a_tTypeList.add("MoveY");
	}
	
	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if (!bAutoCancel || hmList.get("MoveY"))	//取消动画
		{
			AddFilter();
			return 1;
		}
		else
			return 2;
	}

	@Override
	public void StartAnimation(HashMap<String, Boolean> hmList, Boolean jkvbTmp) {
		jkvbExit = jkvbTmp;
		nRemainTime = (int) (lFinishTime - System.currentTimeMillis());
		if (nRemainTime <= 0)	//防止动画不执行
			nRemainTime = 1;
		if (nAnimationTime <= 0)
			nRemainTime = 0;
		PlayAnimation(hmList);
	}

	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		if (bAutoCancel && !hmList.get("MoveY") && CheckStatus("MoveY"))	//取消动画
		{
			if (oaMoveY != null)
				oaMoveY.cancel();
		}
	}
	
	/**
	 * 回调函数默认执行
	 */
	public void FinishAnimation() {
	
		
	}

	@Override
	public void StopAnimation() {
		if (CheckStatus("MoveY"))
		{
			oaMoveY.cancel();
		}
	}

	@Override
	public void PauseAnimation() {
		if (CheckStatus("MoveY"))
		{
			lRunTime = oaMoveY.getCurrentPlayTime();
			oaMoveY.cancel();
		}		
	}

	@Override
	public void RestartAnimation(HashMap<String, Boolean> hmList) {
		PlayAnimation(hmList);
	}
	
	/**
	 * 播放动画
	 * @param hmList 过滤器
	 */
	public void PlayAnimation(HashMap<String, Boolean> hmList)	
	{
		if (bBy)
		{
			fToY = vGroup.getY();
			bBy = false;
		}
		if (bTo)
		{
			fFromY = vGroup.getY();
			bTo = false;
		}

		if ((!bAutoCancel || hmList.get("MoveY")) && nRemainTime>0)
		{
			oaMoveY = ObjectAnimator.ofFloat(vGroup, "y", fFromY, fToY);
			oaMoveY.setDuration(nRemainTime);
			oaMoveY.setInterpolator(tiSet);
			oaMoveY.addListener(new AnimatorListener() {

				public void onAnimationStart(Animator animation) {

				}

				public void onAnimationRepeat(Animator animation) {

				}

				public void onAnimationEnd(Animator animation) {
					if (!jkvbExit) {
						a_tTypeList.remove("MoveY");
						CheckStatus();
					}
				}

				public void onAnimationCancel(Animator animation) {

				}
			});

			oaMoveY.start();
			oaMoveY.setCurrentPlayTime(lRunTime);
		}
		else
		{
			a_tTypeList.remove("MoveY");
			CheckStatus();
		}
		
		//重置动画时间
		lRunTime = 0;
	}
}