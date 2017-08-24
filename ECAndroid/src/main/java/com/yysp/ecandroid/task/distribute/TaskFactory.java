package com.yysp.ecandroid.task.distribute;

import android.os.Build;
import android.support.annotation.RequiresApi;
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

@RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
public class TaskFactory {
    private static SparseArray<TaskManager> sTaskManagerArray = new SparseArray<TaskManager>();

    public static TaskManager createTask(int task) {
        TaskManager lManager = sTaskManagerArray.get(task);
        if (lManager == null) {
            switch (task) {
                case 500:
                    lManager = new TaskManager(new LoginTask());
                    break;
                case 501:
                    lManager = new TaskManager(new SerachNearbyTask());
                    break;
                case 502:
                    lManager = new TaskManager(new AddFriendTask());
                    break;
                case 503:
                    lManager = new TaskManager(new LogoutTask());
                    break;
            }
        }
        return lManager;
    }
}
