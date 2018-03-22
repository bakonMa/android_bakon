package com.jht.doctor.ui.activity.loan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.mine.LoginActivity;
import com.jht.doctor.ui.activity.mine.PersonalActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.HomeLoanBean;
import com.jht.doctor.ui.bean.LoginResponse;
import com.jht.doctor.ui.bean.MaxAmtBean;
import com.jht.doctor.ui.contact.HomeLoanContact;
import com.jht.doctor.ui.presenter.HomeLoanPresenter;
import com.jht.doctor.utils.DensityUtils;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.utils.StringUtils;
import com.jht.doctor.widget.dialog.OngoingDialog;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeLoanActivity extends BaseAppCompatActivity implements HomeLoanContact.View {

    @BindView(R.id.id_rl_personl_center)
    RelativeLayout idRlPersonlCenter;
    @BindView(R.id.id_tv_loan_money)
    TextView idTvLoanMoney;
    @BindView(R.id.id_tv_apply)
    TextView idTvApply;
    @BindView(R.id.id_rl_banner)
    RelativeLayout idRlBanner;

    @Inject
    HomeLoanPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_loan);
        ButterKnife.bind(this);
        setMargin();
        requestMaxAmt();
    }

    private void requestMaxAmt() {
        if (!StringUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, ""))) {
            mPresenter.getMaxAmt();
        }
    }

    private void setMargin() {
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) idRlPersonlCenter.getLayoutParams();
        lp.setMargins(0, DensityUtils.getStatusBarHeight(this), 0, 0);
        idRlPersonlCenter.setLayoutParams(lp);
    }


    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }


    @OnClick({R.id.id_rl_personl_center, R.id.id_tv_apply})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_rl_personl_center:
                startActivity(new Intent(this, PersonalActivity.class));
                break;
            case R.id.id_tv_apply:
                if (StringUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, ""))) {
                    Intent intent = new Intent(this, LoginActivity.class);
                    intent.putExtra(LoginActivity.FROM_KEY, LoginActivity.HOMELOAN_ACTIVITY);
                    startActivity(intent);
                } else {
                    mPresenter.applyStatus();
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.REQUEST_HOMELOAN:
                    //未登录状态下 点击申请  登录成功后返回的逻辑
                    LoginResponse loginResponse = (LoginResponse) event.getData();
                    if (loginResponse.getApplyJudgeDTO().isRepaymentStatus()) {
                        OngoingDialog dialog = new OngoingDialog(this, OngoingDialog.REPAYMENT);
                        dialog.show();
                    } else if (loginResponse.getApplyJudgeDTO().isApplyStatus()) {
                        OngoingDialog dialog = new OngoingDialog(this, OngoingDialog.ONGOING);
                        dialog.show();
                    } else if (loginResponse.getApplyJudgeDTO().isPreTrialStatus()) {
                        Intent intent = new Intent(this, LoanMoneyActivity.class);
                        intent.putExtra(LoanMoneyActivity.ORDER_NUMBER_KEY, loginResponse.getApplyJudgeDTO().getOrderNo());
                        intent.putExtra(LoanMoneyActivity.PRE_TRAIL_AMT, loginResponse.getApplyJudgeDTO().getPreTrialAmt());
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(this, BasicInfoActivity.class));
                    }
                    break;
                case EventConfig.REFRESH_MAX_AMT:
                    requestMaxAmt();
                    break;
                case EventConfig.WHERE_TO_GO:
                    //从个人中心登录后返回后的逻辑
                    LoginResponse response = (LoginResponse) event.getData();
                    //如果有还款计划 跳转到还款首页
                    if (response.getApplyJudgeDTO().isRepaymentStatus()) {
                        OngoingDialog dialog = new OngoingDialog(this, OngoingDialog.REPAYMENT);
                        dialog.show();
                    }
                    break;
            }

        }
    }


    @Override
    public void onError(String errorCode, String errorMsg) {
        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message != null) {
            switch (message.what) {
                case HomeLoanPresenter.APPLY_SUCCESS:
                    //已登录状态下请求接口判断接口
                    HomeLoanBean homeLoanBean = (HomeLoanBean) message.obj;
                    if (homeLoanBean.isRepaymentStatus()) {
                        OngoingDialog dialog = new OngoingDialog(this, OngoingDialog.REPAYMENT);
                        dialog.show();
                    } else if (homeLoanBean.isApplyStatus()) {
                        OngoingDialog dialog = new OngoingDialog(this, OngoingDialog.ONGOING);
                        dialog.show();
                    } else if (homeLoanBean.isPreTrialStatus()) {
                        Intent intent = new Intent(this, LoanMoneyActivity.class);
                        intent.putExtra(LoanMoneyActivity.ORDER_NUMBER_KEY, homeLoanBean.getOrderNo());
                        intent.putExtra(LoanMoneyActivity.PRE_TRAIL_AMT, homeLoanBean.getPreTrialAmt());
                        startActivity(intent);
                    } else {
                        startActivity(new Intent(this, BasicInfoActivity.class));
                    }
                    break;
                case HomeLoanPresenter.MAX_AMT:
                    if (message.obj != null) {
                        double money = Double.parseDouble(((MaxAmtBean) message.obj).getConfigValue());
                        idTvLoanMoney.setText(RegexUtil.formatMoney(money));
                    }
                    break;
            }
        }
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
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
