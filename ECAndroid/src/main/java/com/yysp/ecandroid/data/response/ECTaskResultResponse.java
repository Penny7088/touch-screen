package com.yysp.ecandroid.data.response;

import java.util.List;

/**
 * Created by Administrator on 2017/4/25.
 */

public class ECTaskResultResponse {

    private int amount;
    private String deviceAlias;
    private int status;
    private String taskId;
    private List<TaskResultBean> taskResult;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getDeviceAlias() {
        return deviceAlias;
    }

    public void setDeviceAlias(String deviceAlias) {
        this.deviceAlias = deviceAlias;
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
        /**
         * account : string
         * area : string
         * birth : string
         * mobile : string
         * nickname : string
         * profession : string
         * reamrk : string
         * result : string
         * sex : string
         * status : 0
         * taskId : string
         */

        private String account;
        private String area;
        private String birth;
        private String mobile;
        private String nickname;
        private String profession;
        private String reamrk;
        private String result;
        private String sex;
        private int status;
        private String taskId;

        public String getAccount() {
            return account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public String getBirth() {
            return birth;
        }

        public void setBirth(String birth) {
            this.birth = birth;
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

        public String getProfession() {
            return profession;
        }

        public void setProfession(String profession) {
            this.profession = profession;
        }

        public String getReamrk() {
            return reamrk;
        }

        public void setReamrk(String reamrk) {
            this.reamrk = reamrk;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
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
    }
}
