package com.jkframework.callback;

import android.view.View;


public interface JKViewPagerListener {
	
	/**
	 * 滑动改变后回调
	 * @param nCurrentPos 当前视图位置
	 * @param nDirection 滑动方向(内容往左边走为正数)
	 * @param bSuccess true为滑动成功,false为滑动取消
	 */
	public void OnChanged(int nCurrentPos, int nDirection, boolean bSuccess);
	
	/**
	 * 滑动与拖动中的各个界面状态更新
	 * @param nCurrentPos 界面所在的索引位置(从0开始)
	 * @param vView 界面布局对象
	 * @param fScale 界面与当前屏幕的位置(0为正中间,1为界面在当前屏幕左边,负数反之,支持浮点位置)
	 */
	public void OnScroll(int nCurrentPos, View vView, float fScale);
}
