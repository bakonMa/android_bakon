package com.jht.doctor.ui.activity.loan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.ApplyInfoBean;
import com.jht.doctor.ui.contact.BasicInfoContact;
import com.jht.doctor.ui.presenter.BasicInfoPresenter;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.EditTextlayout;
import com.jht.doctor.widget.ScheduleView;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BasicInfoActivity extends BaseAppCompatActivity implements BasicInfoContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_edit_name)
    EditTextlayout idEditName;
    @BindView(R.id.id_edit_idcard)
    EditTextlayout idEditIdcard;
    @BindView(R.id.id_btn_next_step)
    TextView idBtnNextStep;
    @BindView(R.id.id_step)
    ScheduleView idStep;

    @Inject
    BasicInfoPresenter mPresenter;


    private boolean commited;//是否已经提交过基本信息

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_info);
        ButterKnife.bind(this);
        initToolBar();
        initEvent();
        requestData();
    }

    public void requestData() {
        mPresenter.requestInfo();
    }

    protected void initToolBar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setTitle("基本信息")
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

    private void initEvent() {
        idEditName.setMaxLength(15);
        idEditIdcard.setMaxLength(18);
        idEditName.getEditText().addTextChangedListener(textWatcherImp);
        idEditIdcard.getEditText().addTextChangedListener(textWatcherImp);
    }

    private TextWatcher textWatcherImp = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            idBtnNextStep.setEnabled(idEditName.getEditText().getText().toString().length() > 0
                    && idEditIdcard.getEditText().getText().toString().length() > 0);
        }
    };


    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(CustomerApplication.getAppComponent())
                .build()
                .inject(this);
    }


    @OnClick(R.id.id_btn_next_step)
    public void onViewClicked() {
        if (commited) {
            startActivity(new Intent(this, JobInfoActivity.class));
        } else {
            mPresenter.commitInfo(idEditName.getEditText().getText().toString().trim()
                    , idEditIdcard.getEditText().getText().toString().trim());
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.REFRESH_APPLY_INFO_BASIC:
                    requestData();
                    break;
                case EventConfig.FINISH_LOAN:
                    finish();
                    break;
            }
        }

    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case BasicInfoPresenter.COMMIT_BASICINFO:
                //刷新个人中心
                EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_PERSONAL));
                startActivity(new Intent(this, JobInfoActivity.class));
                break;
            case BasicInfoPresenter.APPLY_INFO:
                ApplyInfoBean applyInfoBean = (ApplyInfoBean) message.obj;
                showInfo(applyInfoBean);
                break;
        }
    }

    private void showInfo(ApplyInfoBean applyInfoBean) {
        if (applyInfoBean == null) {
            return;
        }
        idStep.setCurrentIndex(U.checkStep(applyInfoBean));
        ApplyInfoBean.UserDTOBean userDTO = applyInfoBean.getUserDTO();
        if (!TextUtils.isEmpty(userDTO.getUserName())) {
            Log.e("name", userDTO.getUserName());
            idEditName.setText(RegexUtil.hideFirstName(userDTO.getUserName()));
            idEditIdcard.setText(RegexUtil.hideIDNormal(userDTO.getCertNo()));
            idBtnNextStep.setEnabled(true);
            commited = true;
        }
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
