package com.jkframework.animation.action;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;




public class JKAnimationScaleX extends JKAnimationOne{

	/**起始ScaleX值*/
	private float fFromScaleX;
	/**目标ScaleX值*/
	private float fToScaleX;
	/**缩放X轴中心(0~1)*/
	private float fAnchorX;
	/**动画剩余时间*/
	private int nRemainTime;
	/**动画经过时间*/
	private long lRunTime;
	/**自定义动画缩放X*/
	private ObjectAnimator oaScaleX;
	/**自定义执行动画的布局*/
	private View vGroup;
	
	/**
	 * 回调函数默认执行
	 */
	public void FinishAnimation() {
		
	}
	
	/**
	 * 构造函数
	 * @param vTmp 动画对象
	 * @param fFrom 起始缩放值
	 * @param fTo 结束缩放值
	 * @param fCenterX 缩放X轴中心
	 * @param nTime 持续时间
	 */
	public JKAnimationScaleX(View vTmp,float fFrom,float fTo,float fCenterX,int nTime)
	{
		nAnimationTime = nTime;
		fFromScaleX = fFrom;
		fToScaleX = fTo;
		fAnchorX = fCenterX;		
		vGroup = vTmp;
		/*固定步骤*/
		InitFilter();	
	}

	@Override
	public void InitFilter() {
		a_tFilterList.add("ScaleX");		
	}
	
	@Override
	public void AddFilter() {
		a_tTypeList.add("ScaleX");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if (!bAutoCancel || hmList.get("ScaleX")) //取消动画
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
		/*设置锚点*/
		float fOldPivotX = vGroup.getPivotX();
		vGroup.setPivotX(fAnchorX*vGroup.getWidth());
			/*是否刷新屏幕*/
		if (fOldPivotX != vGroup.getPivotX())
			vGroup.invalidate();
		if (nRemainTime <= 0)	//防止动画不执行
			nRemainTime = 1;
		if (nAnimationTime <= 0)
			nRemainTime = 0;
		PlayAnimation(hmList);
	}

	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		if (bAutoCancel && !hmList.get("ScaleX") && CheckStatus("ScaleX"))	//取消动画
		{
			if (oaScaleX != null)
				oaScaleX.cancel();
		}
	}

	@Override
	public void StopAnimation() {
		if (CheckStatus("ScaleX"))
		{
			oaScaleX.end();
		}
			
	}

	@Override
	public void PauseAnimation() {
		if (CheckStatus("ScaleX"))
		{
			lRunTime = oaScaleX.getCurrentPlayTime();
			oaScaleX.cancel();
		}
	}

	@Override
	public void RestartAnimation(HashMap<String, Boolean> hmList) {
		PlayAnimation(hmList);
	}
	
	/**
	 * 播放缩放动画
	 * @param hmList  过滤器
	 */
	public void PlayAnimation(HashMap<String, Boolean> hmList)	
	{
		if ((!bAutoCancel || hmList.get("ScaleX")) && nRemainTime>0)
		{
			oaScaleX = ObjectAnimator.ofFloat(vGroup, "scaleX", fFromScaleX, fToScaleX);
			oaScaleX.setDuration(nRemainTime);
			oaScaleX.setInterpolator(new LinearInterpolator());
			oaScaleX.addListener(new AnimatorListener() {

				public void onAnimationStart(Animator animation) {

				}

				public void onAnimationRepeat(Animator animation) {

				}

				public void onAnimationEnd(Animator animation) {
					if (!jkvbExit)
					{
						a_tTypeList.remove("ScaleX");
						CheckStatus();
					}
				}

				public void onAnimationCancel(Animator animation) {

				}
			});

			oaScaleX.start();
			oaScaleX.setCurrentPlayTime(lRunTime);
		}
		else
		{
			a_tTypeList.remove("ScaleX");
			CheckStatus();
		}
		
		//重置当前时间
		lRunTime = 0;
	}

}