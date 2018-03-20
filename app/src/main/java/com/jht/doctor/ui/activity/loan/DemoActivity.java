package com.jht.doctor.ui.activity.loan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.View;

import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.contact.DemoContact;
import com.jht.doctor.ui.presenter.DemoPresenter;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;


public class DemoActivity extends BaseAppCompatActivity implements DemoContact.View {

    @Inject
    DemoPresenter mPresenter;

    private CompositeSubscription compositeSubscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);
        compositeSubscription = new CompositeSubscription();
        initStatusBar();
        mPresenter.login();

    }

    private void toHomeLoanActivity() {
        Subscription subscribe = Observable.timer(1000, TimeUnit.MILLISECONDS).subscribe(avoid -> {
            Intent intent = new Intent(this, HomeLoanActivity.class);
            startActivity(intent);
            finish();
        });
        compositeSubscription.add(subscribe);
    }

    private void initStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
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
    protected void onDestroy() {
        super.onDestroy();
        if (compositeSubscription != null && !compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {

    }

    @Override
    public void onSuccess(Message message) {

    }

    @Override
    public Activity provideContext() {
        return this;
    }


    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }
}
