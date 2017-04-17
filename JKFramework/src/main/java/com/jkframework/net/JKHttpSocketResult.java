package com.jkframework.net;

import java.util.Map;

public class JKHttpSocketResult{
	
	/**http返回状态类型(>0表示失败返回码,-1为发送失败,-2为接受失败,-3为数据超时,-4为用户取消)*/ 
	private int nType;
	/**返回的表头*/
	private Map<String, String> h_tHeaders;
	/**返回的数据*/
	private String tBody;
	/**返回的原始数据*/
	private byte[] a_byBody;

	/**
	 * 构造参数
	 * @param nType 反射失败类型(0,表示成功>0表示失败返回码,-1为发送失败,-2为接受失败,-3为数据超时,-4为用户取消)
	 */
	public  JKHttpSocketResult(int nType)
	{
		this.nType = nType;
	}
	
	/**
	 * 设置成功对象
	 * @param h_tHeaders 返回的表头
	 * @param tBody 返回的数据
	 * @param a_byBody 返回的原始数据
	 */
	void SetResult(Map<String, String> h_tHeaders, String tBody, byte[] a_byBody)
	{
		this.h_tHeaders = h_tHeaders;
		this.tBody = tBody;
		this.a_byBody = a_byBody;
	}
	
	/**
	 * 返回状态
	 * @return 状态码
	 */
	public int GetSuccess()
	{
		return nType;
	}
	
	/**
	 * 请求成功后的表头
	 * @return 表头对象
	 */
	public Map<String, String> GetHeader()
	{
		return h_tHeaders;
	}
	
	/**
	 * 请求成功后的内容
	 * @return 内容对象
	 */
	public String GetBody()
	{
		return tBody;
	}
	
	/**
	 * 请求成功后的内容
	 * @return 字节数组对象
	 */
	public byte[] GetByte()
	{
		return a_byBody;
	}
}