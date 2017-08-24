package com.yysp.ecandroid.task;

import com.yysp.ecandroid.service.HelpService;
import com.yysp.ecandroid.task.distribute.IStrategy;
import com.yysp.ecandroid.util.Logger;

/**
 * Created on 2017/8/24 0024.
 * by penny
 */

public class AddFriendTask implements IStrategy {

    @Override
    public void running(HelpService pService) {
        Logger.d("AddFriendTask","=========================");
    }
}
