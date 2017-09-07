package com.yysp.ecandroid.framework.distribute;

import java.util.ArrayList;

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

    public void running(ArrayList pArrayList) {
        mIStrategy.running(pArrayList);
    }
}
