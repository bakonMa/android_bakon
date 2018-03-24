package com.jht.doctor.ui.activity.loan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.manager.GreenDaoHelp;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.ApplyInfoBean;
import com.jht.doctor.ui.bean.ConfigBean;
import com.jht.doctor.ui.bean.HouseInfoResponse;
import com.jht.doctor.ui.contact.HouseInfoContact;
import com.jht.doctor.ui.presenter.HouseInfoPresenter;
import com.jht.doctor.utils.KeyBoardUtils;
import com.jht.doctor.utils.ScreenUtils;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.EditableLayout;
import com.jht.doctor.widget.ScheduleView;
import com.jht.doctor.widget.popupwindow.AddressPopupView;
import com.jht.doctor.widget.popupwindow.OnePopupWheel;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HouseInfoActivity extends BaseAppCompatActivity implements HouseInfoContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_ed_house_address)
    EditableLayout idEdHouseAddress;
    @BindView(R.id.id_ed_detail_address)
    EditText idEdDetailAddress;
    @BindView(R.id.id_rb_yes)
    RadioButton idRbYes;
    @BindView(R.id.id_rb_no)
    RadioButton idRbNo;
    @BindView(R.id.id_radio_group)
    RadioGroup idRadioGroup;
    @BindView(R.id.id_ed_loan_institution)
    EditableLayout idEdLoanInstitution;
    @BindView(R.id.id_ed_loan_money)
    EditableLayout idEdLoanMoney;
    @BindView(R.id.id_ll_loan)
    LinearLayout idLlLoan;
    @BindView(R.id.id_btn_next_step)
    TextView idBtnNextStep;
    @BindView(R.id.id_activity_house_info)
    LinearLayout idActivityHouseInfo;
    @BindView(R.id.id_ed_house_type)
    EditableLayout idEdHouseType;
    @BindView(R.id.id_ed_house_area)
    EditableLayout idEdHouseArea;
    @BindView(R.id.id_ed_house_name)
    EditableLayout idEdHouseName;
    @BindView(R.id.id_step)
    ScheduleView idStep;
    @BindView(R.id.id_tv_hasLoan)
    TextView idTvHasLoan;
    @BindView(R.id.id_rl_hasLoan)
    RelativeLayout idRlHasLoan;
    @BindView(R.id.id_ed_purchase_money)
    EditableLayout idEdPurchaseMoney;
    @BindView(R.id.id_ed_monthly_money)
    EditableLayout idEdMonthlyMoney;
    @BindView(R.id.id_ll_house)
    LinearLayout idLlHouse;

    private List<String> loanInstitution = new ArrayList<>(); //机构

    private List<ConfigBean.ConfigItem> houseTypeList; //房屋类型

    private String codeHouseType;

    private String mProvinceCode, mCityCode, mDistrictCode;

    private OnePopupWheel mPopupWheel;

    private boolean hasLoan;

    private AddressPopupView mAddressPopupView;

    @Inject
    HouseInfoPresenter mPresenter;

    final String POINT = ".";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_info);
        ButterKnife.bind(this);
        initToolBar();
        initEvent();
        requestData();
    }

    private void requestData() {
//        mPresenter.requestInfo();
        houseTypeList = U.getConfigData().HOUSE_TYPE;
    }

    private void initEvent() {
        //位数限制
        idEdHouseArea.setMaxLength(15);
        idEdHouseName.setMaxLength(15);
        idEdLoanMoney.setMaxLength(8);
        idEdPurchaseMoney.setMaxLength(4);
        idEdMonthlyMoney.setMaxLength(8);
        //按钮是否可点击判断
        idEdHouseAddress.getSelectTextView().addTextChangedListener(textWatcherImp);
        idEdDetailAddress.addTextChangedListener(textWatcherImp);
        idRadioGroup.setOnCheckedChangeListener(checkedChangedImp);
        idEdLoanInstitution.getSelectTextView().addTextChangedListener(textWatcherImp);
        idEdLoanMoney.getEditText().addTextChangedListener(textWatcherImp);
        idEdHouseType.getSelectTextView().addTextChangedListener(textWatcherImp);
        idEdHouseArea.getEditText().addTextChangedListener(textWatcherImp);
        idEdHouseName.getEditText().addTextChangedListener(textWatcherImp);
        idEdPurchaseMoney.getEditText().addTextChangedListener(textWatcherImp);
        //输入框自动带出单位
        idEdHouseArea.getEditText().setOnTouchListener(touchListenerImp);
        idEdHouseArea.getEditText().setOnFocusChangeListener(focusChangeListenerImp);

        idEdLoanMoney.getEditText().setOnTouchListener(touchListenerLoanImp);
        idEdLoanMoney.getEditText().setOnFocusChangeListener(focusChangeListenerLoanImp);

        idEdPurchaseMoney.getEditText().setOnTouchListener(touchListenerBuyImp);
        idEdPurchaseMoney.getEditText().setOnFocusChangeListener(focusChangeListenerBuyImp);

        idEdMonthlyMoney.getEditText().setOnTouchListener(touchListenerMonthImp);
        idEdMonthlyMoney.getEditText().setOnFocusChangeListener(focusChangeListenerMonthImp);
        loanInstitution.add("银行");
        loanInstitution.add("非银行");
    }


    private void initToolBar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("房产信息")
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

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    private RadioGroup.OnCheckedChangeListener checkedChangedImp = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            KeyBoardUtils.hideKeyBoard(radioGroup, HouseInfoActivity.this);
            switch (radioGroup.getCheckedRadioButtonId()) {
                case R.id.id_rb_yes:
                    hasLoan = true;
                    idLlLoan.setVisibility(View.VISIBLE);
                    checkClickable();
                    break;
                case R.id.id_rb_no:
                    hasLoan = false;
                    idLlLoan.setVisibility(View.GONE);
                    checkClickable();
                    break;
            }
        }
    };

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

    public void checkClickable() {
        if (hasLoan) {
            idBtnNextStep.setEnabled(idEdHouseAddress.getText().length() > 0
                    && idEdDetailAddress.getText().toString().length() > 0
                    && idEdLoanInstitution.getText().length() > 0
                    && idEdLoanMoney.getText().length() > 0
                    && idEdHouseName.getText().length() > 0
                    && idEdHouseArea.getText().length() > 0
                    && idEdHouseType.getText().length() > 0
                    && (idRbNo.isChecked() || idRbYes.isChecked())
                    && ((idEdHouseType.getText().equals("商铺") && idEdPurchaseMoney.getText().length() > 0)
                    || (!idEdHouseType.getText().equals("商铺"))));
        } else {
            idBtnNextStep.setEnabled(idEdHouseAddress.getText().length() > 0
                    && idEdDetailAddress.getText().toString().length() > 0
                    && idEdHouseName.getText().length() > 0
                    && idEdHouseArea.getText().length() > 0
                    && idEdHouseType.getText().length() > 0
                    && (idRbNo.isChecked() || idRbYes.isChecked())
                    && ((idEdHouseType.getText().equals("商铺") && idEdPurchaseMoney.getText().length() > 0)
                    || (!idEdHouseType.getText().equals("商铺"))));
        }
    }

    @OnClick({R.id.id_ed_house_address, R.id.id_ed_loan_institution, R.id.id_btn_next_step, R.id.id_ed_house_type})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_ed_house_address:
                //省市区
                KeyBoardUtils.hideKeyBoard(view, this);
                mAddressPopupView = new AddressPopupView(this, new AddressPopupView.ClickedListener() {

                    @Override
                    public void completeClicked(String... info) {
                        idEdHouseAddress.setText(info[0] + "-" + info[2] + "-" + info[4]);
                        mProvinceCode = info[1];
                        mCityCode = info[3];
                        mDistrictCode = info[5];
                    }

                });
                mAddressPopupView.showAtLocation(this.findViewById(R.id.id_activity_house_info), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                ScreenUtils.lightOff(this);
                break;
            case R.id.id_ed_loan_institution:

                KeyBoardUtils.hideKeyBoard(view, this);
                mPopupWheel = new OnePopupWheel(this, loanInstitution, new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        idEdLoanInstitution.setText(loanInstitution.get(position));
                    }
                });
                mPopupWheel.showAtLocation(this.findViewById(R.id.id_activity_house_info), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                ScreenUtils.lightOff(this);
                break;
            case R.id.id_ed_house_type:
                //房屋类型
                KeyBoardUtils.hideKeyBoard(view, this);
                mPopupWheel = new OnePopupWheel(this, U.configToStrs(houseTypeList), new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        codeHouseType = houseTypeList.get(position).itemVal;
                        idEdHouseType.setText(houseTypeList.get(position).colNameCn);
                        idLlHouse.setVisibility(idEdHouseType.getText().equals("商铺") ? View.VISIBLE : View.GONE);
                        checkClickable();
                    }
                });
                mPopupWheel.showAtLocation(this.findViewById(R.id.id_activity_house_info), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                ScreenUtils.lightOff(this);
                break;
            case R.id.id_btn_next_step:
                //提交
                String house = idEdHouseArea.getEditText().getText().toString().trim();
                if (house.startsWith(POINT)) {
                    house = "0" + house;
                }
                mPresenter.commitHouseInfo(codeHouseType, Double.parseDouble(house),
                        idEdHouseName.getText(), mProvinceCode, mCityCode, mDistrictCode, idEdDetailAddress.getText().toString(),
                        hasLoan ? "0" : "1", hasLoan ? ("银行".equals(idEdLoanInstitution.getText()) ? "01" : "02") : "",
                        hasLoan ? Double.parseDouble(idEdLoanMoney.getText().toString().trim()) : 0,
                        idEdHouseType.getText().equals("商铺") ? (Double.parseDouble(idEdPurchaseMoney.getText()) * 10000) : 0,
                        idEdHouseType.getText().equals("商铺") ? (idEdMonthlyMoney.getText().length() == 0 ? 0 : Double.parseDouble(idEdMonthlyMoney.getText())) : 0);

                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        switch (errorCode) {
            case HouseInfoPresenter.COMMIT_ERROR:
                //预审失败，房产信息已经提交
                DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
                EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_APPLY_INFO_BASIC));//刷新基本资料
                EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_APPLY_INFO_JOB));//刷新工作信息
                mPresenter.requestInfo();//自我刷新
                break;
            case HouseInfoPresenter.REQUEST_ERROR:
                DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
                break;
        }

    }

    @Override
    public void onSuccess(Message message) {
        if (message != null) {
            switch (message.what) {
                case HouseInfoPresenter.COMMIT_HOUSE_INFO:
                    EventBusUtil.sendEvent(new Event(EventConfig.FINISH_LOAN));//关闭前两个申请页面
                    //申请成功刷新订单列表
                    EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_ORDER));
                    Intent intent = new Intent(this, LoanMoneyActivity.class);
                    intent.putExtra(LoanMoneyActivity.ORDER_NUMBER_KEY, ((HouseInfoResponse) message.obj).getOrderNo());
                    intent.putExtra(LoanMoneyActivity.PRE_TRAIL_AMT, ((HouseInfoResponse) message.obj).getPreTrialAmt());
                    startActivity(intent);
                    finish();
                    break;
                case HouseInfoPresenter.APPLY_INFO:
                    EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_APPLY_INFO_BASIC));//刷新基本资料
                    EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_APPLY_INFO_JOB));//刷新工作信息
                    ApplyInfoBean applyInfoBean = (ApplyInfoBean) message.obj;
                    showInfo(applyInfoBean);
                    break;
            }
        }
    }

    private void showInfo(ApplyInfoBean applyInfoBean) {
        if (applyInfoBean == null) {
            return;
        }
        idStep.setCurrentIndex(U.checkStep(applyInfoBean));
        ApplyInfoBean.UserHouseDTOBean userHouseDTOBean = applyInfoBean.getUserHouseDTO();
        if (userHouseDTOBean != null) {
            codeHouseType = userHouseDTOBean.getHouseType();
            idEdHouseType.setText(U.keyToValue(codeHouseType, houseTypeList));
            idEdHouseArea.setText(userHouseDTOBean.getHouseArea() + "");
            idEdHouseArea.showUnit("平米");
            idEdHouseName.setText(userHouseDTOBean.getCommunityName());
            mProvinceCode = userHouseDTOBean.getProvinceCode();
            mCityCode = userHouseDTOBean.getCityCode();
            mDistrictCode = userHouseDTOBean.getAreaCode();
            idEdHouseAddress.setText(GreenDaoHelp.getLongCityName(mProvinceCode, mCityCode, mDistrictCode));
            idEdDetailAddress.setText(userHouseDTOBean.getDetailAddress());
            if ("0".equals(userHouseDTOBean.getHasLoan())) {
                idLlLoan.setVisibility(View.VISIBLE);
                idRbYes.setChecked(true);
                idRbNo.setChecked(false);
                idEdLoanInstitution.setText("01".equals(userHouseDTOBean.getLoanOrg()) ? "银行" : "非银行");
                idEdLoanMoney.setText((int) userHouseDTOBean.getLoanAmt() + "");
                idEdLoanMoney.showUnit("元");
            } else {
                idLlLoan.setVisibility(View.GONE);
                idRbYes.setChecked(false);
                idRbNo.setChecked(true);
            }
            if ("05".equals(codeHouseType)) {
                //商铺
                idLlHouse.setVisibility(View.VISIBLE);
                idEdPurchaseMoney.setText((int) (userHouseDTOBean.getTradingAmt() / 10000) + "");
                idEdPurchaseMoney.showUnit("万元");
                idEdMonthlyMoney.setText((int) userHouseDTOBean.getMonthRentalAmount() + "");
                idEdMonthlyMoney.showUnit("元");
            } else {
                //非商铺
                idLlHouse.setVisibility(View.GONE);
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

    /**
     * 住宅面积点击显示单位
     */
    private View.OnTouchListener touchListenerImp = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (idEdHouseArea.getEditText().getText().toString().length() == 0) {
                        idEdHouseArea.showUnit("平米");
                        idEdHouseArea.getEditText().setText("");
                        KeyBoardUtils.showKeyBoard(idEdHouseArea.getEditText(), HouseInfoActivity.this);
                    }
                    break;
            }
            return false;
        }
    };

    /**
     * 住宅面积失去焦点时无内容隐藏单位
     */
    private View.OnFocusChangeListener focusChangeListenerImp = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b && idEdHouseArea.getEditText().getText().toString().trim().length() == 0) {
                idEdHouseArea.getEditText().setText("");
                idEdHouseArea.hideUnit();
            }
        }
    };

    /**
     * 借款金额失去焦点时无内容隐藏单位
     */
    private View.OnFocusChangeListener focusChangeListenerLoanImp = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b && idEdLoanMoney.getEditText().getText().toString().trim().length() == 0) {
                idEdLoanMoney.getEditText().setText("");
                idEdLoanMoney.hideUnit();
            }
        }
    };

    /**
     * 借款金额点击显示单位
     */
    private View.OnTouchListener touchListenerLoanImp = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (idEdLoanMoney.getEditText().getText().toString().length() == 0) {
                        idEdLoanMoney.showUnit("元");
                        idEdLoanMoney.getEditText().setText("");
                        KeyBoardUtils.showKeyBoard(idEdLoanMoney.getEditText(), HouseInfoActivity.this);
                    }
                    break;
            }
            return false;
        }
    };

    /**
     * 购买金额失去焦点时无内容隐藏单位
     */
    private View.OnFocusChangeListener focusChangeListenerBuyImp = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b && idEdPurchaseMoney.getEditText().getText().toString().trim().length() == 0) {
                idEdPurchaseMoney.getEditText().setText("");
                idEdPurchaseMoney.hideUnit();
            }
        }
    };

    /**
     * 购买金额点击显示单位
     */
    private View.OnTouchListener touchListenerBuyImp = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (idEdPurchaseMoney.getEditText().getText().toString().length() == 0) {
                        idEdPurchaseMoney.showUnit("万元");
                        idEdPurchaseMoney.getEditText().setText("");
                        KeyBoardUtils.showKeyBoard(idEdPurchaseMoney.getEditText(), HouseInfoActivity.this);
                    }
                    break;
            }
            return false;
        }
    };

    /**
     * 月出租金额失去焦点时无内容隐藏单位
     */
    private View.OnFocusChangeListener focusChangeListenerMonthImp = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean b) {
            if (!b && idEdMonthlyMoney.getEditText().getText().toString().trim().length() == 0) {
                idEdMonthlyMoney.getEditText().setText("");
                idEdMonthlyMoney.hideUnit();
            }
        }
    };

    /**
     * 月出租金额点击显示单位
     */
    private View.OnTouchListener touchListenerMonthImp = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            switch (motionEvent.getAction()) {
                case MotionEvent.ACTION_UP:
                    if (idEdMonthlyMoney.getEditText().getText().toString().length() == 0) {
                        idEdMonthlyMoney.showUnit("元");
                        idEdMonthlyMoney.getEditText().setText("");
                        KeyBoardUtils.showKeyBoard(idEdMonthlyMoney.getEditText(), HouseInfoActivity.this);
                    }
                    break;
            }
            return false;
        }
    };

}



