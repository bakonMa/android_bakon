package com.renxin.doctor.activity.ui.activity.mine;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.EventConfig;
import com.renxin.doctor.activity.data.eventbus.Event;
import com.renxin.doctor.activity.data.eventbus.EventBusUtil;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean_jht.UserBaseInfoBean;
import com.renxin.doctor.activity.ui.contact.PersonalContact;
import com.renxin.doctor.activity.ui.presenter.PersonalPresenter;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.utils.U;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnTextChanged;

/**
 * UserExplainActivity 填写简介
 * Create at 2018/4/10 上午9:36 by mayakun
 */
public class UserExplainActivity extends BaseActivity implements PersonalContact.View {

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
        return R.layout.activity_explain;
    }

    @Override
    protected void initView() {
        initToolbar();
        baseInfoBean = U.getUserInfo();
        if (baseInfoBean != null) {
            edContent.setText(baseInfoBean.my_explain);
        }
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("简介")
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
        tvCount.setText(MessageFormat.format("{0}/300", len));
    }


    private void addInfo() {
        if (TextUtils.isEmpty(edContent.getText().toString().trim())) {
            ToastUtil.showShort("请输入简介内容");
            return;
        }
        //提交类型标识 1：简介 2：公告
        mPresenter.addUserbasic(edContent.getText().toString().trim(), 1);
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
        //修改后保存
        baseInfoBean.my_explain = edContent.getText().toString().trim();
        U.saveUserInfo(new Gson().toJson(baseInfoBean));
        //完善资料 刷新数据
        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_UPDATE_EXPLAIN));
        ToastUtil.showShort("保存成功");
        finish();
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
