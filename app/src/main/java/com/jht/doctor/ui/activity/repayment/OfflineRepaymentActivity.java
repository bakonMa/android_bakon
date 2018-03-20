package com.jht.doctor.ui.activity.repayment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.mine.LoanDetailActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.RepaymentOffLineBean;
import com.jht.doctor.ui.contact.RepaymentContact;
import com.jht.doctor.ui.presenter.RepaymentPresenter;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 线下还款详情 列表
 */
public class OfflineRepaymentActivity extends BaseAppCompatActivity implements RepaymentContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_recycleView)
    RecyclerView idRecycleView;
    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout refreshLayout;
    TextView tvTotalMoney;

    @Inject
    RepaymentPresenter mPresenter;

    private String orderNo;
    private int pageNum = 0;
    private double totalMoney = 0;
    private List<RepaymentOffLineBean.RepaymentInfoBean> beanList = new ArrayList<>();
    private BaseQuickAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline_repayment);
        ButterKnife.bind(this);
        initToolBar();
        initView();
        orderNo = getIntent().getStringExtra("orderNo");
        mPresenter.getOffLineRepayment(orderNo, pageNum);
    }

    private void initView() {
        refreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_main));
        //模拟自动刷新
//        refreshLayout.setRefreshing(true);

        idRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter(R.layout.item_offline_repayment, beanList) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                RepaymentOffLineBean.RepaymentInfoBean bean = (RepaymentOffLineBean.RepaymentInfoBean) item;
                helper.setText(R.id.tv_periods, bean.period + "/" + bean.totalPeriod)
                        .setText(R.id.tv_overdueStatus, bean.overdueStatus ? "待还款" : "已逾期")
                        .setTextColor(R.id.tv_overdueStatus, bean.overdueStatus ? getResources().getColor(R.color.color_trade_failure) : getResources().getColor(R.color.color_popup_btn))
                        .setText(R.id.tv_totalRepaymentAmt, RegexUtil.formatMoney(bean.totalRepaymentAmt) + "元")
                        .setText(R.id.tv_restReapaymenAmt, RegexUtil.formatMoney(bean.restReapaymenAmt) + "元")
                        .setText(R.id.tv_repaymentPrincipalAmt, RegexUtil.formatMoney(bean.repaymentPrincipalAmt) + "元")
                        .setText(R.id.tv_repaymentInterestAmt, RegexUtil.formatMoney(bean.repaymentInterestAmt) + "元")
                        .setText(R.id.tv_serviceAmt, RegexUtil.formatMoney(bean.serviceAmt) + "元")
                        .setText(R.id.tv_overdueAmt, RegexUtil.formatMoney(bean.overdueAmt) + "元")
                        .setText(R.id.tv_remissionAmt, RegexUtil.formatMoney(bean.remissionAmt) + "元")
                        .addOnClickListener(R.id.id_iv_what);
            }
        };
        //可以下拉加载更多
//        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getOffLineRepayment(orderNo, ++pageNum);
            }
        }, idRecycleView);
        View view = LayoutInflater.from(this).inflate(R.layout.header_offline_repayment, null);
        tvTotalMoney = view.findViewById(R.id.tv_totalMoney);
        adapter.addHeaderView(view);

        idRecycleView.setAdapter(adapter);
        idRecycleView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                showPopup(view);
            }
        });

        //下拉刷新
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 0;
                mPresenter.getOffLineRepayment(orderNo, pageNum);
            }
        });
    }

    private void initToolBar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setLeft(false)
                .setTitle("还款")
                .setRightText("借款详情", true, R.color.color_4f9ef3)
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
                        Intent intent = new Intent(provideContext(), LoanDetailActivity.class);
                        intent.putExtra(LoanDetailActivity.ORDER_KEY, orderNo);
                        startActivity(intent);
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
        if (refreshLayout.isRefreshing()) {
            refreshLayout.setRefreshing(false);
        }
        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null || message.obj == null) {
            if (refreshLayout.isRefreshing()) {
                refreshLayout.setRefreshing(false);
            }
            adapter.setEmptyView(R.layout.empty_view);
            adapter.setEnableLoadMore(false);
            return;
        }

        RepaymentOffLineBean bean = (RepaymentOffLineBean) message.obj;
        //总金额
        totalMoney = bean.currentRepaymentTotal;
        if (pageNum == 0) {
            //刷新
            refreshLayout.setRefreshing(false);
            tvTotalMoney.setText(RegexUtil.formatMoney(totalMoney));
            beanList.clear();

            //添加空白页
            if (bean.repaymentInfo.isEmpty()) {
                adapter.notifyDataSetChanged();
                adapter.setEmptyView(R.layout.empty_view);
                return;
            }
        }

        if (bean.repaymentInfo != null && !bean.repaymentInfo.isEmpty()) {
            beanList.addAll(bean.repaymentInfo);
            adapter.notifyDataSetChanged();
        }
        if (bean.repaymentInfo.size() < U.PAGE_SIZE) {
            adapter.loadMoreEnd();
        } else {
            adapter.loadMoreComplete();
        }
    }

    PopupWindow popupWindow;

    private void showPopup(View view) {
        if (popupWindow == null) {
            View showView = LayoutInflater.from(provideContext()).inflate(R.layout.popup_remission, null);
            popupWindow = new PopupWindow(showView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
        }

        popupWindow.showAsDropDown(view);
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
