package com.jkframework.animation.action;

import android.view.View;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;

public class JKAnimationPosY extends JKAnimationOne{

	/**布局元素*/
	private View vFrame;  
	/**需要移动到的X坐标*/
	private float fY;  
	
	
	/**回调函数默认执行*/
	public void FinishAnimation() {
		
	}

	/**
	 * 构造函数
	 * @param vTmp 布局元素
	 * @param fTmp 元素的Y坐标
	 */
	public JKAnimationPosY(View vTmp,float fTmp) {
		vFrame = vTmp;
		fY = fTmp;
		nAnimationTime = 0;
		/*固定步骤*/
		InitFilter();
	}
	
	@Override
	public void InitFilter() {
		a_tFilterList.add("MoveY");
	}

	@Override
	public void AddFilter() {
		a_tTypeList.add("MoveY");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if (!bAutoCancel || hmList.get("MoveY"))	//取消动画
		{
			AddFilter();
			return 1;
		}
		else
			return 2;
	}

	@Override
	public void StartAnimation(HashMap<String, Boolean> hmList, Boolean jkvbTmp) {
		if (!bAutoCancel || hmList.get("MoveY"))
		{
			vFrame.setY(fY);
			a_tTypeList.remove("MoveY");
			CheckStatus();
		}
		else
		{
			a_tTypeList.remove("MoveY");
			CheckStatus();
		}
	}
	
	@Override
	public void UpdateAnimation(HashMap<String, Boolean> hmList) {
		if (bAutoCancel && !hmList.get("MoveY") && CheckStatus("MoveY"))	//取消动画
		{
			a_tTypeList.remove("MoveY");
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