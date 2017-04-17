package com.jkframework.animation.action;

import android.view.View;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;

public class JKAnimationSizeY extends JKAnimationOne{

	/**布局元素*/
	private View vFrame;  
	/**需要缩放的X轴*/
	private float fScaleY;  
	
	
	/**回调函数默认执行*/
	public void FinishAnimation() {
		
	}

	/**
	 * 构造函数
	 * @param vTmp 布局元素
	 * @param fTmp 元素Y轴缩放倍数
	 */
	public JKAnimationSizeY(View vTmp,float fTmp) {
		vFrame = vTmp;
		fScaleY = fTmp;
		nAnimationTime = 0;
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
		if (!bAutoCancel || hmList.get("ScaleY"))
		{
			vFrame.setScaleY(fScaleY);
			a_tTypeList.remove("ScaleY");
			CheckStatus();
		}
		else
		{
			a_tTypeList.remove("ScaleY");
			CheckStatus();
		}
	}
	
	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		if (bAutoCancel && !hmList.get("ScaleY") && CheckStatus("ScaleY"))	//取消动画
		{
			a_tTypeList.remove("ScaleY");
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