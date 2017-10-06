package com.yysp.ecandroid.data.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/4/18.
 */

public class ContactBean {

    /**
     * targetAccounts : ["TaskAccountVO"]
     * taskID : string
     * taskType : 502
     * internalAccount : string
     * accountPassword : string
     */

    private String taskID;
    private int taskType;
    private String internalAccount;
    private String accountPassword;
    private List<String> targetAccounts;

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

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }

    public List<String> getTargetAccounts() {
        return targetAccounts;
    }

    public void setTargetAccounts(List<String> targetAccounts) {
        this.targetAccounts = targetAccounts;
    }
}
