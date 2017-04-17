package com.jkframework.animation.action;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;


public class JKAnimationScale extends JKAnimationOne{

	/**起始ScaleX值*/
	private float fFromScaleX;
	/**目标ScaleX值*/
	private float fToScaleX;
	/**起始ScaleY值*/
	private float fFromScaleY;
	/**目标ScaleY值*/
	private float fToScaleY;
	/**缩放X轴中心(0~1)*/
	private float fAnchorX;
	/**缩放Y轴中心(0~1)*/
	private float fAnchorY;
	/**动画剩余时间*/
	private int nRemainTime;
	/**动画X轴经过时间*/
	private long lRunTimeX = 0;
	/**动画Y轴经过时间*/
	private long lRunTimeY = 0;
	/**是否需要计算锚点*/
	private boolean bCalAnchor = true;
	/**自定义动画缩放X*/
	private ObjectAnimator oaScaleX;
	/**自定义动画缩放X*/
	private ObjectAnimator oaScaleY;
	/**自定义执行动画的布局*/
	private View vGroup;
	/**动画插补器*/
	private TimeInterpolator  tiSet = new LinearInterpolator();
	
	
	/**
	 * 回调函数默认执行
	 */
	public void FinishAnimation() {
		
	}

	/**
	 * 构造函数
	 * @param vTmp	动画对象
	 * @param fFromX 起始X轴缩放值
	 * @param fToX 目标X轴缩放值
	 * @param fCenterX 缩放中心X值
	 * @param fFromY 起始Y轴缩放值
	 * @param fToY 目标Y轴缩放值
	 * @param fCenterY 缩放中心Y值
	 * @param nTime 持续时间
	 */
	public JKAnimationScale(View vTmp,float fFromX,float fToX,float fCenterX,float fFromY,float fToY,float fCenterY,int nTime)
	{
		nAnimationTime = nTime;
		fFromScaleX = fFromX;
		fToScaleX = fToX;
		fAnchorX = fCenterX;
		fFromScaleY = fFromY;
		fToScaleY = fToY;
		fAnchorY = fCenterY;	
		vGroup = vTmp;
		/*固定步骤*/		
		InitFilter();			
	}

	/**
	 * 构造函数
	 * @param vTmp	动画对象
	 * @param fFromX 起始X轴缩放值
	 * @param fToX 目标X轴缩放值
	 * @param fCenterX 缩放中心X值
	 * @param fFromY 起始Y轴缩放值
	 * @param fToY 目标Y轴缩放值
	 * @param fCenterY 缩放中心Y值
	 * @param nTime 持续时间
	 * @param tiTmp 动画插补器,默认为匀速
	 */
	public JKAnimationScale(View vTmp,float fFromX,float fToX,float fCenterX,float fFromY,float fToY,float fCenterY,int nTime,TimeInterpolator tiTmp)
	{
		nAnimationTime = nTime;
		fFromScaleX = fFromX;
		fToScaleX = fToX;
		fAnchorX = fCenterX;
		fFromScaleY = fFromY;
		fToScaleY = fToY;
		fAnchorY = fCenterY;	
		vGroup = vTmp;
		tiSet = tiTmp;
		/*固定步骤*/		
		InitFilter();
	}
	
	@Override
	public void InitFilter() {
		a_tFilterList.add("ScaleX");
		a_tFilterList.add("ScaleY");
	}

	@Override
	public void AddFilter() {
		a_tTypeList.add("ScaleX");
		a_tTypeList.add("ScaleY");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if ((!bAutoCancel || hmList.get("ScaleX")) || (!bAutoCancel || hmList.get("ScaleY"))) //取消动画
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
		if (bCalAnchor)	//计算锚点
		{
			float fOldPivotX = vGroup.getPivotX();
			float fOldPivotY = vGroup.getPivotY();
			vGroup.setPivotX(fAnchorX*vGroup.getWidth());
			vGroup.setPivotY(fAnchorY*vGroup.getHeight());
				/*是否刷新屏幕*/
			if (fOldPivotX != vGroup.getPivotX() || fOldPivotY != vGroup.getPivotY())
				vGroup.invalidate();
		}
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
		if (bAutoCancel && !hmList.get("ScaleY") && CheckStatus("ScaleY"))	//取消动画
		{
			if (oaScaleY != null)
				oaScaleY.cancel();
		}
	}

	@Override
	public void StopAnimation() {
		if (CheckStatus("ScaleX"))
		{
			oaScaleX.end();
		}
		if (CheckStatus("ScaleY"))
		{
			oaScaleY.end();
		}
	}

	@Override
	public void PauseAnimation() {
		if (CheckStatus("ScaleX"))
		{
			lRunTimeX = oaScaleX.getCurrentPlayTime();
			oaScaleX.cancel();
		}
		if (CheckStatus("ScaleY"))
		{
			lRunTimeY = oaScaleY.getCurrentPlayTime();
			oaScaleY.cancel();
		}
	}

	@Override
	public void RestartAnimation(HashMap<String, Boolean> hmList) {
		PlayAnimation(hmList);
	}
	
	/**
	 * 播放缩放动画
	 * @param hmList 过滤器
	 */
	public void PlayAnimation(HashMap<String, Boolean> hmList)	
	{
		if ((!bAutoCancel || hmList.get("ScaleX")) && nRemainTime>0)
		{
			oaScaleX = ObjectAnimator.ofFloat(vGroup, "scaleX", fFromScaleX, fToScaleX);
			oaScaleX.setDuration(nRemainTime);
			oaScaleX.setInterpolator(tiSet);
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
			oaScaleX.setCurrentPlayTime(lRunTimeX);
		}
		else
		{
			a_tTypeList.remove("ScaleX");
			CheckStatus();
		}

		if ((!bAutoCancel || hmList.get("ScaleY")) && nRemainTime>0)
		{
			oaScaleY = ObjectAnimator.ofFloat(vGroup, "ScaleY", fFromScaleY, fToScaleY);
			oaScaleY.setDuration(nRemainTime);
			oaScaleY.setInterpolator(tiSet);
			oaScaleY.addListener(new AnimatorListener() {

				public void onAnimationStart(Animator animation) {

				}

				public void onAnimationRepeat(Animator animation) {

				}

				public void onAnimationEnd(Animator animation) {
					if (!jkvbExit)
					{
						a_tTypeList.remove("ScaleY");
						CheckStatus();
					}
				}

				public void onAnimationCancel(Animator animation) {

				}
			});

			oaScaleY.start();
			oaScaleY.setCurrentPlayTime(lRunTimeY);
		}
		else
		{
			a_tTypeList.remove("ScaleY");
			CheckStatus();
		}
		
		//重置动画时间
		lRunTimeX = 0;
		lRunTimeY = 0;
	}
}