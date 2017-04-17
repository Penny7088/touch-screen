package com.jkframework.callback;




public interface JKDownloadManagerLinstener {


//	/**
//	 * 请求数据进度
//	 * @param nCurrentSize 当前下载大小
//	 * @param nTotalSize 文件总大小
//	 */
//	void ReciveProgress(int nCurrentSize, int nTotalSize);
	
	/**
	 * 下载完成回调
	 */
	void FinishDownload();
}
