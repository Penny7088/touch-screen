package com.jkframework.thread;


import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

public class JKThread {

    /**
     * 执行短期线程功能
     *
     * @param l 线程功能监听
     */
    public void Start(final JKThreadListener l) {
        Observable.create(new ObservableOnSubscribe<Void>() {

            @Override
            public void subscribe(@NonNull ObservableEmitter<Void> e) throws Exception {
                if (l != null)
                    l.OnThread();
                e.onNext(null);
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Void>() {
                    @Override
                    public void accept(@NonNull Void aVoid) throws Exception {
                        if (l != null)
                            l.OnMain();
                    }
                });

    }

    public interface JKThreadListener {

        /**
         * 线程执行的功能
         */
        void OnThread();

        /**
         * 线程执行完毕回调主线程的功能
         */
        void OnMain();
    }
}
