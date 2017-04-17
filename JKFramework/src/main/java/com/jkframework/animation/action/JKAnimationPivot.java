package com.jkframework.animation.action;

import android.view.View;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;

public class JKAnimationPivot extends JKAnimationOne{

	/**布局元素*/
	private View vFrame;  
	/**需要更改的锚点X*/
	private float fPivotX;  
	/**需要更改的锚点Y*/
	private float fPivotY;
	
	/**回调函数默认执行*/
	public void FinishAnimation() {
		
	}

	/**
	 * 构造函数
	 * @param vTmp 布局元素
	 * @param fTmpX 需要更改的锚点X
	 * @param fTmpY 需要更改的锚点Y
	 */
	public JKAnimationPivot(View vTmp,float fTmpX,float fTmpY) {
		vFrame = vTmp;
		fPivotX = fTmpX;
		fPivotY = fTmpY;
		nAnimationTime = 0;
		/*固定步骤*/
		InitFilter();
	}
	
	@Override
	public void InitFilter() {

	}

	@Override
	public void AddFilter() {

	}
	
	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		AddFilter();
		return 1;
	}

	@Override
	public void StartAnimation(HashMap<String, Boolean> hmList, Boolean jkvbTmp) {
		float fOldPivotX = vFrame.getPivotX();
		float fOldPivotY = vFrame.getPivotY();
		vFrame.setPivotX(fPivotX*vFrame.getWidth());
		vFrame.setPivotY(fPivotY*vFrame.getHeight());
			/*是否刷新屏幕*/
		if (fOldPivotX != vFrame.getPivotX() || fOldPivotY != vFrame.getPivotY())
			vFrame.invalidate();
		CheckStatus();
	}
	
	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		
	}

	@Override
	public void StopAnimation() {
		
	}

	@Override
	public void PauseAnimation() {		
		
	}

	@Override
	public void RestartAnimation(HashMap<String, Boolean> hmList) {
		float fOldPivotX = vFrame.getPivotX();
		float fOldPivotY = vFrame.getPivotY();
		vFrame.setPivotX(fPivotX*vFrame.getWidth());
		vFrame.setPivotY(fPivotY*vFrame.getHeight());
			/*是否刷新屏幕*/
		if (fOldPivotX != vFrame.getPivotX() || fOldPivotY != vFrame.getPivotY())
			vFrame.invalidate();
		CheckStatus();
	}

}