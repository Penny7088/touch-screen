package com.yysp.ecandroid.net;

import com.yysp.ecandroid.data.response.AddErrorMsgResponse;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.bean.DisGetTaskBean;
import com.yysp.ecandroid.data.response.DisSigndoResponse;
import com.yysp.ecandroid.data.response.ECLoginResponse;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by 2016/7/5.
 */
interface ECNetInterface {

    @FormUrlEncoded
    @POST("api/api.php")
    Observable<ECLoginResponse> Login(@Field("version") String tVersion, @Field("vars") String tJson);

    //注册发送设备id
    @POST("device/sign.do")
    Observable<DisBean> sign(@Body DisSigndoResponse response);

    //发送任务确认请求
    @FormUrlEncoded
    @POST("task/taskApply.do")
    Observable<DisGetTaskBean> taskApply(@Field("taskId") String taskId, @Field("deviceAlias") String deviceAlias);

    //发送任务结果回执
    @POST("task/taskStatus.do")
    Observable<DisBean> taskStatus(@Body ECTaskResultResponse taskResultResponse);


    //接口失败
    @POST("/error/addErrorMessage.do")
    Observable<DisBean> addErrorMessage(@Body AddErrorMsgResponse msgResponse);


}
