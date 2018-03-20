package com.jht.doctor.ui.activity.mine.bankcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.repayment.MyAccountActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.BankCardBean;
import com.jht.doctor.ui.contact.BankCardSettingContact;
import com.jht.doctor.ui.presenter.BankCardSettingPresenter;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.widget.dialog.HaveBalanceDialog;
import com.jht.doctor.widget.dialog.HaveMoneyDialog;
import com.jht.doctor.widget.dialog.UnbindDialog;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BankCardSettingActivity extends BaseAppCompatActivity implements BankCardSettingContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_recycleView)
    RecyclerView idRecycleView;
    @BindView(R.id.id_empty)
    RelativeLayout idEmpty;


    @Inject
    BankCardSettingPresenter mPresenter;


    private BankCardBean bankCardBean;

    private List<BankCardBean.JointBean> jointBeans = new ArrayList<>();

    private View headerView;

    private String orderNo;

    private BaseQuickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bank_card_setting);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra(MyBankCardActivity.ORDER_KEY) != null) {
            orderNo = getIntent().getStringExtra(MyBankCardActivity.ORDER_KEY);
        }
        initToolbar();
        mPresenter.getBankList(orderNo);

    }

    private void setAdapter() {
        if (headerView == null) {
            headerView = LayoutInflater.from(this).inflate(R.layout.header_bankcard_setting, null);
        }
        if (adapter == null) {
            adapter = new BaseQuickAdapter(R.layout.item_bankcard_setting, jointBeans) {
                @Override
                protected void convert(BaseViewHolder helper, Object item) {
                    BankCardBean.JointBean bean = (BankCardBean.JointBean) item;
                    String bankNo = bean.getBankNo();
                    //是否有头部
                    boolean hasHead = !TextUtils.isEmpty(bankCardBean.getOwner().getBankNo());
                    helper.setGone(R.id.id_tv_head, helper.getLayoutPosition() == (hasHead ? 1 : 0) ? true : false)
                            .setText(R.id.id_tv_name, RegexUtil.hideFirstName(bean.getUserName()))
                            .setText(R.id.id_tv_bank, MessageFormat.format("{0}（尾号{1}）",
                                    bean.getBankName(), bankNo.substring(bankNo.length() - 3,
                                            bankNo.length())));
                    helper.getView(R.id.id_btn_unbind).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //解绑共借人银行卡
                            unbindCard(bankNo);
                        }
                    });
                }
            };
            if (!TextUtils.isEmpty(bankCardBean.getOwner().getBankNo())) {
                adapter.addHeaderView(headerView);
                setHeadData();
            }
            idRecycleView.setAdapter(adapter);
            idRecycleView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            adapter.removeAllHeaderView();
            if (!TextUtils.isEmpty(bankCardBean.getOwner().getBankNo())) {
                adapter.addHeaderView(headerView);
            }
            adapter.notifyDataSetChanged();
        }

    }

    /**
     * 设置主借人银行卡信息
     */
    private void setHeadData() {
        TextView tv_name = headerView.findViewById(R.id.id_tv_name);
        TextView tv_bank = headerView.findViewById(R.id.id_tv_bank);
        TextView btn_unbind = headerView.findViewById(R.id.id_btn_unbind);
        tv_name.setText(RegexUtil.hideFirstName(bankCardBean.getOwner().getUserName()));
        String bankNo = bankCardBean.getOwner().getBankNo();
        tv_bank.setText(MessageFormat.format("{0}（尾号{1}）",
                bankCardBean.getOwner().getBankName(), bankNo.substring(bankNo.length() - 3,
                        bankNo.length())));
        btn_unbind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //解绑主借人银行卡
                unbindCard(bankNo);
            }
        });
    }


    /**
     * 解绑
     *
     * @param cardNo
     */
    private void unbindCard(String cardNo) {
        UnbindDialog dialog = new UnbindDialog(BankCardSettingActivity.this, cardNo, new UnbindDialog.ClickListener() {
            @Override
            public void confirmClicked(String number) {
                mPresenter.unbind(number, orderNo);
            }
        });
        dialog.show();
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setTitle("银行卡")
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
                .applicationComponent(CustomerApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        switch (errorMsg) {
            case "您的账户还有在途资金请稍后再试":
                HaveMoneyDialog dialog = new HaveMoneyDialog(this, HaveMoneyDialog.HAVE_MONEY);
                dialog.show();
                break;
            case "您的账户还有余额，请提现后再试":
                HaveBalanceDialog haveBalanceDialog = new HaveBalanceDialog(this, HaveBalanceDialog.HAVE_BALANCE, new HaveBalanceDialog.ClickListener() {
                    @Override
                    public void confirm() {
                        Intent intent = new Intent(BankCardSettingActivity.this, MyAccountActivity.class);
                        intent.putExtra("orderNo", orderNo);
                        startActivity(intent);
                    }
                });
                haveBalanceDialog.show();
                break;
            default:
                CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
                break;
        }
    }

    @Override
    public void onSuccess(Message message) {
        if (message != null) {
            switch (message.what) {
                case BankCardSettingPresenter.GET_BANK_LIST:
                    bankCardBean = (BankCardBean) message.obj;
                    if ((bankCardBean.getOwner().getBankNo() == null || bankCardBean.getOwner().getBankNo().equals(""))
                            && ((bankCardBean.getJoint() == null || bankCardBean.getJoint().size() == 0)
                            || convert(bankCardBean.getJoint()).size() == 0)) {
                        //显示空
                        idEmpty.setVisibility(View.VISIBLE);
                        idRecycleView.setVisibility(View.GONE);
                    } else {
                        idEmpty.setVisibility(View.GONE);
                        idRecycleView.setVisibility(View.VISIBLE);
                        jointBeans.clear();
                        jointBeans.addAll(convert(bankCardBean.getJoint()));
                        setAdapter();
                    }
                    break;
                case BankCardSettingPresenter.UNBIND:
                    CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("解绑成功");
                    mPresenter.getBankList(orderNo);
                    EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_BANKLIST));
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

    private List<BankCardBean.JointBean> convert(List<BankCardBean.JointBean> beans) {
        List<BankCardBean.JointBean> jointBeanList = new ArrayList<>();
        for (BankCardBean.JointBean jointBean : beans) {
            if (!TextUtils.isEmpty(jointBean.getBankNo())) {
                jointBeanList.add(jointBean);
            }
        }
        return jointBeanList;
    }
}
