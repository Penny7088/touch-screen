package com.yysp.ecandroid.task;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.yysp.ecandroid.framework.distribute.IStrategy;
import com.yysp.ecandroid.framework.distribute.SuperTask;
import com.yysp.ecandroid.framework.util.Logger;

import java.util.ArrayList;

/**
 * Created on 2017/8/24 0024.
 * by penny
 * 登出任务
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class LogoutTask extends SuperTask implements IStrategy {



    @Override
    public void running() {
        Logger.d("LogoutTask", "=========================");
    }

    @Override
    public void running(ArrayList pArrayList) {

    }
}
