package com.jkframework.callback;

public interface JKImageViewListener {

	/**
	 * 图片加载完毕回调
	 */
	void FinishLoad(boolean bSuccess);
	
	/**
	 * 图片加载进度
	 * @param nPercent 进度百分比[0~100]
	 */
	void LoadingProgress(int nPercent);
}
