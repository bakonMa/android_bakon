package com.renxin.doctor.activity.ui.nimview;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean.CommMessageBean;
import com.renxin.doctor.activity.ui.contact.ChatMessageContact;
import com.renxin.doctor.activity.ui.presenter.ChatMessagePresenter;
import com.renxin.doctor.activity.utils.ToastUtil;
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
 * QuickReplyActivity  快速回复
 * Create at 2018/4/19 上午10:42 by mayakun
 */
public class AddCommMessageActivity extends BaseActivity implements ChatMessageContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.ed_content)
    EditText edContent;
    @BindView(R.id.tv_count)
    TextView tvCount;

    @Inject
    ChatMessagePresenter mPresenter;

    private int type;//1:药物常用语，2:咨询常用语
    private CommonDialog commonDialog;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_explain;
    }

    @Override
    protected void initView() {
        type = getIntent().getIntExtra("type", 1);
        initToolbar();
        edContent.setHint(type == 1 ? "编辑药物常用语" : "编辑咨询常用语");
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle(type == 1 ? "添加药物常用语" : "添加咨询常用语")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setRightText("完成", true, R.color.color_main)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        onBackPressed();
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
            ToastUtil.showShort("请输入常用语内容");
            return;
        }
        mPresenter.adduseful(type, edContent.getText().toString().trim());
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        ToastUtil.showShort("添加成功");
        CommMessageBean messageBean = new CommMessageBean();
        messageBean.id = Integer.parseInt(message.obj.toString());
        messageBean.content = edContent.getText().toString().trim();
        Intent intent = new Intent();
        intent.putExtra("commmessage", messageBean);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
    }

    //点击返回
    @Override
    public void onBackPressed() {
        commonDialog = new CommonDialog(provideContext(), false, "放弃添加常用语吗？", view -> {
            if (view.getId() == R.id.btn_ok) {
                finish();
            }
        });
        commonDialog.show();
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
    public Activity provideContext() {
        return this;
    }

    @Override
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }
}
