package com.jkframework.callback;


public interface JKScrollViewListener {
	
	/**
	 * 滑到顶部触发
	 * @param bSuccess true为滑动到顶,false为离开顶部
	 */
	void OnTop(boolean bSuccess);
	
	/**
	 * 滑到底部触发
	 * @param bSuccess true为滑动到底,false为离开底部
	 */
	void OnBottom(boolean bSuccess);
}
