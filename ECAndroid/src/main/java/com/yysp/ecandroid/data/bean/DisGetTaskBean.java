package com.yysp.ecandroid.data.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/4/21.
 */

public class DisGetTaskBean implements Serializable {

    private int code;
    private DataBean data;
    private String msg;
    private int bizCode;
    private String groupName;
    private String content;
    private String addFriendMessage;
    private int amount;

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAddFriendMessage() {
        return addFriendMessage;
    }

    public void setAddFriendMessage(String addFriendMessage) {
        this.addFriendMessage = addFriendMessage;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getBizCode() {
        return bizCode;
    }

    public void setBizCode(int bizCode) {
        this.bizCode = bizCode;
    }


    public static class DataBean {

        private String groupName;
        private String content;
        private String targetGroupRemark;
        private String targetGroupMessage;
        private String amount;
        private String addFriendMessage;
        private int taskType;
        private String accountPassword;
        private String taskId;
        private String internalAccount;
        private int timeOut;
        private int stopTask;
        private List<TargetAccountsBean> targetAccounts;
        private int hbTimer;

        public int getTimeOut() {
            return timeOut;
        }

        public void setTimeOut(int timeOut) {
            this.timeOut = timeOut;
        }

        public String getGroupName() {
            return groupName;
        }

        public void setGroupName(String groupName) {
            this.groupName = groupName;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getTargetGroupRemark() {
            return targetGroupRemark;
        }

        public void setTargetGroupRemark(String targetGroupRemark) {
            this.targetGroupRemark = targetGroupRemark;
        }

        public String getTargetGroupMessage() {
            return targetGroupMessage;
        }

        public void setTargetGroupMessage(String targetGroupMessage) {
            this.targetGroupMessage = targetGroupMessage;
        }

        public int getStopTask() {
            return stopTask;
        }

        public void setStopTask(int stopTask) {
            this.stopTask = stopTask;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAddFriendMessage() {
            return addFriendMessage;
        }

        public void setAddFriendMessage(String addFriendMessage) {
            this.addFriendMessage = addFriendMessage;
        }


        public int getTaskType() {
            return taskType;
        }

        public void setTaskType(int taskType) {
            this.taskType = taskType;
        }

        public String getAccountPassword() {
            return accountPassword;
        }

        public void setAccountPassword(String accountPassword) {
            this.accountPassword = accountPassword;
        }

        public int gethbTimer() {
            return hbTimer;
        }

        public void sethbTimer(int hbTimer) {
            this.hbTimer = hbTimer;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }

        public String getInternalAccount() {
            return internalAccount;
        }

        public void setInternalAccount(String internalAccount) {
            this.internalAccount = internalAccount;
        }

        public List<TargetAccountsBean> getTargetAccounts() {
            return targetAccounts;
        }

        public void setTargetAccounts(List<TargetAccountsBean> targetAccounts) {
            this.targetAccounts = targetAccounts;
        }


        public static class TargetAccountsBean {

            private int currentPage;
            private int pageSize;
            private String taskId;
            private String mobile;
            private String account;
            private Object status;
            private String nickname;
            private Object remark;
            private Object result;

            public int getCurrentPage() {
                return currentPage;
            }

            public void setCurrentPage(int currentPage) {
                this.currentPage = currentPage;
            }

            public int getPageSize() {
                return pageSize;
            }

            public void setPageSize(int pageSize) {
                this.pageSize = pageSize;
            }

            public String getTaskId() {
                return taskId;
            }

            public void setTaskId(String taskId) {
                this.taskId = taskId;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }

            public String getAccount() {
                return account;
            }

            public void setAccount(String account) {
                this.account = account;
            }

            public Object getStatus() {
                return status;
            }

            public void setStatus(Object status) {
                this.status = status;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public Object getRemark() {
                return remark;
            }

            public void setRemark(Object remark) {
                this.remark = remark;
            }

            public Object getResult() {
                return result;
            }

            public void setResult(Object result) {
                this.result = result;
            }
        }
    }
}
