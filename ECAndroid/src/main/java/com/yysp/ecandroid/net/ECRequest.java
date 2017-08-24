package com.yysp.ecandroid.net;

import android.content.Context;

import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.bean.DisGetTaskBean;
import com.yysp.ecandroid.data.response.AddErrorMsgResponse;
import com.yysp.ecandroid.data.response.DisSigndoResponse;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by 2016/7/5.
 */
public class ECRequest {

    public static Observable<DisBean> signUid(String deviceAlias, String machineCode) {
        DisSigndoResponse signdoResponse = new DisSigndoResponse(deviceAlias, machineCode);
        return HttpUtils.getApiService()
                .sign(signdoResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<DisGetTaskBean> searchToDoJobByDevice(String taskId, String deviceAlias) {
        return HttpUtils.getApiService().searchToDoJobByDevice(taskId, deviceAlias)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<DisGetTaskBean> taskApply(String taskId, String deviceAlias) {
        return HttpUtils.getApiService().taskApply(taskId, deviceAlias)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<DisBean> taskStatus(ECTaskResultResponse resultResponse, Context context) {
        ECConfig.OpenScreenOrder(context);
        return HttpUtils.getApiService().taskStatus(resultResponse)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public static Observable<DisBean> addErrorMsg(AddErrorMsgResponse response) {
        return HttpUtils.getApiService().addErrorMessage(response)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }


}
