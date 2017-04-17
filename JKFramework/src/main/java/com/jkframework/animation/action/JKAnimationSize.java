package com.jkframework.animation.action;

import android.view.View;

import com.jkframework.animation.JKAnimationOne;

import java.util.HashMap;

public class JKAnimationSize extends JKAnimationOne{

	/**布局元素*/
	private View vFrame;  
	/**需要缩放的X轴*/
	private float fScaleX;  
	/**需要缩放的X轴*/
	private float fScaleY;  
	
	/**回调函数默认执行*/
	public void FinishAnimation() {
		
	}

	/**
	 * 构造函数
	 * @param vTmp 布局元素
	 * @param fTmpX 元素X轴缩放倍数
	 * @param fTmpY 元素Y轴缩放倍数
	 */
	public JKAnimationSize(View vTmp,float fTmpX,float fTmpY) {
		vFrame = vTmp;
		fScaleX = fTmpX;
		fScaleY = fTmpY;
		nAnimationTime = 0;
		/*固定步骤*/
		InitFilter();
	}
	
	@Override
	public void InitFilter() {
		a_tFilterList.add("ScaleX");
		a_tFilterList.add("ScaleY");
	}

	@Override
	public void AddFilter() {
		a_tTypeList.add("ScaleX");
		a_tTypeList.add("ScaleY");
	}

	@Override
	public int InitAnimation(HashMap<String, Boolean> hmList) {
		if ((!bAutoCancel || hmList.get("ScaleX")) || (!bAutoCancel || hmList.get("ScaleY"))) //取消动画
		{
			AddFilter();
			return 1;
		}
		else
			return 2;
	}

	@Override
	public void StartAnimation(HashMap<String, Boolean> hmList, Boolean jkvbTmp) {
		//修改X轴比例
		if (!bAutoCancel || hmList.get("ScaleX"))
		{
			vFrame.setScaleX(fScaleX);
			a_tTypeList.remove("ScaleX");
			CheckStatus();
		}
		else
		{
			a_tTypeList.remove("ScaleX");
			CheckStatus();
		}
		//修改Y轴比例
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
		if (bAutoCancel && !hmList.get("ScaleX") && CheckStatus("ScaleX"))	//取消动画
		{
			a_tTypeList.remove("ScaleX");
			CheckStatus();
		}
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