package com.jht.doctor.ui.base;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.jht.doctor.data.eventbus.EventBusUtil;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import org.greenrobot.eventbus.EventBus;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
public abstract class BaseAppCompatActivity extends RxAppCompatActivity implements BasicProvider {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActivityComponent();
        initStatusBar();
        if (isUseEventBus() && !EventBus.getDefault().isRegistered(this)) {
            EventBusUtil.register(this);
        }

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

    protected abstract void setupActivityComponent();

    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    public Context actContext() {
        return this;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isUseEventBus()) {
            EventBusUtil.unregister(this);
        }
    }
}
