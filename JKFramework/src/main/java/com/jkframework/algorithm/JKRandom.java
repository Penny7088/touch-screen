package com.jkframework.algorithm;

import java.security.SecureRandom;





public class JKRandom
{
	/**
	 * 获取制定长度的随机数
	 * @param nLen	随机数长度
	 * @return  返回随机整型字符串
	 */
	public static String GetIntRandom(int nLen)
	{
		SecureRandom srRandom = new SecureRandom();
		StringBuilder tBack = new StringBuilder();
		for (int i=0; i<nLen; ++i)
			tBack.append(srRandom.nextInt(10));
		return tBack.toString();
	}
	
	/**
	 * 获取范围内随机数[nStart,nEnd]
	 * @param nStart 随机范围起始值
	 * @param nEnd 随机范围结束值
	 * @return 返回的随机数
	 */
    public static int GetRandom(int nStart, int nEnd)
    {
    	if (nStart > nEnd)
        {
            int nTmp = nStart;
            nStart = nEnd;
            nEnd = nTmp;
        }
    	SecureRandom srRandom = new SecureRandom();
        int nRandom = srRandom.nextInt(nEnd - nStart + 1);
        return nRandom + nStart;
    }

    /**
     * 生成一个GUID
     * @return 返回GUID
     */
    public static String MakeGUID()
    {
    	return java.util.UUID.randomUUID().toString();
    }
}