package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.DrugBean;
import com.junhetang.doctor.ui.bean.PaperInfoBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.widget.EmptyView;
import com.junhetang.doctor.widget.dialog.CommSuperDialog;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle2.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * JzrHistoryListActivity 就诊人历史处方列表
 * Create at 2018/10/16 上午10:47 by mayakun
 */
public class JzrHistoryListActivity extends BaseActivity implements OpenPaperContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @Inject
    OpenPaperPresenter mPresenter;

    private List<PaperInfoBean> paperInfoBeans = new ArrayList<>();
    private BaseQuickAdapter mAdapter;
    private int pageNum = 1;
    private boolean isShowDialog;
    private String mPhone, mName, membNo;//手机号，姓名
    private CommSuperDialog commSuperDialog;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_comm_recycleview;
    }

    @Override
    protected void initView() {
        mPhone = getIntent().getStringExtra("phone");
        mName = getIntent().getStringExtra("name");
        membNo = getIntent().getStringExtra("memb_no");
        //是否弹出提示
        isShowDialog = getIntent().getBooleanExtra("hasdrug", false);
        initToolbar();

        //下拉刷新
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                pageNum = 1;
                mPresenter.getJZRHistoryPaper(pageNum, mPhone, mName, membNo);
            }
        });

        recycleview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<PaperInfoBean, BaseViewHolder>(R.layout.item_patient_paper_medicinal, paperInfoBeans) {
            @Override
            protected void convert(BaseViewHolder helper, PaperInfoBean item) {
                helper.setText(R.id.tv_paper_date, "开方时间：" + (TextUtils.isEmpty(item.create_time) ? "" : item.create_time));
                //详细药品展示
                RecyclerView recyclerView = helper.getView(R.id.drug_recycle);
                recyclerView.setLayoutManager(new GridLayoutManager(actContext(), 4));
                BaseQuickAdapter adapter = new BaseQuickAdapter<DrugBean, BaseViewHolder>(R.layout.item_classic_text, item.param) {
                    @Override
                    protected void convert(BaseViewHolder helper, DrugBean bean) {
                        helper.setText(R.id.tv_drug_name, UIUtils.formateDrugName(bean.drug_name) + " " + bean.drug_num + bean.unit);
                    }
                };
                recyclerView.setAdapter(adapter);
            }
        };
        //加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getJZRHistoryPaper(pageNum + 1, mPhone, mName, membNo);
            }
        }, recycleview);
        //item点击
        recycleview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isShowDialog) {
                    commSuperDialog = new CommSuperDialog(JzrHistoryListActivity.this,
                            "您的处方中已添加药材，载入后将覆盖现有处方，是否确认载入？", new CommSuperDialog.ClickListener() {
                        @Override
                        public void btnOnClick(int btnId) {
                            if (btnId == R.id.btn_right) {//确定
                                goToAddDrug(position);
                            }
                        }
                    });
                    commSuperDialog.show();
                } else {
                    goToAddDrug(position);
                }

                ToastUtil.showCenterToast(paperInfoBeans.get(position).phone);
            }
        });

        recycleview.setAdapter(mAdapter);

        //请求数据
        mPresenter.getJZRHistoryPaper(pageNum, mPhone, mName, membNo);
    }

    //进入编辑药材
    private void goToAddDrug(int pos) {
        if (paperInfoBeans.get(pos).param.isEmpty()) {
            ToastUtil.showCenterToast("次处方没有药材");
            return;
        }

        Intent intent = new Intent(this, AddDrugActivity.class);
        intent.putExtra("form", 3);//同开方
        intent.putExtra("store_id", paperInfoBeans.get(pos).store_id);//药房id
        intent.putParcelableArrayListExtra("druglist", (ArrayList<? extends Parcelable>) paperInfoBeans.get(pos).param);
        startActivity(intent);
    }

    //title设置
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle(TextUtils.isEmpty(mName) ? "历史处方" : mName + "的历史处方")
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
        if (idSwipe.isRefreshing()) {
            idSwipe.setRefreshing(false);
        }
        if (message == null) {
            return;
        }
        switch (message.what) {
            case OpenPaperPresenter.GET_JZR_HISTORY_MEDICINAL:
                BasePageBean<PaperInfoBean> tempBean = (BasePageBean<PaperInfoBean>) message.obj;
                if (tempBean != null && tempBean.list != null) {
                    pageNum = tempBean.page;
                    //第一页 情况
                    if (pageNum == 1) {
                        paperInfoBeans.clear();
                    }
                    paperInfoBeans.addAll(tempBean.list);
                    mAdapter.notifyDataSetChanged();

                    if (tempBean.is_last == 1) {//最后一页
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.loadMoreComplete();
                    }
                }

                if (paperInfoBeans.isEmpty()) {
                    mAdapter.setEmptyView(new EmptyView(this, "该患者尚未有开方记录", true)   );
                }

                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_JZR_EDITE_DRUG://载入处方，关闭画面
                finish();
                break;
        }
    }


    @Override
    public void onError(String errorCode, String errorMsg) {
        CommonDialog commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
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
