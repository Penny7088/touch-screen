package com.jkframework.animation.action;

import android.view.View;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;

public class JKAnimationPosX extends JKAnimationOne{

	/**布局元素*/
	private View vFrame;  
	/**需要移动到的X坐标*/
	private float fX;  
	
	
	/**回调函数默认执行*/
	public void FinishAnimation() {
		
	}

	/**
	 * 构造函数
	 * @param vTmp 布局元素
	 * @param fTmp 元素的X坐标
	 */
	public JKAnimationPosX(View vTmp,float fTmp) {
		vFrame = vTmp;
		fX = fTmp;
		nAnimationTime = 0;
		/*固定步骤*/
		InitFilter();
	}
	
	@Override
	public void InitFilter() {
		a_tFilterList.add("MoveX");
	}

	@Override
	public void AddFilter() {
		a_tTypeList.add("MoveX");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if (!bAutoCancel || hmList.get("MoveX"))	//取消动画
		{
			AddFilter();
			return 1;
		}
		else
			return 2;
	}

	@Override
	public void StartAnimation(HashMap<String, Boolean> hmList, Boolean jkvbTmp) {
		if (!bAutoCancel || hmList.get("MoveX"))
		{
			vFrame.setX(fX);
			a_tTypeList.remove("MoveX");
			CheckStatus();
		}
		else
		{
			a_tTypeList.remove("MoveX");
			CheckStatus();
		}
	}
	
	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		if (bAutoCancel && !hmList.get("MoveX") && CheckStatus("MoveX"))	//取消动画
		{
			a_tTypeList.remove("MoveX");
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