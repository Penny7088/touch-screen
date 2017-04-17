package com.jkframework.animation.action;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;




public class JKAnimationMove extends JKAnimationOne{
	
	/**起始X坐标*/
	private float fFromX;
	/**目标X坐标*/
	private float fToX;
	/**起始Y坐标*/
	private float fFromY;
	/**目标Y坐标*/
	private float fToY;
	/**表示动作结束坐标是否已知*/
	private boolean bBy = false;
	/**表示动作起始坐标是否已知*/
	private boolean bTo = false;
	/**动画剩余时间*/
	private int nRemainTime;	
	/**动画X坐标运行时间*/
	private long lRunTimeX = 0;
	/**动画Y坐标运行时间*/
	private long lRunTimeY = 0;
	/**自定义动画移动X*/
	private ObjectAnimator oaMoveX;
	/**自定义动画移动Y*/
	private ObjectAnimator oaMoveY;
	/**自定义执行动画的布局*/
	private View vView;	
	/**动画插补器*/
	private TimeInterpolator  tiSet = new LinearInterpolator();
		
	/**
	 * 构造函数	
	 * @param vTmp 动画对象
	 * @param fFrom1 X坐标起始值
	 * @param fTo1 X坐标目标值
	 * @param fFrom2 Y坐标起始值
	 * @param fTo2 Y坐标目标值
	 * @param nTime 持续时间
	 */
	public JKAnimationMove(View vTmp,float fFrom1,float fTo1,float fFrom2,float fTo2,int nTime)
	{		
		vView = vTmp;
		fFromX = fFrom1;
		fToX = fTo1;
		fFromY = fFrom2;
		fToY = fTo2;
		nAnimationTime = nTime;
		/*固定步骤*/
		InitFilter();
	}
	
	/**
	 * 构造函数	
	 * @param vTmp  动画对象
	 * @param fOneX  X坐标起始/结束值
	 * @param fOneY  Y坐标起始/结束值
	 * @param bByto  为true时表示结束值已知,或反之
	 * @param nTime 持续时间
	 */
	public JKAnimationMove(View vTmp,float fOneX,float fOneY,boolean bByto,int nTime)
	{				
		vView = vTmp;
		if (bByto)	//结束值已知
		{
			fFromX = fOneX;
			fFromY = fOneY;
			bBy = true;
		}
		else {
			fToX = fOneX;
			fToY = fOneY;
			bTo = true;
		}
		nAnimationTime = nTime;
		/*固定步骤*/
		InitFilter();
	}
	
	/**
	 * 构造函数	
	 * @param vTmp 动画对象
	 * @param fFrom1 X坐标起始值
	 * @param fTo1 X坐标目标值
	 * @param fFrom2 Y坐标起始值
	 * @param fTo2 Y坐标目标值
	 * @param nTime 持续时间
	 * @param tiTmp	动画插补器,默认为匀速
	 */
	public JKAnimationMove(View vTmp,float fFrom1,float fTo1,float fFrom2,float fTo2,int nTime,TimeInterpolator tiTmp)
	{		
		vView = vTmp;
		fFromX = fFrom1;
		fToX = fTo1;
		fFromY = fFrom2;
		fToY = fTo2;
		nAnimationTime = nTime;
		tiSet = tiTmp;
		/*固定步骤*/
		InitFilter();
	}
	
	@Override
	public void InitFilter() {				
		a_tFilterList.add("MoveX");
		a_tFilterList.add("MoveY");
	}
	
	@Override
	public void AddFilter() {
		a_tTypeList.add("MoveX");
		a_tTypeList.add("MoveY");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if ((!bAutoCancel || hmList.get("MoveX")) || (!bAutoCancel || hmList.get("MoveY")))	//取消动画
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
		if (bAutoCancel && !hmList.get("MoveX") && CheckStatus("MoveX"))	//取消动画
		{
			if (oaMoveX != null)
				oaMoveX.cancel();
		}	
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
		if (CheckStatus("MoveX"))
		{
			oaMoveX.cancel();
		}
			
		if (CheckStatus("MoveY"))
		{
			oaMoveY.cancel();
		}
	}

	@Override
	public void PauseAnimation() {		
		
		if (CheckStatus("MoveX"))
		{
			lRunTimeX = oaMoveX.getCurrentPlayTime();
			oaMoveX.cancel();
		}
		if (CheckStatus("MoveY"))
		{
			lRunTimeY = oaMoveY.getCurrentPlayTime();
			oaMoveY.cancel();
		}
	}

	@Override
	public void RestartAnimation(HashMap<String, Boolean> hmList) {	
		if (nRemainTime <= 0)
		{
			a_tTypeList.remove("MoveX");
			a_tTypeList.remove("MoveY");
			CheckStatus();
		}
		else
			PlayAnimation(hmList);
	}
	
	/**
	 * 播放动画
	 * @param hmList 过滤列表
	 */
	public void PlayAnimation(HashMap<String, Boolean> hmList)
	{	
		if (bBy)
		{
			fToX = vView.getX();
			fToY = vView.getY();
			bBy = false;
		}
		if (bTo)
		{
			fFromX = vView.getX();
			fFromY = vView.getY();
			bTo = false;
		}

		if ((!bAutoCancel || hmList.get("MoveX")) && nRemainTime>0)
		{
			oaMoveX = ObjectAnimator.ofFloat(vView, "x", fFromX, fToX);
			oaMoveX.setDuration(nRemainTime);
			oaMoveX.setInterpolator(tiSet);
			oaMoveX.addListener(new AnimatorListener() {

				public void onAnimationStart(Animator animation) {

				}

				public void onAnimationRepeat(Animator animation) {

				}

				public void onAnimationEnd(Animator animation) {
					if (!jkvbExit)
					{
						a_tTypeList.remove("MoveX");
						CheckStatus();
					}
				}

				public void onAnimationCancel(Animator animation) {

				}
			});

			oaMoveX.start();
			oaMoveX.setCurrentPlayTime(lRunTimeX);
		}
		else
		{
			a_tTypeList.remove("MoveX");
			CheckStatus();
		}

		if ((!bAutoCancel || hmList.get("MoveY")) && nRemainTime>0)
		{
			oaMoveY = ObjectAnimator.ofFloat(vView, "y", fFromY, fToY);
			oaMoveY.setDuration(nRemainTime);
			oaMoveY.setInterpolator(tiSet);
			oaMoveY.addListener(new AnimatorListener() {

				public void onAnimationStart(Animator animation) {

				}

				public void onAnimationRepeat(Animator animation) {

				}

				public void onAnimationEnd(Animator animation) {
					if (!jkvbExit)
					{
						a_tTypeList.remove("MoveY");
						CheckStatus();
					}
				}

				public void onAnimationCancel(Animator animation) {

				}
			});

			oaMoveY.start();
			oaMoveY.setCurrentPlayTime(lRunTimeY);
		}
		else
		{
			a_tTypeList.remove("MoveY");
			CheckStatus();
		}
		
		//重置时间
		lRunTimeX = 0;
		lRunTimeY = 0;
	}
}