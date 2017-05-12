package com.yysp.ecandroid.config;

/**
 * Created by Administrator on 2017/4/17.
 */

public class ECSdCardPath {
    //存放KRQ脚本文件夹
    public static final String SD_CARD_PATH = "/storage/emulated/legacy/KRQ/";
    //任务命令文件
    public static final String Task_List_TXT = SD_CARD_PATH + "task.json";
    //获取任务结束命令文件
    public static final String Task_Finish_TXT = SD_CARD_PATH + "task_finish.txt";
    //检测账号是否合格
    public final static String DETECTION_TASK_Finish_TXT = SD_CARD_PATH + "detection_task_finish.txt";
    //任务失败原因
    public final static String Task_Fail_TXT = SD_CARD_PATH + "task_fail_reason.txt";

}
