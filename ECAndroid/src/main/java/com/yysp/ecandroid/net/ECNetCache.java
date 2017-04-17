package com.yysp.ecandroid.net;

import com.yysp.ecandroid.data.response.ECLoginResponse;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.rx_cache2.DynamicKey;
import io.rx_cache2.EvictProvider;
import io.rx_cache2.LifeCache;

/**
 * Created by 2016/7/5.
 */
interface ECNetCache {

    @LifeCache(duration = 2, timeUnit = TimeUnit.MINUTES)
    Observable<ECLoginResponse> Login(Observable<ECLoginResponse> oReps, DynamicKey parameter, EvictProvider evictProvider);

}
