package com.yysp.ecandroid.data.bean;

/**
 * Created by Administrator on 2017/4/21.
 */

public class EcPostBean {

    /**
     * code : 200
     * data : null
     * msg :
     * bizCode : 0
     */

    private int code;
    private Object data;
    private String msg;
    private int bizCode;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
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
}
