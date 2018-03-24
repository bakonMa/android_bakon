package com.jht.doctor.ui.activity.mine.bankcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.AddCoborrowerBean;
import com.jht.doctor.ui.bean.ConfigBean;
import com.jht.doctor.ui.bean.ContributiveBean;
import com.jht.doctor.ui.contact.AddCoborrowerContact;
import com.jht.doctor.ui.presenter.AddCoborrowerPresenter;
import com.jht.doctor.utils.KeyBoardUtils;
import com.jht.doctor.utils.ScreenUtils;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.EditTextlayout;
import com.jht.doctor.widget.EditableLayout;
import com.jht.doctor.widget.dialog.HaveMoneyDialog;
import com.jht.doctor.widget.dialog.SelectBankCardPswDialog;
import com.jht.doctor.widget.popupwindow.AddressPopupView;
import com.jht.doctor.widget.popupwindow.OnePopupWheel;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddCoborrowerActivity extends BaseAppCompatActivity implements AddCoborrowerContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_edit_name)
    EditTextlayout idEditName;
    @BindView(R.id.id_edit_idcard)
    EditTextlayout idEditIdcard;
    @BindView(R.id.id_edit_phone)
    EditTextlayout idEditPhone;
    @BindView(R.id.id_edit_address)
    EditableLayout idEditAddress;
    @BindView(R.id.id_ed_address)
    EditText idEdAddress;
    @BindView(R.id.id_edit_relation)
    EditableLayout idEditRelation;
    @BindView(R.id.btn_comfirm)
    Button btnComfirm;


    @Inject
    AddCoborrowerPresenter mPresenter;

    private String mProvinceCode, mCityCode, mDistrictCode;

    private String relationCode;

    private List<ConfigBean.ConfigItem> relationList;//与主借人关系

    private AddressPopupView mAddressPopupView;

    private OnePopupWheel mPopupWheel;

    private String orderNo;
    private String platfirmId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_coborrower);
        ButterKnife.bind(this);
        relationList = U.getConfigData().RELATION;
        orderNo = getIntent().getStringExtra(MyBankCardActivity.ORDER_KEY);
        initToolbar();
        initEvent();

    }

    private void initEvent() {
        idEditPhone.setMaxLength(11);
        idEditName.getEditText().addTextChangedListener(textWatcherImp);
        idEditIdcard.getEditText().addTextChangedListener(textWatcherImp);
        idEditPhone.getEditText().addTextChangedListener(textWatcherImp);
        idEdAddress.addTextChangedListener(textWatcherImp);
        idEditAddress.getSelectTextView().addTextChangedListener(textWatcherImp);
        idEditRelation.getSelectTextView().addTextChangedListener(textWatcherImp);

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
            btnComfirm.setEnabled(idEditName.getEditText().getText().toString().length() > 0
                    && idEditIdcard.getEditText().getText().toString().length() > 0
                    && idEditPhone.getEditText().getText().toString().length() > 0
                    && idEdAddress.getText().toString().length() > 0
                    && idEditAddress.getText().length() > 0
                    && idEditRelation.getText().length() > 0);
        }
    };

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("添加共借人")
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

    @OnClick({R.id.id_edit_address, R.id.id_edit_relation, R.id.btn_comfirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_edit_address:
                //省市区
                KeyBoardUtils.hideKeyBoard(view, this);
                mAddressPopupView = new AddressPopupView(this, new AddressPopupView.ClickedListener() {
                    @Override
                    public void completeClicked(String... info) {
                        idEditAddress.setText(info[0] + "-" + info[2] + "-" + info[4]);
                        mProvinceCode = info[1];
                        mCityCode = info[3];
                        mDistrictCode = info[5];
                    }
                });
                mAddressPopupView.showAtLocation(this.findViewById(R.id.id_activity_add_coborrower), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                ScreenUtils.lightOff(this);
                break;
            case R.id.id_edit_relation:
                KeyBoardUtils.hideKeyBoard(view, this);
                mPopupWheel = new OnePopupWheel(this, U.configToStrs(relationList), new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        relationCode = relationList.get(position).itemVal;
                        idEditRelation.setText(relationList.get(position).colNameCn);
                    }
                });
                mPopupWheel.showAtLocation(this.findViewById(R.id.id_activity_add_coborrower), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
                ScreenUtils.lightOff(this);
                break;
            case R.id.btn_comfirm:
                mPresenter.addCoborrower(mDistrictCode, mCityCode, mProvinceCode,
                        idEdAddress.getText().toString(),
                        idEditIdcard.getEditText().getText().toString().trim(),
                        idEditPhone.getEditText().getText().toString().trim(),
                        idEditName.getEditText().getText().toString().trim(),
                        orderNo,
                        relationCode);
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        if (errorMsg.equals("共借人信息不能与主借人相同，请重新添加!")) {
            new HaveMoneyDialog(AddCoborrowerActivity.this, HaveMoneyDialog.NOT_SAME).show();
        } else {
            DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
        }
    }

    @Override
    public void onSuccess(Message message) {
        if (message != null) {
            switch (message.what) {
                case AddCoborrowerPresenter.ADD_COBORROWER:
                    AddCoborrowerBean bean = (AddCoborrowerBean) message.obj;
                    switch (bean.getNeedDebtorInfo()) {
                        case "02":
                            //不需要绑卡
                            DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("添加成功");
                            EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_BANKLIST));
                            finish();
                            break;
                        case "03":
                            //需要绑卡
                            EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_BANKLIST));
                            mPresenter.contributiveTypeJudge(idEditIdcard.getEditText().getText().toString().trim(),
                                    idEditPhone.getEditText().getText().toString().trim(),
                                    orderNo,
                                    idEditName.getEditText().getText().toString().trim());
                            break;
                    }
                    break;
                case AddCoborrowerPresenter.CONTRIBUTIVE:
                    //判断是否为银行存管 是否有卡
                    ContributiveBean contributiveBean = (ContributiveBean) message.obj;
                    if (contributiveBean.isContributiveTypeJudge()) {
                        platfirmId = contributiveBean.getOtherPlatformId();
                        //银行存管
                        if (contributiveBean.isHaveBankCard()) {
                            //银行存管下有卡
                            SelectBankCardPswDialog dialog = new SelectBankCardPswDialog(AddCoborrowerActivity.this, contributiveBean, clickListener);
                            dialog.show();
                        } else {
                            //银行存管下没有卡
                            jumpToBind();
                        }
                    } else {
                        //非银行存管
                        platfirmId = "";
                        jumpToBind();
                    }
                    break;
                case AddCoborrowerPresenter.ENSURE_CONTRIBUTION:
                    //确认使用存管下银行卡
                    EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_BANKLIST));
                    finish();
                    break;
            }
        }
    }

    private void jumpToBind(){
        Intent intent = new Intent(AddCoborrowerActivity.this, AddMainCardActivity.class);
        intent.putExtra(AddMainCardActivity.USER_NAME, idEditName.getEditText().getText().toString());
        intent.putExtra(AddMainCardActivity.ID_CARD, idEditIdcard.getEditText().getText().toString());
        intent.putExtra(AddMainCardActivity.PLATFORM_ID, platfirmId);
        intent.putExtra(MyBankCardActivity.ORDER_KEY, orderNo);
        intent.putExtra("type", "1");
        startActivity(intent);
        finish();
    }

    /**
     * 银行存管下确认使用dialog回调实现
     */
    private SelectBankCardPswDialog.ClickListener clickListener = new SelectBankCardPswDialog.ClickListener() {
        @Override
        public void confirmClicked(ContributiveBean contributiveBean) {
            //确认使用
            mPresenter.ensureBankCardOfCunGuan(contributiveBean.getBankCard(), contributiveBean.getBankMobile(),
                    contributiveBean.getBankName(), contributiveBean.getIdCard(), orderNo, contributiveBean.getOtherPlatformId(),
                    contributiveBean.getUserName(), "1");
        }

        @Override
        public void closed() {
            finish();
        }
    };

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }


    @Override
    public void ensureFailure(String errorMsg) {
        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
        finish();
    }
}
