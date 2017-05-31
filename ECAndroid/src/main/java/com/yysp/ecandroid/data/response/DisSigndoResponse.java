package com.yysp.ecandroid.data.response;

/**
 * Created by Administrator on 2017/5/19.
 */

public class DisSigndoResponse {

    public DisSigndoResponse(String deviceAlias, String machineCode) {
        this.deviceAlias = deviceAlias;
        this.machineCode = machineCode;
    }

    /**
     * deviceAlias : string
     * machineCode : string
     */

    private String deviceAlias;
    private String machineCode;

    public String getDeviceAlias() {
        return deviceAlias;
    }

    public void setDeviceAlias(String deviceAlias) {
        this.deviceAlias = deviceAlias;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }
}
