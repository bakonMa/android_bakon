package com.jht.doctor.ui.activity.mine.bankcard;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.SupportBankBean;
import com.jht.doctor.ui.contact.SupportBankContact;
import com.jht.doctor.ui.presenter.SupportBankPresenter;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SupportBankActivity extends BaseAppCompatActivity implements SupportBankContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_recycleView)
    RecyclerView idRecycleView;

    private BaseQuickAdapter mAdapter;

    private List<SupportBankBean> bankBeans = new ArrayList<>();

    private View headerView;

    private String orderNo;

    @Inject
    SupportBankPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_bank);
        ButterKnife.bind(this);
        initToolbar();
        initEvent();
        orderNo = getIntent().getStringExtra("orderNo");
        mPresenter.getSupportankList(orderNo);
    }

    private void initEvent() {
        mAdapter = new BaseQuickAdapter(R.layout.item_support_bank, bankBeans) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                SupportBankBean bean = (SupportBankBean) item;
                helper.setText(R.id.id_tv_bankname,bean.getBankName())
                        .setText(R.id.id_tv1,(int)bean.getSingleAmt()/10000+"万")
                        .setText(R.id.id_tv2,(int)bean.getSingleDayAmt()/10000+"万")
                        .setText(R.id.id_tv3,(int)bean.getSingleMonthAmt()/10000+"万");
            }
        };
        headerView = LayoutInflater.from(this).inflate(R.layout.header_support_bank, null);
        mAdapter.addHeaderView(headerView);
        idRecycleView.setAdapter(mAdapter);
        idRecycleView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("支持银行")
                .setLeft(false)
                .setStatuBar(R.color.white)
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
    public void onError(String errorCode, String errorMsg) {
        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message != null) {
            bankBeans.clear();
            bankBeans.addAll((List<SupportBankBean>) message.obj);
            mAdapter.notifyDataSetChanged();
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.unsubscribe();
    }
}
