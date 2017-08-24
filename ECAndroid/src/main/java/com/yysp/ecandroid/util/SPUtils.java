package com.yysp.ecandroid.util;

import android.content.Context;
import android.content.SharedPreferences;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.util.List;

import it.sauronsoftware.base64.Base64;


/**
 * Created by penny on 2016/9/14 0014.
 */
public class SPUtils {

    public static final String PREFERNCE_FILE_NAME = "yysp_script"; // 缓存文件名
    public static final String KEY_USER_INFO = "userinfo";		//存储用户信息
    public static final String KEY_USER_CODE = "usercode";		//存储验证码

    ///////////////////////////////通用的/////////////////////////////////////////////
    /**
     * 取布尔值
     *
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static boolean getBoolean(Context context, String key,boolean defValue) {
        SharedPreferences prefe = context.getSharedPreferences(PREFERNCE_FILE_NAME, 0);
        return prefe.getBoolean(key, defValue);

    }

    /**
     * 存布尔值
     * @param context
     * @param key
     * @param value
     */
    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences prefe = context.getSharedPreferences(PREFERNCE_FILE_NAME, 0);
        prefe.edit().putBoolean(key, value).apply();
    }
    /**
     * 存String方法
     * @param context
     * @param key
     * @param value
     */
    public static void putString(Context context, String key, String value){
        SharedPreferences prefe = context.getSharedPreferences(PREFERNCE_FILE_NAME, 0);
        prefe.edit().putString(key, value).apply();
    }
    /**
     * 取String方法
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static String getString(Context context, String key, String defValue){
        SharedPreferences prefe = context.getSharedPreferences(PREFERNCE_FILE_NAME, 0);
        return prefe.getString(key, defValue);
    }

    /**
     * 存Int方法
     * @param context
     * @param key
     * @param value
     */
    public static void putInt(Context context, String key, int value){
        SharedPreferences prefe = context.getSharedPreferences(PREFERNCE_FILE_NAME, 0);
        prefe.edit().putInt(key, value).apply();
    }


    /**
     * 取Int方法
     * @param context
     * @param key
     * @param defValue
     * @return
     */
    public static int getInt(Context context, String key, int defValue){
        SharedPreferences prefe = context.getSharedPreferences(PREFERNCE_FILE_NAME, 0);
        int getInt = prefe.getInt(key, defValue);
        return getInt;
    }


    /**存储集合
     * @param SceneList
     * @param context
     * @param key
     */
    public static void saveList(List<?> SceneList,Context context,String key){
        String SceneListString = null;
        SharedPreferences preferences = context.getSharedPreferences(PREFERNCE_FILE_NAME,  0);
        // 实例化一个ByteArrayOutputStream对象，用来装载压缩后的字节文件。
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        // 然后将得到的字符数据装载到ObjectOutputStream
        try {
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
            // writeObject 方法负责写入特定类的对象的状态，以便相应的 readObject 方法可以还原它
            objectOutputStream.writeObject(SceneList);
            // 最后，用Base64.encode将字节文件转换成Base64编码保存在String中
            SceneListString = new String(Base64.encode(byteArrayOutputStream.toByteArray()));
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(key, SceneListString);
            editor.apply();
            // 关闭objectOutputStream
            objectOutputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**读取集合
     * @param
     * @return
     */
    public static List<?> readList(Context context,String key) {
        List<?> SceneList = null;
        SharedPreferences preferences = context.getSharedPreferences(PREFERNCE_FILE_NAME,0);
        String stringList = preferences.getString(key, "");
        byte[] mobileBytes = Base64.decode(stringList.getBytes());
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(mobileBytes);
        try {
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
            SceneList = (List<?>) objectInputStream.readObject();
            objectInputStream.close();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return SceneList;
    }
    /**
     * 清除某个key对应的缓存
     * @param key
     * @param context
     */
    public static void clearByKey(String key, Context context) {
        SharedPreferences prefe = context.getSharedPreferences(PREFERNCE_FILE_NAME, 0);
        SharedPreferences.Editor editor = prefe.edit();
        editor.putString(key, "");
        editor.apply();
    }

    /**清除所有的缓存数据
     * @param context
     */
    public static void clearAll(Context context) {
        SharedPreferences prefe = context.getSharedPreferences(PREFERNCE_FILE_NAME, 0);
        SharedPreferences.Editor editor = prefe.edit();
        editor.clear();
        editor.apply();
    }
}

