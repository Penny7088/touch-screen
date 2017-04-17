package com.yysp.ecandroid.net;

import com.yysp.ecandroid.data.response.ECLoginResponse;

import io.reactivex.Observable;
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
}
