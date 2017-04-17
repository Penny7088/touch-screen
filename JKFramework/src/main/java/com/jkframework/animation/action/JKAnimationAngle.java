package com.jkframework.animation.action;

import android.view.View;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;

public class JKAnimationAngle extends JKAnimationOne{

	/**布局元素*/
	private View vFrame;  
	/**需要移动到的角度*/
	private float fAngle;  
	
	
	/**回调函数默认执行*/
	public void FinishAnimation() {
		
	}

	/**
	 * 构造函数
	 * @param vTmp 布局元素
	 * @param fTmp 元素的旋转角度
	 */
	public JKAnimationAngle(View vTmp,float fTmp) {
		vFrame = vTmp;
		fAngle = fTmp;
		nAnimationTime = 0;
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
		if (!bAutoCancel || hmList.get("Rotate"))
		{
			vFrame.setRotation(fAngle);
			a_tTypeList.remove("Rotate");
			CheckStatus();
		}
		else
		{
			a_tTypeList.remove("Rotate");
			CheckStatus();
		}
	}
	
	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		if (bAutoCancel && !hmList.get("Rotate") && CheckStatus("Rotate"))	//取消动画
		{
			a_tTypeList.remove("Rotate");
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
	}

}