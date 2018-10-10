package com.junhetang.doctor.ui.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.utils.StatusBarUtil;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;
import com.umeng.analytics.MobclickAgent;

import butterknife.ButterKnife;

/**
 * BaseActivity
 * Create by mayakun at 2018/3/26 下午5:27
 */
public abstract class BaseActivity extends RxAppCompatActivity implements BasicProvider {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();
        //沉浸式状态栏
        StatusBarUtil.initStatusBar(this);
        //处理启动页，id=0
        if (provideRootLayout() != 0) {
            setContentView(provideRootLayout());
        }
        if (useButterKnife()) {
            ButterKnife.bind(this);
        }
        if (useEventBus()) {
            EventBusUtil.register(this);
        }
        initView();
    }

    //设置layoutid
    protected abstract int provideRootLayout();

    /**
     * initView
     */
    protected abstract void initView();

    //设置ActivityComponent
    protected abstract void setupActivityComponent();

    /**
     * 是否使用EventBus，默认为不使用(false)，
     *
     * @return
     */
    protected boolean useEventBus() {
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
    public Context actContext() {
        return this;
    }

    @Override
    public void onResume() {
        super.onResume();
        //Umeng 统计
        MobclickAgent.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        //Umeng 统计
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            EventBusUtil.unregister(this);
        }
    }
}
