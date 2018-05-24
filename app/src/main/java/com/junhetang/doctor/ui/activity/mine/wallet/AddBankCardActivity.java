package com.junhetang.doctor.ui.activity.mine.wallet;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.junhetang.doctor.R;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.ui.presenter.WalletPresenter;
import com.junhetang.doctor.widget.EditableLayout;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.BankTypeBean;
import com.junhetang.doctor.ui.contact.WalletContact;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.widget.EditTextlayout;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.popupwindow.OnePopupWheel;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * AddBankCardActivity  添加银行卡
 * Create at 2018/4/11 下午2:14 by mayakun
 */
public class AddBankCardActivity extends BaseActivity implements WalletContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.et_name)
    EditTextlayout etName;
    @BindView(R.id.et_bankname)
    EditableLayout etBankname;
    @BindView(R.id.et_cardnum)
    EditTextlayout etCardnum;
    @BindView(R.id.et_cardbankname)
    EditTextlayout etCardbankname;
    @BindView(R.id.llt_rootview)
    LinearLayout lltRootview;

    private List<BankTypeBean> bankTypeBeans;//银行类型
    private List<String> bankStrList = new ArrayList<>();//银行类型str
    @Inject
    WalletPresenter mPresenter;

    private int bankID;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_add_bankcard;
    }

    @Override
    protected void initView() {
        initToolbar();
        mPresenter.getBankType();
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("添加银行卡")
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

    @OnClick({R.id.et_bankname, R.id.tv_save})
    public void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.et_bankname:
                OnePopupWheel mPopupWheel = new OnePopupWheel(this, bankStrList, "请选择银行", new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        if (bankStrList.isEmpty()) {
                            ToastUtil.showShort("服务器异常，请返回重试");
                            return;
                        }
                        bankID = bankTypeBeans.get(position).id;
                        etBankname.setText(bankStrList.get(position));
                    }
                });
                mPopupWheel.show(lltRootview);
                break;
            case R.id.tv_save:
                //保存
                checkInput();
                break;
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    private void checkInput() {
        if (TextUtils.isEmpty(etName.getEditText().getText().toString().trim())) {
            ToastUtil.showShort("请填写持卡人姓名");
            return;
        }
        if (TextUtils.isEmpty(etBankname.getText())) {
            ToastUtil.showShort("请选择银行卡所属银行");
            return;
        }
        if (TextUtils.isEmpty(etCardnum.getEditText().getText().toString().trim())) {
            ToastUtil.showShort("请填写储蓄卡卡号");
            return;
        }
        if (TextUtils.isEmpty(etCardbankname.getEditText().getText().toString().trim())) {
            ToastUtil.showShort("请填写开户行名称");
            return;
        }
        mPresenter.addBankcard(etName.getEditText().getText().toString().trim(),
                bankID,
                etCardnum.getEditText().getText().toString().trim(),
                etCardbankname.getEditText().getText().toString().trim());
    }

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case WalletPresenter.GET_BANKTYPE_OK:
                bankTypeBeans = (List<BankTypeBean>) message.obj;
                for (BankTypeBean bean : bankTypeBeans) {
                    bankStrList.add(bean.ch_name);
                }
                break;
            case WalletPresenter.ADD_BANKCARD_OK:
                EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_ADDBANKCARD_OK));
                ToastUtil.showShort("添加银行卡成功");
                finish();
                break;
        }
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
