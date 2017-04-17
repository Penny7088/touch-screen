#ifndef JKDATE_H_
#define JKDATE_H_

#include <time.h>

class JKDate{
public:		//函数
	/**获取系统日历时间*/
	static long long GetCalendarTime();
	/**获取系统精确日历时间*/
	static long long GetExactCalendarTime();
	/** 
	 * 根据时间戳获取时间位置
	 * @param lTime 时间戳
	 * @param nType (0为年，1为月，2为日，3为星期，4为小时，5为分钟，6为秒)
	 * return 返回数字(星期用1-7 失败返回-1)
	 */
	static int GetOneTime(long long lTime , int nType);
	/** 
	 * @param tTime 将yyyymmddhhmmss转换成时间戳（智能判断长度14和8）
	 * return 将时间格式转换成时间戳
	 */
	static long long GetTime(string tTime);
	/** 
	 * @param lTime 将时间戳转换成yyyymmddhhmmss
	 * return yyyymmddhhmmss时间格式
	 */
	static string GetTime(long long lTime);
	/**
     * 将当前时间转换为年月日形式
     * @param bUse 是否使用中文
     * @return XXXX年X月X日(不使用中文时用-代替)
     */
	static string GetLongDate(bool bUse);
	/**
    * 将当前时间转换为年月日形式
    * @param lTime 时间戳
    * @param bUse 是否使用中文
    * @return XXXX年X月X日(不使用中文时用-代替)
    */
    static string GetLongDate(bool bUse , long long lTime);
    /**
     * 将当前时间转换为时分秒形式
     * @return XX:XX:XX
     */
    static string GetLongTime();

	/**
     * 根据时间戳转换为时分秒形式
     * @param lTime 时间戳
     * @return XX:XX:XX
     */
    static string GetLongTime(long long lTime);

	 /**
     * 将当前时间转换为完整的形式
     * @param bUse 是否使用中文
     * @return XXXX年X月X日 XX:XX:XX(不使用中文时用-代替)
     */
    static string GetFullDate(bool bUse);

	 /**
     * 将当前时间转换为完整的形式
     * @param lTime 时间戳
     * @param bUse 是否使用中文
     * @return XXXX年X月X日 XX:XX:XX(不使用中文时用-代替)
     */
    static string GetFullDate(bool bUse,long long lTime);

	 /**
     * 验证时间合不合法（智能判断8位14位）
     * @param tTime yyyymmddhhmmss 时间格式
     * @return true合法 false非法
     */
	static bool IsLegalDate(string tTime);
};

#endif /* JKDATE_H_ */
