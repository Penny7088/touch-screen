package com.jkframework.animation.action;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;


public class JKAnimationRotate extends JKAnimationOne{

	
	/**起始Rotate值*/
	private float fFromRotate;
	/**目标Rotate值*/
	private float fToRotate;
	/**动画旋转中心X(0~1)*/
	private float fAnchorX;
	/**动画旋转中心Y(0~1)*/
	private float fAnchorY;
	/**表示动作结束坐标是否已知*/
	private boolean bBy = false;
	/**表示动作起始坐标是否已知*/
	private boolean bTo = false;
	/**动画剩余时间*/
	private int nRemainTime;
	/**布局对象宽度*/
	private int nViewWidth = 0;
	/**布局对象高度*/
	private int nViewHeight = 0;
	/**动画经过时间*/
	private long lRunTime = 0;
	/**自定义动画移动X*/
	private ObjectAnimator oaRotate;
	/**自定义执行动画的布局*/
	private View vGroup;
	
	/**
	 * 回调函数默认执行
	 */
	public void FinishAnimation() {
			
	}

	/**
	 * 构造函数
	 * @param vTmp
	 * @param fFrom
	 * @param fTo
	 * @param fCenterX
	 * @param fCenterY
	 * @param nTime
	 */
	public JKAnimationRotate(View vTmp,float fFrom,float fTo,float fCenterX,float fCenterY,int nTime)
	{				
		vGroup = vTmp;
		fFromRotate = fFrom;
		fToRotate = fTo;		
		nAnimationTime = nTime;
		fAnchorX = fCenterX;
		fAnchorY = fCenterY;
		/*固定步骤*/
		InitFilter();
	}
	
	/**
	 * 构造函数
	 * @param vTmp  动画对象
	 * @param fOne  起始/结束旋转角度
	 * @param bByto  为true时表示结束值已知,或反之
	 * @param fCenterX 中心点X
	 * @param fCenterY 中心点Y
	 * @param nTime 持续时间
	 */
	public JKAnimationRotate(View vTmp,float fOne,boolean bByto,float fCenterX,float fCenterY,int nTime)
	{				
		vGroup = vTmp;
		if (bByto)	//结束值已知
		{
			fFromRotate = fOne;
			bBy = true;
		}
		else {
			fToRotate = fOne;
			bTo = true;
		}
		nAnimationTime = nTime;
		fAnchorX = fCenterX;
		fAnchorY = fCenterY;
		/*固定步骤*/
		InitFilter();
	}
	
	@Override
	public void InitFilter() {
		a_tFilterList.add("Rotate");		
	}

	@Override
	public void AddFilter() {
		a_tTypeList.add("Rotate");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if (!bAutoCancel || hmList.get("Rotate"))	//取消动画
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
		vGroup.setPivotX(fAnchorX*(nViewWidth > 0 ? nViewWidth :vGroup.getWidth()));
		vGroup.setPivotY(fAnchorY*(nViewHeight > 0 ? nViewHeight : vGroup.getHeight()));
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
		if (bAutoCancel && !hmList.get("Rotate") && CheckStatus("Rotate"))	//取消动画
		{
			if (oaRotate != null)
				oaRotate.cancel();
		}
	}

	@Override
	public void StopAnimation() {
		if (CheckStatus("Rotate"))
		{
			oaRotate.end();
		}
	}

	@Override
	public void PauseAnimation() {
		if (CheckStatus("Rotate"))
		{
			lRunTime = oaRotate.getCurrentPlayTime();
			oaRotate.cancel();
		}		
	}

	@Override
	public void RestartAnimation(HashMap<String, Boolean> hmList) {
		PlayAnimation(hmList);
	}

	/**
	 * 播放旋转动画
	 * @param hmList 过滤器
	 */
	public void PlayAnimation(HashMap<String, Boolean> hmList)	
	{
		if (bBy)
		{
			fToRotate = vGroup.getRotation();
			bBy = false;
		}
		if (bTo)
		{
			fFromRotate = vGroup.getRotation();
			bTo = false;
		}

		if ((!bAutoCancel || hmList.get("Rotate")) && nRemainTime>0)
		{
			oaRotate = ObjectAnimator.ofFloat(vGroup, "rotation", fFromRotate, fToRotate);
			oaRotate.setDuration(nRemainTime);
			oaRotate.setInterpolator(new LinearInterpolator());
			oaRotate.addListener(new AnimatorListener() {

				public void onAnimationStart(Animator animation) {

				}

				public void onAnimationRepeat(Animator animation) {

				}

				public void onAnimationEnd(Animator animation) {
					if (!jkvbExit)
					{
						a_tTypeList.remove("Rotate");
						CheckStatus();
					}
				}

				public void onAnimationCancel(Animator animation) {

				}
			});

			oaRotate.start();
			oaRotate.setCurrentPlayTime(lRunTime);
		}
		else
		{
			a_tTypeList.remove("Rotate");
			CheckStatus();
		}
		
		//重置动画时间
		lRunTime = 0;
	}

}