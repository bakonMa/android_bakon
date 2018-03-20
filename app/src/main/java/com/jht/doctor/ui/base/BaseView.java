package com.jht.doctor.ui.base;

import android.app.Activity;
import android.os.Message;

import com.trello.rxlifecycle.LifecycleTransformer;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
public interface BaseView<T> {

    void onError(String errorCode, String errorMsg);
    
    void onSuccess(Message message);

    Activity provideContext();

    <R> LifecycleTransformer<R> toLifecycle();

}
