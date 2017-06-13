package com.yysp.ecandroid.net;

import com.jkframework.algorithm.JKEncryption;
import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.net.JKHttpRetrofit;
import com.jkframework.serialization.JKJson;
import com.yysp.ecandroid.config.ECSdCardPath;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.bean.DisGetTaskBean;
import com.yysp.ecandroid.data.response.AddErrorMsgResponse;
import com.yysp.ecandroid.data.response.DisSigndoResponse;
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

    public static final String Host = "http://192.168.1.45:8080/saas-api/";
//    public static final String Host = "http://192.168.1.134:8080/dis-api/";

    public static Retrofit retrofit = JKHttpRetrofit.GetRetrofitBuilder()
            .baseUrl(Host)
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


    public static Observable<DisBean> signUid(String deviceAlias, String machineCode) {
        DisSigndoResponse signdoResponse = new DisSigndoResponse(deviceAlias, machineCode);
        return service.sign(signdoResponse);
    }

    public static Observable<DisGetTaskBean> taskApply(String taskId, String deviceAlias) {
        return service.taskApply(taskId, deviceAlias);
    }

    public static Observable<DisBean> taskStatus(ECTaskResultResponse resultResponse) {
        JKFile.WriteFile(ECSdCardPath.NendBF, JKPreferences.GetSharePersistentString("pushData"));
        return service.taskStatus(resultResponse);
    }

    public static Observable<DisBean> addErrorMsg(AddErrorMsgResponse response) {
        return service.addErrorMessage(response);
    }


}
