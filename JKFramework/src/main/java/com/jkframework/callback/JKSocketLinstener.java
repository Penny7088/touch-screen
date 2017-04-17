package com.jkframework.callback;

import java.util.Map;


public interface JKSocketLinstener {


	/**
	 * 请求数据成功
	 * @param h_tHeaders 返回的表头
	 * @param tBody 返回的数据
	 * @param a_byBody 返回的原始数据
	 */
	void ReceiveOK(Map<String,String> h_tHeaders,String tBody,byte[] a_byBody);
	
	/**
	 * 请求数据失败
	 * @param nCode  返回码(>0表示失败返回码,-1为发送失败,-2为接受失败,-3为数据超时,-4为用户取消)
	 */
	void ReceiveFaild(int nCode);
}
