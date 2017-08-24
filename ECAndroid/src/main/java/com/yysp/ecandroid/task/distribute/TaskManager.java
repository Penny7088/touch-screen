package com.yysp.ecandroid.task.distribute;

import com.yysp.ecandroid.service.HelpService;

/**
 * Created on 2017/8/24 0024.
 * by penny
 */

public class TaskManager {

    private HelpService mService;
    private IStrategy mIStrategy;

    public TaskManager(IStrategy pStrategy, HelpService pService) {
        this.mIStrategy = pStrategy;
        this.mService = pService;
    }

    public void running() {
        mIStrategy.running(mService);
    }
}
