package com.junhetang.doctor.ui.activity.mine;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.UserBaseInfoBean;
import com.junhetang.doctor.ui.contact.PersonalContact;
import com.junhetang.doctor.ui.presenter.PersonalPresenter;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * UserNoticeActivity 填写公告
 * Create at 2018/4/10 上午9:36 by mayakun
 */
public class UserNoticeActivity extends BaseActivity implements PersonalContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.ed_content)
    EditText edContent;
    @BindView(R.id.tv_count)
    TextView tvCount;

    @Inject
    PersonalPresenter mPresenter;

    private UserBaseInfoBean baseInfoBean;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_notice;
    }

    @Override
    protected void initView() {
        initToolbar();
        baseInfoBean = U.getUserInfo();
        if (baseInfoBean != null) {
            edContent.setText(TextUtils.isEmpty(baseInfoBean.notice) ? "" : baseInfoBean.notice);
        } else {
            mPresenter.getUserBasicInfo();
        }
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("公告")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setRightText("保存", true, R.color.color_main)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        addInfo();
                    }
                }).bind();
    }


    @OnTextChanged(value = R.id.ed_content, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterContentChanged(Editable s) {
        int len = s.length();
        edContent.setSelection(len);
        tvCount.setText(MessageFormat.format("{0}/300", len));
    }

    @OnClick({R.id.tv_notice1, R.id.tv_notice2, R.id.tv_notice3, R.id.tv_notice4})
    void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_notice1:
                edContent.setText(UIUtils.getString(R.string.str_notice1));
                break;
            case R.id.tv_notice2:
                edContent.setText(UIUtils.getString(R.string.str_notice2));
                break;
            case R.id.tv_notice3:
                edContent.setText(UIUtils.getString(R.string.str_notice3));
                break;
            case R.id.tv_notice4:
                edContent.setText(UIUtils.getString(R.string.str_notice4));
                break;
        }
    }


    private void addInfo() {
        if (TextUtils.isEmpty(edContent.getText().toString().trim())) {
            ToastUtil.showShort("请输入公告内容");
            return;
        }
        //提交类型标识 1：简介 2：公告
        mPresenter.addUserbasic(edContent.getText().toString().trim(), 2);
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
            case PersonalPresenter.GET_USEBASE_INFO://获取基本信息
                baseInfoBean = U.getUserInfo();
                if (baseInfoBean != null) {
                    edContent.setText(TextUtils.isEmpty(baseInfoBean.notice) ? "" : baseInfoBean.notice);
                }
                break;
            case PersonalPresenter.ADD_USER_BASEINFO://修改公告
                //修改后保存
                baseInfoBean.notice = edContent.getText().toString().trim();
                U.saveUserInfo(new Gson().toJson(baseInfoBean));
                //完善资料 刷新数据
                EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_UPDATE_NOTICE));
                ToastUtil.showShort("保存成功");
                finish();
                break;
        }

    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CommonDialog commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
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
