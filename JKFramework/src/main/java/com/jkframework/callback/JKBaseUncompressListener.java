package com.jkframework.callback;


public interface JKBaseUncompressListener {
	
	/**
	 * 解压进度
	 * @param nCurrentNum 当前文件数
	 * @param nTotalNum 当前总文件数
	 * @param tPathName 当前解压缩文件路径
	 * @param tFileName 当前解压缩文件
	 */
	void UnzipProgress(int nCurrentNum,int nTotalNum,String tPathName,String tFileName);
	
	/**
	 * 解压状态
	 * @param nCode  返回码(0表示开始解压,1为解压成功,2为解压失败)
	 */
	void UnzipStatus(int nCode);
}
