package com.jht.doctor.ui.activity.loan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.ConfigBean;
import com.jht.doctor.ui.contact.LoanMoneyContact;
import com.jht.doctor.ui.presenter.LoanMoneyPresenter;
import com.jht.doctor.utils.KeyBoardUtils;
import com.jht.doctor.utils.ScreenUtils;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.EditableLayout;
import com.jht.doctor.widget.dialog.AddedCoborrowDialog;
import com.jht.doctor.widget.popupwindow.OnePopupWheel;
import com.jht.doctor.widget.popupwindow.TwoPopupWheel;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoanMoneyActivity extends BaseAppCompatActivity implements LoanMoneyContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_tv_max_money)
    TextView idTvMaxMoney;
    @BindView(R.id.id_ed_loan_money)
    EditText idEdLoanMoney;
    @BindView(R.id.id_ed_loan_usage)
    EditableLayout idEdLoanUsage;
    @BindView(R.id.id_ed_repayment_method)
    EditableLayout idEdRepaymentMethod;
    @BindView(R.id.id_btn_apply)
    TextView idBtnApply;
    @BindView(R.id.id_activity_loan_money)
    LinearLayout idActivityLoanMoney;
    @BindView(R.id.id_tv_unit)
    TextView idTvUnit;
    @BindView(R.id.id_ll_edit)
    LinearLayout idLlEdit;

    @Inject
    LoanMoneyPresenter mPresenter;


    private List<ConfigBean.ConfigItem> loanUseList;//借款用途

    private List<ConfigBean.ConfigItem> repaymentTypeList;//还款方式

    private List<ConfigBean.ConfigItem> periodNumberList; // 还款期数

    private String codeLoanUse, codeRepaymentType, periodNumber;

    public static final String ORDER_NUMBER_KEY = "orderNo";
    public static final String PRE_TRAIL_AMT = "pre_trail_amt";
    private String orderNo;
    private int preMoney;

    private OnePopupWheel mPopupWheel;
    private TwoPopupWheel twoPopupWheel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_money);
        ButterKnife.bind(this);
        initToolBar();
        initEvent();
        initData();
    }

    private void initData() {
        if (getIntent().getStringExtra(ORDER_NUMBER_KEY) != null) {
            orderNo = getIntent().getStringExtra(ORDER_NUMBER_KEY);
        }
        //单位是万元，上个页面返回的单位是元
        preMoney = (int) (getIntent().getDoubleExtra(PRE_TRAIL_AMT, 0) / 10000);
        Log.e("pre",preMoney+"");
        idTvMaxMoney.setText(preMoney+"万元");
        loanUseList = U.getConfigData().LOAN_USER;
        repaymentTypeList = U.getConfigData().REPAYMENT_TYPE;
        periodNumberList = U.getConfigData().PERIOD_NUMBER;
    }

    private void initEvent() {
        idEdLoanMoney.addTextChangedListener(textWatcherImp);
        idEdLoanUsage.getSelectTextView().addTextChangedListener(textWatcherImp);
        idEdRepaymentMethod.getSelectTextView().addTextChangedListener(textWatcherImp);
        idEdLoanMoney.setOnFocusChangeListener(focusChangeListenerImp);
        idEdLoanMoney.setOnTouchListener(touchListenerImp);
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
            checkClickable();
        }
    };

    private View.OnTouchListener touchListenerImp = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (idEdLoanMoney.getText().toString().length() == 0) {
                        idTvUnit.setVisibility(View.VISIBLE);
                        idEdLoanMoney.setHint("");
                        idEdLoanMoney.setText(" ");
                    }
                    KeyBoardUtils.showKeyBoard(idEdLoanMoney, LoanMoneyActivity.this);
                    break;
            }
            return true;
        }
    };

    private View.OnFocusChangeListener focusChangeListenerImp = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b && idEdLoanMoney.getText().toString().trim().length() == 0) {
                idTvUnit.setVisibility(View.GONE);
            }
        }
    };

    public void checkClickable() {
        idBtnApply.setEnabled(idEdLoanMoney.getText().toString().trim().length() > 0
                && idEdLoanUsage.getText().length() > 0
                && idEdRepaymentMethod.getText().length() > 0);
    }

    private void initToolBar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setLeft(false)
                .setLeftImage(R.drawable.icon_back_1)
                .setStatuBar(R.color.color_main_blue)
                .setToolBarColor(R.color.color_main_blue)
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

    @OnClick({R.id.id_ed_loan_usage, R.id.id_ed_repayment_method, R.id.id_btn_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_ed_loan_usage:
                //借款用途
                KeyBoardUtils.hideKeyBoard(view, this);
                mPopupWheel = new OnePopupWheel(this, U.configToStrs(loanUseList), new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        codeLoanUse = loanUseList.get(position).itemVal;
                        idEdLoanUsage.setText(loanUseList.get(position).colNameCn);
                    }
                });
                mPopupWheel.showAtLocation(this.findViewById(R.id.id_activity_loan_money), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                ScreenUtils.lightOff(this);
                break;
            case R.id.id_ed_repayment_method:
                //还款方式
                KeyBoardUtils.hideKeyBoard(view, this);
                twoPopupWheel = new TwoPopupWheel(this, U.configToStrs(repaymentTypeList), U.configToStrs(periodNumberList), new TwoPopupWheel.ClickedListener() {
                    @Override
                    public void completeClicked(int pos1, int pos2) {
                        idEdRepaymentMethod.setText(repaymentTypeList.get(pos1).colNameCn + periodNumberList.get(pos2).colNameCn);
                        codeRepaymentType = repaymentTypeList.get(pos1).itemVal;
                        periodNumber = periodNumberList.get(pos2).itemVal;
                    }
                });
                twoPopupWheel.showAtLocation(this.findViewById(R.id.id_activity_loan_money), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                ScreenUtils.lightOff(this);
                break;
            case R.id.id_btn_apply:
                if (Integer.parseInt(idEdLoanMoney.getText().toString().trim()) < 20) {
                    CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("借款额度必须大于20万元，请重新填写");
                } else if (Integer.parseInt(idEdLoanMoney.getText().toString().trim()) > preMoney) {
                    CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("借款额度超出可申请的最高额度，请重新填写");
                } else {
                    mPresenter.loanMoney(Double.parseDouble(idEdLoanMoney.getText().toString().trim()) * 10000, codeLoanUse, orderNo,
                            Integer.parseInt(periodNumber), codeRepaymentType);
                }
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        if("USER_LORN_APPLY_REFUSE".equals(errorCode)){
            //订单已被拒绝或后台已取消
            AddedCoborrowDialog dialog = new AddedCoborrowDialog(LoanMoneyActivity.this, AddedCoborrowDialog.HAVE_REJECTED, new AddedCoborrowDialog.ConfirmListenter() {
                @Override
                public void confirmClicked() {
                    EventBusUtil.sendEvent(new Event<String>(EventConfig.CONTROL_FRAGMENT,"订单"));
                    EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_ORDER));
                    finish();
                }
            });
            dialog.show();
        }else {
            CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
        }
    }

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case LoanMoneyPresenter.LOAN_MONEY:
                //申请成功刷新订单列表
                EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_ORDER));
                Intent intent = new Intent(this, LoanApplyStateActivity.class);
                intent.putExtra("orderNo", orderNo);
                intent.putExtra("moneytype", idEdRepaymentMethod.getText().toString());
                intent.putExtra("money", Integer.parseInt(idEdLoanMoney.getText().toString().trim()));
                startActivity(intent);
                finish();
                break;
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
