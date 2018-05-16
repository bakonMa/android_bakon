package com.renxin.doctor.activity.ui.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.nim.NimManager;
import com.renxin.doctor.activity.ui.activity.login.LoginActivity;
import com.renxin.doctor.activity.ui.activity.login.ResetPasswordActivity;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.contact.LoginContact;
import com.renxin.doctor.activity.ui.presenter.LoginPresenter;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.utils.U;
import com.renxin.doctor.activity.widget.RelativeWithText;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SettingActivity 设置
 * Create at 2018/4/13 下午2:13 by mayakun
 */
public class SettingActivity extends BaseActivity implements LoginContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.st_changeflag)
    Switch stChangeFlag;
    @BindView(R.id.tv_reset_password)
    RelativeWithText tvResetPassword;

    @Inject
    LoginPresenter mPresenter;
    private boolean pushStatus;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        initToolbar();
        pushStatus = U.getMessageStatus();
        //消息提醒
        stChangeFlag.setChecked(pushStatus);
        stChangeFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                U.setMessageStatus(b);
                mPresenter.setPushStatus(b ? 1 : 0);
            }
        });
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("设置")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                }).bind();
    }

    @OnClick({R.id.tv_reset_password, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_reset_password:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
            case R.id.tv_logout:
                CommonDialog commonDialog = new CommonDialog(this, false, "确定退出当前账号吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.btn_ok) {
//                            mPresenter.logout();
                            U.logout();
                            NIMClient.getService(AuthService.class).logout();
                            Intent intent = new Intent(DocApplication.getInstance(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                commonDialog.show();
                break;
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .applicationComponent(DocApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case LoginPresenter.SETPUSHSTATIS_SUCCESS://设置提醒状态
                NimManager.setPushStatus(U.getMessageStatus());
               break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        ToastUtil.show(errorMsg);
        //只有一个请求 就不做多余的判断了，请求失败 恢复原来的状态
        stChangeFlag.setChecked(pushStatus);
        U.setMessageStatus(pushStatus);
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }
}
