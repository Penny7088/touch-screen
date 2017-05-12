package com.yysp.ecandroid.data.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */

public class EcGetTaskBean  {

    /**
     * targetMobiles : ["string"]
     * taskID : string
     * taskType : 2
     * internalAccount : string
     * accountPassword : string
     */

    private String taskID;
    private int taskType;
    private String internalAccount;
    private String accountPassword;
    private String groupName;


    private List<String> targetMobiles;

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public int getTaskType() {
        return taskType;
    }

    public void setTaskType(int taskType) {
        this.taskType = taskType;
    }

    public String getInternalAccount() {
        return internalAccount;
    }

    public void setInternalAccount(String internalAccount) {
        this.internalAccount = internalAccount;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public List<String> getTargetMobiles() {
        return targetMobiles;
    }

    public void setTargetMobiles(List<String> targetMobiles) {
        this.targetMobiles = targetMobiles;
    }
}
