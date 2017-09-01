package com.yysp.ecandroid.framework.net;

import rx.Subscriber;

/**
 * Created by penny on 2017/7/3 0003.
 */

public abstract class BaseSubscriber<T> extends Subscriber<T> {


    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }


}
