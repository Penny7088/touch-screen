package com.yysp.ecandroid.config;

/**
 * Created by Administrator on 2017/4/17.
 */

public class ECSdCardPath {
    //存放KRQ脚本文件夹
//    public static final String SD_CARD_PATH = "/storage/emulated/legacy/KRQ/";
    public static final String SD_CARD_PATH = "/storage/emulated/0/KRQ/";
    //任务命令文件
    public static final String Task_List_TXT = SD_CARD_PATH + "task.json";
    //获取任务结束命令文件
    public static final String Task_Finish_TXT = SD_CARD_PATH + "task_finish.txt";
    //检测账号是否合格
    public final static String DETECTION_TASK_Finish_TXT = SD_CARD_PATH + "detection_task_finish.txt";
    //任务失败原因
    public final static String Task_Fail_TXT = SD_CARD_PATH + "task_fail_reason.txt";
    //需要备份
    public final static String NendBF = SD_CARD_PATH + "NeedBf.txt";
    //备注
    public final static String ResultTxt = SD_CARD_PATH + "result.txt";
    //隐私手机号文件
    public final static String ErroPhones = SD_CARD_PATH + "ErroPhones.txt";
}
