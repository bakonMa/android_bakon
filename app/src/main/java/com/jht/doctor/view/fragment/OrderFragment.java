package com.jht.doctor.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.config.OrderStatue;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.injection.components.DaggerFragmentComponent;
import com.jht.doctor.injection.modules.FragmentModule;
import com.jht.doctor.ui.activity.loan.LoanMoneyActivity;
import com.jht.doctor.ui.activity.mine.LoanDetailActivity;
import com.jht.doctor.ui.activity.mine.bankcard.MyBankCardActivity;
import com.jht.doctor.ui.activity.mine.webview.WebViewActivity;
import com.jht.doctor.ui.base.BaseAppCompatFragment;
import com.jht.doctor.ui.bean.MyLoanBean;
import com.jht.doctor.ui.contact.MyLoanContact;
import com.jht.doctor.ui.presenter.MyLoanPresenter;
import com.jht.doctor.utils.DensityUtils;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.utils.StringUtils;
import com.jht.doctor.widget.recycle_view.RecycleWithEmpty;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * Created by table on 2018/1/8.
 * description:
 */

public class OrderFragment extends BaseAppCompatFragment implements MyLoanContact.View {


    @BindView(R.id.id_ll_top)
    LinearLayout idLlTop;
    @BindView(R.id.id_recycle)
    RecycleWithEmpty idRecycle;
    @BindView(R.id.id_empty_view)
    RelativeLayout idEmptyView;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;

    @Inject
    MyLoanPresenter mPresenter;

    private BaseQuickAdapter mAdapter;

    private List<MyLoanBean> mData = new ArrayList<>();

    @Override
    protected View setViewId(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_loan, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initEvent();
    }


    /**
     * 设置item
     *
     * @param helper
     * @param bean
     * @param state
     * @param colorRes
     * @param isRepayment
     */
    public void setItem(BaseViewHolder helper, MyLoanBean bean, String state, int colorRes, boolean isRepayment) {
        if (isRepayment) {
            helper.setGone(R.id.id_rl_top2, true)
                    .setGone(R.id.id_rl_top1, false)
                    .setGone(R.id.id_rl_total, true)
                    .setText(R.id.id_tv_state2, state)
                    .setTextColor(R.id.id_tv_state2, getResources().getColor(colorRes))
                    .setText(R.id.id_tv_time2, bean.getCreateTime())
                    .setBackgroundRes(R.id.id_ll_main, R.drawable.shadow1)
                    .setText(R.id.id_tv_repayment, RegexUtil.formatMoney(bean.getCurrentRepaymentAmt()) + "元")
                    .setText(R.id.id_repayment_total, RegexUtil.formatMoney(bean.getRestRepaymentAmt()) + "元")
                    .setText(R.id.id_repayment_date, bean.getRepaymentDate());
        } else {
            helper.setGone(R.id.id_rl_top2, false)
                    .setGone(R.id.id_rl_top1, true)
                    .setGone(R.id.id_rl_total, false)
                    .setText(R.id.id_tv_state1, state)
                    .setTextColor(R.id.id_tv_state1, getResources().getColor(colorRes))
                    .setText(R.id.id_tv_time1, bean.getCreateTime())
                    .setBackgroundRes(R.id.id_ll_main, R.drawable.shadow2);
        }
    }

