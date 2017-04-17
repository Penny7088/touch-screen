package com.jkframework.net;


import com.franmontiel.persistentcookiejar.ClearableCookieJar;
import com.franmontiel.persistentcookiejar.PersistentCookieJar;
import com.franmontiel.persistentcookiejar.cache.SetCookieCache;
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor;
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.jkframework.algorithm.JKFile;
import com.jkframework.debug.JKDebug;

import java.io.File;
import java.util.concurrent.TimeUnit;

import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class JKHttpRetrofit
{
	/** Http请求类 */
    private static final Retrofit.Builder rftBuilder = new Retrofit.Builder();
	static{
        ClearableCookieJar cookieJar =
                new PersistentCookieJar(new SetCookieCache(), new SharedPrefsCookiePersistor(JKDebug.hContext));
		HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
		logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient okhcClient = new OkHttpClient().newBuilder().readTimeout(10, TimeUnit.SECONDS)
				.connectTimeout(10,TimeUnit.SECONDS)
				.writeTimeout(10,TimeUnit.SECONDS)
				.addInterceptor(logging)
                .cookieJar(cookieJar).build();

        rftBuilder.addConverterFactory(GsonConverterFactory.create());
        rftBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        rftBuilder.client(okhcClient);
	}

    /**
     * 获取RetrofitBuilder
     * @return Builder对象
     */
    public static Retrofit.Builder GetRetrofitBuilder()
    {
        return rftBuilder;
    }

    /**
     * 获取缓存对象
     * @return Builder对象
     */
    public static RxCache GetRetrofitCache()
    {
        JKFile.CreateDir(JKFile.GetPublicCachePath() + "/JKCache/JKHttpRetrofit/");
        File cacheDir = new File(JKFile.GetPublicCachePath() + "/JKCache/JKHttpRetrofit");
        return new RxCache.Builder()
                .persistence(cacheDir,new GsonSpeaker());
    }

}