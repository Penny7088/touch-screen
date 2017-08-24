package com.yysp.ecandroid.task.distribute;

import com.yysp.ecandroid.service.HelpService;

/**
 * Created on 2017/8/24 0024.
 * by penny
 */

public class TaskManager {

    private IStrategy mIStrategy;

    public TaskManager(IStrategy pStrategy) {
        this.mIStrategy = pStrategy;
    }

    public void running() {
        mIStrategy.running();
    }
}
