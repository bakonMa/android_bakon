package com.jht.doctor.ui.activity.mine;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.config.OrderStatue;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.mine.bankcard.MyBankCardActivity;
import com.jht.doctor.ui.activity.repayment.MyAccountActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.LoanDetailBean;
import com.jht.doctor.ui.contact.LoanDetailContact;
import com.jht.doctor.ui.presenter.LoanDetailPresenter;
import com.jht.doctor.utils.DensityUtils;
import com.jht.doctor.utils.OsUtil;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.utils.ScreenUtils;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.TabLayout;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoanDetailActivity extends BaseAppCompatActivity implements TabLayout.ItemClickedListener, LoanDetailContact.View {
    @BindView(R.id.id_tv_loan_money)
    TextView idTvLoanMoney;
    @BindView(R.id.id_tv_state)
    TextView idTvState;
    @BindView(R.id.id_tv1)
    TextView idTv1;
    @BindView(R.id.id_tv2)
    TextView idTv2;
    @BindView(R.id.id_tv3)
    TextView idTv3;
    @BindView(R.id.id_tv4)
    TextView idTv4;
    @BindView(R.id.id_tv5)
    TextView idTv5;
    @BindView(R.id.id_tv6)
    TextView idTv6;
    @BindView(R.id.id_tv7)
    TextView idTv7;
    @BindView(R.id.id_rl_back)
    RelativeLayout idRlBack;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.id_tab_layout)
    TabLayout idTabLayout;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.id_empty_img)
    ImageView idEmptyImg;
    @BindView(R.id.id_empty_view)
    NestedScrollView idEmptyView;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.id_collapsing)
    CollapsingToolbarLayout idCollapsing;
    @BindView(R.id.id_rl1)
    RelativeLayout idRl1;
    @BindView(R.id.id_rl4)
    RelativeLayout idRl4;
    @BindView(R.id.id_rl5)
    RelativeLayout idRl5;
    @BindView(R.id.id_rl6)
    RelativeLayout idRl6;
    @BindView(R.id.id_rl_right)
    RelativeLayout idRlRight;
    @BindView(R.id.id_empty_tv)
    TextView idEmptyTv;
    @BindView(R.id.id_rl_toolbar)
    RelativeLayout idRlToolbar;
    @BindView(R.id.id_tv_bottom)
    TextView idTvBottom;
    @BindView(R.id.id_bottom_view)
    RelativeLayout idBottomView;

    //----------测试数据---------
    private int lastOffset;
    private int lastPosition;
    //----------end------------

    @Inject
    LoanDetailPresenter mPresenter;

    private BaseQuickAdapter adapter1, adapter2, adapter3;

    private List<LoanDetailBean.PaidUpLoanListBean> paidUpLoanListBeans = new ArrayList<>();//已还款

    private List<LoanDetailBean.WaitPaymentListBean> waitPaymentListBeans = new ArrayList<>();//待还款

    private List<LoanDetailBean.OutstandingAccountListBean> outstandingAccountListBeans = new ArrayList<>();//未出账

    private int mPos = 0;//当前列表选中项

    private String orderNo;

    public static final String ORDER_KEY = "orderNo";

    //动画相关
    private ValueAnimator showAnimator;

    private ValueAnimator hideAnimator;

    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLLING = 1;

    private int currentState;

    private View footer1, footer2, footer3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_detail_coordination);
        ButterKnife.bind(this);
        setTranslucentStatusBar();

        initData();
        initEvent();
    }

    private void initData() {
        if (getIntent().getStringExtra(ORDER_KEY) != null) {
            orderNo = getIntent().getStringExtra(ORDER_KEY);
        }
    }

    private void initEvent() {
        idSwipe.setProgressViewOffset(true, -20, 100);
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getLoanDetail(orderNo);
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (verticalOffset >= 0) {
                    idSwipe.setEnabled(true);
                } else {
                    idSwipe.setEnabled(false);
                }
            }
        });
        idTabLayout.setOnItemClicked(this);
        //底部当前应还view动画相关
        currentState = SCROLL_STATE_IDLE;
        initAnimator();
        recyclerView.addOnScrollListener(onScrollListenerImp);
        footer1 = LayoutInflater.from(this).inflate(R.layout.footer_loan_detail, null);
        footer2 = LayoutInflater.from(this).inflate(R.layout.footer_loan_detail, null);
        footer3 = LayoutInflater.from(this).inflate(R.layout.footer_loan_detail, null);
        //首次进入自动刷新
        mPresenter.getLoanDetail(orderNo);
    }

    @Override
    public void onSuccess(Message message) {
        idSwipe.setRefreshing(false);
        if (message != null) {
            switch (message.what) {
                case LoanDetailPresenter.LOAN_DETAIL:
                    LoanDetailBean bean = (LoanDetailBean) message.obj;
                    setCommonData(bean);
                    String statue = bean.getOrderStatus();
                    if (bean.isCancelStatus()) {
                        //已取消
                        idTvState.setText("已取消");
                        idTvState.setTextColor(getResources().getColor(R.color.color_666));
                        setFirstData(bean);
                    } else {
                        switch (statue) {
                            //2018年03月07日 mayakun添加 sta
                            case OrderStatue.PRE_REJECT://已拒绝
                            case OrderStatue.APPLY_REJECT:
                                idTvState.setText("已拒绝");
                                idTvState.setTextColor(getResources().getColor(R.color.color_999));
                                setFirstData(bean);
                                break;
                            //2018年03月07日 mayakun添加 end
                            case OrderStatue.ALREADY_APPLY:
                            case OrderStatue.ADD_INFO:
                            case OrderStatue.ADD_INFO_SUCCESS://审核中
                                idTvState.setText("审核中");
                                idTvState.setTextColor(getResources().getColor(R.color.tab_unselected));
                                setFirstData(bean);
                                break;
                            case OrderStatue.SIGN_SUCCESS://抵押
                                idTvState.setText("抵押");
                                idTvState.setTextColor(getResources().getColor(R.color.tab_unselected));
                                setFirstData(bean);
                                break;
                            case OrderStatue.PENDING_MONEY: //待放款
                                idTvState.setText("待放款");
                                idTvState.setTextColor(getResources().getColor(R.color.tab_unselected));
                                setFirstData(bean);
                                break;
                            case OrderStatue.SIGN_FAILURED:
                            case OrderStatue.SIGN_REFUSED:
                            case OrderStatue.TERMINAL_REFUSED://已拒绝
                                idTvState.setText("已拒绝");
                                idTvState.setTextColor(getResources().getColor(R.color.color_trade_failure));
                                setFirstData(bean);
                                break;
                            case OrderStatue.REPAYMENT://已逾期，已放款
                                idTvState.setText(bean.isOverdueStatus() ? "已逾期" : "还款中");
                                idTvState.setTextColor(getResources().getColor(bean.isOverdueStatus() ? R.color.color_trade_failure : R.color.tab_unselected));
                                setSecondData(bean);
                                break;
                            case OrderStatue.END://已结清
                                idTvState.setText("已结清");
                                idTvState.setTextColor(getResources().getColor(R.color.color_666));
                                setThirdData(bean);
                                break;
                        }
                    }
                    break;
            }
        }
    }

    /**
     * 差别信息 第三种情况
     * 已结清
     * 显示6-7行
     * 不显示tab 有借款银行卡
     * 底部view显示查看还款详情
     *
     * @param bean
     */
    private void setThirdData(LoanDetailBean bean) {
        idRl1.setVisibility(View.VISIBLE);
        idRl4.setVisibility(View.VISIBLE);
        idRl5.setVisibility(View.VISIBLE);
        idRl6.setVisibility(bean.isOverdueStatus() ? View.VISIBLE : View.GONE);
        idTabLayout.setVisibility(View.GONE);
        idRlRight.setVisibility(View.VISIBLE);
        setHeight(bean.isOverdueStatus() ? 0 : 1, false);
        setAppBarScrolled(true);
        idTv1.setText(RegexUtil.formatMoney(bean.getRepaymentInterestTotal()) + "元");
        idTv4.setText(bean.getFinalRepaymentDate());
        idTv5.setText(RegexUtil.formatMoney(bean.getServiceAmtTotal()) + "元");
        idTv6.setText(RegexUtil.formatMoney(bean.getOverdueAmtTotal()) + "元");
        paidUpLoanListBeans.clear();
        paidUpLoanListBeans.addAll(bean.getPaidUpLoanList());
        initAdapter(true);
        if (paidUpLoanListBeans == null || paidUpLoanListBeans.size() == 0) {
            //已结清 按道理列表不会为空
            adapter1.removeAllFooterView();
        } else {
            adapter1.addFooterView(footer1);
            idEmptyView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter1);
            adapter1.notifyDataSetChanged();
        }

        if (bean.isContributiveTypeJudge()) {
            //线上存管
            idBottomView.setVisibility(View.VISIBLE);
            idTvBottom.setText("查看还款详情");
        } else {
            //线下非存管
            idBottomView.setVisibility(View.GONE);
        }
    }

    /**
     * 差别信息 第二种情况
     * 还款中 已逾期
     * 显示6-7行
     * 显示tab 有借款银行卡
     * 底部view显示当前应还。。元
     *
     * @param bean
     */
    private void setSecondData(LoanDetailBean bean) {
        idRl1.setVisibility(View.VISIBLE);
        idRl4.setVisibility(View.VISIBLE);
        idRl5.setVisibility(View.VISIBLE);
        idRl6.setVisibility(bean.isOverdueStatus() ? View.VISIBLE : View.GONE);
        idTabLayout.setVisibility(View.VISIBLE);
        idRlRight.setVisibility(View.VISIBLE);
        setHeight(bean.isOverdueStatus() ? 0 : 1, true);
        setAppBarScrolled(true);
        idTv1.setText(RegexUtil.formatMoney(bean.getRepaymentInterestTotal()) + "元");
        idTv4.setText(bean.getFinalRepaymentDate());
        idTv5.setText(RegexUtil.formatMoney(bean.getServiceAmtTotal()) + "元");
        idTv6.setText(RegexUtil.formatMoney(bean.getOverdueAmtTotal()) + "元");

        paidUpLoanListBeans.clear();
        paidUpLoanListBeans.addAll(bean.getPaidUpLoanList());
        waitPaymentListBeans.clear();
        waitPaymentListBeans.addAll(bean.getWaitPaymentList());
        outstandingAccountListBeans.clear();
        outstandingAccountListBeans.addAll(bean.getOutstandingAccountList());
        initAdapter(false);
        setAdapter();

        if (bean.isContributiveTypeJudge()) {
            //线上存管
            idBottomView.setVisibility(View.VISIBLE);
            idTvBottom.setText(MessageFormat.format("当前应还 {0} 元", RegexUtil.formatMoney(bean.getCurrentRepaymentAmt())));
        } else {
            //线下非存管
            idBottomView.setVisibility(View.GONE);
        }
    }

    /**
     * 差别信息 第一种情况
     * 审核中 抵押 待放款 已拒绝
     * 显示3行
     * 不显示tab 无借款银行卡
     * 底view不显示
     *
     * @param bean
     */
    private void setFirstData(LoanDetailBean bean) {
        idRl1.setVisibility(View.GONE);
        idRl4.setVisibility(View.GONE);
        idRl5.setVisibility(View.GONE);
        idRl6.setVisibility(View.GONE);
        idTabLayout.setVisibility(View.GONE);
        idRlRight.setVisibility(View.GONE);
        setHeight(4, false);
        setAppBarScrolled(false);

        idBottomView.setVisibility(View.GONE);
    }

    /**
     * 设置公共信息
     *
     * @param bean
     */
    private void setCommonData(LoanDetailBean bean) {
        //2018年03月07日 mayakun添加 sta
        if (OrderStatue.PRE_REJECT.equals(bean.getOrderStatus())||OrderStatue.APPLY_REJECT.equals(bean.getOrderStatus())) {
            idTvLoanMoney.setText("- -");
            idTv2.setText("- -");
            idTv3.setText("- -");
        //2018年03月07日 mayakun添加 end
        } else {
            idTvLoanMoney.setText(RegexUtil.formatMoney(bean.getLoanAmt()) + "元");
            idTv2.setText(U.keyToValue(bean.getRepaymentType(), U.getConfigData().REPAYMENT_TYPE));
            idTv3.setText(bean.getPeriodNumber() + "期");
        }
        idTv7.setText(bean.getOrderNo());
    }

    /**
     * 设置appbar滚动与否
     *
     * @param isScroll
     */
    private void setAppBarScrolled(boolean isScroll) {
        AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) idCollapsing.getLayoutParams();
        if (isScroll) {
            lp.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                    | AppBarLayout.LayoutParams.SCROLL_FLAG_EXIT_UNTIL_COLLAPSED);
        } else {
            lp.setScrollFlags(0);
        }
        idCollapsing.setLayoutParams(lp);
    }

    /**
     * 设置布局高度
     *
     * @param missNumber 上方布局隐藏条目个数
     * @param isShowTab  是否显示tablayout
     */
    private void setHeight(int missNumber, boolean isShowTab) {
        int missHeight = 0, toolbarHeght;
        missHeight += missNumber * 26;
        if (!isShowTab) {
            missHeight += 50;
            toolbarHeght = 45;
        } else {
            toolbarHeght = 95;
        }
        AppBarLayout.LayoutParams lp = (AppBarLayout.LayoutParams) idCollapsing.getLayoutParams();
        lp.height = DensityUtils.dp2Px(LoanDetailActivity.this, 417 - missHeight);
        idCollapsing.setLayoutParams(lp);

        CollapsingToolbarLayout.LayoutParams lp1 = (CollapsingToolbarLayout.LayoutParams) toolbar.getLayoutParams();
        lp1.height = DensityUtils.dp2Px(LoanDetailActivity.this, toolbarHeght) + DensityUtils.getStatusBarHeight(this);
        toolbar.setLayoutParams(lp1);
    }


    /**
     * 初始化adpter
     *
     * @param isEnd 是否已结清
     */
    private void initAdapter(boolean isEnd) {
        if (adapter1 == null) {
            adapter1 = new BaseQuickAdapter(R.layout.item_loan_detail, paidUpLoanListBeans) {
                @Override
                protected void convert(BaseViewHolder helper, Object item) {
                    LoanDetailBean.PaidUpLoanListBean bean = (LoanDetailBean.PaidUpLoanListBean) item;
                    helper.setGone(R.id.id_label3, false)
                            .setGone(R.id.id_tv_remain, false)
                            .setText(R.id.id_label2, "已还金额：")
                            .setText(R.id.id_tv_period, bean.getPeriod() + "/" + bean.getTotalPeriod())
                            .setText(R.id.id_tv_total, RegexUtil.formatMoney(bean.getTotalRepaymentAmt()) + "元")
                            .setText(R.id.id_tv1, RegexUtil.formatMoney(bean.getRepaymentPrincipalAmt()) + "元")
                            .setText(R.id.id_tv2, RegexUtil.formatMoney(bean.getRepaymentInterestAmt()) + "元")
                            .setText(R.id.id_tv3, RegexUtil.formatMoney(bean.getServiceAmt()) + "元")
                            .setText(R.id.id_tv4, RegexUtil.formatMoney(bean.getOverdueAmt()) + "元")
                            .setText(R.id.id_tv5, RegexUtil.formatMoney(bean.getRemissionAmt()) + "元")
                            .setGone(R.id.id_rl4, bean.getOverdueAmt() == 0 ? false : true)
                            .setGone(R.id.id_rl5, bean.getRemissionAmt() == 0 ? false : true)
                            .setTextColor(R.id.id_tv_state, getResources().getColor(R.color.color_666))
                            .setText(R.id.id_tv_state, "已还款");
                    ImageView imageView = helper.getView(R.id.id_iv_triangle);
                    imageView.setImageResource(R.drawable.icon_sj_2);
                    LinearLayout ll_bottom = helper.getView(R.id.id_ll_bottom);
                    ll_bottom.setVisibility(View.GONE);
                    helper.getView(R.id.id_rl_top).setOnClickListener(
                            new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    if (ll_bottom.getVisibility() == View.VISIBLE) {
                                        upAnimation(imageView);
                                        ll_bottom.setVisibility(View.GONE);
                                    } else {
                                        downAnimation(imageView);
                                        ll_bottom.setVisibility(View.VISIBLE);
                                    }
                                }
                            }
                    );
                    helper.getView(R.id.id_rl_click).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            showPopup(helper.getView(R.id.id_img_what));
                        }
                    });
                }
            };
        }
        if (!isEnd) {
            if (adapter2 == null) {
                adapter2 = new BaseQuickAdapter(R.layout.item_loan_detail, waitPaymentListBeans) {
                    @Override
                    protected void convert(BaseViewHolder helper, Object item) {
                        LoanDetailBean.WaitPaymentListBean bean = (LoanDetailBean.WaitPaymentListBean) item;
                        helper.setGone(R.id.id_label3, true)
                                .setGone(R.id.id_tv_remain, true)
                                .setText(R.id.id_label2, "应还总计：")
                                .setText(R.id.id_tv_period, bean.getPeriod() + "/" + bean.getTotalPeriod())
                                .setText(R.id.id_tv_total, RegexUtil.formatMoney(bean.getTotalRepaymentAmt()) + "元")
                                .setText(R.id.id_tv_remain, RegexUtil.formatMoney(bean.getRestReapaymenAmt()) + "元")
                                .setText(R.id.id_tv1, RegexUtil.formatMoney(bean.getRepaymentPrincipalAmt()) + "元")
                                .setText(R.id.id_tv2, RegexUtil.formatMoney(bean.getRepaymentInterestAmt()) + "元")
                                .setText(R.id.id_tv3, RegexUtil.formatMoney(bean.getServiceAmt()) + "元")
                                .setText(R.id.id_tv4, RegexUtil.formatMoney(bean.getOverdueAmt()) + "元")
                                .setText(R.id.id_tv5, RegexUtil.formatMoney(bean.getRemissionAmt()) + "元")
                                .setGone(R.id.id_rl4, bean.getOverdueAmt() == 0 ? false : true)
                                .setGone(R.id.id_rl5, bean.getRemissionAmt() == 0 ? false : true)
                                .setTextColor(R.id.id_tv_state, getResources().getColor(bean.getOverdueAmt() == 0 ? R.color.color_4f9ef3 : R.color.color_trade_failure))
                                .setText(R.id.id_tv_state, bean.getOverdueAmt() == 0 ? "待还款" : "已逾期");
                        ImageView imageView = helper.getView(R.id.id_iv_triangle);
                        imageView.setImageResource(bean.getOverdueAmt() == 0 ? R.drawable.icon_sj_4 : R.drawable.icon_sj_3);
                        imageView.setRotation(-180);
                        LinearLayout ll_bottom = helper.getView(R.id.id_ll_bottom);
                        helper.getView(R.id.id_rl_top).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (ll_bottom.getVisibility() == View.VISIBLE) {
                                            upAnimation(imageView);
                                            ll_bottom.setVisibility(View.GONE);
                                        } else {
                                            downAnimation(imageView);
                                            ll_bottom.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                        );
                        helper.getView(R.id.id_rl_click).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                showPopup(helper.getView(R.id.id_img_what));
                            }
                        });
                    }
                };
            }
            if (adapter3 == null) {
                adapter3 = new BaseQuickAdapter(R.layout.item_loan_detail, outstandingAccountListBeans) {
                    @Override
                    protected void convert(BaseViewHolder helper, Object item) {
                        LoanDetailBean.OutstandingAccountListBean bean = (LoanDetailBean.OutstandingAccountListBean) item;
                        helper.setGone(R.id.id_label3, false)
                                .setGone(R.id.id_tv_remain, false)
                                .setText(R.id.id_label2, "应还金额：")
                                .setText(R.id.id_tv_period, bean.getPeriod() + "/" + bean.getTotalPeriod())
                                .setText(R.id.id_tv_total, RegexUtil.formatMoney(bean.getTotalRepaymentAmt()) + "元")
                                .setText(R.id.id_tv1, RegexUtil.formatMoney(bean.getRepaymentPrincipalAmt()) + "元")
                                .setText(R.id.id_tv2, RegexUtil.formatMoney(bean.getRepaymentInterestAmt()) + "元")
                                .setText(R.id.id_tv3, RegexUtil.formatMoney(bean.getServiceAmt()) + "元")
                                .setGone(R.id.id_rl4, false)
                                .setGone(R.id.id_rl5, false)
                                .setTextColor(R.id.id_tv_state, getResources().getColor(R.color.color_666))
                                .setText(R.id.id_tv_state, "未出账");
                        ImageView imageView = helper.getView(R.id.id_iv_triangle);
                        imageView.setImageResource(R.drawable.icon_sj_2);
                        LinearLayout ll_bottom = helper.getView(R.id.id_ll_bottom);
                        ll_bottom.setVisibility(View.GONE);
                        helper.getView(R.id.id_rl_top).setOnClickListener(
                                new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        if (ll_bottom.getVisibility() == View.VISIBLE) {
                                            upAnimation(imageView);
                                            ll_bottom.setVisibility(View.GONE);
                                        } else {
                                            downAnimation(imageView);
                                            ll_bottom.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                        );
                    }
                };
            }
        }

    }


    /**
     * 设置adpter
     */
    private void setAdapter() {
        switch (mPos) {
            case 0:
                if (paidUpLoanListBeans == null || paidUpLoanListBeans.size() == 0) {
                    adapter1.removeAllFooterView();
                    setEmpty();
                } else {
                    adapter1.removeAllFooterView();
                    adapter1.addFooterView(footer1);
                    setRecycle();

                }
                break;
            case 1:
                if (waitPaymentListBeans == null || waitPaymentListBeans.size() == 0) {
                    adapter2.removeAllFooterView();
                    setEmpty();
                } else {
                    adapter2.removeAllFooterView();
                    adapter2.addFooterView(footer2);
                    setRecycle();
                }
                break;
            case 2:
                if (outstandingAccountListBeans == null || outstandingAccountListBeans.size() == 0) {
                    adapter3.removeAllFooterView();
                    setEmpty();
                } else {
                    adapter3.removeAllFooterView();
                    adapter3.addFooterView(footer3);
                    setRecycle();
                }
                break;
        }
    }

    /**
     * 根据当前位置设置空view
     */
    private void setEmpty() {
        idEmptyView.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        switch (mPos) {
            case 0:
                idEmptyImg.setImageResource(R.drawable.msg_1);
                idEmptyTv.setText("这里空空如也~");
                break;
            case 1:
                idEmptyImg.setImageResource(R.drawable.msg_2);
                idEmptyTv.setText("本期借款已还清");
                break;
            case 2:
                idEmptyImg.setImageResource(R.drawable.msg_3);
                idEmptyTv.setText("这里空空如也~");
                break;
        }
    }

    /**
     * 根据当前位置设置recycle
     */
    private void setRecycle() {
        idEmptyView.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
        switch (mPos) {
            case 0:
                recyclerView.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
                break;
            case 1:
                recyclerView.setAdapter(adapter2);
                adapter2.notifyDataSetChanged();
                break;
            case 2:
                recyclerView.setAdapter(adapter3);
                adapter3.notifyDataSetChanged();
                break;
        }
    }

    /**
     * 三角向上动画
     *
     * @param view
     */
    public void upAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", -180, 0);
        animator.setDuration(500);
        animator.start();
    }

    /**
     * 三角向下动画
     *
     * @param view
     */
    private void downAnimation(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "rotation", 0, -180);
        animator.setDuration(500);
        animator.start();
    }


    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(CustomerApplication.getAppComponent())
                .build()
                .inject(this);
    }


    private PopupWindow popupWindow;

    /**
     * 弹出框违约金说明
     *
     * @param view
     */
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
    public void itemClicked(int position) {
        switch (position) {
            case 0:
                if (mPos != 0) {
                    mPos = position;
                    setAdapter();
                }
                mPos = 0;
                break;
            case 1:
                if (mPos != 1) {
                    mPos = position;
                    setAdapter();
                }
                mPos = 1;
                break;
            case 2:
                if (mPos != 2) {
                    mPos = position;
                    setAdapter();
                }
                mPos = 2;
                break;
        }
    }


    /**
     * 沉浸状态栏
     */
    public void setTranslucentStatusBar() {
//        if (toolbar != null) {
//            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
//            layoutParams.setMargins(
//                    layoutParams.leftMargin,
//                    layoutParams.topMargin + DensityUtils.getStatusBarHeight(this),
//                    layoutParams.rightMargin,
//                    layoutParams.bottomMargin);
//        }
        Toolbar.LayoutParams lp = (Toolbar.LayoutParams) idRlToolbar.getLayoutParams();
        lp.height = DensityUtils.dp2Px(this, 45) + DensityUtils.getStatusBarHeight(this);
        idRlToolbar.setLayoutParams(lp);

        idRlToolbar.setPadding(0, DensityUtils.getStatusBarHeight(this), 0, 0);
        toolbar.setMinimumHeight(DensityUtils.getStatusBarHeight(this) + DensityUtils.dp2Px(this, 45));

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || OsUtil.isMIUI() || OsUtil.isFlyme()) {
            ScreenUtils.setStatusBarFontIconDark(this, true);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(getResources().getColor(R.color.statueBar_color));
        }
    }

    /**
     * 记录RecyclerView当前位置
     */
    private void getPositionAndOffset() {
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //获取可视的第一个view
        View topView = layoutManager.getChildAt(0);
        if (topView != null) {
            //获取与该view的顶部的偏移量
            lastOffset = topView.getTop();
            //得到该View的数组位置
            lastPosition = layoutManager.getPosition(topView);
        }
    }

    /**
     * 让RecyclerView滚动到指定位置
     */
    private void scrollToPosition() {
        if (recyclerView.getLayoutManager() != null && lastPosition >= 0) {
            ((LinearLayoutManager) recyclerView.getLayoutManager()).scrollToPositionWithOffset(lastPosition, lastOffset);
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }


    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }

    @OnClick({R.id.id_rl_back, R.id.id_rl_right, R.id.id_bottom_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_rl_back:
                finish();
                break;
            case R.id.id_rl_right:
                //还款银行卡
                Intent intent = new Intent(this, MyBankCardActivity.class);
                intent.putExtra(MyBankCardActivity.ORDER_KEY, orderNo);
                startActivity(intent);
                break;
            case R.id.id_bottom_view:
                //底部当前应还
                Intent intentRepayment = new Intent(LoanDetailActivity.this, MyAccountActivity.class);
                intentRepayment.putExtra("orderNo", orderNo);
                startActivity(intentRepayment);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }

    private RecyclerView.OnScrollListener onScrollListenerImp = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (currentState == SCROLL_STATE_IDLE && newState != RecyclerView.SCROLL_STATE_IDLE) {
                if (hideAnimator.isRunning()) {
                    hideAnimator.end();
                }
                if (!showAnimator.isRunning()) {
                    showAnimator.start();
                }
                currentState = SCROLLING;
            } else if (currentState == SCROLLING && newState == RecyclerView.SCROLL_STATE_IDLE) {
                if (showAnimator.isRunning()) {
                    showAnimator.end();
                }
                if (!hideAnimator.isRunning()) {
                    hideAnimator.start();
                }
                currentState = SCROLL_STATE_IDLE;
            }

        }
    };

    /**
     * 初始化动画
     */
    private void initAnimator() {
        showAnimator = ValueAnimator.ofInt(DensityUtils.dp2Px(this, 0), DensityUtils.dp2Px(this, -50));
        showAnimator.setDuration(100);
        showAnimator.addUpdateListener(animatorListenerImp);
        hideAnimator = ObjectAnimator.ofInt(DensityUtils.dp2Px(this, -50), DensityUtils.dp2Px(this, 0));
        hideAnimator.setDuration(100);
        hideAnimator.addUpdateListener(animatorListenerImp);

    }

    private ValueAnimator.AnimatorUpdateListener animatorListenerImp = new ValueAnimator.AnimatorUpdateListener() {
        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            CoordinatorLayout.LayoutParams lp = (CoordinatorLayout.LayoutParams) idBottomView.getLayoutParams();
            lp.bottomMargin = (int) animation.getAnimatedValue();
            idBottomView.setLayoutParams(lp);
        }
    };

}
