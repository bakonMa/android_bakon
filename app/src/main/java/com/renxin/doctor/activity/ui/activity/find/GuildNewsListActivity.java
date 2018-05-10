package com.renxin.doctor.activity.ui.activity.find;

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
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.ui.activity.WebViewActivity;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.base.BasePageBean;
import com.renxin.doctor.activity.ui.bean.NewsInfoBean;
import com.renxin.doctor.activity.ui.contact.FindContact;
import com.renxin.doctor.activity.ui.presenter.present_jht.FindPresenter;
import com.renxin.doctor.activity.utils.ImageUtil;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * GuildNewsListActivity 行业追踪
 * Create at 2018/5/10 上午10:27 by mayakun
 */
public class GuildNewsListActivity extends BaseActivity implements FindContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @Inject
    FindPresenter mPresenter;

    private List<NewsInfoBean> dealDetailBeans = new ArrayList<>();
    private BaseQuickAdapter mAdapter;
    private int pageNum = 1;
    private int type = 0;//资讯分类：0行业追踪 1健康教育

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_comm_recycleview;
    }

    @Override
    protected void initView() {
        type = getIntent().getIntExtra("type", 0);
        initToolbar();

        //下拉刷新
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                pageNum = 1;
                mPresenter.getNewsList(type, pageNum);
            }
        });

        recycleview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<NewsInfoBean, BaseViewHolder>(R.layout.item_news_info, dealDetailBeans) {
            @Override
            protected void convert(BaseViewHolder helper, NewsInfoBean item) {
                helper.setText(R.id.tv_title, TextUtils.isEmpty(item.title) ? "" : item.title)
                        .setText(R.id.tv_des, TextUtils.isEmpty(item.description) ? "" : item.description);

                ImageUtil.showImage(item.thumbnail, helper.getView(R.id.iv_image));
            }
        };
        //加载更多
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getNewsList(type, pageNum + 1);
            }
        }, recycleview);
        //item点击
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                WebViewActivity.startAct(actContext(),
                        true,
                        WebViewActivity.WEB_TYPE.WEB_TYPE_NEWS,
                        dealDetailBeans.get(position).title,
                        dealDetailBeans.get(position).url
                );
            }
        });

        recycleview.setAdapter(mAdapter);

        //请求数据
        idSwipe.setRefreshing(true);
        mPresenter.getNewsList(type, pageNum);
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle(type == 0 ? "行业追踪" : "健康教育")
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
        if (message == null) {
            return;
        }
        switch (message.what) {
            case FindPresenter.GET_NEWS_OK:
                if (idSwipe.isRefreshing()) {
                    idSwipe.setRefreshing(false);
                }

                BasePageBean<NewsInfoBean> tempBean = (BasePageBean<NewsInfoBean>) message.obj;
                if (tempBean != null && tempBean.list != null) {
                    pageNum = tempBean.page;
                    //第一页 情况
                    if (pageNum == 1) {
                        dealDetailBeans.clear();
                    }
                    dealDetailBeans.addAll(tempBean.list);
                    mAdapter.notifyDataSetChanged();

                    if (tempBean.is_last == 1) {//最后一页
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.loadMoreComplete();
                    }
                }

                if (dealDetailBeans.isEmpty()) {
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
