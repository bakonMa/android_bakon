package com.jht.doctor.ui.activity.repayment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.config.PathConfig;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.mine.webview.WebViewActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.DealInfoBean;
import com.jht.doctor.ui.contact.RepaymentContact;
import com.jht.doctor.ui.presenter.RepaymentPresenter;
import com.jht.doctor.utils.DensityUtils;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.popupwindow.TradeTypePopup;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TradeDetailActivity extends BaseAppCompatActivity implements PopupWindow.OnDismissListener, RepaymentContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_recycleView)
    RecyclerView idRecycleView;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.id_acitivity_trade_detail)
    LinearLayout idAcitivityTradeDetail;

    @Inject
    RepaymentPresenter mPresenter;

    private BaseQuickAdapter adapter;
    private DealInfoBean dealInfoBean;
    private List<DealInfoBean.DealItemBean> beanList;

    private TradeTypePopup mPopupView;
    private ToolbarBuilder toolbarBuilder;
    private int pageNum = 1;
    private int type = 0;
    private int nameType;
    private String name, bankNum;
    private String platformUserNo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trade_detail);
        ButterKnife.bind(this);
        initToolbar();

        name = getIntent().getStringExtra("name");
        nameType = getIntent().getIntExtra("nameType", 0);
        bankNum = getIntent().getStringExtra("bankNum");
        platformUserNo = getIntent().getStringExtra("platformUserNo");

        initView();
        mPresenter.transactionDetails(platformUserNo, type, pageNum);
    }

    private void initView() {
        if (beanList == null) {
            beanList = new ArrayList<>();
        }
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        //模拟自动刷新
//        idSwipe.setRefreshing(true);

        idRecycleView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter(R.layout.item_trade_detail, beanList) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                DealInfoBean.DealItemBean bean = (DealInfoBean.DealItemBean) item;
                helper.setText(R.id.id_tv_date, bean.updateAt)
                        .setText(R.id.id_tv_type, U.RECHARGE_WITHRAW.get(bean.orderType))
                        .setText(R.id.id_tv_money, ("2001".equals(bean.orderType) ? "+" : "-") + RegexUtil.formatMoney(bean.amount) + "元")
                        .setText(R.id.id_tv_state, U.DEAL_STATUS.get(bean.resStatus))
                        .setTextColor(R.id.id_tv_state, getResources().getColor(U.DEAL_STATUS_COLOR.get(bean.resStatus)));
            }
        };

        View view = LayoutInflater.from(this).inflate(R.layout.header_trade_detail, null);
        ((TextView) view.findViewById(R.id.id_tv_name)).setText((nameType == 0 ? "主借人：" : "共借人：") + RegexUtil.hideFirstName(name));
        ((TextView) view.findViewById(R.id.id_tv_bank_card)).setText(TextUtils.isEmpty(bankNum) ? "" : RegexUtil.hideBankCardId(bankNum));
        adapter.addHeaderView(view);

        idRecycleView.setAdapter(adapter);

        //下拉刷新
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                mPresenter.transactionDetails(platformUserNo, type, pageNum);
            }
        });

        //可以下拉加载更多
        adapter.setEnableLoadMore(true);
        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.transactionDetails(platformUserNo, type, ++pageNum);
            }
        }, idRecycleView);
    }

    private void initToolbar() {
        toolbarBuilder = ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setClickTitle("交易明细")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setRightImg(R.drawable.icon_wenhao1, true)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        Intent intent = new Intent(provideContext(), WebViewActivity.class);
                        intent.putExtra("title", "常见问题");
                        intent.putExtra("url", PathConfig.H5_QUESTION);
                        startActivity(intent);
                    }

                    @Override
                    public void up() {
                        super.up();
                        hidePopup();
                    }

                    @Override
                    public void down() {
                        super.down();
                        showPopup();
                    }
                }).bind();
    }

    private void hidePopup() {
        if (mPopupView.isShowing()) {
            mPopupView.dismiss();
        }
    }

    //显示筛选
    private void showPopup() {
        if (mPopupView == null) {
            mPopupView = new TradeTypePopup(this, new TradeTypePopup.SelectTypeCallBack() {
                @Override
                public void selectType(int currentType) {
                    if (type == currentType) {
                        return;
                    }
                    pageNum = 1;
                    type = currentType;
                    if (!beanList.isEmpty()) {
                        idRecycleView.scrollToPosition(0);
                    }
                    idSwipe.setRefreshing(true);
                    mPresenter.transactionDetails(platformUserNo, type, pageNum);
                }
            });
            mPopupView.setOnDismissListener(this);
        }
        mPopupView.showAsDropDown(idToolbar, 0, DensityUtils.dp2Px(this, 1));
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
    public void onDismiss() {
        toolbarBuilder.upAnimation();
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        if (pageNum == 1) {
            idSwipe.setRefreshing(false);
        } else {
            --pageNum;
            adapter.loadMoreFail();
        }
        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        //刷新或下一页
        if (message.obj == null) {
            if (idSwipe.isRefreshing()) {
                idSwipe.setRefreshing(false);
            }
            if (beanList.isEmpty()) {
                adapter.setEmptyView(R.layout.empty_view);
                adapter.setEnableLoadMore(false);
            }
            return;
        }
        dealInfoBean = (DealInfoBean) message.obj;
        if (pageNum == 1) {
            //刷新
            idSwipe.setRefreshing(false);
            beanList.clear();
            adapter.notifyDataSetChanged();
            //添加空白页
            if (dealInfoBean.data == null || dealInfoBean.data.isEmpty()) {
                adapter.setEmptyView(R.layout.empty_view);
                return;
            }
        }

        if (dealInfoBean.data != null && !dealInfoBean.data.isEmpty()) {
            beanList.addAll(dealInfoBean.data);
            adapter.notifyDataSetChanged();
        }
        if (dealInfoBean.data.size() < U.PAGE_SIZE) {
            adapter.loadMoreEnd();
        } else {
            adapter.loadMoreComplete();
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
