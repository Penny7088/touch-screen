package com.jkframework.callback;




public interface JKDownloadLinstener {


	/**
	 * 请求数据进度
	 * @param nCurrentSize 当前下载大小
	 * @param nTotalSize 文件总大小
	 */
	void ReceiveProgress(int nCurrentSize,int nTotalSize);
	
	/**
	 * 请求数据状态
	 * @param nCode  返回码(0表示开始下载,-2为接受失败,-3为数据超时,-4为用户取消,1表示下载完毕,2表示数据异常)
	 */
	void ReceiveStatus(int nCode);
}
