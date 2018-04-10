package com.jht.doctor.ui.activity.mine.wallet;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.ui.bean.BankCardBean;
import com.jht.doctor.ui.bean_jht.WalletBean;
import com.jht.doctor.ui.contact.WalletContact;
import com.jht.doctor.ui.presenter.present_jht.WalletPresenter;
import com.jht.doctor.utils.SoftHideKeyBoardUtil;
import com.jht.doctor.widget.dialog.CommonDialog;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * WalletActivity 钱包
 * Create at 2018/4/10 上午11:56 by mayakun
 */
public class WalletActivity extends BaseActivity implements WalletContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.tv_allmoney)
    TextView tvAllmoney;
    @BindView(R.id.tv_hidemoney)
    TextView tvHidemoney;
    @BindView(R.id.rlt_addbankcard)
    RelativeLayout rltAddbankcard;
    @BindView(R.id.tv_bankname)
    TextView tvBankname;
    @BindView(R.id.tv_cardtype)
    TextView tvCardtype;
    @BindView(R.id.tv_cardnum)
    TextView tvCardnum;
    @BindView(R.id.llt_bankcardinfo)
    LinearLayout lltBankcardinfo;
    @BindView(R.id.tv_yearmonth)
    TextView tvYearmonth;
    @BindView(R.id.tv_guahao)
    TextView tvGuahao;
    @BindView(R.id.tv_wenzhen)
    TextView tvWenzhen;
    @BindView(R.id.tv_chufang)
    TextView tvChufang;
    @BindView(R.id.tv_jiangli)
    TextView tvJiangli;
    @BindView(R.id.tv_tixian)
    TextView tvTixian;

    @Inject
    WalletPresenter mPresenter;
    private WalletBean walletBean;
    private List<BankCardBean> bankCardBeans;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_wallet;
    }

    @Override
    protected void initView() {
        SoftHideKeyBoardUtil.assistActivity(this);
        initToolbar();
        mPresenter.getWallet();
        mPresenter.userBankList();
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("钱包")
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


    @OnClick({R.id.tv_hidemoney, R.id.tv_withdraw, R.id.rlt_addbankcard, R.id.rlt_deal})
    public void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hidemoney:
                break;
            case R.id.tv_withdraw:
                break;
            case R.id.rlt_addbankcard:
                break;
            case R.id.rlt_deal:
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

    @Override
    public void onError(String errorCode, String errorMsg) {
        CommonDialog commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
    }

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case WalletPresenter.GET_WALLET_OK:
                walletBean = (WalletBean) message.obj;
                if (walletBean == null) {
                    return;
                }
                tvYearmonth.setText(TextUtils.isEmpty(walletBean.nowtime) ? "" : walletBean.nowtime);
                tvAllmoney.setText(TextUtils.isEmpty(walletBean.remain) ? "0.00" : walletBean.remain);
                tvGuahao.setText((TextUtils.isEmpty(walletBean.registration_money) ? "" : walletBean.registration_money) + "\n(网)挂号收入");
                tvWenzhen.setText((TextUtils.isEmpty(walletBean.inquiry_money) ? "" : walletBean.inquiry_money) + "\n问诊收入");
                tvChufang.setText((TextUtils.isEmpty(walletBean.recipe_money) ? "" : walletBean.recipe_money) + "\n处方收入");
                tvJiangli.setText((TextUtils.isEmpty(walletBean.award) ? "" : walletBean.award) + "\n奖励收入");
                tvTixian.setText((TextUtils.isEmpty(walletBean.deposit) ? "" : walletBean.deposit) + "\n已提现 ");
                break;

            case WalletPresenter.GET_BANKCARD_OK://银行卡
                bankCardBeans = (List<BankCardBean>) message.obj;
                if (bankCardBeans.isEmpty()) {
                    rltAddbankcard.setVisibility(View.VISIBLE);
                    lltBankcardinfo.setVisibility(View.GONE);
                } else {
                    rltAddbankcard.setVisibility(View.GONE);
                    lltBankcardinfo.setVisibility(View.VISIBLE);
                    tvBankname.setText(TextUtils.isEmpty(bankCardBeans.get(0).ch_name) ? "" : bankCardBeans.get(0).ch_name);
                    tvCardnum.setText(TextUtils.isEmpty(bankCardBeans.get(0).bank_number) ? "" : bankCardBeans.get(0).bank_number);
                }
                break;

        }
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
