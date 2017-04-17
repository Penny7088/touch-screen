package com.jkframework.callback;


public interface JKBaseCompressListener {
	
	/**
	 * 解压状态
	 * @param nCode 返回码(0表示开始压缩,1为压缩成功,2为压缩失败)
	 */
	void ZipStatus(int nCode);
	
	/**
	 * 解压进度
	 * @param nCurrentNum 当前文件数
	 * @param nTotalNum 当前总文件数
	 * @param tPathName 当前压缩文件路径
	 * @param tFileName 当前压缩文件
	 */
	void ZipProgress(int nCurrentNum,int nTotalNum,String tPathName,String tFileName);
}
