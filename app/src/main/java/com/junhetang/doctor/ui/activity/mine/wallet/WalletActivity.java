package com.junhetang.doctor.ui.activity.mine.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.WalletBean;
import com.junhetang.doctor.ui.contact.WalletContact;
import com.junhetang.doctor.ui.presenter.WalletPresenter;
import com.junhetang.doctor.utils.SoftHideKeyBoardUtil;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.bean.BankCardBean;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

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
    private ArrayList<BankCardBean> bankCardBeans;

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


    @OnClick({R.id.tv_hidemoney, R.id.tv_withdraw, R.id.rlt_addbankcard, R.id.rlt_deal, R.id.llt_bankcardinfo})
    public void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_hidemoney:
                tvHidemoney.setSelected(!tvHidemoney.isSelected());
                if (tvHidemoney.isSelected()) {
                    tvAllmoney.setText("******");
                } else {
                    tvAllmoney.setText(TextUtils.isEmpty(walletBean.remain) ? "0.00" : walletBean.remain);
                }
                break;
            case R.id.tv_withdraw:
                if (bankCardBeans == null || bankCardBeans.isEmpty()) {
                    CommonDialog commonDialog = new CommonDialog(this, false, "您尚未绑定银行卡，点击[确定]\n绑定银行卡",
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (view.getId() == R.id.btn_ok) {
                                        startActivity(new Intent(actContext(), AddBankCardActivity.class));
                                    }
                                }
                            });
                    commonDialog.show();
                    return;
                }
                Intent intent = new Intent(this, WithdrawActivity.class);
                intent.putExtra("bankcard", bankCardBeans.get(0));
                intent.putExtra("remain", walletBean.remain);
                startActivity(intent);
                break;
            case R.id.rlt_addbankcard:
                startActivity(new Intent(actContext(), AddBankCardActivity.class));
                break;
            case R.id.llt_bankcardinfo://我的银行卡
                startActivity(new Intent(actContext(), MyBankCardActivity.class));
                break;
            case R.id.rlt_deal:
                startActivity(new Intent(actContext(), DealDetailListActivity.class));
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
                //日期 2018年05月
                if (TextUtils.isEmpty(walletBean.nowtime)) {
                    tvYearmonth.setText("");
                } else {
                    String[] yearDate = walletBean.nowtime.split("/");
                    if (yearDate.length >= 1) {
                        tvYearmonth.setText(String.format("%s年%s月", yearDate[0], yearDate[1]));
                    } else {
                        tvYearmonth.setText(walletBean.nowtime);
                    }
                }

                tvAllmoney.setText(TextUtils.isEmpty(walletBean.remain) ? "0.00" : walletBean.remain);
                tvWenzhen.setText((TextUtils.isEmpty(walletBean.inquiry_money) ? "" : walletBean.inquiry_money) + "\n问诊收入");
                tvChufang.setText((TextUtils.isEmpty(walletBean.recipe_money) ? "" : walletBean.recipe_money) + "\n处方收入");
                tvJiangli.setText((TextUtils.isEmpty(walletBean.award) ? "" : walletBean.award) + "\n奖励收入");
                tvTixian.setText((TextUtils.isEmpty(walletBean.deposit) ? "" : walletBean.deposit) + "\n已提现 ");
                break;

            case WalletPresenter.GET_BANKCARD_OK://银行卡
                bankCardBeans = (ArrayList<BankCardBean>) message.obj;
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.EVENT_KEY_ADDBANKCARD_OK://添加银行卡成功
                case EventConfig.EVENT_KEY_DELBANKCARD_OK://删除银行卡成功
                    mPresenter.userBankList();
                    break;
                case EventConfig.EVENT_KEY_WITHRAW_OK://提现成功
                    mPresenter.getWallet();
//                    finish();
                    break;
            }
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
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
