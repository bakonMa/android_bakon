package com.jht.doctor.ui.activity.mine.bankcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.ApplyAuthorizationBean;
import com.jht.doctor.ui.bean.ContributiveBean;
import com.jht.doctor.ui.bean.IfBankOfJointBean;
import com.jht.doctor.ui.bean.JudgeIfTiedBean;
import com.jht.doctor.ui.bean.WrapperBean;
import com.jht.doctor.ui.contact.CoborrowerHistoryContact;
import com.jht.doctor.ui.presenter.CoborrowerHistoryPresenter;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.widget.dialog.SelectBankCardDialog;
import com.jht.doctor.widget.dialog.SelectBankCardPswDialog;
import com.jht.doctor.widget.dialog.SelectMultiBankJointDialog;
import com.jht.doctor.widget.dialog.UnbindDialog;
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

public class CoborrowerHistoryActivity extends BaseAppCompatActivity implements CoborrowerHistoryContact.View {


    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_recycleView)
    RecyclerView idRecycleView;
    @BindView(R.id.btn_comfirm)
    Button btnComfirm;

    @Inject
    CoborrowerHistoryPresenter mPresenter;

    private BaseQuickAdapter adapter;

    private List<JudgeIfTiedBean.DataBean> mData;

    private JudgeIfTiedBean judgeIfTiedBean;

    private String isBank;

    private List<WrapperBean<JudgeIfTiedBean.DataBean>> wrapperBeans;

    private String orderNo;

    private int pos = 0;

    private String platfirmId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coborrower_history);
        ButterKnife.bind(this);
        initData();
        initToolbar();
        initEvent();
    }

    private void initData() {
        orderNo = getIntent().getStringExtra(MyBankCardActivity.ORDER_KEY);
        judgeIfTiedBean = (JudgeIfTiedBean) getIntent().getSerializableExtra("data");
        isBank = getIntent().getStringExtra("isBank");
        mData = judgeIfTiedBean.getData();
        wrapperBeans = wrapperBeans(mData);
    }

    private void initEvent() {
        adapter = new BaseQuickAdapter(R.layout.item_coborrower, wrapperBeans) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                WrapperBean<JudgeIfTiedBean.DataBean> wrapperBean = (WrapperBean<JudgeIfTiedBean.DataBean>) item;
                helper.setVisible(R.id.id_line, helper.getLayoutPosition() != wrapperBeans.size() - 1)
                        .setChecked(R.id.id_cb, wrapperBean.isChecked())
                        .setText(R.id.id_tv_name, RegexUtil.hideFirstName(wrapperBean.getT().getUserName()))
                        .setText(R.id.id_tv_idcard, RegexUtil.hideID(wrapperBean.getT().getIdCardNo()));
                CheckBox checkBox = helper.getView(R.id.id_cb);
                checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            int current = helper.getAdapterPosition();
                            if (pos != -1) {
                                wrapperBeans.get(pos).setChecked(false);
                            }
                            wrapperBeans.get(current).setChecked(true);
                            specialUpdate(current);
                        } else {
                            if (pos == helper.getAdapterPosition()) {
                                pos = -1;
                            }
                        }
                    }
                });
            }
        };
        idRecycleView.setAdapter(adapter);
        idRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void specialUpdate(int current) {
        Handler handler = new Handler();
        final Runnable r = new Runnable() {
            public void run() {
                adapter.notifyItemChanged(current);
                adapter.notifyItemChanged(pos);
                pos = current;
            }
        };
        handler.post(r);
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("历史共借人")
                .setLeft(false)
                .setRightText("添加共借人", true, R.color.color_4f9ef3)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        finish();
                        Intent intent = new Intent(CoborrowerHistoryActivity.this, AddCoborrowerActivity.class);
                        intent.putExtra(MyBankCardActivity.ORDER_KEY, orderNo);
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

    @OnClick(R.id.btn_comfirm)
    public void onViewClicked() {
        //确认使用历史共借人
        if (pos == -1) {
            DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("请选择你要使用的历史共借人");
        } else {
            JudgeIfTiedBean.DataBean bean = mData.get(pos);
            mPresenter.reusingBank(bean.getId(), bean.getIdCardNo(), bean.getOrderNo(),
                    orderNo, bean.getUserName());
        }
    }

    /**
     * 包装共借人信息list
     *
     * @param strs
     * @return
     */
    private List<WrapperBean<JudgeIfTiedBean.DataBean>> wrapperBeans(List<JudgeIfTiedBean.DataBean> strs) {
        List<WrapperBean<JudgeIfTiedBean.DataBean>> wrapperBeans = new ArrayList<>();
        for (JudgeIfTiedBean.DataBean dataBean : strs) {
            wrapperBeans.add(new WrapperBean<JudgeIfTiedBean.DataBean>(dataBean));
        }
        wrapperBeans.get(0).setChecked(true);
        return wrapperBeans;
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message != null) {
            switch (message.what) {
                case CoborrowerHistoryPresenter.REUSING_BANK:
                    if ("02".equals(isBank)) {
                        //复用历史共借人信息不需要绑卡
                        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("添加成功");
                        EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_BANKLIST));
                        finish();
                    } else if (("03").equals(isBank)) {
                        EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_BANKLIST));
                        JudgeIfTiedBean.DataBean bean = mData.get(pos);
                        mPresenter.contributiveTypeJudge(bean.getIdCardNo(), bean.getBankMobilePhone(), orderNo, bean.getUserName());
                    }
                    break;
                case CoborrowerHistoryPresenter.CONTRIBUTIVE:
                    //判断是否为银行存管 是否有卡 是否设置过交易密码
                    ContributiveBean contributiveBean = (ContributiveBean) message.obj;
                    JudgeIfTiedBean.DataBean dataBean = mData.get(pos);
                    if (contributiveBean.isContributiveTypeJudge()) {
                        platfirmId = contributiveBean.getOtherPlatformId();
                        //银行存管
                        if (contributiveBean.isHaveBankCard()) {
                            //银行存管下有卡
                            SelectBankCardPswDialog dialog = new SelectBankCardPswDialog(CoborrowerHistoryActivity.this, contributiveBean, clickListener);
                            dialog.show();
                        } else {
                            //银行存管下没有卡
                            mPresenter.ifBankJoint(dataBean.getIdCardNo(), orderNo, dataBean.getUserName());
                        }
                    } else {
                        //非银行存管
                        platfirmId = "";
                        mPresenter.ifBankJoint(dataBean.getIdCardNo(), orderNo, dataBean.getUserName());
                    }
                    break;
                case CoborrowerHistoryPresenter.UNBIND:
                    //解绑历史主借人银行卡成功
                    DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("解绑成功");
                    jumpToAddCard();
                    break;
                case CoborrowerHistoryPresenter.ENSURE_CARD:
                    //确认使用主借人历史银行卡
                    EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_BANKLIST));
                    finish();
                    break;
                case CoborrowerHistoryPresenter.ENSURE_CONTRIBUTION:
                    //确认使用存管下银行卡
                    EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_BANKLIST));
                    finish();
                    break;
                case CoborrowerHistoryPresenter.IF_BANK_JOINT:
                    IfBankOfJointBean ifBankOfJointBean = (IfBankOfJointBean) message.obj;
                    if (ifBankOfJointBean.isShowJointBanner() && ifBankOfJointBean.getData().size() > 0) {
                        if (ifBankOfJointBean.isShowBanner()) {
                            //有可复用的共借人银行卡
                            SelectBankCardDialog dialog = new SelectBankCardDialog(CoborrowerHistoryActivity.this, translate(ifBankOfJointBean.getData().get(0)), listenerImp);
                            dialog.show();
                        } else {
                            //平台银行卡有符合该资方要求的银行卡（新增 一个或多个银行卡）
                            SelectMultiBankJointDialog dialog = new SelectMultiBankJointDialog(CoborrowerHistoryActivity.this, ifBankOfJointBean.getData(), listenerMulti);
                            dialog.show();
                        }

                    } else {
                        //没有可复用的共借人银行卡
                        jumpToAddCard();
                    }
                    break;
                case CoborrowerHistoryPresenter.APPLY_ATHORATION_ONLINE:
                    //申请开户线上成功
                    ApplyAuthorizationBean applyAuthorizationBean = (ApplyAuthorizationBean) message.obj;
                    Intent intent = new Intent(CoborrowerHistoryActivity.this, AddBankCardVerifyActivity.class);
                    intent.putExtra("password", "");
                    intent.putExtra("bean", applyAuthorizationBean);
                    startActivity(intent);
                    finish();
                    break;
                case CoborrowerHistoryPresenter.APPLY_ATHORATION_OFFLINE:
                    //申请开户线下成功
                    EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_BANKLIST));
                    finish();
                    break;
            }
        }
    }

    /**
     * 共借人有多张银行卡确认使用
     */
    private SelectMultiBankJointDialog.ClickListener listenerMulti = new SelectMultiBankJointDialog.ClickListener() {
        @Override
        public void confirmClicked(IfBankOfJointBean.DataBean dataBean) {
            //确认使用 todo 去开户
            mPresenter.addMainCard(orderNo,
                    dataBean.getBankCardNo(),
                    dataBean.getUserName(),
                    dataBean.getBankMobilePhone(),
                    "1",
                    dataBean.getBankName(),
                    dataBean.getIdCardNo(),
                    "");
        }

        @Override
        public void addClicked() {
            //新增卡 todo 去新增银行卡
            jumpToAddCard();
        }
    };

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

        }
    };

    private void jumpToAddCard() {
        Intent intent = new Intent(CoborrowerHistoryActivity.this, AddMainCardActivity.class);
        JudgeIfTiedBean.DataBean dataBean = mData.get(pos);
        intent.putExtra(AddMainCardActivity.USER_NAME, dataBean.getUserName());
        intent.putExtra(AddMainCardActivity.ID_CARD, dataBean.getIdCardNo());
        intent.putExtra(MyBankCardActivity.ORDER_KEY, orderNo);
        intent.putExtra(AddMainCardActivity.PLATFORM_ID, platfirmId);
        intent.putExtra("type", "1");
        startActivity(intent);
        finish();
    }

    /**
     * 选择可复用共借人银行卡
     */
    private SelectBankCardDialog.ClickListener listenerImp = new SelectBankCardDialog.ClickListener() {
        @Override
        public void confirmClicked(String bankCardNo) {
            //确认使用
            mPresenter.ensureCard(bankCardNo, orderNo);
        }

        @Override
        public void unbindClicked(String bankCardNo) {
            //解绑
            unbindCard(bankCardNo);
        }
    };

    /**
     * 解绑确认弹出框
     *
     * @param cardNo
     */
    private void unbindCard(String cardNo) {
        UnbindDialog dialog = new UnbindDialog(CoborrowerHistoryActivity.this, cardNo, new UnbindDialog.ClickListener() {
            @Override
            public void confirmClicked(String number) {
                mPresenter.unbind(number, orderNo);
            }
        });
        dialog.show();
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }


    private JudgeIfTiedBean.DataBean translate(IfBankOfJointBean.DataBean bean) {
        JudgeIfTiedBean.DataBean dataBean = new JudgeIfTiedBean.DataBean();
        dataBean.setBankCardNo(bean.getBankCardNo() == null ? "" : bean.getBankCardNo());
        dataBean.setId(bean.getId());
        dataBean.setUserName(bean.getUserName() == null ? "" : bean.getUserName());
        dataBean.setBankName(bean.getBankName() == null ? "" : bean.getBankName());
        dataBean.setIdCardNo(bean.getIdCardNo() == null ? "" : bean.getIdCardNo());
        dataBean.setOrderNo(bean.getOrderNo() == null ? "" : bean.getOrderNo());
        return dataBean;
    }
}
