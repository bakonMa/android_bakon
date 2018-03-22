package com.jht.doctor.ui.activity.repayment;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.MessageBean;
import com.jht.doctor.ui.contact.MessageContact;
import com.jht.doctor.ui.presenter.MessagePresenter;
import com.jht.doctor.utils.DateUtil;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.recycle_view.ItemSlideHelper;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * mayakun 2017/11/20
 * 消息中心
 */
public class MessageActivity extends BaseAppCompatActivity implements ItemSlideHelper.Callback, MessageContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.rv_recyclerView)
    RecyclerView rvRecyclerView;
    @BindView(R.id.sfl_swipe)
    SwipeRefreshLayout sflSwipe;

    @Inject
    MessagePresenter mPresenter;
    private int pageNum = 1;
    private int tempDeletePos = -1;

    private MessageBean messageBean;
    private List<MessageBean.MessageItem> beanList;
    private BaseQuickAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messsage_list);
        ButterKnife.bind(this);
        initToolbar();
        initView();
        mPresenter.getMessageList(pageNum);
        //消除红点【我】画面
        EventBusUtil.sendEvent(new Event(EventConfig.REFRESH_MESSAGE_RED_POINT));

    }

    private void initView() {
        sflSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        //模拟自动刷新
//        sflSwipe.setRefreshing(true);
        if (beanList == null) {
            beanList = new ArrayList<>();
        }

        rvRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter(R.layout.message_item, beanList) {
            @Override
            protected void convert(BaseViewHolder helper, Object item) {
                MessageBean.MessageItem bean = (MessageBean.MessageItem) item;
                helper.setText(R.id.tv_title, TextUtils.isEmpty(bean.msgTitle) ? "" : bean.msgTitle)
                        .setText(R.id.tv_time, TextUtils.isEmpty(bean.createAt) ? "" : DateUtil.formatToYMD(bean.createAt))
                        .setText(R.id.tv_content, TextUtils.isEmpty(bean.msgContent) ? "" : bean.msgContent)
                        .setVisible(R.id.iv_redpoint, !bean.readStatus)
                        .addOnClickListener(R.id.tv_delete);
            }
        };
        //子item点击
        rvRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.tv_delete://删除按钮
                        tempDeletePos = position;
                        mPresenter.deleteMessage(beanList.get(position).id);
                        break;
                }
            }
        });

        adapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getMessageList(++pageNum);
            }
        }, rvRecyclerView);

        //添加滑动 删除菜单
        ItemSlideHelper itemSlideHelper = new ItemSlideHelper(this, this);
        rvRecyclerView.addOnItemTouchListener(itemSlideHelper);

        rvRecyclerView.setAdapter(adapter);

        //下拉刷新
        sflSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                pageNum = 1;
                mPresenter.getMessageList(pageNum);
            }
        });
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("消息中心")
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

    /**************** 滑动删除 start***********************/
    // 此方法用来计算水平方向移动的距离
    @Override
    public int getHorizontalRange(RecyclerView.ViewHolder holder) {
        if (holder.itemView instanceof LinearLayout) {
            ViewGroup viewGroup = (ViewGroup) holder.itemView;
            //viewGroup.getChildAt(1) 对应的 LinearLayout
            return viewGroup.getChildAt(1).getLayoutParams().width;
        }
        return 0;
    }

    //获取当前viewitem
    @Override
    public RecyclerView.ViewHolder getChildViewHolder(View childView) {
        return rvRecyclerView.getChildViewHolder(childView);
    }

    //获取触点所在的view
    @Override
    public View findTargetView(float x, float y) {
        return rvRecyclerView.findChildViewUnder(x, y);
    }

    /**************** 滑动删除 end***********************/

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        //删除
        if (message.what == MessagePresenter.MESSAGE_DELETE) {
            beanList.remove(tempDeletePos);
            adapter.notifyDataSetChanged();
            if (beanList.isEmpty()) {
                adapter.setEmptyView(R.layout.empty_view);
            }
            return;
        }
        //刷新或下一页
        if (message.obj == null) {
            if (sflSwipe.isRefreshing()) {
                sflSwipe.setRefreshing(false);
            }
            if (beanList.isEmpty()) {
                adapter.setEmptyView(R.layout.empty_view);
                adapter.setEnableLoadMore(false);
            }
            return;
        }
        messageBean = (MessageBean) message.obj;
        if (pageNum == 1) {
            //刷新
            sflSwipe.setRefreshing(false);
            beanList.clear();
            //添加空白页
            if (messageBean.data.isEmpty()) {
                adapter.setEmptyView(R.layout.empty_view);
                return;
            }
        }

        if (messageBean.data != null && !messageBean.data.isEmpty()) {
            beanList.addAll(messageBean.data);
            adapter.notifyDataSetChanged();
        }
        if (messageBean.data.size() < U.PAGE_SIZE) {
            adapter.loadMoreEnd();
        } else {
            adapter.loadMoreComplete();
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        if (pageNum == 1) {
            sflSwipe.setRefreshing(false);
        } else {
            adapter.loadMoreFail();
        }
        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
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
