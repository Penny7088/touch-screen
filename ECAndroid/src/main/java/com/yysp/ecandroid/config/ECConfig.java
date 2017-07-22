package com.yysp.ecandroid.config;


import android.content.Context;
import android.telephony.TelephonyManager;

import com.jkframework.config.JKSystem;

public class ECConfig {

    /**
     * 消息平台反射类
     */
    public final static String MESSAGE_CENTER = "com.yysp.ecandroid.test.ECReflect";
    //任务结束
    public final static int TASK_FINISH = 604;
    //任务失败
    public final static int TASK_Fail = 605;
    //搜索频繁
    public final static int TASK_searMoreErro = 606;
    //成功
    public final static String success="成功";
    //失败
    public final static String fail="失败";
    //别名type
    public final static String AliasType="uid";
    //别名
    public final static String AliasName= JKSystem.GetGUID(TelephonyManager.PHONE_TYPE_GSM);

    public final static void CloseScreenOrder(Context context) {//关闭屏幕触摸
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        wm.setTpDisable(0);
    }

    public final static void OpenScreenOrder(Context context) {//打开屏幕触摸
//        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
//        wm.setTpDisable(1);
    }
    //任务编号
    public final static int SearchAddFriendType = 501;
    public final static int ContactGetFriendInfo = 502;
    public final static int GetWxUserInfo = 503;//不走脚本
    public final static int CreatGroupType = 504;
    public final static int CreatGroupTypeBySmallWx = 505;
    public final static int FindGroupJoinPeo = 506;
    public final static int GetCreatGroupInfo = 507;
    public final static int ChatWithFriend = 508;
    public final static int LookFriendCircle = 509;
    public final static int VoiceWithFriend = 510;
    public final static int VideoWithFriend = 511;
    public final static int Forwarding = 512;//转发
    public final static int SendFriendCircle = 513;
    public final static int Clicklike = 514;
    public final static int Comment = 515;
    public final static int DetectionTask = 516;
    public final static int FriendNumInfo = 517;
    public final static int AgressAddInGroupMsg = 518;
    public final static int GoOutGroup = 519;
    public final static int KickOutGroup = 520;
    public final static int DeleFriend = 521;
    public final static int AgressAddFriend = 525;
    public final static int NeedContactAddFriend = 527;
    public final static int ViewMessage = 532;
    public final static int ViewFriendNews = 533;
    public final static int AddFriendFromGroup = 534;
    public final static int CleanSystemFile = 535;
    public final static int SearchGroupAndGetPeoInfo = 536;
    public final static int PickingUpBottles = 537;
    public final static int ThrowTheBottle = 538;
    public final static int AddNearPeople = 539;





}
