package com.yysp.ecandroid.task.distribute;

import android.util.SparseArray;

import com.yysp.ecandroid.service.HelpService;
import com.yysp.ecandroid.task.AddFriendTask;
import com.yysp.ecandroid.task.LoginTask;
import com.yysp.ecandroid.task.LogoutTask;
import com.yysp.ecandroid.task.SerachNearbyTask;

/**
 * Created on 2017/8/24 0024.
 * by penny
 */

public class TaskFactory {
    private static SparseArray<TaskManager> sTaskManagerArray = new SparseArray<TaskManager>();

    public static TaskManager createTask(int task, HelpService pService) {
        TaskManager lManager = sTaskManagerArray.get(task);
        if (lManager == null) {
            switch (task) {
                case 500:
                    lManager = new TaskManager(new LoginTask(), pService);
                    break;
                case 501:
                    lManager = new TaskManager(new SerachNearbyTask(), pService);
                    break;
                case 502:
                    lManager = new TaskManager(new AddFriendTask(), pService);
                    break;
                case 503:
                    lManager = new TaskManager(new LogoutTask(), pService);
                    break;
            }
        }
        return lManager;
    }
}
