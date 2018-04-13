package com.renxin.doctor.activity.ui.activity.mine.wallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.data.eventbus.Event;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean.BankCardBean;
import com.renxin.doctor.activity.ui.contact.WalletContact;
import com.renxin.doctor.activity.ui.presenter.present_jht.WalletPresenter;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.renxin.doctor.activity.config.EventConfig;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * AddBankCardActivity  添加银行卡
 * Create at 2018/4/11 下午2:14 by mayakun
 */
public class MyBankCardActivity extends BaseActivity implements WalletContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.bank_recycleview)
    RecyclerView bankRecycleview;


    @Inject
    WalletPresenter mPresenter;
    private int type;//0:银行卡管理 1：选择银行卡
    private List<BankCardBean> bankCardBeans = new ArrayList<>();//银行卡
    private BaseQuickAdapter adapter;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_comm_recycleview;
    }

    @Override
    protected void initView() {
        type = getIntent().getIntExtra("type", 0);
        initToolbar();

        bankRecycleview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<BankCardBean, BaseViewHolder>(R.layout.item_bankcard, bankCardBeans) {
            @Override
            protected void convert(BaseViewHolder helper, BankCardBean item) {
                helper.setText(R.id.tv_bankname, TextUtils.isEmpty(item.ch_name) ? "" : item.ch_name)
                        .setText(R.id.tv_cardnum, TextUtils.isEmpty(item.bank_number) ? "" : item.bank_number);
            }

        };
        bankRecycleview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (type == 0) {
                    Intent intent = new Intent(actContext(), DeleteBankCardActivity.class);
                    intent.putExtra("bankcard", bankCardBeans.get(position));
                    startActivity(intent);
                } else if (type == 1) {//选择银行卡
                    Intent intent = new Intent();
                    intent.putExtra("bankcard", bankCardBeans.get(position));
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        bankRecycleview.setAdapter(adapter);

        mPresenter.userBankList();
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder toolbarBuilder = ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("添加银行卡")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        startActivity(new Intent(actContext(), AddBankCardActivity.class));
                    }
                }).bind();
        //管理银行卡显示 添加
        if (type == 0) {
            toolbarBuilder.setRightImg(R.drawable.icon_add_bank, true);
        }
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
                bankCardBeans.clear();
                bankCardBeans.addAll((List<BankCardBean>) message.obj);
                adapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CommonDialog commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.EVENT_KEY_ADDBANKCARD_OK://添加银行卡成功
                case EventConfig.EVENT_KEY_DELBANKCARD_OK://删除银行卡成功
                    mPresenter.userBankList();
                    break;
            }
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
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