    private void initEvent() {
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        //自动刷新
        mAdapter = new BaseQuickAdapter(R.layout.item_my_loan_new, mData) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                MyLoanBean bean = (MyLoanBean) item;
                //公共部分
                helper.setText(R.id.id_tv_orderNo, bean.getOrderNo())
                        .setText(R.id.id_loan_money, RegexUtil.formatMoney(bean.getLoanAmt()) + "元")
                        .setText(R.id.id_loan_period, MessageFormat.format("{0}个月", bean.getPeriodNumber()));
                if (bean.isCancelStatus()) {
                    setItem(helper, bean, "已取消", R.color.color_666, false);
                } else {
                    switch (bean.getOrderStatus()) {
                        //2018年03月07日 mayakun添加 sta
                        case OrderStatue.PRE_REJECT:
                        case OrderStatue.APPLY_REJECT:
                            setItem(helper, bean, "已拒绝", R.color.color_999, false);
                            helper.setText(R.id.id_loan_money, "- -")
                                    .setText(R.id.id_loan_period, "- -");
                            break;
                        //2018年03月07日 mayakun添加 end
                        case OrderStatue.PRE_APPLY:
                            setItem(helper, bean, "待申请", R.color.color_4f9ef3, false);
                            helper.setText(R.id.id_loan_money, "- -")
                                    .setText(R.id.id_loan_period, "- -");
                            break;
                        case OrderStatue.ALREADY_APPLY:
                        case OrderStatue.ADD_INFO:
                        case OrderStatue.ADD_INFO_SUCCESS:
                            if (bean.isCreditStatus()) {
                                setItem(helper, bean, "审核中", R.color.tab_unselected, false);
                            } else {
                                setItem(helper, bean, "待征信验证", R.color.color_4f9ef3, false);

                            }
                            break;
                        case OrderStatue.TERMINAL_SUCCESS:
                            setItem(helper, bean, "待绑卡", R.color.color_4f9ef3, false);
                            break;
                        case OrderStatue.SIGN_SUCCESS:
                            setItem(helper, bean, "抵押", R.color.tab_unselected, false);
                            break;
                        case OrderStatue.PENDING_MONEY:
                            setItem(helper, bean, "待放款", R.color.tab_unselected, false);
                            break;
                        case OrderStatue.REPAYMENT:
                            if (bean.isOverdueStatus()) {
                                setItem(helper, bean, "已逾期", R.color.color_trade_failure, true);
                            } else {
                                setItem(helper, bean, "待还款", R.color.color_4f9ef3, true);
                            }
                            break;
                        case OrderStatue.END:
                            setItem(helper, bean, "已结清", R.color.color_666, false);
                            break;
                        case OrderStatue.SIGN_FAILURED:
                        case OrderStatue.SIGN_REFUSED:
                        case OrderStatue.TERMINAL_REFUSED:
                            setItem(helper, bean, "已拒绝", R.color.color_trade_failure, false);
                            break;
                    }
                }
            }
        };
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                String statue = mData.get(position).getOrderStatus();//订单状态
                boolean isCreditStatus = mData.get(position).isCreditStatus();//是否人行征信
                boolean isCancelStatus = mData.get(position).isCancelStatus();//是否已取消
                String orderNo = mData.get(position).getOrderNo();
                Intent intent = new Intent();
                if (isCancelStatus) {
                    //已取消  to借款详情 优先判断
                    intent.setClass(actContext(), LoanDetailActivity.class);
                    intent.putExtra(LoanDetailActivity.ORDER_KEY, orderNo);
                } else {
                    switch (statue) {
                        //2018年03月07日 mayakun添加 sta
                        case OrderStatue.PRE_REJECT://已拒绝
                        case OrderStatue.APPLY_REJECT:
                            intent.setClass(actContext(), LoanDetailActivity.class);
                            intent.putExtra(LoanDetailActivity.ORDER_KEY, orderNo);
                            break;
                        //2018年03月07日 mayakun添加 end
                        case OrderStatue.PRE_APPLY://待申请
                            intent.setClass(actContext(), LoanMoneyActivity.class);
                            intent.putExtra(LoanMoneyActivity.ORDER_NUMBER_KEY, orderNo);
                            intent.putExtra(LoanMoneyActivity.PRE_TRAIL_AMT, mData.get(position).getPreTrialAmt());
                            break;
                        case OrderStatue.ALREADY_APPLY:
                        case OrderStatue.ADD_INFO:
                        case OrderStatue.ADD_INFO_SUCCESS:
                            if (isCreditStatus) {//审核中
                                //todo to借款详情
                                intent.setClass(actContext(), LoanDetailActivity.class);
                                intent.putExtra(LoanDetailActivity.ORDER_KEY, orderNo);
                            } else {//人行征信
                                //todo to人行征信
                                intent.setClass(actContext(), WebViewActivity.class);
                                intent.putExtra("title", "征信报告查询");
                                intent.putExtra("orderNo", orderNo);
                            }
                            break;
                        case OrderStatue.TERMINAL_SUCCESS://待绑卡
                            //todo to绑卡页面
                            intent.putExtra(MyBankCardActivity.ORDER_KEY, orderNo);
                            intent.setClass(actContext(), MyBankCardActivity.class);
                            break;
                        case OrderStatue.SIGN_SUCCESS://抵押
                        case OrderStatue.PENDING_MONEY: //待放款
                        case OrderStatue.REPAYMENT://已逾期，已放款
                        case OrderStatue.END://已结清
                        case OrderStatue.SIGN_FAILURED:
                        case OrderStatue.SIGN_REFUSED:
                        case OrderStatue.TERMINAL_REFUSED://已拒绝
                            //todo to借款详情
                            intent.setClass(actContext(), LoanDetailActivity.class);
                            intent.putExtra(LoanDetailActivity.ORDER_KEY, orderNo);
                            break;
                    }
                }
                startActivity(intent);
            }
        });
        idRecycle.setAdapter(mAdapter);
        idRecycle.setLayoutManager(new LinearLayoutManager(actContext()));
        if (!StringUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, ""))) {
            mPresenter.getLoanList();
        }
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (!StringUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, ""))) {
                    mPresenter.getLoanList();
                } else {
                    idSwipe.setRefreshing(false);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    private void initView() {
        idLlTop.setPadding(0, DensityUtils.getStatusBarHeight(actContext()), 0, 0);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {

    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.REFRESH_ORDER:
                    mPresenter.getLoanList();
                    break;
            }

        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        idSwipe.setRefreshing(false);
        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        idSwipe.setRefreshing(false);
        if (message != null) {
            mData.clear();
            mData.addAll((List<MyLoanBean>) message.obj);
            if (mData.size() == 0) {
                idEmptyView.setVisibility(View.VISIBLE);
                idRecycle.setVisibility(View.GONE);
            } else {
                idEmptyView.setVisibility(View.GONE);
                idRecycle.setVisibility(View.VISIBLE);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public Activity provideContext() {
        return getActivity();
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }
}
