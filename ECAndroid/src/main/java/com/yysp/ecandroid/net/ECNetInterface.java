package com.yysp.ecandroid.net;

import com.yysp.ecandroid.data.bean.EcPostBean;
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
    @FormUrlEncoded
    @POST("device/sign.do")
    Observable<EcPostBean> sign(@Field("deviceAlias") String deviceAlias);

    //发送任务确认请求
    @FormUrlEncoded
    @POST("task/taskApplyDemo2.do")
    Observable<EcPostBean> taskApply(@Field("taskType") int taskType, @Field("deviceAlias") String deviceAlias);

    //发送任务结果回执
    @POST("task/taskStatusDemo.do")
    Observable<EcPostBean> taskStatus(@Body ECTaskResultResponse taskResultResponse);

}
