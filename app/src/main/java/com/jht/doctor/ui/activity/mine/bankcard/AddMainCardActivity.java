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
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.ApplyAuthorizationBean;
import com.jht.doctor.ui.bean.BankBean;
import com.jht.doctor.ui.bean.OtherBean;
import com.jht.doctor.ui.contact.AddMainCardContact;
import com.jht.doctor.ui.presenter.AddMainCardPresenter;
import com.jht.doctor.utils.KeyBoardUtils;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.widget.EditTextlayout;
import com.jht.doctor.widget.dialog.NoSupportDialog;
import com.jht.doctor.widget.dialog.SetPasswordDialog;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddMainCardActivity extends BaseAppCompatActivity implements AddMainCardContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_edit_bankcard)
    EditTextlayout idEditBankcard;
    @BindView(R.id.id_tv_bankname)
    TextView idTvBankname;
    @BindView(R.id.id_edit_phone)
    EditTextlayout idEditPhone;
    @BindView(R.id.id_btn_confirm)
    TextView idBtnConfirm;
    @BindView(R.id.id_tv_name)
    TextView idTvName;
    @BindView(R.id.id_tv_idcard)
    TextView idTvIdcard;


    @Inject
    AddMainCardPresenter mPresenter;


    private boolean isGetted;//是否已经获取到银行卡名称

    public static final String USER_NAME = "name";
    public static final String ID_CARD = "idcard";
    public static final String PLATFORM_ID = "platform_id";

    private String userName;
    private String idCard;
    private String orderNo;
    private String password;
    private String type;
    private String platformId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_main_card);
        ButterKnife.bind(this);
        getData();
        initToolbar();
        initEvent();
    }

    private void getData() {
        if (getIntent().getStringExtra(USER_NAME) != null) {
            userName = getIntent().getStringExtra(USER_NAME);
            idTvName.setText(RegexUtil.hideFirstName(userName));
        }
        if (getIntent().getStringExtra(ID_CARD) != null) {
            idCard = getIntent().getStringExtra(ID_CARD);
            idTvIdcard.setText(RegexUtil.hideID(idCard));
        }
        if (getIntent().getStringExtra(MyBankCardActivity.ORDER_KEY) != null) {
            orderNo = getIntent().getStringExtra(MyBankCardActivity.ORDER_KEY);
        }
        if (getIntent().getStringExtra("type") != null) {
            type = getIntent().getStringExtra("type");
        }
        if (getIntent().getStringExtra(PLATFORM_ID) != null) {
            platformId = getIntent().getStringExtra(PLATFORM_ID);
        }
    }

    private void initEvent() {
        idEditBankcard.getEditText().addTextChangedListener(textWatcherImp);
        idEditPhone.getEditText().addTextChangedListener(textWatcherImp);
        idEditBankcard.getEditText().setHint("0".equals(type) ? "请输入主借人银行卡号" : "请输入共借人银行卡号");
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
            idBtnConfirm.setEnabled(idEditBankcard.getEditText().getText().toString().trim().length() > 0
                    && idEditPhone.getEditText().getText().toString().trim().length() > 0);
            if (!isGetted
                    && idEditBankcard.getEditText().getText().toString().trim().length() >= 6
                    && idEditBankcard.getEditText().getText().toString().trim().length() <= 10) {
                //未获取到银行卡号并且位数在6-10位,去请求银行卡号
                mPresenter.getBankName(idEditBankcard.getEditText().getText().toString().trim());
            }
            if (idEditBankcard.getEditText().getText().toString().trim().length() < 6) {
                isGetted = false;
            }
            if (idEditBankcard.getEditText().getText().toString().trim().length() == 0) {
                //银行卡号清空后，清空银行卡名
                isGetted = false;
                idTvBankname.setText("");
            }
        }
    };

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("0".equals(type) ? "添加主借人银行卡" : "添加共借人银行卡")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setRightText("支持银行", true, R.color.color_4f9ef3)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        //支持银行
                        Intent intent = new Intent(AddMainCardActivity.this, SupportBankActivity.class);
                        intent.putExtra("orderNo",orderNo);
                        startActivity(intent);
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

    @OnClick(R.id.id_btn_confirm)
    public void onViewClicked() {
        if (idTvBankname.getText().toString().length() == 0) {
            NoSupportDialog dialog = new NoSupportDialog(this,orderNo);
            dialog.show();
        } else if ("0".equals(type)) {
            //主借人需要判断是否有交易密码
            mPresenter.bindCardTradePwdStatus(orderNo,platformId);
        } else if ("1".equals(type)) {
            //共借人不需要判断
            mPresenter.addMainCard(orderNo,
                    idEditBankcard.getEditText().getText().toString().trim(),
                    userName,
                    idEditPhone.getEditText().getText().toString().trim(),
                    type,        //主借人为0、共借人为1
                    idTvBankname.getText().toString(),
                    idCard,
                    password = "");
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
                case AddMainCardPresenter.GET_BANK_NAME:
                    //获取银行卡名称
                    isGetted = true;
                    BankBean bankBean = (BankBean) message.obj;
                    idTvBankname.setText(bankBean.getBankName());
                    break;
                case AddMainCardPresenter.TRADE_PWD_STATUS:
                    //获取是否有交易密码
                    OtherBean bean = (OtherBean) message.obj;
                    if (bean.tradePwdStatus) {
                        //已经有交易密码
                        mPresenter.addMainCard(orderNo,
                                idEditBankcard.getEditText().getText().toString().trim(),
                                userName,
                                idEditPhone.getEditText().getText().toString().trim(),
                                type,        //主借人为0、共借人为1
                                idTvBankname.getText().toString(),
                                idCard,
                                password = "");        //有交易密码时，不需要传交易密码
                    } else {
                        SetPasswordDialog dialog = new SetPasswordDialog(this, new SetPasswordDialog.ClickListener() {
                            @Override
                            public void confirmClick(String psw) {
                                //没有交易密码
                                password = psw;
                                mPresenter.bindCardSetTradePwd(orderNo,platformId,psw);

                            }
                        });
                        dialog.show();
                        KeyBoardUtils.showKeyBoard(dialog.getPswView(),AddMainCardActivity.this);
                    }
                    break;
                case AddMainCardPresenter.SETPASSWORD:
                    mPresenter.addMainCard(orderNo,
                            idEditBankcard.getEditText().getText().toString().trim(),
                            userName,
                            idEditPhone.getEditText().getText().toString().trim(),
                            type,        //主借人为0、共借人为1
                            idTvBankname.getText().toString(),
                            idCard,
                            password);        //无交易密码时，需要传交易密码
                    break;
                case AddMainCardPresenter.APPLY_ATHORATION_ONLINE:
                    //申请开户线上成功
                    ApplyAuthorizationBean applyAuthorizationBean = (ApplyAuthorizationBean) message.obj;
                    Intent intent = new Intent(AddMainCardActivity.this, AddBankCardVerifyActivity.class);
                    intent.putExtra("password", "0".equals(type) ? password : "");
                    intent.putExtra("bean", applyAuthorizationBean);
                    startActivity(intent);
                    finish();
                    break;
                case AddMainCardPresenter.APPLY_ATHORATION_OFFLINE:
                    //申请开户线下成功
                    EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_BANKLIST));
                    finish();
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
