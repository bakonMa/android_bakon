package com.jht.doctor.ui.activity.repayment;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.mine.LoanDetailActivity;
import com.jht.doctor.ui.activity.mine.PersonalActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.MessageCountBean;
import com.jht.doctor.ui.bean.RepaymentHomeBean;
import com.jht.doctor.ui.contact.HomeRepaymentContact;
import com.jht.doctor.ui.presenter.HomeRepaymentPresenter;
import com.jht.doctor.utils.DensityUtils;
import com.jht.doctor.utils.RegexUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeRepaymentActivity extends BaseAppCompatActivity implements HomeRepaymentContact.View {
    @BindView(R.id.id_tv_remain)
    TextView idTvRemain;
    @BindView(R.id.id_tv_loan)
    TextView idTvLoan;
    @BindView(R.id.id_rl_msg)
    RelativeLayout idRlMsg;
    @BindView(R.id.id_rl_personal)
    RelativeLayout idRlPersonal;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appBar)
    AppBarLayout appBar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.id_tv_remain_money)
    TextView idTvRemainMoney;
    @BindView(R.id.id_bottom_view)
    RelativeLayout idBottomView;
    @BindView(R.id.id_red_point)
    View idRedPoint;

    @Inject
    HomeRepaymentPresenter mPresenter;


    private ValueAnimator showAnimator;

    private ValueAnimator hideAnimator;

    public static final int SCROLL_STATE_IDLE = 0;
    public static final int SCROLLING = 1;

    private int currentState;

    private BaseQuickAdapter mAdapter;

    private List<RepaymentHomeBean.RepaymentListBean> mData;

    private RepaymentHomeBean repaymentHomeBean;

    private View footer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_repayment);
        ButterKnife.bind(this);
        setTranslucentStatusBar();
        initEvent();
        mPresenter.getHomeRepayment();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    private void initEvent() {
        appBar.addOnOffsetChangedListener(offsetChangedListenerImp);
        idSwipe.setProgressViewOffset(true, -20, 100);
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getHomeRepayment();
            }
        });

        currentState = SCROLL_STATE_IDLE;
        initAnimator();
        recyclerView.addOnScrollListener(onScrollListenerImp);
        footer = LayoutInflater.from(this).inflate(R.layout.footer_home_repayment, null);
    }

    //appbar滑动监听
    private AppBarLayout.OnOffsetChangedListener offsetChangedListenerImp = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            //上面全部展示时允许下拉刷新，否则禁止
            if (verticalOffset >= 0) {
                idSwipe.setEnabled(true);
            } else {
                idSwipe.setEnabled(false);
            }
            //上面平滑消失start
            float max = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 270, getResources().getDisplayMetrics());
            int abs = Math.abs(verticalOffset);
            float percent = abs * 1.0f / max;
            float diff = 1 - percent;
            CoordinatorLayout.LayoutParams layoutParams = ((CoordinatorLayout.LayoutParams) recyclerView.getLayoutParams());
            layoutParams.topMargin = (int) (-72 * diff);
            recyclerView.setLayoutParams(layoutParams);
            //上面平滑消失end
        }
    };


    public void setTranslucentStatusBar() {
        if (toolbar != null) {
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) toolbar.getLayoutParams();
            layoutParams.setMargins(
                    layoutParams.leftMargin,
                    layoutParams.topMargin + DensityUtils.getStatusBarHeight(this),
                    layoutParams.rightMargin,
                    layoutParams.bottomMargin);
        }
    }

    @OnClick({R.id.id_rl_msg, R.id.id_rl_personal, R.id.id_bottom_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_rl_msg:
                startActivity(new Intent(this, MessageActivity.class));
                break;
            case R.id.id_rl_personal:
                startActivity(new Intent(this, PersonalActivity.class));
                break;
            case R.id.id_bottom_view:
                if (repaymentHomeBean.getPayType() == null) {
                    return;
                }
                Intent intent = new Intent();
                intent.putExtra("orderNo", repaymentHomeBean.getOrderNo());
                if ("0".equals(repaymentHomeBean.getPayType())) {
                    //线上
                    intent.setClass(HomeRepaymentActivity.this, MyAccountActivity.class);
                } else if ("1".equals(repaymentHomeBean.getPayType())) {
                    //线下
                    intent.setClass(HomeRepaymentActivity.this, OfflineRepaymentActivity.class);
                }
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
        idSwipe.setRefreshing(false);
    }

    @Override
    public void onSuccess(Message message) {
        idSwipe.setRefreshing(false);
        if (message == null) {
            return;
        }
        switch (message.what) {
            case HomeRepaymentPresenter.REPAYMENT: //还款首页
//                mPresenter.getMessageCount();
                repaymentHomeBean = (RepaymentHomeBean) message.obj;
                if (!repaymentHomeBean.isRepaymentStatus()) {
                    //设置空view
                } else {
                    idTvLoan.setText(RegexUtil.formatMoney(repaymentHomeBean.getLoanAmt()));
                    idTvRemain.setText(RegexUtil.formatMoney(repaymentHomeBean.getRestReapaymenAmt()));
                    if (repaymentHomeBean.getCurrentRepaymentTotal() == null || Double.parseDouble(repaymentHomeBean.getCurrentRepaymentTotal()) == 0) {
                        idBottomView.setVisibility(View.GONE);
                    } else {
                        idBottomView.setVisibility(View.VISIBLE);
                        idTvRemainMoney.setText(RegexUtil.formatMoney(repaymentHomeBean.getCurrentRepaymentTotal()));
                    }
                    if (repaymentHomeBean.getRepaymentList() == null || repaymentHomeBean.getRepaymentList().size() == 0) {
                        //设置空view
                    } else {
                        mData = repaymentHomeBean.getRepaymentList();
                        setAdapter();
                    }
                }
                break;
            case HomeRepaymentPresenter.MESSAGE_COUNT:
                MessageCountBean bean = (MessageCountBean) message.obj;
                idRedPoint.setVisibility(bean.getUnreadCount() > 0 ? View.VISIBLE : View.GONE);
                break;
        }
    }

    private void setAdapter() {
        if (mAdapter == null) {
            mAdapter = new BaseQuickAdapter(R.layout.item_home_repayment, mData) {
                @Override
                protected void convert(BaseViewHolder helper, Object item) {
                    RepaymentHomeBean.RepaymentListBean bean = (RepaymentHomeBean.RepaymentListBean) item;
                    helper.setGone(R.id.id_line, helper.getLayoutPosition() != 0)
                            .setText(R.id.id_times, bean.getPeriod() + "/" + bean.getTotalPeriod())
                            .setText(R.id.id_money, RegexUtil.formatMoney(bean.getRepaymentAmt()))
                            .setText(R.id.id_date, bean.getRepaymentDate())
                            .setGone(R.id.id_line_bottom, helper.getLayoutPosition() != mData.size() - 1)
                            .setVisible(R.id.id_line_2, mData.size() != 1)
                            .setText(R.id.id_state, bean.getRepaymentStatus());
                    TextView tv_state = helper.getView(R.id.id_state);
                    if (bean.isOverdueStatus()) {
                        tv_state.setTextColor(getResources().getColor(R.color.color_trade_failure));
                        tv_state.setText("已逾期");
                    } else if ("WAIT".equals(bean.getRepaymentStatus())) {
                        tv_state.setTextColor(getResources().getColor(R.color.tab_unselected));
                        tv_state.setText("待还款");
                    } else if ("SUCCESS".equals(bean.getRepaymentStatus())) {
                        tv_state.setTextColor(getResources().getColor(R.color.color_999));
                        tv_state.setText("已结清");
                    }
                }
            };
            mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                    Intent loanIntent = new Intent(HomeRepaymentActivity.this, LoanDetailActivity.class);
                    loanIntent.putExtra("orderNo", repaymentHomeBean.getOrderNo());
                    startActivity(loanIntent);
                }
            });
            mAdapter.addFooterView(footer);
            recyclerView.setAdapter(mAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
        } else {
            mAdapter.notifyDataSetChanged();
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
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) idBottomView.getLayoutParams();
            lp.bottomMargin = (int) animation.getAnimatedValue();
            idBottomView.setLayoutParams(lp);
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
