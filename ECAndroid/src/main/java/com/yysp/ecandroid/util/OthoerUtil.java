package com.yysp.ecandroid.util;

import android.content.Context;
import android.content.Intent;

import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.debug.JKLog;
import com.yysp.ecandroid.config.ECSdCardPath;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.response.AddErrorMsgResponse;
import com.yysp.ecandroid.net.ECNetSend;
import com.yysp.ecandroid.view.activity.ECTaskActivity;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by Administrator on 2017/5/11.
 */

public class OthoerUtil {

    public static void launcherWx(Context context) {
        Intent intent = new Intent();
        intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
        intent.setClassName(ECTaskActivity.MM, ECTaskActivity.LauncherUI);
        context.startActivity(intent);
    }


    public static void doOfTaskEnd() {
        JKFile.WriteFile(ECSdCardPath.Task_List_TXT, "");
        JKFile.WriteFile(ECSdCardPath.Task_Finish_TXT, "");
        JKFile.WriteFile(ECSdCardPath.Task_Fail_TXT, "");
        JKFile.WriteFile(ECSdCardPath.DETECTION_TASK_Finish_TXT, "");
        JKFile.WriteFile(ECSdCardPath.ResultTxt, "");

        JKPreferences.RemoveSharePersistent("doTasking");
        JKPreferences.RemoveSharePersistent("taskType");
        JKPreferences.RemoveSharePersistent("taskId");
        JKPreferences.RemoveSharePersistent("pushData");
    }


    public static void CreatNeedFile() {
        if (JKFile.IsSDCardAvailable()) {
            if (JKFile.IsExists(ECSdCardPath.SD_CARD_PATH)) {

            } else {
                //不存在则创建path文件夹
                JKFile.CreateDir(ECSdCardPath.SD_CARD_PATH);
            }

            //任务完成文件
            if (!JKFile.IsFile(ECSdCardPath.Task_Finish_TXT)) {
                JKFile.creatFileTxt(ECSdCardPath.Task_Finish_TXT);
            }
            if (!JKFile.IsFile(ECSdCardPath.Task_List_TXT)) {
                JKFile.creatFileTxt(ECSdCardPath.Task_List_TXT);
            }
            if (!JKFile.IsFile(ECSdCardPath.Task_Fail_TXT)) {
                JKFile.creatFileTxt(ECSdCardPath.Task_Fail_TXT);
            }
            if (!JKFile.IsFile(ECSdCardPath.DETECTION_TASK_Finish_TXT)) {
                JKFile.creatFileTxt(ECSdCardPath.DETECTION_TASK_Finish_TXT);
            }
            if (!JKFile.IsFile(ECSdCardPath.NendBF)) {
                JKFile.creatFileTxt(ECSdCardPath.NendBF);
            }
            if (!JKFile.IsFile(ECSdCardPath.ResultTxt)) {
                JKFile.creatFileTxt(ECSdCardPath.ResultTxt);
            }
        }
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


    public static void deleContanct(Context context) {
        //删除通讯录
        ArrayList<String> list = JKPreferences.GetSharePersistentArrayString("phoneList");
        for (String phone : list) {
            try {
                ContactUtil.deleteContact(context, phone);
                JKLog.i("RT", "del:" + phone);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


}
