package com.jht.doctor.ui.activity.mine;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.contact.FeedBackContact;
import com.jht.doctor.ui.presenter.FeedBackPresenter;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;

public class FeedbackActivity extends BaseAppCompatActivity implements FeedBackContact.View {

    @BindView(R.id.id_rl_left)
    RelativeLayout idRlLeft;
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_ed_content)
    EditText idEdContent;
    @BindView(R.id.id_tv_count)
    TextView idTvCount;
    @BindView(R.id.id_btn_submit)
    TextView idBtnSubmit;

    @Inject
    FeedBackPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        ButterKnife.bind(this);
        initToolbar();
    }

    @OnTextChanged(value = R.id.id_ed_content, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterContentChanged(Editable s) {
        int len = idEdContent.getText().toString().length();
        idTvCount.setText(MessageFormat.format("{0}/500", len));
        idBtnSubmit.setEnabled(len > 0);
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setTitle("使用反馈")
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

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(CustomerApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @OnClick(R.id.id_btn_submit)
    public void onViewClicked() {
        mPresenter.confirmMessage(idEdContent.getText().toString().trim());
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("提交成功，感谢您的反馈！");
        finish();
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
