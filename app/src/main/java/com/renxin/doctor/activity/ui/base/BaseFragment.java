package com.renxin.doctor.activity.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle.components.support.RxFragment;

import org.greenrobot.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * BaseFragment
 * Create by mayakun at 2018/3/27 上午9:26
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
        View view = inflater.inflate(provideRootLayout(), container, false);
        if (useButterKnife()) {
            mUnbinder = ButterKnife.bind(this, view);
        }
        setupActivityComponent();
        initView();
        return view;//return view的时候开始绘制
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

    //设置layoutid
    protected abstract int provideRootLayout();

    //initview
    protected abstract void initView();

    //设置ActivityComponent
    protected abstract void setupActivityComponent();

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

    /**
     * 是否使用ButterKnife，默认为使用(true)
     *
     * @return
     */
    protected boolean useButterKnife() {
        return true;
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
