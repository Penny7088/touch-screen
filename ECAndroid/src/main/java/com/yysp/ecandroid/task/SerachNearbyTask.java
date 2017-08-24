package com.yysp.ecandroid.task;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.yysp.ecandroid.service.HelpService;
import com.yysp.ecandroid.task.distribute.IStrategy;
import com.yysp.ecandroid.task.distribute.SuperTask;
import com.yysp.ecandroid.util.Logger;

/**
 * Created on 2017/8/24 0024.
 * by penny
 * 搜索附近好友任务
 */

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class SerachNearbyTask extends SuperTask implements IStrategy {


    @Override
    public void running() {
        Logger.d("SearchNearbyTask", "=========================");
    }
}
