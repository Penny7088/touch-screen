package com.jkframework.algorithm;

import com.google.common.base.CharMatcher;
import com.google.common.base.Splitter;
import com.google.common.base.Strings;
import com.google.common.collect.Iterables;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;








public class JKAnalysis  
{
	/**
	 * 将字符串转成大写
	 * @param tText  传入字符串
	 * @return 转成大写后的字符串
	 */
	static public String toUpper(String tText)
	{
		return tText.toUpperCase(Locale.getDefault());
	}
	
	/**
	 * 将字符串转成小写
	 * @param tText  传入字符串
	 * @return 转成小写后的字符串
	 */
	static public String toLower(String tText)
	{
		return tText.toLowerCase(Locale.getDefault());
	}
	
	/**
	 * 将字符串按规定字符进行切割
	 * @param tText  完整字符串
	 * @param tDelimiter 切割方式
	 * @return 切割后的字符串数组
	 */
	static public ArrayList<String> Split(String tText,String tDelimiter)
	{
        if (tText == null)
            return new ArrayList<>();
		Iterable<String> split = Splitter.on(tDelimiter).split(tText);
		ArrayList<String> a_tBack = new ArrayList<>();
		Iterables.addAll(a_tBack, split);
		return  a_tBack;
	}

    /**
     * 将字符串按规定字符进行切割
     * @param tText  完整字符串
     * @param tDelimiter 切割方式
     * @param bRemoveNull 是否移除空数组
     * @return 切割后的字符串数组
     */
    static public ArrayList<String> Split(String tText,String tDelimiter,boolean bRemoveNull)
    {
        if (tText == null)
            return new ArrayList<>();
		Iterable<String> split = Splitter.on(tDelimiter).split(tText);
        ArrayList<String> a_tBack = new ArrayList<>();
		Iterables.addAll(a_tBack, split);
        for (int i=0; i<a_tBack.size(); ++i)
        {
            if (bRemoveNull && Strings.isNullOrEmpty(a_tBack.get(i)))
            {
                a_tBack.remove(i);
                i--;
            }
        }
		return  a_tBack;
    }
	
	/**
	 * 判断字符串是否为正确的数字格式
	 * @param tText 需要判断的字符串
	 * @return true表示为数字格式
	 */
	static public boolean IsNumber(String tText)
	{
		//判断首字母是否为0
		if (tText.substring(0, 1).equals("0"))
			return false;
		//判断是否全为数字
		Pattern pPattern = Pattern.compile("[0-9]+"); 
		Matcher mMatcher = pPattern.matcher(tText);
        return mMatcher.matches();

    }
	
	/**
	 * 判断字符串是否为合法字符串
	 * @param tText 需要判断的字符串
	 * @return true表示为合法格式
	 */
	static public boolean IsLegal(String tText)
	{
		Pattern pPattern = Pattern.compile("[^s]+"); 
		Matcher mMatcher = pPattern.matcher(tText);
        return mMatcher.matches();

    }

    /**
     * 替换字符串
     * @param tText 原字符串
     * @param tOld 需要替换的字符串
     * @param tNew 替换的字符串
     * @return 替换后的字符串
     */
    static public String ReplaceAll(String tText,String tOld,String tNew)
    {
        return CharMatcher.anyOf(tOld).replaceFrom(tText, tNew);
    }
	
	/**
	 * 获取字符串中指定字符串区间内容
	 * @param tText	搜寻的字符串
	 * @param tFirst  字符串的起始区间
	 * @param tEnd  字符串的结束区间
	 * @return  字符串中间的内容
	 */
	static public String GetMiddleString(String tText, String tFirst, String tEnd)
	{
		return GetMiddleString(tText, 0, tFirst, tEnd);
	}
	
	/**
	 * 获取字符串中指定字符串区间内容
	 * @param tText	搜寻的字符串
	 * @param tFirst  字符串的起始区间
	 * @param tEnd  字符串的结束区间
	 * @return  字符串中间的内容
	 */
	static public String GetMiddleString(String tText, int nPos, String tFirst, String tEnd)
	{
		if (tText == null)
			return "";
		int nStart = tText.indexOf(tFirst, nPos);
		if (nStart < 0)     //内容没有找到
			return "";
		nStart += tFirst.length();

		int nEnd = tText.indexOf(tEnd, nStart + 1);
		if (nEnd < 0)     //内容没有找到
			return "";

		return tText.substring(nStart, nEnd);
	}
	
	/**
	 * 获取字符串中指定字符串区间内容
	 * @param tText	搜寻的字符串
	 * @param tFirst  字符串的起始区间
	 * @param tEnd  字符串的结束区间
	 * @param nOrder 字符串指定匹配次数
	 * @return  字符串中间的内容
	 */
	static public String GetMiddleString(String tText, String tFirst, String tEnd,int nOrder)
	{
        if (tText == null)
            return "";
		int nStart = -1;       //其实搜索位置
		while (nOrder > 0)
		{
			nStart = tText.indexOf(tFirst, nStart + 1);
			if (nStart < 0)     //内容没有找到
				return "";
			--nOrder;
		}

		return JKAnalysis.GetMiddleString(tText, nStart, tFirst, tEnd);
	}
	
	/**
	 * 从文末获取最后一个字符串中指定字符串区间内容
	 * @param tText 搜寻的字符串
	 * @param tFirst 字符串的起始区间
	 * @param tEnd 字符串的结束区间
	 * @return 字符串中间内容
	 */
	static public String GetLastMiddleString(String tText, String tFirst, String tEnd)
    {
        if (tText == null)
            return "";
        int nStart = tText.lastIndexOf(tEnd);
        if (nStart < 0)     //内容没有找到
            return "";

        int nEnd = tText.lastIndexOf(tFirst, nStart - 1);
        if (nEnd < 0)     //内容没有找到
            return "";
        nEnd += tEnd.length();
        return tText.substring(nEnd, nStart);
    }
	
	/**
	 * 是否为合法身份证信息
	 * @param tID 身份证信息
	 * @return true为合法身份证
	 */
	public static boolean IsLegalIdentification(String tID)
	{
		if (tID != null && tID.length() == 18)
		{
			String tBirthDay = tID.substring(6,8);
			if (JKDate.IsLegalDate(tBirthDay))
			{
				/*判断身份证最后一位是否合法*/
				String[] a_tMod = {"1","0","X","9","8","7","6","5","4","3","2"};
				int[] a_nRatio = {7,9,10,5,8,4,2,1,6,3,7,9,10,5,8,4,2};
				int nSum = 0;
				for (int i=0; i<17; ++i)
				{
					nSum += JKConvert.toInt(tID.substring(i,i+1)) * a_nRatio[i];
				}
				if (a_tMod[nSum % 11].equals(tID.substring(17).toUpperCase(Locale.getDefault())))
					return true;
			}			
		}
		return false;
	}
	
}