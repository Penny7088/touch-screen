package com.jkframework.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;

import com.jkframework.algorithm.JKFile;
import com.jkframework.debug.JKLog;


public class JKConfig{
	
	/**配置文件夹路径*/
	private String tPath = null;

	public JKConfig(String tPath)
	{
		this.tPath = tPath;
		JKFile.CreateDir(tPath);	
	}
	
	/**
	 * 通过键获取配置值
	 * @param tKey 键
	 * @return 配置值(无设置返回null)
	 */
	public String Get(String tKey)
	{
		Properties props = get();
		if (props == null)
			return "";
		else 
		{
			String tBack = props.getProperty(tKey);
			if (tBack == null)
				return "";
			else
				return tBack;
		}
	}
	
	/**
	 * 添加键值到Properties对象
	 * @param tKey 键	
	 * @param tValue 值
	 */
	public void Set(String tKey,String tValue)
	{
		Properties props = get();
		props.setProperty(tKey, tValue);
		setProps(props);
	}
	
	/**
	 * 清除所有数据
	 */
	public void ClearAll()
	{
		Properties props = get();
		props.clear();
		setProps(props);
	}
	
	/**
	 * 获取指定路径Properties对象
	 * @return Properties对象
	 */
	private Properties get() {
		Properties props = new Properties();
		if (JKFile.IsExists(tPath))
		{
			FileInputStream fis = null;
			try{
				fis = new FileInputStream(tPath);
				props.load(fis);
			}catch(Exception e){
				JKLog.ErrorLog("JKConfig获取对象失败.原因为" + e.getMessage());
			}finally{
				try {
                    if (fis != null) {
                        fis.close();
                    }
                } catch (Exception e) {
					JKLog.ErrorLog("JKConfig获取对象失败.原因为" + e.getMessage());
				}
			}
		}
		return props;
	}
	
	/**
	 * 添加Properties对象到指定路径
	 * @param p Properties对象
	 */
	private void setProps(Properties p) {
		FileOutputStream fos = null;
		try{
			File flConf = new File(tPath);
			fos = new FileOutputStream(flConf);
			p.store(fos, null);
			fos.flush();
		}catch(Exception e){	
			JKLog.ErrorLog("JKConfig设置对象失败.原因为" + e.getMessage());
		}finally{
			try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
				JKLog.ErrorLog("JKConfig设置对象失败.原因为" + e.getMessage());
			}
		}
	}
}
