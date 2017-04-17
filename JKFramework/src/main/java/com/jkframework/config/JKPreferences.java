package com.jkframework.config;

import java.util.ArrayList;

import android.content.SharedPreferences;

import com.jkframework.algorithm.JKConvert;
import com.jkframework.debug.JKDebug;

public class JKPreferences{
	
	private final static String FILE_NAME = "JKPreferences"; //数据记录文件名称
	
	/**
	 * 存储缓存数据
	 * @param tFile 存储文件名
	 * @param key  存储的key值
	 */
	public static void SaveSharePersistent(String tFile, String key,String value) {
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(tFile, 0); //读取文件,如果没有则会创建
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.apply();
	}
	
	/**
	 * 存储缓存数据
	 * @param key  存储的key值
	 * @param value  存储的数据(字符串)
	 */
	public static void SaveSharePersistent(String key,	String value) {
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0); //读取文件,如果没有则会创建
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, value);
		editor.apply();
	}
	
	/**
	 * 存储缓存数据
	 * @param tFile 存储文件名
	 * @param key  存储的key值
	 * @param value  存储的数据(长整型)
	 */
	public static void SaveSharePersistent(String tFile, String key,long value) {
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(tFile, 0); //读取文件,如果没有则会创建
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);		
		editor.apply();
	}
	
	/**
	 * 存储缓存数据
	 * @param key  存储的key值
	 * @param value  存储的数据(浮点数)
	 */
	public static void SaveSharePersistent(String key,float value) {
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0); //读取文件,如果没有则会创建
		SharedPreferences.Editor editor = settings.edit();
		editor.putFloat(key, value);
		editor.apply();
	}
	
	/**
	 * 存储缓存数据
	 * @param key  存储的key值
	 * @param value  存储的数据(双精度浮点数)
	 */
	public static void SaveSharePersistent(String key,double value) {
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0); //读取文件,如果没有则会创建
		SharedPreferences.Editor editor = settings.edit();
		editor.putString(key, JKConvert.toString(value));
		editor.apply();
	}
	
	/**
	 * 存储缓存数据
	 * @param key  存储的key值
	 * @param value  存储的数据(长整型)
	 */
	public static void SaveSharePersistent(String key,long value) {
		
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0); //读取文件,如果没有则会创建
		SharedPreferences.Editor editor = settings.edit();
		editor.putLong(key, value);		
		editor.apply();
	}
	
	/**
	 * 存储缓存数据
	 * @param key  存储的key值
	 * @param value  存储的数据(整型)
	 */
	public static void SaveSharePersistent(String key,int value) {
		
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0); //读取文件,如果没有则会创建
		SharedPreferences.Editor editor = settings.edit();
		editor.putInt(key, value);		
		editor.apply();
	}
	
	/**
	 * 存储缓存数据
	 * @param key  存储的key值
	 * @param value  存储的数据(布尔型)
	 */
	public static void SaveSharePersistent(String key,boolean value) {
		
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0); //读取文件,如果没有则会创建
		SharedPreferences.Editor editor = settings.edit();
		editor.putBoolean(key, value);		
		editor.apply();
	}
	
	/**
	 * 存储缓存数据
	 * @param key  存储的key值
	 * @param a_tValue  存储的数据(字符串数组)
	 */
	public static void SaveSharePersistent(String key,ArrayList<String> a_tValue) {
		
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0); //读取文件,如果没有则会创建
		SharedPreferences.Editor editor = settings.edit();
		int nItem = 0;
		/*去除数组所有历史记录*/
		while (settings.getString(key + "_JKPreferences" + nItem, null) != null)
		{
			RemoveSharePersistent(key + "_JKPreferences" + nItem);
			++nItem;
		}
		for (int i=0; i<a_tValue.size(); ++i)
		{
			editor.putString(key + "_JKPreferences" + i, a_tValue.get(i));	
		}
		editor.apply();
	}
	
	/**
	 * 获取缓存数据
	 * @param key   存储数据时所对应的键
	 * @return 对应的值（字符串）
	 * */
	public static String GetSharePersistentString(String key){
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0);
		return settings.getString(key, "");
	}
	
	/**
	 * 获取缓存数据
	 * @param key  存储数据时所对应的键
	 * @return 对应的值（整型）
	 * */
	public static int GetSharePersistentInt(String key){
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0);
		return settings.getInt(key, 0);
	}
	
	/**
	 * 获取缓存数据
	 * @param key  存储数据时所对应的键
	 * @return 对应的值（长整型）
	 * */
	public static long GetSharePersistentLong(String key){
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0);
		return settings.getLong(key, 0);
	}
	
	/**
	 * 获取缓存数据
	 * @param key  存储数据时所对应的键
	 * @return 对应的值（浮点型）
	 */
	public static float GetSharePersistentFloat(String key){
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0);
		return settings.getFloat(key, 0);
	}
	
	/**
	 * 获取缓存数据
	 * @param key  存储数据时所对应的键
	 * @return 对应的值（浮点型）
	 */
	public static boolean GetSharePersistentBoolean(String key){
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0);
		return settings.getBoolean(key, false);
	}
	
	/**
	 * 获取缓存数据
	 * @param key  存储数据时所对应的键
	 * @return 对应的值（双精度浮点型）
	 */
	public static double GetSharePersistentDouble(String key){
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0);
		return JKConvert.toDouble(settings.getString(key, "0"));
	}
	
	/**
	 * 获取缓存数据
	 * @param key   存储数据时所对应的键
	 * @return 对应的值（字符串数组）
	 * */
	public static ArrayList<String> GetSharePersistentArrayString(String key){
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0);
		ArrayList<String> a_tList = new ArrayList<>();
		int nItem = 0;
		/*去除数组所有历史记录*/
		while (settings.getString(key + "_JKPreferences" + nItem, null) != null)
		{
			a_tList.add(settings.getString(key + "_JKPreferences" + nItem, ""));
			++nItem;
		}
		return a_tList;
	}
	
	/**
	 * 清除特定缓存数据
	 * @param key  存储的key值
	 */
	public static void RemoveSharePersistent(String key) {
		
		SharedPreferences settings = JKDebug.hContext.getSharedPreferences(FILE_NAME, 0); //读取文件,如果没有则会创建
		SharedPreferences.Editor editor = settings.edit();
		editor.remove(key);		
		editor.apply();
	}
}
