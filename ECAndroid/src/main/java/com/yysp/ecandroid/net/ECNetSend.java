package com.yysp.ecandroid.net;

import com.jkframework.algorithm.JKEncryption;
import com.jkframework.net.JKHttpRetrofit;
import com.jkframework.serialization.JKJson;
import com.yysp.ecandroid.data.bean.EcPostBean;
import com.yysp.ecandroid.data.response.ECLoginResponse;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 2016/7/5.
 */
public class ECNetSend {

    public static final String TestHost = "http://139.196.93.211:8080/saas-api/";

    public static Retrofit retrofit = JKHttpRetrofit.GetRetrofitBuilder()
            .baseUrl(TestHost)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static ECNetInterface service = retrofit.create(ECNetInterface.class);


    public static Observable<ECLoginResponse> Login(String tAccount, String tPassword) {
        JKJson jkjJson = new JKJson();
        jkjJson.CreateNode("action", "user_login_get");
        jkjJson.CreateNode("username", tAccount);
        jkjJson.CreateNode("password", JKEncryption.MD5_32(tPassword));
        jkjJson.CreateNode("account_type", "2001");


        return JKHttpRetrofit.GetRetrofitCache().using(ECNetCache.class)
                .Login(service.Login("v2", jkjJson.GetString()), new DynamicKey(jkjJson.GetString()), new EvictProvider(true));
    }


    public static Observable<EcPostBean> signUid(String deviceAlias) {
        return service.sign(deviceAlias);
    }

    public static Observable<EcPostBean> taskApply(int taskType, String deviceAlias) {
        return service.taskApply(taskType, deviceAlias);
    }

    public static Observable<EcPostBean> taskStatus(ECTaskResultResponse resultResponse) {
        return service.taskStatus(resultResponse);
    }


}
