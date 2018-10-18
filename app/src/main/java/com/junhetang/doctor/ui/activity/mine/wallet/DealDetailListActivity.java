package com.junhetang.doctor.ui.activity.mine.wallet;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.DealDetailBean;
import com.junhetang.doctor.ui.contact.WalletContact;
import com.junhetang.doctor.ui.presenter.WalletPresenter;
import com.junhetang.doctor.utils.Constant;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * GuildNewsListActivity 交易明细
 * Create at 2018/4/11 下午6:50 by mayakun
 */
public class DealDetailListActivity extends BaseActivity implements WalletContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @Inject
    WalletPresenter mPresenter;

    private List<DealDetailBean> dealDetailBeans = new ArrayList<>();
    private BaseQuickAdapter mAdapter;
    private int pageNum = 1;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_comm_recycleview;
    }

    @Override
    protected void initView() {
        initToolbar();

        //下拉刷新
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                pageNum = 1;
                mPresenter.getDealFlow(pageNum);
            }
        });

        recycleview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<DealDetailBean, BaseViewHolder>(R.layout.item_deal_detail, dealDetailBeans) {
            @Override
            protected void convert(BaseViewHolder helper, DealDetailBean item) {
                helper.setText(R.id.tv_dealtype, TextUtils.isEmpty(item.type) ? "" : item.type)
                        .setText(R.id.tv_patient_name, TextUtils.isEmpty(item.patient_name) ? "" : "就诊人：" + item.patient_name)
                        .setText(R.id.tv_date, TextUtils.isEmpty(item.deal_time) ? "" : item.deal_time);
                if (item.type_id == -1) {//提现
                    helper.setText(R.id.tv_patient_name, Constant.WITHDRAW_TYPE.get(item.status))
                            .setText(R.id.tv_money, TextUtils.isEmpty(item.money) ? "" : ((item.status == 1 || item.status == 2) ? "-" : "") + item.money)
                            .setTextColor(R.id.tv_patient_name, UIUtils.getColor(item.status == -1 ? R.color.red : R.color.color_main));
                    //拒绝受理-红色
                } else {
                    helper.setText(R.id.tv_patient_name, TextUtils.isEmpty(item.patient_name) ? "" : "就诊人：" + item.patient_name)
                            .setText(R.id.tv_money, TextUtils.isEmpty(item.money) ? "" : ("+" + item.money))
                            .setTextColor(R.id.tv_patient_name, UIUtils.getColor(R.color.color_000));
                }
            }
        };

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getDealFlow(pageNum + 1);
            }
        }, recycleview);

        recycleview.setAdapter(mAdapter);

        //请求数据
//        idSwipe.setRefreshing(true);
        mPresenter.getDealFlow(pageNum);
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("交易明细")
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
        if (message == null) {
            return;
        }
        switch (message.what) {
            case WalletPresenter.GET_DEAL_LIST_OK:
                if (idSwipe.isRefreshing()) {
                    idSwipe.setRefreshing(false);
                }

                BasePageBean<DealDetailBean> tempBean = (BasePageBean<DealDetailBean>) message.obj;
                if (tempBean != null && tempBean.list != null) {
                    pageNum = tempBean.page;
                    //第一页 情况
                    if (pageNum == 1) {
                        dealDetailBeans.clear();
                    }
                    dealDetailBeans.addAll(tempBean.list);
                    mAdapter.notifyDataSetChanged();

                    if (tempBean.is_last == 1) {//最后一页
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.loadMoreComplete();
                    }
                }

                if (dealDetailBeans.isEmpty()) {
                    mAdapter.setEmptyView(R.layout.empty_view);
                }

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
