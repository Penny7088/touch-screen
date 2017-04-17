package com.jkframework.animation.action;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;




public class JKAnimationRotateLR extends JKAnimationOne{

	/**起始RotateY值*/
	private float fFromY;
	/**目标RotateY值*/
	private float fToY;
	/**动画旋转中心X(0~1)*/
	private float fAnchorX;
	/**动画旋转中心Y(0~1)*/
	private float fAnchorY;
	/**动画剩余时间*/
	private int nRemainTime;
	/**动画经过时间*/
	private long lRunTime = 0;
	/**自定义动画移动X*/
	private ObjectAnimator oaRotateY;
	/**自定义执行动画的布局*/
	private View vGroup;
	
	/**
	 * 回调函数默认执行
	 */
	public void FinishAnimation() {
		
	}
	
	/**
	 * 构造函数
	 * @param vTmp  动画对象
	 * @param fFrom 起始RotateY值
	 * @param fTo 目标RotateY值
	 * @param fCenterX 中心点X
	 * @param fCenterY 中心点Y
	 * @param nTime 持续时间
	 */
	public JKAnimationRotateLR(View vTmp,float fFrom,float fTo,float fCenterX,float fCenterY,int nTime)
	{				
		vGroup = vTmp;
		fFromY = fFrom;
		fToY = fTo;
		fAnchorX = fCenterX;
		fAnchorY = fCenterY;
		nAnimationTime = nTime;
		/*固定步骤*/
		InitFilter();
	}

	@Override
	public void InitFilter() {
		a_tFilterList.add("RotateLR");
	}

	@Override
	public void AddFilter() {
		a_tTypeList.add("RotateLR");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if (!bAutoCancel || hmList.get("RotateLR"))	//取消动画
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
		float fOldPivotY = vGroup.getPivotY();
		vGroup.setPivotX(fAnchorX*vGroup.getWidth());
		vGroup.setPivotY(fAnchorY*vGroup.getHeight());
			/*是否刷新屏幕*/
		if (fOldPivotX != vGroup.getPivotX() || fOldPivotY != vGroup.getPivotY())
			vGroup.invalidate();
		if (nRemainTime <= 0)	//防止动画不执行
			nRemainTime = 1;
		if (nAnimationTime <= 0)
			nRemainTime = 0;
		PlayAnimation(hmList);
	}

	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		if (bAutoCancel && !hmList.get("RotateLR") && CheckStatus("RotateLR"))	//取消动画
		{
			if (oaRotateY != null)
				oaRotateY.cancel();
		}
	}

	@Override
	public void StopAnimation() {
		if (CheckStatus("RotateLR"))
			oaRotateY.end();
	}

	@Override
	public void PauseAnimation() {
		if (CheckStatus("RotateLR"))
		{
			lRunTime = oaRotateY.getCurrentPlayTime();
			oaRotateY.cancel();
		}
	}

	@Override
	public void RestartAnimation(HashMap<String, Boolean> hmList) {
		PlayAnimation(hmList);
	}
	
	/**
	 * 播放翻转动画
	 * @param hmList 过滤器
	 */
	public void PlayAnimation(HashMap<String, Boolean> hmList)	
	{
		if ((!bAutoCancel || hmList.get("RotateLR")) && nRemainTime>0)
		{
			oaRotateY = ObjectAnimator.ofFloat(vGroup, "rotationY", fFromY, fToY);
			oaRotateY.setDuration(nRemainTime);
			oaRotateY.setInterpolator(new LinearInterpolator());
			oaRotateY.addListener(new AnimatorListener() {

				public void onAnimationStart(Animator animation) {

				}

				public void onAnimationRepeat(Animator animation) {

				}

				public void onAnimationEnd(Animator animation) {
					if (!jkvbExit)
					{
						a_tTypeList.remove("RotateLR");
						CheckStatus();
					}
				}

				public void onAnimationCancel(Animator animation) {

				}
			});

			oaRotateY.start();
			oaRotateY.setCurrentPlayTime(lRunTime);
		}
		else
		{
			a_tTypeList.remove("RotateLR");
			CheckStatus();
		}
		
		//重置动画时间
		lRunTime = 0;
	}
}