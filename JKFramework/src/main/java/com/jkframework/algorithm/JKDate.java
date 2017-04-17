package com.jkframework.algorithm;

import com.jkframework.debug.JKLog;

import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


public class JKDate{
	
	/**
	 * 获得日历时间
	 * @return 日历时间秒数
	 */
    public static long GetCalendarTime()
    {
        return System.currentTimeMillis() / 1000;
    }

    /**
	 * 获得日历时间
	 * @return 日历时间毫秒数
	 */
    public static long GetExactCalendarTime()
    {
		return System.currentTimeMillis();
    }

    /**
     * 根据时间戳获取指定时间位置
     * @param lTime 时间戳
     * @param nType (0为年,1为月,2为日,3为星期,4为小时,5为分钟,6为秒)
     * @return 返回数字(星期用1~7表示,失败返回-1)
     */
    public static int GetOneTime(long lTime,int nType)
    {
		DateTime dt = new DateTime(lTime);
		switch (nType) {
			case 0:
				return dt.getYear();
			case 1:
				return dt.getMonthOfYear();
			case 2:
				return dt.getDayOfMonth();
			case 3:
				int nWeek = dt.getDayOfWeek();
				if (nWeek == 0)
					nWeek = 7;
				return nWeek;
			case 4:
				return dt.getHourOfDay();
			case 5:
				return dt.getMinuteOfHour();
			case 6:
                return dt.getSecondOfMinute();
            case 7:
                return dt.getMillisOfSecond();
			default:
				return -1;
		}
    }

    /**
     * 获取完整时间字符串
     * @param bUse 是否使用英文
     * @return 格式化后的时间格式
     */
    public static String GetFullDate(boolean bUse)
    {
        return GetFullDate(System.currentTimeMillis(),bUse);
    }

    /**
     * 格式化输出时间
     * @param lTime 时间戳
     * @param bUse 是否使用中文
     * @return 格式化后的时间格式
     */
    public static String GetFullDate(long lTime,boolean bUse)
    {
        if (bUse)
            return GetFormatTime(lTime,"yyyy年MM月dd日 HH:mm:ss");
        else
            return GetFormatTime(lTime,"yyyy-MM-dd HH:mm:ss");
    }

    /**
     * 格式化输出时间
     * @param lTime 时间戳
     * @param tFormat 格式化字符
     * @return 格式化后的时间格式
     */
    public static String GetFormatTime(long lTime,String tFormat)
    {
        DateTime dt = new DateTime(lTime);
        return dt.toString(tFormat);
    }

    /**
     * 将yyyymmddhhmmss转换成时间戳(智能判断长度8与14)
     * @param tTime yyyymmddhhmmss时间格式
     * @return 时间戳
     */
    public static long GetTime(String tTime)
    {
    	if (tTime.length() == 14)
    	{
    		DateFormat dfDate = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
    		Date dtDate;
			try {
				dtDate = dfDate.parse(tTime);
			} catch (ParseException e) {
				e.printStackTrace();
				JKLog.ErrorLog("无法将\"" + tTime + "\"转成时间戳.原因为" + e.getMessage());
				return 0;
			}
            return dtDate.getTime();
    	}
    	else if (tTime.length() == 8){
    		DateFormat dfDate = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
    		Date dtDate;
			try {
				dtDate = dfDate.parse(tTime);
			} catch (ParseException e) {
				e.printStackTrace();
				JKLog.ErrorLog("无法将\"" + tTime + "\"转成时间戳.原因为" + e.getMessage());
				return 0;
			}
            return dtDate.getTime();
    	}
    	return 0;
    }

    /**
     * 将时间戳转换成yyyymmddhhmmss
     * @param lTime 时间戳
     * @return yyyymmddhhmmss时间格式
     */
    public static String GetTime(long lTime)
    {
    	DateFormat dfDate = new SimpleDateFormat("yyyyMMddHHmmss",Locale.getDefault());
    	return dfDate.format(lTime);
    }

    /**
     * 验证时间是否合法(智能判断长度8与14)
     * @param tTime yyyymmddhhmmss时间格式
     * @return true表示时间合法,false表示为非法时间
     */
    public static boolean IsLegalDate(String tTime)
    {
    	long lTime = GetTime(tTime);
    	String tBack = GetTime(lTime);
        return tBack.indexOf(tTime) == 0;
    }
}