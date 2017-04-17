package com.jkframework.debug;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.jkframework.bean.JKReflectData;



public class JKReflect{
		
	
	/**
	 * 反射函数
	 * @param oClass 动态函数的反射类(需要之前new好),静态函数传null
	 * @param tClass 反射函数包名+类名
	 * @param tFunction 反射函数名(区分大小写)
	 * @param a_ClassList 反射函数参数的类型(例如String.class)
	 * @param a_Parameter 反射函数参数
	 * @return 反射函数返回值
	 */
	public static JKReflectData Reflect(Object oClass,String tClass,String tFunction,Class<?>[] a_ClassList,Object[] a_Parameter)
	{
		try {
			Class<?> Debug = Class.forName(tClass);			
			Method moFuction = Debug.getMethod(tFunction,a_ClassList);
			JKReflectData jkrrTmp = new JKReflectData(0);
			jkrrTmp.SetResult(moFuction.invoke(oClass,a_Parameter));
			return jkrrTmp;
		} catch (ClassNotFoundException e) {	
			e.printStackTrace();
			return new JKReflectData(1);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return new JKReflectData(2);
		} catch (IllegalArgumentException e) {
			JKLog.ErrorLog("反射函数失败.原因为" + e.getMessage());
			e.printStackTrace();
			return new JKReflectData(3);
		} catch (IllegalAccessException e) {			
			JKLog.ErrorLog("反射函数失败.原因为" + e.getMessage());
			e.printStackTrace();
			return new JKReflectData(3);
		} catch (InvocationTargetException e) {
			e.printStackTrace();
			return new JKReflectData(4); 
		}
	}
}