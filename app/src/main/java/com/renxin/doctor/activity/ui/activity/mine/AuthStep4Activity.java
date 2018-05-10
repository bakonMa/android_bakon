package com.renxin.doctor.activity.ui.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.ImageView;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean_jht.AuthInfoBean;
import com.renxin.doctor.activity.ui.contact.AuthContact;
import com.renxin.doctor.activity.ui.presenter.present_jht.AuthPresenter;
import com.renxin.doctor.activity.utils.ImageUtil;
import com.renxin.doctor.activity.widget.EditTextlayout;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 认证信息
 * AuthStep4Activity
 * Create at 2018/4/3 下午3:58 by mayakun
 */
public class AuthStep4Activity extends BaseActivity implements AuthContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.tv_idcard)
    EditTextlayout tvIdcard;
    @BindView(R.id.tv_address)
    EditTextlayout tvAddress;
    @BindView(R.id.tv_organization)
    EditTextlayout tvOrganization;
    @BindView(R.id.tv_lab_type)
    EditTextlayout tvLabType;
    @BindView(R.id.tv_title)
    EditTextlayout tvTitle;
    @BindView(R.id.iv_img1)
    ImageView ivImg1;
    @BindView(R.id.iv_img2)
    ImageView ivImg2;
    @BindView(R.id.iv_img3)
    ImageView ivImg3;


    @Inject
    AuthPresenter mPresenter;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_auth_step4;
    }

    @Override
    protected void initView() {
        initToolbar();
        mPresenter.getUserIdentify();
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("认证信息")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                }).bind();
    }

    @OnClick(R.id.tv_reset)
    public void resetOnClick() {
        startActivity(new Intent(this, AuthStep1Activity.class));
        finish();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case AuthPresenter.GET_AUTHINFO_OK:
                AuthInfoBean bean = (AuthInfoBean) message.obj;
                tvIdcard.setText(TextUtils.isEmpty(bean.id_card) ? "" : bean.id_card);
                tvAddress.setText((TextUtils.isEmpty(bean.province) ? "" : bean.province + "-") + (TextUtils.isEmpty(bean.city) ? "" : bean.city));
                tvOrganization.setText(TextUtils.isEmpty(bean.organization) ? "" : bean.organization);
                tvLabType.setText(TextUtils.isEmpty(bean.department) ? "" : bean.department);
                tvTitle.setText(TextUtils.isEmpty(bean.title) ? "" : bean.title);
                ImageUtil.showImage(bean.id_card_photo, ivImg1);
                ImageUtil.showImage(bean.profession_photo, ivImg2);
                ImageUtil.showImage(bean.qalification_photo, ivImg3);
                break;
        }

    }

    @Override
    public void onError(String errorCode, String errorMsg) {

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
