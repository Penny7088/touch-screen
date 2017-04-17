package com.yysp.ecandroid.net;

import com.jkframework.algorithm.JKEncryption;
import com.jkframework.net.JKHttpRetrofit;
import com.jkframework.serialization.JKJson;
import com.yysp.ecandroid.data.response.ECLoginResponse;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import retrofit2.Retrofit;

/**
 * Created by 2016/7/5.
 */
public class ECNetSend {

    public static Observable<ECLoginResponse> Login(String tAccount, String tPassword)
    {
        JKJson jkjJson = new JKJson();
        jkjJson.CreateNode("action", "user_login_get");
        jkjJson.CreateNode("username", tAccount);
        jkjJson.CreateNode("password", JKEncryption.MD5_32(tPassword));
        jkjJson.CreateNode("account_type", "2001");

        Retrofit retrofit = JKHttpRetrofit.GetRetrofitBuilder()
                .baseUrl("http://alli.ishowtu.com/")
                .build();
        ECNetInterface service = retrofit.create(ECNetInterface.class);

        return  JKHttpRetrofit.GetRetrofitCache().using(ECNetCache.class)
                .Login(service.Login("v2", jkjJson.GetString()),new DynamicKey(jkjJson.GetString()),new EvictProvider(true));
    }

//    public static Observable<AFPlayingListResponse> RequestPlaying(int nPage)
//    {
//        JKJson jkjJson = new JKJson();
//        jkjJson.CreateNode("action", "user_plaything_get");
//        jkjJson.CreateNode("page", nPage);
//
//        Retrofit retrofit = JKHttpRetrofit.GetRetrofitBuilder()
//                .baseUrl("http://alli.ishowtu.com/")
//                .build();
//        ECNetInterface service = retrofit.create(ECNetInterface.class);
//
////        return  JKHttpClient.GetRetrofitCache().using(AFNetCache.class)
////                .RequestPlaying(service.RequestPlaying("v2", jkjJson.GetString()),new DynamicKey(jkjJson.GetString()),new EvictProvider(true));
//        return service.RequestPlaying("v2", jkjJson.GetString());
//    }

}
