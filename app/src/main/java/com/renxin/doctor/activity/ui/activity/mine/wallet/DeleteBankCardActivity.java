package com.renxin.doctor.activity.ui.activity.mine.wallet;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.data.eventbus.Event;
import com.renxin.doctor.activity.data.eventbus.EventBusUtil;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean.BankCardBean;
import com.renxin.doctor.activity.ui.contact.WalletContact;
import com.renxin.doctor.activity.ui.presenter.present_jht.WalletPresenter;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.renxin.doctor.activity.config.EventConfig;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * DeleteBankCardActivity 删除银行卡
 * Create at 2018/4/11 下午5:05 by mayakun
 */
public class DeleteBankCardActivity extends BaseActivity implements WalletContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;

    @BindView(R.id.tv_bankname)
    TextView tvBankname;
    @BindView(R.id.tv_cardnum)
    TextView tvCardnum;

    @Inject
    WalletPresenter mPresenter;
    private BankCardBean bean;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_delete_bankcard;
    }

    @Override
    protected void initView() {
        initToolbar();
        bean = getIntent().getParcelableExtra("bankcard");
        if (bean == null) {
            ToastUtil.showShort("服务器异常，请返回重试");
            finish();
        }

        tvBankname.setText(TextUtils.isEmpty(bean.ch_name) ? "" : bean.ch_name);
        tvCardnum.setText(TextUtils.isEmpty(bean.bank_number) ? "" : bean.bank_number);
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("银行卡管理")
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

    @OnClick(R.id.tv_delete)
    void deleteOncick() {
        CommonDialog commonDialog = new CommonDialog(this, 1, "您确定删除该银行卡吗？", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_ok) {
                    mPresenter.deleteBankCard(bean.id);
                }
            }
        });
        commonDialog.show();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case WalletPresenter.DELETE_BANKCARD_OK:
                EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_DELBANKCARD_OK));
                ToastUtil.showShort("银行卡删除成功");
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
