package com.renxin.doctor.activity.tool;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
public class RxThread {

    /**
     * 给任何Observable加上在Activity或Fragment中运行的线程调度器
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T,R> Observable.Transformer<T,R>  uiSchedulersTransformer(){
        return new Observable.Transformer<T , R>(){
            @Override
            public Observable<R> call(Observable<T> tObservable) {
                return (Observable<R>) tObservable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
            }
        };
    }

    /**
     * 给任何Observable加上在Service中运行的线程调度器
     * @param <T>
     * @param <R>
     * @return
     */
    public static <T,R> Observable.Transformer<T,R>  serviceSchedulersTransformer(){
        return new Observable.Transformer<T , R>(){
            @Override
            public Observable<R> call(Observable<T> tObservable) {
                return (Observable<R>) tObservable.subscribeOn(Schedulers.io()).observeOn(Schedulers.io());
            }
        };
    }

}
