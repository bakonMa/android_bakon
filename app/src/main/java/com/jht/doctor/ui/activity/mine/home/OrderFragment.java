package com.jht.doctor.ui.activity.mine.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.SimpleClickListener;
import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.config.SessionMapKey;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.injection.components.DaggerFragmentComponent;
import com.jht.doctor.injection.modules.FragmentModule;
import com.jht.doctor.ui.activity.repayment.RechageActivity;
import com.jht.doctor.ui.activity.repayment.TradeDetailActivity;
import com.jht.doctor.ui.activity.repayment.WithdrawCashActivity;
import com.jht.doctor.ui.base.BaseAppCompatFragment;
import com.jht.doctor.ui.bean.MyAccountInfoBean;
import com.jht.doctor.ui.contact.RepaymentContact;
import com.jht.doctor.ui.presenter.RepaymentPresenter;
import com.jht.doctor.utils.RegexUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * 订单列表
 */
public class OrderFragment extends BaseAppCompatFragment implements RepaymentContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_recycleView)
    RecyclerView idRecycleView;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;

    TextView tvRemainMoney, tvTotalMoney;

    @Inject
    RepaymentPresenter mPresenter;
    private String orderNo;
    private List<MyAccountInfoBean.UserAccountInfoDTOBean> beanList = new ArrayList<>();
    private BaseQuickAdapter adapter;

    private MyAccountInfoBean myAccountBean;

    @Override
    protected void initData(Bundle savedInstanceState) {
        orderNo = getArguments().getString("orderNo");
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        //模拟自动刷新
//        idSwipe.setRefreshing(true);

        idRecycleView.setLayoutManager(new LinearLayoutManager(provideContext()));
        adapter = new BaseQuickAdapter(R.layout.item_my_account, beanList) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                MyAccountInfoBean.UserAccountInfoDTOBean bean = (MyAccountInfoBean.UserAccountInfoDTOBean) item;
                helper.setText(R.id.tv_userName, RegexUtil.hideFirstName(bean.getUserName()))
                        .setText(R.id.tv_userType, bean.getUserRole() == 0 ? "主借人：" : "共借人：")
                        .setText(R.id.tv_cardBalanceAmt, RegexUtil.formatMoney(bean.getAvailableCredit()))
                        .setText(R.id.tv_repaymentAmt, RegexUtil.formatMoney(bean.getTotalRepaymentAmt()))
                        .addOnClickListener(R.id.id_btn_detail)
                        .addOnClickListener(R.id.id_btn_get_money)
                        .addOnClickListener(R.id.id_btn_recharge);
            }
        };

        View view = LayoutInflater.from(provideContext()).inflate(R.layout.item_my_account_header, null);
        tvRemainMoney = view.findViewById(R.id.id_tv_remain_money);
        tvTotalMoney = view.findViewById(R.id.id_tv_total_money);
        adapter.addHeaderView(view);

        idRecycleView.setAdapter(adapter);

        idRecycleView.addOnItemTouchListener(new SimpleClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
            }

            @Override
            public void onItemLongClick(BaseQuickAdapter adapter, View view, int position) {
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent();
                switch (view.getId()) {
                    case R.id.id_btn_detail://明细
                        intent.setClass(provideContext(), TradeDetailActivity.class);
                        intent.putExtra("name", beanList.get(position).getUserName());
                        intent.putExtra("nameType", beanList.get(position).getUserRole());
                        intent.putExtra("bankNum", beanList.get(position).getUserBankNo());
                        intent.putExtra("platformUserNo", beanList.get(position).getPlatformUserNo());
                        startActivity(intent);
                        break;
                    case R.id.id_btn_get_money://提现
                        //提现成功后使用
                        CustomerApplication.getAppComponent().sessionMap().put(SessionMapKey.WITHDRAW_BEAN, beanList.get(position));
                        intent.setClass(provideContext(), WithdrawCashActivity.class);
                        intent.putExtra("name", beanList.get(position).getUserName());
                        intent.putExtra("idnum", beanList.get(position).getUserBankNo());
                        intent.putExtra("ownAmt", beanList.get(position).getAvailableCredit());
                        intent.putExtra("platformUserNo", beanList.get(position).getPlatformUserNo());
                        startActivity(intent);
                        break;
                    case R.id.id_btn_recharge://充值
                        //充值成功后使用
                        CustomerApplication.getAppComponent().sessionMap().put(SessionMapKey.RECHAGE_BEAN, beanList.get(position));
                        intent.setClass(provideContext(), RechageActivity.class);
                        intent.putExtra("name", beanList.get(position).getUserName());
                        intent.putExtra("idnum", beanList.get(position).getUserBankNo());
                        intent.putExtra("phone", beanList.get(position).getUserPhone());
                        intent.putExtra("platformUserNo", beanList.get(position).getPlatformUserNo());
                        startActivity(intent);
                        break;
                }
            }

            @Override
            public void onItemChildLongClick(BaseQuickAdapter adapter, View view, int position) {

            }
        });

        //下拉刷新
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getMyAccountRepayment(orderNo);
            }
        });


        mPresenter.getMyAccountRepayment(orderNo);
    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_my_account, null);
    }

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(CustomerApplication.getAppComponent())
                .build()
                .inject(this);
    }


    @Override
    public void onError(String errorCode, String errorMsg) {
        if (idSwipe.isRefreshing()) {
            idSwipe.setRefreshing(false);
        }
        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);

    }

    @Override
    public void onSuccess(Message message) {
        if (idSwipe.isRefreshing()) {
            idSwipe.setRefreshing(false);
        }

        if (message == null || message.obj == null) {
            adapter.setEmptyView(R.layout.empty_view);
            return;
        }

        myAccountBean = (MyAccountInfoBean) message.obj;
        //总金额
        tvTotalMoney.setText(RegexUtil.formatMoney(myAccountBean.getTotalRepayAmount()));
        tvRemainMoney.setText(RegexUtil.formatMoney(myAccountBean.getTotalAvailableCredit()));
        beanList.clear();

        if (myAccountBean.getUserAccountInfoDTO().isEmpty()) {
            adapter.setEmptyView(R.layout.empty_view);
            adapter.setEnableLoadMore(false);
        } else {
            beanList.addAll(myAccountBean.getUserAccountInfoDTO());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public Activity provideContext() {
        return getActivity();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    //充值，提现刷新
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                //充值，提现成功，刷新数据
                case EventConfig.RECHARGE_OK:
                case EventConfig.WITHDRAW_OK:
                    mPresenter.getMyAccountRepayment(orderNo);
                    break;
            }
        }
    }

    @Override
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }
}
