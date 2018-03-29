package com.jht.doctor.ui.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.injection.components.DaggerFragmentComponent;
import com.jht.doctor.injection.modules.FragmentModule;
import com.jht.doctor.ui.activity.LoginActivity;
import com.jht.doctor.ui.base.BaseFragment;
import com.jht.doctor.ui.bean.MaxAmtBean;
import com.jht.doctor.ui.contact.HomeLoanContact;
import com.jht.doctor.ui.presenter.HomeLoanPresenter;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by table on 2018/1/8.
 * description:
 */

public class HomeFragment extends BaseFragment implements HomeLoanContact.View {
    @BindView(R.id.id_tv_loan_money)
    TextView idTvLoanMoney;
    @BindView(R.id.id_tv_apply)
    TextView idTvApply;

    @Inject
    HomeLoanPresenter mPresenter;


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        requestMaxAmt();
    }

    private void requestMaxAmt() {
        if (!TextUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, ""))) {
            mPresenter.getMaxAmt();
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build().inject(this);
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_home_loan;
    }

    @Override
    protected void initView() {

    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.REQUEST_HOMELOAN:
                    //未登录状态下 点击申请  登录成功后返回的逻辑
//                    startActivity(new Intent(actContext(), BasicInfoActivity.class));
                    break;
                case EventConfig.REFRESH_MAX_AMT:
                    requestMaxAmt();
                    break;
            }

        }
    }


    @Override
    public void onError(String errorCode, String errorMsg) {
        ToastUtil.show(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case HomeLoanPresenter.MAX_AMT:
                if (message.obj != null) {
                    double money = Double.parseDouble(((MaxAmtBean) message.obj).getConfigValue());
                    idTvLoanMoney.setText(RegexUtil.formatMoney(money));
                }
                break;
        }
    }

    @Override
    public Activity provideContext() {
        return getActivity();
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @OnClick(R.id.id_tv_apply)
    public void onViewClicked() {
        if (TextUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, ""))) {
            Intent intent = new Intent(actContext(), LoginActivity.class);
            intent.putExtra(LoginActivity.FROM_KEY, LoginActivity.HOMELOAN_ACTIVITY);
            startActivity(intent);
        } else {
//            startActivity(new Intent(actContext(), BasicInfoActivity.class));
        }
    }


}
