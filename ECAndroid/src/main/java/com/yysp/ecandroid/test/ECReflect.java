package com.yysp.ecandroid.test;

import com.jkframework.activity.JKBaseActivity;
import com.jkframework.config.JKSystem;
import com.jkframework.debug.JKDebug;
import com.jkframework.manager.JKActivityManager;

/**
 * Created by 2015/6/14.
 */
public class ECReflect {

    /**
     * 获取堆栈信息
     * @return 堆栈信息
     */
    public static String getactivity()
    {
        StringBuilder tBack = new StringBuilder();
        for (int i=0; i< JKActivityManager.getAllActivity().size(); ++i)
        {
            JKBaseActivity Tmp = JKActivityManager.getAllActivity().get(i);
            if (i != 0)
                tBack.append("\r\n");
            tBack.append(Tmp.getClass().getSimpleName());
        }
        return tBack.toString();
    }

    /**
     * 是否有context对象
     * @return 查询结果
     */
    public static String hascontext()
    {
        if (JKDebug.hContext != null)
            return "有Context对象";
        else
            return "无Context对象";
    }

    /**
     * 是否有context对象
     * @return 查询结果
     */
    public static String cleancache()
    {
        JKSystem.CleanCache();
        return "清除缓存成功";
    }
}
