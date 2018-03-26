package com.jht.doctor.ui.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.jht.doctor.data.eventbus.EventBusUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

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
        initStatusBar();
        setContentView(provideRootLayout());
        if (useButterKnife()) {
            ButterKnife.bind(this);
        }
        if (useEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBusUtil.register(this);
        }
        initView();
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {       //5.0以上(包含)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_OVERSCAN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        } else if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {    //4.3以上(不包含)
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
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
    protected void onDestroy() {
        super.onDestroy();
        if (useEventBus()) {
            EventBusUtil.unregister(this);
        }
    }
}
