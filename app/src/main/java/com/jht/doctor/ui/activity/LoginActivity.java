package com.jht.doctor.ui.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.config.PathConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.home.MainActivity;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.ui.bean.LoginResponse;
import com.jht.doctor.ui.contact.LoginContact;
import com.jht.doctor.ui.presenter.LoginPresenter;
import com.jht.doctor.utils.KeyBoardUtils;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Func1;

/**
 * mayakun 2017/11/15
 * 登录画面
 */
public class LoginActivity extends BaseActivity implements LoginContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.iv_phone_clean)
    ImageView ivPhoneClean;
    @BindView(R.id.et_code)
    EditText etCode;
    @BindView(R.id.iv_code_clean)
    ImageView ivCodeClean;
    @BindView(R.id.tv_sendcode)
    TextView tvSendcode;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.cb_agreement)
    CheckBox cbAgreement;
    @BindView(R.id.tv_agreement)
    TextView tvAgreement;

    @Inject
    LoginPresenter mPresenter;

    public static final String FROM_KEY = "from";
    public static final String TOKEN_OVERDUE = "token_overdue";
    public static final String PERSONAL_ACTIVITY = "personal_activity";
    public static final String HOMELOAN_ACTIVITY = "homeloan_activity";
    public static final String ORDER_ACTIVITY = "order_activity";

    private String from;

    private LoginResponse obj;

    /**
     * 验证码倒计时时间
     */
    private int time = 60;//每次验证请求需要间隔60S
    private Subscription subscription;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        initToolbar();
        initEvent();
    }

    private void initEvent() {
        //获取从哪个页面进入的登录页面
        if (getIntent() != null) {
            from = getIntent().getStringExtra(FROM_KEY);
        }
        cbAgreement.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                changeLoginStatus();
            }
        });
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("")
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
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @OnTextChanged(value = R.id.et_phone, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPhoneChanged(Editable s) {
        ivPhoneClean.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        changeLoginStatus();
    }

    @OnTextChanged(value = R.id.et_code, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterCodeChanged(Editable s) {
        ivCodeClean.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        changeLoginStatus();
    }

    //login btn 状态
    private void changeLoginStatus() {
        btnLogin.setEnabled((etPhone.getText().toString().trim().length() > 0
                && etCode.getText().toString().trim().length() > 0)
                && cbAgreement.isChecked());
    }

    @OnClick({R.id.iv_phone_clean, R.id.iv_code_clean, R.id.tv_sendcode, R.id.btn_login, R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_phone_clean:
                etPhone.setText("");
                break;
            case R.id.iv_code_clean:
                etCode.setText("");
                break;
            case R.id.tv_sendcode:
                //发生验证码接口
                mPresenter.sendVerifyCode(etPhone.getText().toString().trim());
                break;
            case R.id.tv_agreement:
                //进入协议
                Intent intent = new Intent(this, WebViewActivity.class);
                intent.putExtra("title", "服务协议");
                intent.putExtra("url", PathConfig.H5_REGISTERPROTOCOL);
                startActivity(intent);
                break;
            case R.id.btn_login:
                //登录接口
                if (!cbAgreement.isChecked()) {
                    DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("未同意协议");
                } else {
                    mPresenter.login(etPhone.getText().toString().trim(), etCode.getText().toString().trim());
                }
                break;
        }
    }

    private AlertDialog dialog;

    //dialog 邀请码
    private void showInvitationDialog() {
        dialog = new AlertDialog.Builder(this).create();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setBackgroundDrawableResource(android.R.color.transparent);
        dialog.show();
        window.setContentView(R.layout.dialog_login);
        window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

        EditText etCode = window.findViewById(R.id.et_invitation_code);
        window.findViewById(R.id.btn_no).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //todo 跳转
                handleLoginSuccess();
            }
        });
        window.findViewById(R.id.btn_ok).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (etCode.getText().length() >= 0) {
                    //todo 发送邀请码
                    mPresenter.bind(etCode.getText().toString());
                } else {
                    DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("请输入邀请码");
                }
            }
        });
    }

    public void timeDown() {
        subscription = Observable.interval(0, 1, TimeUnit.SECONDS)
                .take(time) //设置循环次数
                .map(new Func1<Long, Long>() {
                    @Override
                    public Long call(Long aLong) {
                        return time - aLong;
                    }
                })
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        tvSendcode.setEnabled(false);//不可点击
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        tvSendcode.setText("重新发送");//数据发送完后设置为原来的文字
                        tvSendcode.setEnabled(true);
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onNext(Long aLong) { //接受到一条就是会操作一次UI
                        tvSendcode.setText(aLong + "s");
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消订阅
        if (subscription != null) {
            subscription.unsubscribe();
        }
        mPresenter.unsubscribe();
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case LoginPresenter.SENDVERIFY_CODE:
                etCode.setFocusable(true);
                etCode.setFocusableInTouchMode(true);
                etCode.requestFocus();
                KeyBoardUtils.showKeyBoard(etCode, this);
                timeDown();
                break;
            case LoginPresenter.LOGIN_SUCCESS:
                obj = (LoginResponse) message.obj;
                if (((LoginResponse) message.obj).isIsFirstLogin()) {
                    showInvitationDialog();
                } else {
                    handleLoginSuccess();
                }
                break;
            case LoginPresenter.BIND:
                //绑定销售员
                dialog.dismiss();
                handleLoginSuccess();
                break;
        }
    }

    /**
     * 处理登录成功逻辑
     */
    public void handleLoginSuccess() {
        switch (from) {
            case TOKEN_OVERDUE:
                //todo 判断跳转到哪个页面
//                if (obj.getApplyJudgeDTO().isRepaymentStatus()) {
//                    startActivity(new Intent(this, HomeRepaymentActivity.class));
//                } else {
//                    startActivity(new Intent(this, HomeLoanActivity.class));
//                }
                startActivity(new Intent(this, MainActivity.class));
                finish();
                break;
            case PERSONAL_ACTIVITY:
//                //刷新申请主页跳转逻辑(不包含)
//                EventBusUtil.sendEvent(new Event<LoginResponse>(EventConfig.WHERE_TO_GO, obj));

                //刷新fragment
                refresh();
                finish();
                break;
            case HOMELOAN_ACTIVITY:
                //刷新申请主页跳转逻辑(包含是否弹出未完成订单dialog)
                EventBusUtil.sendEvent(new Event<LoginResponse>(EventConfig.REQUEST_HOMELOAN, obj));
                //刷新fragment
                refresh();
                finish();
                break;
            case ORDER_ACTIVITY:
                EventBusUtil.sendEvent(new Event<String>(EventConfig.CONTROL_FRAGMENT, "订单"));
                refresh();
                finish();
                break;
            default:
                finish();
                break;
        }
    }

    private void refresh() {
        EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_MAX_AMT));
        EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_PERSONAL));
        EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_ORDER));
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
    public void bindError(String errorMsg) {
        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }
}
