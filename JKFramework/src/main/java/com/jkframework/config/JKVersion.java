package com.jkframework.config;

import com.jkframework.algorithm.JKConvert;
import com.jkframework.algorithm.JKFile;




public class JKVersion
{
	
	/**
	 * 检查版本是否需要更新
	 * @return true为需要更新
	 */
	public static boolean CheckVersion()
	{	
		JKConfig jkcConfig = new JKConfig(JKFile.GetPublicPath() + "/JKVersion");
		if (jkcConfig.Get("JKVersion").equals(""))
		{
			return true;
		}
		else {
			int nVersion = JKConvert.toInt(jkcConfig.Get("JKVersion"));
            return nVersion < JKSystem.GetVersionCode();
		}
	}
	
	/**
	 * 获取上次安装的版本
	 * @return 返回上次安装版本号
	 */
	public static int GetLastVersion()
	{
		JKConfig jkcConfig = new JKConfig(JKFile.GetPublicPath() + "/JKVersion");
		if (jkcConfig.Get("JKVersion").equals(""))
			return 0;
		else 
			return JKConvert.toInt(jkcConfig.Get("JKVersion"));	
	}
	
	/**
	 * 保存当前版本
	 */
	public static void SaveVersion()
	{
		JKConfig jkcConfig = new JKConfig(JKFile.GetPublicPath() + "/JKVersion");
		jkcConfig.Set("JKVersion", JKConvert.toString(JKSystem.GetVersionCode()));
	}
}