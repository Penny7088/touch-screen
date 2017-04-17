package com.jkframework.animation.action;

import android.view.View;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;

public class JKAnimationOpacity extends JKAnimationOne{
	
	/**布局元素*/
	private View vFrame;  
	/**需要更改的透明度*/
	private float fAlpha;  
	
	
	/**回调函数默认执行*/
	public void FinishAnimation() {
		
	}
	
	/**
	 * 构造函数
	 * @param vTmp 元素
	 * @param fTmp 元素的X坐标
	 */
	public JKAnimationOpacity(View vTmp,float fTmp) {
		vFrame = vTmp;
		fAlpha = fTmp;
		nAnimationTime = 0;
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
		if (!bAutoCancel || hmList.get("Alpha"))	//取消动画
		{
			AddFilter();
			return 1;
		}
		else
			return 2;
	}

	@Override
	public void StartAnimation(HashMap<String, Boolean> hmList, Boolean jkvbTmp) {
		if (!bAutoCancel || hmList.get("Alpha"))
		{
			vFrame.setAlpha(fAlpha);
			a_tTypeList.remove("Alpha");
			CheckStatus();
		}
		else
		{
			a_tTypeList.remove("Alpha");
			CheckStatus();
		}
	}
	
	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		if (bAutoCancel && !hmList.get("Alpha") && CheckStatus("Alpha"))	//取消动画
		{
			a_tTypeList.remove("Alpha");
			CheckStatus();
		}
	}

	@Override
	public void StopAnimation() {
		
	}

	@Override
	public void PauseAnimation() {		
		
	}

	@Override
	public void RestartAnimation(HashMap<String, Boolean> hmList) {
		CheckStatus();
	}

}