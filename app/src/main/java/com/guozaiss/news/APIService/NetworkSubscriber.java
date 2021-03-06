package com.guozaiss.news.APIService;


import com.guozaiss.news.utils.ToastUtil;

import rx.Subscriber;
import timber.log.Timber;

/**
 * Created by bruce on 16/5/9.
 */
public class NetworkSubscriber<T> extends Subscriber<T> {

    @Override
    public void onCompleted() {
    }

    @Override
    public void onError(Throwable e) {
        Timber.e(e.getMessage() + "");
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    public void onSuccess(T data) {

    }

    public void onMessage(String message) {
        ToastUtil.showToast(message);
    }
}
