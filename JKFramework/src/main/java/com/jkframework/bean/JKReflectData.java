package com.jkframework.bean;

public class JKReflectData{  
	
	/**反射失败类型(0表示正常,1为类不存在,2为方法不存在,3为未知错误,4为反射方法出现崩溃情况)*/ 
	private int nType;
	/**反射成功后的对象*/
	private Object oObject = null;
	
	/**
	 * 构造参数
	 * @param nType 反射失败类型(0表示正常,1为类不存在,2为方法不存在,3为未知错误,4为反射方法出现崩溃情况)
	 */ 
	public JKReflectData(int nType)
	{
		this.nType = nType;
	}
	
	/**
	 * 设置成功对象
	 * @param oObject 成功对象
	 */
	public void SetResult(Object oObject)
	{
		this.oObject = oObject;
	}
	
	/**
	 * 反射成功后的值
	 * @return 成功后的对象
	 */
	public Object GetObject()
	{
		return oObject;
	}
	
	/**
	 * 返回状态
	 * @return 状态码
	 */
	public int GetSuccess()
	{
		return nType;
	}
	
	
}