package com.jkframework.callback;

import java.util.Map;


public interface JKUploadLinstener {


	/**
	 * 请求数据进度
	 * @param lCurrentSize 当前上传大小
	 * @param lTotalSize 文件总大小
	 */
	void ReceiveProgress(long lCurrentSize,long lTotalSize);
	
	/**
	 * 请求数据状态
	 * @param nCode  返回码(0表示开始上传,>0表示失败返回码,-1为发送失败,-2为接受失败,-3为数据超时,-4为用户取消,-5表示上传完毕)
	 */
	void ReceiveStatus(int nCode);
	
	/**
	 * 请求数据成功
	 * @param h_tHeaders 返回的表头
	 * @param tBody 返回的数据
	 * @param a_byBody 返回的原始数据
	 */
	void ReceiveOK(Map<String,String> h_tHeaders,String tBody,byte[] a_byBody);
}
