package com.yysp.ecandroid.util;

import android.content.Context;
import android.content.Intent;

import com.jkframework.config.JKPreferences;
import com.yysp.ecandroid.view.activity.ECTaskActivity;

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
        JKPreferences.RemoveSharePersistent("doTasking");
        JKPreferences.RemoveSharePersistent("taskType");
        JKPreferences.RemoveSharePersistent("isRunning");
    }
}
