package com.jht.doctor.ui.activity.loan;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.activity.mine.webview.WebViewActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.contact.LoanApplyContact;
import com.jht.doctor.ui.presenter.LoanApplyPresenter;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoanApplyStateActivity extends BaseAppCompatActivity implements LoanApplyContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_tv_money)
    TextView idTvMoney;
    @BindView(R.id.id_tv_repayment_method)
    TextView idTvRepaymentMethod;
    @BindView(R.id.id_tv1)
    TextView idTv1;
    @BindView(R.id.id_tv2)
    TextView idTv2;
    @BindView(R.id.id_tv3)
    TextView idTv3;
    @BindView(R.id.id_btn_next_step)
    TextView idBtnNextStep;
    //进入征信，回来后重新请求接口
    private int REQUEST_CODE = 10100;

    @Inject
    LoanApplyPresenter mPresenter;
    private int totalMoney;
    private String moneyType;
    private String orderNo;
    private boolean hasCredit = false;//是否已经通过征信

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_apply_state);
        ButterKnife.bind(this);
        initToolBar();
        //前画面传过来的数据  必须
        orderNo = getIntent().getStringExtra("orderNo");
        moneyType = getIntent().getStringExtra("moneytype");
        totalMoney = getIntent().getIntExtra("money", 0);

        idTvMoney.setText(totalMoney + "");
        idTvRepaymentMethod.setText(moneyType);

        mPresenter.getOrderCreditStatus(orderNo);
    }

    private void initToolBar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setLeft(false)
//                .setRightImg(R.drawable.icon_gerenzhongxin, true)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                }).bind();
    }

    @OnClick(R.id.id_btn_next_step)
    public void onViewClicked() {
        if (hasCredit) {
            EventBusUtil.sendEvent(new Event<String>(EventConfig.CONTROL_FRAGMENT,"订单"));//控制主页面切换到订单页面
            EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_ORDER));//刷新订单列表
            finish();
        } else {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("title", "征信报告查询");
            intent.putExtra("orderNo", orderNo);
            startActivityForResult(intent, REQUEST_CODE);
        }
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            mPresenter.getOrderCreditStatus(orderNo);
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null || message.obj == null) {
            return;
        }
        switch (message.what) {
            case LoanApplyPresenter.GET_CREDIT_STATUS:
                if ("D".equals(message.obj)) {
                    hasCredit = true;//征信通过
                    idTv1.setText("申请审核中");
                    idTv2.setText("您的专属顾问会在1个工作日内与你联系\n请耐心等待，保持手机通畅");
                    idBtnNextStep.setText("查看我的借款");
                }
                break;
        }
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
