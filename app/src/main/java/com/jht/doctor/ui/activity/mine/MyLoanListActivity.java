package com.jht.doctor.ui.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.config.OrderStatue;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.loan.LoanMoneyActivity;
import com.jht.doctor.ui.activity.mine.bankcard.MyBankCardActivity;
import com.jht.doctor.ui.activity.mine.webview.WebViewActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.MyLoanBean;
import com.jht.doctor.ui.contact.MyLoanContact;
import com.jht.doctor.ui.presenter.MyLoanPresenter;
import com.jht.doctor.utils.RegexUtil;
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

/**
 * Created by table on 2017/12/4.
 * description:
 */

public class MyLoanListActivity extends BaseAppCompatActivity implements MyLoanContact.View {


    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_recycle)
    RecyclerView idRecycle;
    @BindView(R.id.id_empty_view)
    RelativeLayout idEmptyView;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;

    @Inject
    MyLoanPresenter mPresenter;

    private BaseQuickAdapter mAdapter;

    private List<MyLoanBean> mData = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_loan);
        ButterKnife.bind(this);
        initToolbar();
        initEvent();
    }

    /**
     * 设置item
     * @param helper
     * @param bean
     * @param state
     * @param colorRes
     * @param isRepayment
     */
    public void setItem(BaseViewHolder helper,MyLoanBean bean, String state, int colorRes, boolean isRepayment) {
        if (isRepayment) {
            helper.setVisible(R.id.id_rl_top2,true)
                    .setVisible(R.id.id_rl_top1,false)
                    .setVisible(R.id.id_rl_total,true)
                    .setText(R.id.id_tv_state2,state)
                    .setTextColor(R.id.id_tv_state2,colorRes)
                    .setText(R.id.id_tv_time2,bean.getCreateTime())
                    .setText(R.id.id_tv_repayment, RegexUtil.formatMoney(bean.getCurrentRepaymentAmt())+"元")
                    .setText(R.id.id_repayment_date,bean.getRepaymentDate());
        } else {
            helper.setVisible(R.id.id_rl_top2,false)
                    .setVisible(R.id.id_rl_top1,true)
                    .setVisible(R.id.id_rl_total,false)
                    .setText(R.id.id_tv_state1,state)
                    .setTextColor(R.id.id_tv_state1,colorRes)
                    .setText(R.id.id_tv_time1,bean.getCreateTime());
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
                    setItem(helper,bean,"已取消",R.color.color_666,false);
                } else {
                    switch (bean.getOrderStatus()) {
                        case OrderStatue.PRE_APPLY:
                            setItem(helper,bean,"待申请",R.color.color_4f9ef3,false);
                            helper.setText(R.id.id_loan_money, "- -")
                                    .setText(R.id.id_loan_period, "- -");
                            break;
                        case OrderStatue.ALREADY_APPLY:
                        case OrderStatue.ADD_INFO:
                        case OrderStatue.ADD_INFO_SUCCESS:
                            if(bean.isCreditStatus()){
                                setItem(helper,bean,"审核中",R.color.tab_unselected,false);
                            }else{
                                setItem(helper,bean,"待征信验证",R.color.color_4f9ef3,false);

                            }
                            break;
                        case OrderStatue.TERMINAL_SUCCESS:
                            setItem(helper,bean,"待绑卡",R.color.color_4f9ef3,false);
                            break;
                        case OrderStatue.SIGN_SUCCESS:
                            setItem(helper,bean,"抵押",R.color.tab_unselected,false);
                            break;
                        case OrderStatue.PENDING_MONEY:
                            setItem(helper,bean,"待放款",R.color.tab_unselected,false);
                            break;
                        case OrderStatue.REPAYMENT:
                            if(bean.isOverdueStatus()){
                                setItem(helper,bean,"已逾期",R.color.color_trade_failure,true);
                            }else {
                                setItem(helper,bean,"待还款",R.color.tab_unselected,true);
                            }
                            break;
                        case OrderStatue.END:
                            setItem(helper,bean,"已结清",R.color.color_666,false);
                            break;
                        case OrderStatue.SIGN_FAILURED:
                        case OrderStatue.SIGN_REFUSED:
                        case OrderStatue.TERMINAL_REFUSED:
                            setItem(helper,bean,"已拒绝",R.color.color_trade_failure,false);
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
                    intent.setClass(MyLoanListActivity.this, LoanDetailActivity.class);
                    intent.putExtra(LoanDetailActivity.ORDER_KEY, orderNo);
                } else {
                    switch (statue) {
                        case OrderStatue.PRE_APPLY://待申请
                            intent.setClass(MyLoanListActivity.this, LoanMoneyActivity.class);
                            intent.putExtra(LoanMoneyActivity.ORDER_NUMBER_KEY,orderNo);
                            intent.putExtra(LoanMoneyActivity.PRE_TRAIL_AMT,mData.get(position).getPreTrialAmt());
                            break;
                        case OrderStatue.ALREADY_APPLY:
                        case OrderStatue.ADD_INFO:
                        case OrderStatue.ADD_INFO_SUCCESS:
                            if (isCreditStatus) {//审核中
                                //todo to借款详情
                                intent.setClass(MyLoanListActivity.this, LoanDetailActivity.class);
                                intent.putExtra(LoanDetailActivity.ORDER_KEY, orderNo);
                            } else {//人行征信
                                //todo to人行征信
                                intent.setClass(MyLoanListActivity.this, WebViewActivity.class);
                                intent.putExtra("title", "征信报告查询");
                                intent.putExtra("orderNo", orderNo);
                            }
                            break;
                        case OrderStatue.TERMINAL_SUCCESS://待绑卡
                            //todo to绑卡页面
                            intent.putExtra(MyBankCardActivity.ORDER_KEY, orderNo);
                            intent.setClass(MyLoanListActivity.this, MyBankCardActivity.class);
                            break;
                        case OrderStatue.SIGN_SUCCESS://抵押
                        case OrderStatue.PENDING_MONEY: //待放款
                        case OrderStatue.REPAYMENT://已逾期，已放款
                        case OrderStatue.END://已结清
                        case OrderStatue.SIGN_FAILURED:
                        case OrderStatue.SIGN_REFUSED:
                        case OrderStatue.TERMINAL_REFUSED://已拒绝
                            //todo to借款详情
                            intent.setClass(MyLoanListActivity.this, LoanDetailActivity.class);
                            intent.putExtra(LoanDetailActivity.ORDER_KEY, orderNo);
                            break;
                    }
                }
                startActivity(intent);
            }
        });
        idRecycle.setAdapter(mAdapter);
        idRecycle.setLayoutManager(new LinearLayoutManager(this));
        mPresenter.getLoanList();
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getLoanList();
            }
        });
    }


    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setTitle("我的借款")
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
        idSwipe.setRefreshing(false);
        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
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
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
