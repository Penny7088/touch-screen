package com.jkframework.animation.action;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;




public class JKAnimationAlpha extends JKAnimationOne{
	
	/**起始透明度*/
	private float fFromAlpha;
	/**目标透明度*/
	private float fToAlpha;
	/**表示动作结束坐标是否已知*/
	private boolean bBy = false;
	/**表示动作起始坐标是否已知*/
	private boolean bTo = false;
	/**动画剩余时间*/
	private int nRemainTime;	
	/**动画播放时间*/
	private long lRunTime = 0;	
	/**自定义透明动画*/
	private ObjectAnimator oaAlpha;
	/**自定义执行动画的布局*/
	private View vGroup;
	
	
	/**
	 * 构造函数
	 * @param vTmp 布局对象
	 * @param fFrom 起始透明度
	 * @param fTo  结束透明度
	 * @param nTime 持续时间
	 */
	public JKAnimationAlpha(View vTmp,float fFrom,float fTo,int nTime)
	{	
		vGroup = vTmp;
		fFromAlpha = fFrom;
		fToAlpha = fTo;
		nAnimationTime = nTime;
		/*固定步骤*/
		InitFilter();
	}
	
	/**
	 * 构造函数
	 * @param vTmp  布局对象
	 * @param fOne 起始/结束值
	 * @param bByto 为true时表示结束值已知
	 * @param nTime 持续时间
	 */
	public JKAnimationAlpha(View vTmp,float fOne,boolean bByto,int nTime)
	{	
		vGroup = vTmp;
		if (bByto)	//结束值已知
		{
			fFromAlpha = fOne;
			bBy = true;
		}
		else {
			fToAlpha = fOne;
			bTo = true;
		}
		nAnimationTime = nTime;
		/*固定步骤*/
		InitFilter();
	}
	
	@Override
	public void InitFilter() {		
		a_tFilterList.add("Alpha");		
	}
	
	@Override
	public void AddFilter() {
		a_tTypeList.add("Alpha");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if ((!bAutoCancel || hmList.get("Alpha")))	//取消动画
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
		if (bAutoCancel && !hmList.get("Alpha") && CheckStatus("Alpha"))	//取消动画
		{
			if (oaAlpha != null)
				oaAlpha.cancel();
		}	
	}

	/**
	 * 回调函数默认执行
	 */
	public void FinishAnimation() {
		
	}

	@Override
	public void StopAnimation() {
		if (CheckStatus("Alpha"))
		{
			oaAlpha.end();
		}
	}

	@Override
	public void PauseAnimation() {	
		
		if (CheckStatus("Alpha"))
		{
			lRunTime = oaAlpha.getCurrentPlayTime();
			oaAlpha.cancel();
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
	private void PlayAnimation(HashMap<String, Boolean> hmList)
	{		
		if (bBy)
		{
			fToAlpha = vGroup.getAlpha();
			bBy = false;
		}
		if (bTo)
		{
			fFromAlpha = vGroup.getAlpha();
			bTo = false;
		}

		if ((!bAutoCancel || hmList.get("Alpha")) && nRemainTime>0)
		{
			oaAlpha = ObjectAnimator.ofFloat(vGroup, "alpha", fFromAlpha, fToAlpha);
			oaAlpha.setDuration(nRemainTime);
			oaAlpha.setInterpolator(new LinearInterpolator());
			oaAlpha.addListener(new AnimatorListener() {

				public void onAnimationCancel(Animator animation) {

				}

				public void onAnimationEnd(Animator animation) {
					if (!jkvbExit)
					{
						a_tTypeList.remove("Alpha");
						CheckStatus();
					}
				}

				public void onAnimationRepeat(Animator animation) {

				}

				public void onAnimationStart(Animator animation) {

				}

			});

			oaAlpha.start();	//播放动画
			oaAlpha.setCurrentPlayTime(lRunTime);
		}
		else
		{
			a_tTypeList.remove("Alpha");
			CheckStatus();
		}
		//重置动画时间
		lRunTime = 0;
	}

}