package com.yysp.ecandroid.data.bean;

/**
 * Created by Administrator on 2017/5/19.
 */

public class DisBean {
    /**
     * bizCode : 0
     * code : 200
     * data : {"smsCode":"","wechatNo":"15973077088"}
     * msg :
     */

    private int bizCode;
    private int code;
    private DataBean data;
    private String msg;

    public int getBizCode() {
        return bizCode;
    }

    public void setBizCode(int bizCode) {
        this.bizCode = bizCode;
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

    public static class DataBean {
        /**
         * smsCode :
         * wechatNo : 15973077088
         */

        private String smsCode;
        private String wechatNo;

        public String getSmsCode() {
            return smsCode;
        }

        public void setSmsCode(String smsCode) {
            this.smsCode = smsCode;
        }

        public String getWechatNo() {
            return wechatNo;
        }

        public void setWechatNo(String wechatNo) {
            this.wechatNo = wechatNo;
        }
    }


//    /**
//     * bizCode : 0
//     * code : 200
//     * data : {"smsCode":"1234"}
//     * msg :
//     */
//
//    private int bizCode;
//    private int code;
//    private DataBean data;
//    private String msg;
//
//    public int getBizCode() {
//        return bizCode;
//    }
//
//    public void setBizCode(int bizCode) {
//        this.bizCode = bizCode;
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public DataBean getData() {
//        return data;
//    }
//
//    public void setData(DataBean data) {
//        this.data = data;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
//
//    public static class DataBean {
//        /**
//         * smsCode : 1234
//         */
//
//        private String smsCode;
//
//        public String getSmsCode() {
//            return smsCode;
//        }
//
//        public void setSmsCode(String smsCode) {
//            this.smsCode = smsCode;
//        }
//    }


//    /**
//     * bizCode : 0
//     * code : 0
//     * data : string
//     * msg : string
//     */
//
//    private int bizCode;
//    private int code;
//    private String data;
//    private String msg;
//
//    public int getBizCode() {
//        return bizCode;
//    }
//
//    public void setBizCode(int bizCode) {
//        this.bizCode = bizCode;
//    }
//
//    public int getCode() {
//        return code;
//    }
//
//    public void setCode(int code) {
//        this.code = code;
//    }
//
//    public String getData() {
//        return data;
//    }
//
//    public void setData(String data) {
//        this.data = data;
//    }
//
//    public String getMsg() {
//        return msg;
//    }
//
//    public void setMsg(String msg) {
//        this.msg = msg;
//    }
}
