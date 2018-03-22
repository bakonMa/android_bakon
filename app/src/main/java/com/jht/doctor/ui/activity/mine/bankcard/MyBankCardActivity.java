package com.jht.doctor.ui.activity.mine.bankcard;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.repayment.MyAccountActivity;
import com.jht.doctor.ui.adapter.BankCardAdapter;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.ApplyAuthorizationBean;
import com.jht.doctor.ui.bean.BankCardBean;
import com.jht.doctor.ui.bean.ContributiveBean;
import com.jht.doctor.ui.bean.IfBankOfJointBean;
import com.jht.doctor.ui.bean.JudgeIfTiedBean;
import com.jht.doctor.ui.contact.MyBankCardContact;
import com.jht.doctor.ui.presenter.MyBankCardPresenter;
import com.jht.doctor.utils.KeyBoardUtils;
import com.jht.doctor.widget.dialog.AddedCoborrowDialog;
import com.jht.doctor.widget.dialog.HaveBalanceDialog;
import com.jht.doctor.widget.dialog.HaveMoneyDialog;
import com.jht.doctor.widget.dialog.SelectBankCardDialog;
import com.jht.doctor.widget.dialog.SelectBankCardPswDialog;
import com.jht.doctor.widget.dialog.SelectMultiBankDialog;
import com.jht.doctor.widget.dialog.SelectMultiBankJointDialog;
import com.jht.doctor.widget.dialog.SetBankPasswordDialog;
import com.jht.doctor.widget.dialog.UnbindDialog;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MyBankCardActivity extends BaseAppCompatActivity implements MyBankCardContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_recycleView)
    RecyclerView idRecycleView;

    private BankCardAdapter mAdapter;

    private String orderNo;

    public static final String ORDER_KEY = "orderNo";

    private BankCardBean bankCardBean;

    private String mName, mIdCard;

    private String type;

    private ContributiveBean setPswContributiveBean;//设置交易密码并确认使用保存的实体

    private String otherPlatformId;//如果是银行存管下需要传otherPlatformId到绑卡页面

    @Inject
    MyBankCardPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bank_card);
        ButterKnife.bind(this);
        if (getIntent().getStringExtra(ORDER_KEY) != null) {
            orderNo = getIntent().getStringExtra(ORDER_KEY);
        }
        initToolbar();
        mPresenter.getBankList(orderNo);
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new BankCardAdapter(this, bankCardBean, clickListenerImp);
            idRecycleView.setLayoutManager(new LinearLayoutManager(this));
            idRecycleView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(bankCardBean);
            mAdapter.notifyDataSetChanged();
        }
    }

    private BankCardAdapter.ClickListener clickListenerImp = new BankCardAdapter.ClickListener() {
        @Override
        public void addMainCard() {
            //添加主借人银行卡
            type = "0";
            mName = bankCardBean.getOwner().getUserName();
            mIdCard = bankCardBean.getOwner().getUserIdCard();
            //判断是否为银行存管 是否有卡 是否设置过交易密码
            mPresenter.contributiveTypeJudge(mIdCard, bankCardBean.getOwner().getUserPhone(), orderNo, mName, type);
        }

        @Override
        public void addCoborrower() {
            //添加共借人
            if (TextUtils.isEmpty(bankCardBean.getOwner().getBankNo())) {
                //请先添加主借人银行卡
                AddedCoborrowDialog dialog = new AddedCoborrowDialog(MyBankCardActivity.this, AddedCoborrowDialog.PELEASE_ADD_MAIN_CARD, new AddedCoborrowDialog.ConfirmListenter() {
                    @Override
                    public void confirmClicked() {
                    //do nothing
                    }
                });
                dialog.show();
            } else if (bankCardBean.getOwner().getIsBank() != null && "01".equals(bankCardBean.getOwner().getIsBank())) {
                //不需要添加共借人
                DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("您无需添加共借人");
            } else {
                mPresenter.judgeIsTiedCard(orderNo, "1");//判断是否有历史共借人
            }
        }

        @Override
        public void addCoborrowerCard(int pos) {
            //添加共借人银行卡
            type = "1";
            mName = bankCardBean.getJoint().get(pos).getUserName();
            mIdCard = bankCardBean.getJoint().get(pos).getUserIdCard();
            mPresenter.contributiveTypeJudge(mIdCard, bankCardBean.getJoint().get(pos).getUserPhone(), orderNo, mName, type);
            //mPresenter.ifBankJoint(idCard, orderNo, name);
        }
    };

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("银行卡管理")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setRightText("编辑", true, R.color.color_4f9ef3)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        Intent intent = new Intent(MyBankCardActivity.this, BankCardSettingActivity.class);
                        intent.putExtra(ORDER_KEY, orderNo);
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
                        Intent intent = new Intent(MyBankCardActivity.this, MyAccountActivity.class);
                        intent.putExtra("orderNo", orderNo);
                        startActivity(intent);
                    }
                });
                haveBalanceDialog.show();
                break;
            default:
                DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
                break;
        }
    }

    /**
     * 选择可复用主借人银行卡
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
     * 主借人有多张银行卡确认使用
     */
    private SelectMultiBankDialog.ClickListener listenerMulti = new SelectMultiBankDialog.ClickListener() {
        @Override
        public void confirmClicked(JudgeIfTiedBean.DataBean dataBean) {
            //确认使用 todo 去开户
            mPresenter.addMainCard(orderNo,
                    dataBean.getBankCardNo(),
                    dataBean.getUserName(),
                    dataBean.getBankMobilePhone(),
                    type,        //主借人为0、共借人为1
                    dataBean.getBankName(),
                    dataBean.getIdCardNo(),
                    "");
        }

        @Override
        public void addClicked() {
            //新增卡 todo 去新增银行卡
            jumpToBind();
        }
    };

    /**
     * 共借人有多张银行卡确认使用
     */
    private SelectMultiBankJointDialog.ClickListener listenerMultiJoint = new SelectMultiBankJointDialog.ClickListener() {
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
            jumpToBind();
        }
    };


    /**
     * 解绑确认弹出框
     *
     * @param cardNo
     */
    private void unbindCard(String cardNo) {
        UnbindDialog dialog = new UnbindDialog(MyBankCardActivity.this, cardNo, new UnbindDialog.ClickListener() {
            @Override
            public void confirmClicked(String number) {
                mPresenter.unbind(number, orderNo);
            }
        });
        dialog.show();
    }

    @Override
    public void onSuccess(Message message) {
        if (message != null) {
            switch (message.what) {
                case MyBankCardPresenter.GET_BANK_LIST:
                    bankCardBean = (BankCardBean) message.obj;
                    setAdapter();
                    break;
                case MyBankCardPresenter.JUDGE_IS_TIED_MAIN:
                    //主借人是否绑过卡
                    JudgeIfTiedBean bean1 = (JudgeIfTiedBean) message.obj;
                    if (bean1.isShowOwnerBanner() && bean1.getData().size() > 0) {
                        if (bean1.isShowBanner()) {
                            //主借人之前绑过卡,显示绑卡列表(解绑 单个银行卡)
                            SelectBankCardDialog dialog = new SelectBankCardDialog(MyBankCardActivity.this, bean1.getData().get(0), listenerImp);
                            dialog.show();
                        } else {
                            //平台银行卡有符合该资方要求的银行卡（新增 一个或多个银行卡）
                            SelectMultiBankDialog dialog = new SelectMultiBankDialog(MyBankCardActivity.this, bean1.getData(), listenerMulti);
                            dialog.show();
                        }
                    } else {
                        //主借人未绑过卡
                        jumpToBind();
                    }
                    break;
                case MyBankCardPresenter.JUDGE_IS_TIED_JOINT:
                    //是否有历史共借人
                    JudgeIfTiedBean bean2 = (JudgeIfTiedBean) message.obj;
                    if (bean2.getData() != null && bean2.getData().size() > 0) {
                        //有历史共借人
                        Intent intent = new Intent(MyBankCardActivity.this, CoborrowerHistoryActivity.class);
                        intent.putExtra(ORDER_KEY, orderNo);
                        intent.putExtra("isBank", bankCardBean.getOwner().getIsBank());
                        intent.putExtra("data", bean2);//共借人信息
                        startActivity(intent);

                    } else {
                        //没有历史共借人
                        Intent intent = new Intent(MyBankCardActivity.this, AddCoborrowerActivity.class);
                        intent.putExtra(ORDER_KEY, orderNo);
                        startActivity(intent);
                    }
                    break;
                case MyBankCardPresenter.UNBIND:
                    //解绑历史主借人银行卡成功
                    DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("解绑成功");
                    jumpToBind();
                    break;
                case MyBankCardPresenter.ENSURE_CARD:
                    //确认使用历史银行卡
                    mPresenter.getBankList(orderNo);
                    break;
                case MyBankCardPresenter.IF_BANK_JOINT:
                    //是否有可复用的共借人银行卡
                    IfBankOfJointBean ifBankOfJointBean = (IfBankOfJointBean) message.obj;
                    if (ifBankOfJointBean.isShowJointBanner() && ifBankOfJointBean.getData().size() > 0) {
                        if (ifBankOfJointBean.isShowBanner()) {
                            //有可复用的共借人银行卡
                            SelectBankCardDialog dialog = new SelectBankCardDialog(MyBankCardActivity.this, translate(ifBankOfJointBean.getData().get(0)), listenerImp);
                            dialog.show();
                        } else {
                            //平台银行卡有符合该资方要求的银行卡（新增 一个或多个银行卡）
                            SelectMultiBankJointDialog dialog = new SelectMultiBankJointDialog(MyBankCardActivity.this, ifBankOfJointBean.getData(), listenerMultiJoint);
                            dialog.show();
                        }

                    } else {
                        //没有可复用的共借人银行卡
                        jumpToBind();
                    }
                    break;
                case MyBankCardPresenter.CONTRIBUTIVE:
                    //主借人判断是否为银行存管 是否有卡 是否设置过交易密码
                    ContributiveBean contributiveBean = (ContributiveBean) message.obj;
                    if (contributiveBean.isContributiveTypeJudge()) {
                        //银行存管
                        otherPlatformId = contributiveBean.getOtherPlatformId();
                        if (contributiveBean.isHaveBankCard()) {
                            //银行存管下有卡
                            if (contributiveBean.isTradePwdStatus()) {
                                //设置过交易密码
                                SelectBankCardPswDialog dialog = new SelectBankCardPswDialog(MyBankCardActivity.this, contributiveBean, clickListener);
                                dialog.show();
                            } else {
                                //没有设置过交易密码
                                SetBankPasswordDialog dialog = new SetBankPasswordDialog(MyBankCardActivity.this, contributiveBean, clickListenerPsw);
                                dialog.show();
                                KeyBoardUtils.showKeyBoard(dialog.getPswView(), MyBankCardActivity.this);
                            }
                        } else {
                            //银行存管下没有卡，判断平台是否有卡
                            mPresenter.judgeIsTiedCard(orderNo, "0");
                        }
                    } else {
                        //非银行存管
                        otherPlatformId = "";
                        mPresenter.judgeIsTiedCard(orderNo, "0");//判断主借人是否绑卡
                    }
                    break;
                case MyBankCardPresenter.CONTRIBUTIVE_JOINT:
                    //共借人添加卡时判断是否是银行存管
                    ContributiveBean contributiveBean_joint = (ContributiveBean) message.obj;
                    if (contributiveBean_joint.isContributiveTypeJudge()) {
                        //银行存管
                        otherPlatformId = contributiveBean_joint.getOtherPlatformId();
                        if (contributiveBean_joint.isHaveBankCard()) {
                            //银行存管下有卡
                            SelectBankCardPswDialog dialog = new SelectBankCardPswDialog(MyBankCardActivity.this, contributiveBean_joint, clickListener);
                            dialog.show();
                        } else {
                            //银行存管下没有卡，判断平台是否有卡
                            mPresenter.ifBankJoint(mIdCard, orderNo, mName);
                        }
                    } else {
                        //非银行存管
                        otherPlatformId = "";
                        mPresenter.ifBankJoint(mIdCard, orderNo, mName);
                    }
                    break;
                case MyBankCardPresenter.ENSURE_CONTRIBUTION:
                    //确认使用存管下银行卡
                    mPresenter.getBankList(orderNo);
                    break;

                case MyBankCardPresenter.SETPASSWORD:
                    //绑卡时设置交易密码
                    mPresenter.ensureBankCardOfCunGuan(setPswContributiveBean.getBankCard(), setPswContributiveBean.getBankMobile(),
                            setPswContributiveBean.getBankName(), setPswContributiveBean.getIdCard(), orderNo, setPswContributiveBean.getOtherPlatformId(),
                            setPswContributiveBean.getUserName(), type);
                    break;
                case MyBankCardPresenter.APPLY_ATHORATION_ONLINE:
                    //申请开户线上成功
                    ApplyAuthorizationBean applyAuthorizationBean = (ApplyAuthorizationBean) message.obj;
                    Intent intent = new Intent(MyBankCardActivity.this, AddBankCardVerifyActivity.class);
                    intent.putExtra("password", "");
                    intent.putExtra("bean", applyAuthorizationBean);
                    startActivity(intent);
                    break;
                case MyBankCardPresenter.APPLY_ATHORATION_OFFLINE:
                    //申请开户线下成功
                    mPresenter.getBankList(orderNo);
                    break;
            }
        }
    }

    private SetBankPasswordDialog.ClickListener clickListenerPsw = new SetBankPasswordDialog.ClickListener() {
        @Override
        public void confirmClicked(ContributiveBean contributiveBean, String password) {
            //设置交易密码并确认使用
            setPswContributiveBean = contributiveBean;
            mPresenter.bindCardSetTradePwd(orderNo, contributiveBean.getOtherPlatformId(), password);
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
                    contributiveBean.getUserName(), type);
        }
        @Override
        public void closed() {

        }
    };


    /**
     * 跳转到绑卡界面
     */
    private void jumpToBind() {
        Intent intent = new Intent(MyBankCardActivity.this, AddMainCardActivity.class);
        intent.putExtra(AddMainCardActivity.USER_NAME, mName);
        intent.putExtra(AddMainCardActivity.ID_CARD, mIdCard);
        intent.putExtra(MyBankCardActivity.ORDER_KEY, orderNo);
        intent.putExtra(AddMainCardActivity.PLATFORM_ID, otherPlatformId);
        intent.putExtra("type", type);
        startActivity(intent);
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventCome(Event event) {
        if (event != null) {
            if (event.getCode() == EventConfig.REFRESH_BANKLIST) {
                mPresenter.getBankList(orderNo);
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

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
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
