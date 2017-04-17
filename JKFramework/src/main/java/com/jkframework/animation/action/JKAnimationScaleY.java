package com.jkframework.animation.action;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;




public class JKAnimationScaleY extends JKAnimationOne{

	/**起始ScaleY值*/
	private float fFromScaleY;
	/**目标ScaleY值*/
	private float fToScaleY;
	/**缩放Y轴中心(0~1)*/
	private float fAnchorY;
	/**动画剩余时间*/
	private int nRemainTime;
	/**动画经过时间*/
	private long lRunTime = 0;
	/**自定义动画缩放X*/
	private ObjectAnimator oaScaleY;
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
	 * @param fCenterY 缩放X轴中心
	 * @param nTime 持续时间
	 */
	public JKAnimationScaleY(View vTmp,float fFrom,float fTo,float fCenterY,int nTime)
	{
		nAnimationTime = nTime;
		fFromScaleY = fFrom;
		fToScaleY = fTo;
		fAnchorY = fCenterY;		
		vGroup = vTmp;
		/*固定步骤*/
		InitFilter();
	}

	@Override
	public void InitFilter() {
		a_tFilterList.add("ScaleY");		
	}
	
	@Override
	public void AddFilter() {
		a_tTypeList.add("ScaleY");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if (!bAutoCancel || hmList.get("ScaleY")) //取消动画
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
		float fOldPivotY = vGroup.getPivotY();
		vGroup.setPivotY(fAnchorY*vGroup.getHeight());
			/*是否刷新屏幕*/
		if (fOldPivotY != vGroup.getPivotY())
			vGroup.invalidate();
		if (nRemainTime <= 0)	//防止动画不执行
			nRemainTime = 1;
		if (nAnimationTime <= 0)
			nRemainTime = 0;
		PlayAnimation(hmList);
	}

	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		if (bAutoCancel && !hmList.get("ScaleY") && CheckStatus("ScaleY"))	//取消动画
		{
			if (oaScaleY != null)
				oaScaleY.cancel();
		}
	}

	@Override
	public void StopAnimation() {
		if (CheckStatus("ScaleY"))
		{
			oaScaleY.end();
		}			
	}

	@Override
	public void PauseAnimation() {
		if (CheckStatus("ScaleY"))
		{
			lRunTime = oaScaleY.getCurrentPlayTime();
			oaScaleY.cancel();
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
		if ((!bAutoCancel || hmList.get("ScaleY")) && nRemainTime>0)
		{
			oaScaleY = ObjectAnimator.ofFloat(vGroup, "scaleY", fFromScaleY, fToScaleY);
			oaScaleY.setDuration(nRemainTime);
			oaScaleY.setInterpolator(new LinearInterpolator());
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
			oaScaleY.setCurrentPlayTime(lRunTime);
		}
		else
		{
			a_tTypeList.remove("ScaleY");
			CheckStatus();
		}
		
		//重置动画时间
		lRunTime = 0;
	}

}