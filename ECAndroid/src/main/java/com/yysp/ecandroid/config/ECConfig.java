package com.yysp.ecandroid.config;


import com.jkframework.config.JKSystem;

public class ECConfig {

    /**
     * 消息平台反射类
     */
    public final static String MESSAGE_CENTER = "com.yysp.ecandroid.test.ECReflect";
    //任务结束
    public final static int TASK_FINISH = 604;
    //任务执行中
    public final static int TASK_DOING_AlREALY_REQ = 602;
    //任务失败
    public final static int TASK_Fail = 605;
    //检测成功
    public final static String Detetion_Success="检测成功";
    //成功
    public final static String success="成功";
    //失败
    public final static String fail="失败";
    //别名type
    public final static String AliasType="uid";
    //别名
    public final static String AliasName= JKSystem.GetGUID();

}
