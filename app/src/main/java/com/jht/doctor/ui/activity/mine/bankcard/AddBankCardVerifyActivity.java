package com.jht.doctor.ui.activity.mine.bankcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.config.PathConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.mine.webview.WebViewActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.ApplyAuthorizationBean;
import com.jht.doctor.ui.bean.ApplyUserBean;
import com.jht.doctor.ui.bean.UserAuthorizationBO;
import com.jht.doctor.ui.contact.AddBankCardVerifyContact;
import com.jht.doctor.ui.presenter.AddBankCardVerifyPresenter;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;

public class AddBankCardVerifyActivity extends BaseAppCompatActivity implements AddBankCardVerifyContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.tv_sendcode)
    TextView tvSendcode;
    @BindView(R.id.cb_agreement)
    CheckBox cbAgreement;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;
    @BindView(R.id.btn_comfirm)
    Button btnComfirm;

    @Inject
    AddBankCardVerifyPresenter mPresenter;

    private String password;
    private ApplyAuthorizationBean applyAuthorizationBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank_card_verify);
        ButterKnife.bind(this);
        getData();
        initToolBar();
        initEvent();
    }

    /**
     * 从上个页面接受数据
     */
    private void getData() {
        password = getIntent().getStringExtra("password");
        applyAuthorizationBean = (ApplyAuthorizationBean) getIntent().getSerializableExtra("bean");
    }

    private void initEvent() {
        etCode.addTextChangedListener(textWatcherImp);
        //开始倒计时
        timeDown();
        tvPhone.setText(MessageFormat.format("验证码已发送至 {0}", RegexUtil.hidePhone(applyAuthorizationBean.getApplyAuthorizationDTO().getBankPhone())));
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
            btnComfirm.setEnabled(editable.length() == 6);
        }
    };

    private void initToolBar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("验证码")
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
                .applicationComponent(DocApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    @OnClick({R.id.tv_sendcode, R.id.btn_comfirm, R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_sendcode:
                mPresenter.addMainCard(applyAuthorizationBean.getApplyAuthorizationDTO().getOrderNo(),
                        applyAuthorizationBean.getApplyAuthorizationDTO().getBankCardNo(),
                        applyAuthorizationBean.getApplyAuthorizationDTO().getUserName(),
                        applyAuthorizationBean.getApplyAuthorizationDTO().getBankPhone(),
                        applyAuthorizationBean.getApplyAuthorizationDTO().getUserType(),
                        applyAuthorizationBean.getApplyAuthorizationDTO().getBankCardName(),
                        applyAuthorizationBean.getApplyAuthorizationDTO().getIdCard(),
                        password);
                break;
            case R.id.btn_comfirm:
                if (!cbAgreement.isChecked()) {
                    DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("请先同意《借款人服务协议》");
                } else {
                    UserAuthorizationBO userAuthorizationBO = new UserAuthorizationBO();
                    userAuthorizationBO.platformUserNo = applyAuthorizationBean.getLainlianDTO().getModel().getPlatformUserNo();
                    userAuthorizationBO.token = applyAuthorizationBean.getLainlianDTO().getModel().getToken();
                    userAuthorizationBO.verifyCode = etCode.getText().toString().trim();
                    mPresenter.userAuthorization(applyAuthorizationBean.getApplyAuthorizationDTO(), userAuthorizationBO);
                }
                break;
            case R.id.tv_agreement:
                //进入协议
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("title", "借款人服务协议");
                intent.putExtra("url", PathConfig.H5_BORROWERPROTOCOL);
                startActivity(intent);
                break;
        }
    }

    private int time = 60;
    private Subscription mSucsription;

    private void timeDown() {
        mSucsription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(60)
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return time - aLong;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        tvSendcode.setEnabled(false);
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Long>() {
                    @Override
                    public void call(Long aLong) {
                        tvSendcode.setText(aLong + "s");
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {

                    }
                }, new Action0() {
                    @Override
                    public void call() {
                        tvSendcode.setText("获取验证码");
                        tvSendcode.setEnabled(true);
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mSucsription != null) {
            mSucsription.unsubscribe();
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message != null) {
            switch (message.what) {
                case AddBankCardVerifyPresenter.APPLY_ATHORATION_ONLINE:
                    //开户：
                    tvPhone.setText(MessageFormat.format("验证码已发送至 {0}", RegexUtil.hidePhone(applyAuthorizationBean.getApplyAuthorizationDTO().getBankPhone())));
                    timeDown();
                    break;
                case AddBankCardVerifyPresenter.APPLY_USER:
                    ApplyUserBean bean = (ApplyUserBean) message.obj;
                    if ("200".equals(bean.getCode())) {
                        EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_BANKLIST));
                        finish();
                    }
                    break;
            }
        }
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
