package com.bakon.android.ui.base;

import android.app.Activity;
import android.os.Message;

import com.trello.rxlifecycle2.LifecycleTransformer;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
public interface BaseView<T> {

    void onSuccess(Message message);

    void onError(String errorCode, String errorMsg);

    Activity provideContext();

    <R> LifecycleTransformer<R> toLifecycle();

}
