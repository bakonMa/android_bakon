package com.jht.doctor.ui.activity.mine.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.injection.components.DaggerFragmentComponent;
import com.jht.doctor.injection.modules.FragmentModule;
import com.jht.doctor.ui.base.BaseAppCompatFragment;
import com.jht.doctor.ui.contact.HomeLoanContact;
import com.jht.doctor.ui.presenter.HomeLoanPresenter;
import com.trello.rxlifecycle.LifecycleTransformer;

import javax.inject.Inject;

/**
 * 订单列表
 */
public class TestFragment extends BaseAppCompatFragment implements HomeLoanContact.View {


    @Inject
    HomeLoanPresenter mPresenter;

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(CustomerApplication.getAppComponent())
                .build().inject(this);

    }

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_my_info, null);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {
        mPresenter.getMaxAmt();
    }

    @Override
    public void onError(String errorCode, String errorMsg) {

    }

    @Override
    public void onSuccess(Message message) {
        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("sssssssss");

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
