package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;

import com.junhetang.doctor.R;
import com.junhetang.doctor.ui.activity.login.LoginActivity;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.base.BaseView;
import com.trello.rxlifecycle2.LifecycleTransformer;

import butterknife.OnClick;

/**
 * LogoutActivity 互踢 全局dialog 用activity来实现
 * Create at 2018/6/1 下午3:08 by mayakun 
 */
public class LogoutActivity extends BaseActivity implements BaseView {

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_logout_dialog;
    }

    @Override
    protected void initView() {
    }

    @OnClick(R.id.btn_ok)
    public void btnOnClick() {
        //关闭所有activity
        //会出现黑屏
        //DocApplication.getAppComponent().mgrRepo().actMgr().finishAllActivity();

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setClass(this, LoginActivity.class);
        startActivity(intent);
    }


    @Override
    protected void setupActivityComponent() {
    }

    @Override
    public void onBackPressed() {
        return;
    }

    @Override
    public void onSuccess(Message message) {

    }

    @Override
    public void onError(String errorCode, String errorMsg) {

    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }
}
