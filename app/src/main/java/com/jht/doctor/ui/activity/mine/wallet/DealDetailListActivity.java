package com.jht.doctor.ui.activity.mine.wallet;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.ui.bean.DealDetailBean;
import com.jht.doctor.ui.contact.WalletContact;
import com.jht.doctor.ui.presenter.present_jht.WalletPresenter;
import com.jht.doctor.widget.dialog.CommonDialog;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * DealDetailListActivity 交易明细
 * Create at 2018/4/11 下午6:50 by mayakun
 */
public class DealDetailListActivity extends BaseActivity implements WalletContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.bank_recycleview)
    RecyclerView bankRecycleview;

    @Inject
    WalletPresenter mPresenter;

    private List<DealDetailBean> bankCardBeans = new ArrayList<>();
    private BaseQuickAdapter adapter;
    private int pageNum = 1;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_comm_recycleview;
    }

    @Override
    protected void initView() {
        initToolbar();

        bankRecycleview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<DealDetailBean, BaseViewHolder>(R.layout.item_deal_detail, bankCardBeans) {
            @Override
            protected void convert(BaseViewHolder helper, DealDetailBean item) {
                helper.setText(R.id.tv_dealstr, TextUtils.isEmpty(item.deal_time) ? "" : item.deal_time)
                        .setText(R.id.tv_money, TextUtils.isEmpty(item.money) ? "" : item.money);
            }

        };

        bankRecycleview.setAdapter(adapter);
        mPresenter.getDealFlow(pageNum);
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("交易明细")
                .setStatuBar(R.color.white)
                .setLeft(false)
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
    public void onSuccess(Message message) {
        switch (message.what) {
            case WalletPresenter.GET_BANKCARD_OK:
                bankCardBeans.addAll((List<DealDetailBean>) message.obj);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CommonDialog commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
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
