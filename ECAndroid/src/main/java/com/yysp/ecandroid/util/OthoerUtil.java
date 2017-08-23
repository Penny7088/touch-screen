package com.yysp.ecandroid.util;

import android.content.Context;
import android.content.Intent;

import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.bean.DisGetTaskBean;
import com.yysp.ecandroid.data.response.AddErrorMsgResponse;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.view.activity.ECTaskActivity;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Administrator on 2017/5/11.
 */

public class OthoerUtil {

    public static final String TAG = "RT";

    //任务是否进行开关
    public static boolean isTasking = false;
    public static String ActivityName = "";
    public static int taskType;
    public static String TaskId = "";
    public static DisGetTaskBean TaskBean;

    public static void launcherWx(Context context) {
        Intent intent = new Intent();
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
        context.startActivity(intent);
    }


    public static void doOfTaskEnd() {
        isTasking = false;
//        JKPreferences.RemoveSharePersistent("taskType");
//        JKPreferences.RemoveSharePersistent("taskId");
//        JKPreferences.RemoveSharePersistent("pushData");
    }

    public static void AddErrorMsgUtil(String msg) {
        AddErrorMsgResponse errorMsgResponse = new AddErrorMsgResponse(msg);
        ECNetSend.addErrorMsg(errorMsgResponse).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<DisBean>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(DisBean disBean) {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }


//    public static void deleContanct(Context context) {
//        //删除通讯录
//        ArrayList<String> list = JKPreferences.GetSharePersistentArrayString("phoneList");
//        for (String phone : list) {
//            try {
//                ContactUtil.deleteContact(context, phone);
//                JKLog.i("RT", "del:" + phone);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }


}
