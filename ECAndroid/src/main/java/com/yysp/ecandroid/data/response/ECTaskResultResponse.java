package com.yysp.ecandroid.data.response;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */

public class ECTaskResultResponse {


    private int amount;
    private int currentPage;
    private String deviceAlias;
    private boolean isBankcard;
    private boolean isConsume;
    private boolean isRealname;
    private String machineCode;
    private int pageSize;
    private String reason;
    private int status;
    private String taskId;
    private List<TaskResultBean> taskResult;
    private Boolean loginFail = false;


    public Boolean getLoginFail() {
        return loginFail;
    }

    public void setLoginFail(Boolean loginFail) {
        this.loginFail = loginFail;
    }


    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public String getDeviceAlias() {
        return deviceAlias;
    }

    public void setDeviceAlias(String deviceAlias) {
        this.deviceAlias = deviceAlias;
    }

    public boolean isIsBankcard() {
        return isBankcard;
    }

    public void setIsBankcard(boolean isBankcard) {
        this.isBankcard = isBankcard;
    }

    public boolean isIsConsume() {
        return isConsume;
    }

    public void setIsConsume(boolean isConsume) {
        this.isConsume = isConsume;
    }

    public boolean isIsRealname() {
        return isRealname;
    }

    public void setIsRealname(boolean isRealname) {
        this.isRealname = isRealname;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public List<TaskResultBean> getTaskResult() {
        return taskResult;
    }

    public void setTaskResult(List<TaskResultBean> taskResult) {
        this.taskResult = taskResult;
    }

    public static class TaskResultBean {


        private String account;
        private int currentPage;
        private String mobile;
        private String nickname;
        private int pageSize;
        private String remark;
        private String result;
        private int status;
        private String sex;
        private List<ChatVo> chatList;

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        private String area;

        private String taskId;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(int currentPage) {
            this.currentPage = currentPage;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public int getPageSize() {
            return pageSize;
        }

        public void setPageSize(int pageSize) {
            this.pageSize = pageSize;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getTaskId() {
            return taskId;
        }

        public void setTaskId(String taskId) {
            this.taskId = taskId;
        }


        public List<ChatVo> getChatList() {
            return chatList;
        }

        public void setChatList(List<ChatVo> chatList) {
            this.chatList = chatList;
        }


        public static class ChatVo {
            private String name;
            private String content;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }
        }
    }

}
