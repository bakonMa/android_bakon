package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.H5Config;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.activity.WebViewActivity;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.SystemMsgBean;
import com.junhetang.doctor.ui.contact.WorkRoomContact;
import com.junhetang.doctor.ui.presenter.WorkRoomPresenter;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * SystemMsgListActivity 系统消息
 * Create at 2018/5/11 下午4:42 by mayakun
 */
public class SystemMsgListActivity extends BaseActivity implements WorkRoomContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @Inject
    WorkRoomPresenter mPresenter;

    private List<SystemMsgBean> systemMsgBeans = new ArrayList<>();
    private BaseQuickAdapter mAdapter;
    private int pageNum = 1;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_comm_recycleview;
    }

    @Override
    protected void initView() {
        initToolbar();

        //下拉刷新
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                pageNum = 1;
                mPresenter.getSystemMsgList(pageNum);
            }
        });

        recycleview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<SystemMsgBean, BaseViewHolder>(R.layout.item_news_info, systemMsgBeans) {
            @Override
            protected void convert(BaseViewHolder helper, SystemMsgBean item) {
                helper.setText(R.id.tv_title, TextUtils.isEmpty(item.title) ? "" : item.title)
                        .setText(R.id.tv_des, TextUtils.isEmpty(item.summary) ? "" : item.summary);

                ImageUtil.showImage(item.thumbnail, helper.getView(R.id.iv_image));
            }
        };
        //加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getSystemMsgList(pageNum + 1);
            }
        }, recycleview);
        //item点击
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WebViewActivity.startAct(actContext(),
                        true,
                        WebViewActivity.WEB_TYPE.WEB_TYPE_SYSTEMMSG,
                        systemMsgBeans.get(position).title,
                        H5Config.H5_SYSTEM_MSG + systemMsgBeans.get(position).id
                );
            }
        });

        recycleview.setAdapter(mAdapter);

        //请求数据
//        idSwipe.setRefreshing(true);
        mPresenter.getSystemMsgList(pageNum);
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("系统消息")
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
            case WorkRoomPresenter.GET_SYSTEMMSG_LIST_OK:
                BasePageBean<SystemMsgBean> tempBean = (BasePageBean<SystemMsgBean>) message.obj;
                if (tempBean != null && tempBean.list != null) {
                    pageNum = tempBean.page;
                    //第一页 情况
                    if (pageNum == 1) {
                        systemMsgBeans.clear();
                    }
                    systemMsgBeans.addAll(tempBean.list);
                    mAdapter.notifyDataSetChanged();

                    if (tempBean.is_last == 1) {//最后一页
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.loadMoreComplete();
                    }
                }

                if (systemMsgBeans.isEmpty()) {
                    mAdapter.setEmptyView(R.layout.empty_view);
                }

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
