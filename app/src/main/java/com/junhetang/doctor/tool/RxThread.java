package com.junhetang.doctor.tool;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
public class RxThread {

    /**
     * 给任何Observable加上在Activity或Fragment中运行的线程调度器
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> uiSchedulersTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 给任何Observable加上在Service中运行的线程调度器
     *
     * @param <T>
     * @return
     */
    public static <T> ObservableTransformer<T, T> serviceSchedulersTransformer() {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(Schedulers.io());
            }
        };
    }

}
