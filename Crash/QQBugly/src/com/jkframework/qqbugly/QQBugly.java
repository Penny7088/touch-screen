package com.jkframework.qqbugly;

import com.jkframework.debug.JKDebug;
import com.tencent.bugly.crashreport.CrashReport;

public class QQBugly {
	
	/**腾讯crash单例对象*/
	private static QQBugly qqMain = null;

	
	/**
	 * 获取百度单例对象
	 * @return  百度统计对象
	 */
	public static QQBugly GetInstance()
	{
		QQBugly tmp = qqMain;
		if (tmp == null) {
			synchronized (QQBugly.class) {
				tmp = qqMain;
				if (tmp == null) {
					tmp = new QQBugly();
					qqMain = tmp;
				}
			}
		}
		return tmp;
	}

	private QQBugly()
	{
		try {
			CrashReport.initCrashReport(JKDebug.hContext);
		}
		catch (Exception ignored)
		{

		}
	}

}
