package com.yysp.ecandroid.net;

import android.content.Context;

import com.jkframework.algorithm.JKFile;
import com.jkframework.config.JKPreferences;
import com.jkframework.net.JKHttpRetrofit;
import com.yysp.ecandroid.config.ECConfig;
import com.yysp.ecandroid.config.ECSdCardPath;
import com.yysp.ecandroid.data.bean.DisBean;
import com.yysp.ecandroid.data.bean.DisGetTaskBean;
import com.yysp.ecandroid.data.response.AddErrorMsgResponse;
import com.yysp.ecandroid.data.response.DisSigndoResponse;
import com.yysp.ecandroid.data.response.ECTaskResultResponse;

import io.reactivex.Observable;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 2016/7/5.
 */
public class ECNetSend {

    //    public static final String Host = "http://192.168.1.45:8080/saas-api/";
//    public static final String Host = "http://192.168.1.134:8080";
        public static final String Host = "http://118.31.51.197:8101/";

    public static Retrofit retrofit = JKHttpRetrofit.GetRetrofitBuilder()
            .baseUrl(Host)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    public static ECNetInterface service = retrofit.create(ECNetInterface.class);


    public static Observable<DisBean> signUid(String deviceAlias, String machineCode) {
        DisSigndoResponse signdoResponse = new DisSigndoResponse(deviceAlias, machineCode);
        return service.sign(signdoResponse);
    }

    public static Observable<DisGetTaskBean> searchToDoJobByDevice(String taskId, String deviceAlias) {
        return service.searchToDoJobByDevice(taskId, deviceAlias);
    }

    public static Observable<DisGetTaskBean> taskApply(String taskId, String deviceAlias) {
        return service.taskApply(taskId, deviceAlias);
    }

    public static Observable<DisBean> taskStatus(ECTaskResultResponse resultResponse, Context context) {
        ECConfig.OpenScreenOrder(context);
        if (!resultResponse.getLoginFail()) {
            JKFile.WriteFile(ECSdCardPath.NendBF, JKPreferences.GetSharePersistentString("pushData"));
        }
        return service.taskStatus(resultResponse);
    }

    public static Observable<DisBean> addErrorMsg(AddErrorMsgResponse response) {
        return service.addErrorMessage(response);
    }


}
