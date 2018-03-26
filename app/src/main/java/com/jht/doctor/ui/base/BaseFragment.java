package com.jht.doctor.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author: mayakun
 * @date: 2017/11/16
 */
public abstract class BaseFragment extends RxFragment implements BasicProvider {

    //fragment ButterKnife没有在自动解绑，自己解绑
    public Unbinder mUnbinder;

//    public BaseFragment() {
//        //必须确保在Fragment实例化时setArguments()
//        setArguments(new Bundle());
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(provideRootLayout(), container, false);
        mUnbinder = ButterKnife.bind(this, v);
        initView();
        setupActivityComponent();
        return v;//return view的时候开始绘制
    }

    // onViewCreated是在onCreateView后被触发的事件
    // 完成绘制后执行的初始化方法，防止view没有初始化完成，就接受到event事件，防止bug
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        if (useEventBus()) {
            EventBus.getDefault().register(this);
        }
        super.onViewCreated(view, savedInstanceState);
    }

    //设置ActivityComponent
    protected abstract void setupActivityComponent();

    //设置layoutid
    protected abstract int provideRootLayout();

    //initview
    protected abstract void initView();

    @Override
    public Context actContext() {
        return getContext();
    }

    /**
     * 是否使用eventBus,默认为使用(true)，
     *
     * @return
     */
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void onDestroy() {
        if (useEventBus()) {
            EventBus.getDefault().unregister(this);
        }
        if (mUnbinder != null && mUnbinder != Unbinder.EMPTY) {
            mUnbinder.unbind();
        }
        this.mUnbinder = null;
        super.onDestroy();
    }


}
