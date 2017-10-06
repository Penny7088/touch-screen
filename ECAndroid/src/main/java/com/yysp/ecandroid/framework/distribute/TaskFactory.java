package com.yysp.ecandroid.framework.distribute;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.SparseArray;

import com.yysp.ecandroid.task.AddFriendTask;
import com.yysp.ecandroid.task.AddContactTask;
import com.yysp.ecandroid.task.ContactDiscernTask;
import com.yysp.ecandroid.task.CreateGounpTask;
import com.yysp.ecandroid.task.DiscernFriendTotalTask;
import com.yysp.ecandroid.task.InsertContactTask;
import com.yysp.ecandroid.task.InvitationFriendTask;
import com.yysp.ecandroid.task.LoginTask;
import com.yysp.ecandroid.task.LogoutTask;
import com.yysp.ecandroid.task.SerachNearbyTask;
import com.yysp.ecandroid.task.ClearCacheTask;

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
                case 504:
                    lManager = new TaskManager(new AddContactTask());
                    break;
                case 505:
                    lManager = new TaskManager(new ClearCacheTask());
                    break;
                case 506:
                    lManager = new TaskManager(new ContactDiscernTask());
                    break;
                case 507:
                    lManager = new TaskManager(new InsertContactTask());
                    break;
                case 508:
                    lManager = new TaskManager(new InvitationFriendTask());
                    break;
                case 509:
                    lManager = new TaskManager(new ClearCacheTask());
                    break;
                case 510:
                    lManager = new TaskManager(new CreateGounpTask());
                    break;
                case 511:
                    lManager = new TaskManager(new DiscernFriendTotalTask());
                    break;
            }
        }
        return lManager;
    }
}
