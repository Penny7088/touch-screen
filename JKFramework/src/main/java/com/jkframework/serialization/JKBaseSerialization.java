package com.jkframework.serialization;

public abstract class JKBaseSerialization {

	/**
	 * 从文件里读取序列化内容
	 * @param tPath 文件地址(assets不加"/")
	 * @param tEncoding 编码支持"UTF-8","GBK
	 */
	public abstract void LoadFile(String tPath,String tEncoding);
	
	/**
	 * 从文件里读取序列化内容
	 * @param tPath 文件地址(assets不加"/")
	 */
	public void LoadFile(String tPath)
	{
		LoadFile(tPath,"UTF-8");
	}
	
	/**
	 * 设置序列化内容
	 * @param tText 序列化内容
	 */
	public abstract void LoadString(String tText);

	/**
	 * 将序列化内容保存文件
	 * @param tPath 保存的sdcard路径
	 * @param tEncoding 编码支持"UTF-8","GBK
	 */
	public abstract void SaveFile(String tPath,String tEncoding);
	
	/**
	 * 将序列化内容保存文件
	 * @param tPath 保存的sdcard路径
	 */
	public void SaveFile(String tPath)
	{
		SaveFile(tPath,"UTF-8");
	}
	
	/**
	 * 将序列化内容转为字符串
	 * @return 转化后的字符串
	 */
	public abstract String GetString();

	/**
	 * 询问地址是否存在
	 * @param tQuestion 匹配规则为"XXX/XXX[n]/@XXX"(匹配从1开始)
	 * @return 若该节点存在返回true
	 */
	public abstract boolean IsExist(String tQuestion);

	/**
	 * 询问地址获取节点文本
	 * @param tQuestion 匹配规则为"XXX/XXX[n]/@XXX"(匹配从1开始)
	 * @return 匹配成功返回文本,失败返回空
	 */
	public abstract String GetNodeText(String tQuestion);
	
	/**
	 * 询问地址获取节点个数
	 * @param tQuestion 匹配规则为"XXX/XXX[n]/@XXX"(匹配从1开始)
	 * @return 节点个数
	 */
	public abstract int GetNodeCount(String tQuestion);
	
	/**
	 * 询问地址创建节点属性
	 * @param tQuestion 匹配规则为"XXX/XXX[n]/@XXX"(匹配从1开始)
	 * @param tText 属性值
	 */
	public abstract void CreateNode(String tQuestion, String tText);
	
	/**
	 * 询问地址创建节点属性
	 * @param tQuestion 匹配规则为"XXX/XXX[n]/@XXX"(匹配从1开始)
	 * @param nText 属性值
	 */
	public abstract void CreateNode(String tQuestion, int nText);

	/**
	 * 询问地址创建节点属性
	 * @param tQuestion 匹配规则为"XXX/XXX[n]/@XXX"(匹配从1开始)
	 * @param bBool 属性值
	 */
	public abstract void CreateNode(String tQuestion, boolean bBool);
}
