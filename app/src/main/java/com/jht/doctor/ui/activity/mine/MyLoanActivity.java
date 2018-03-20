package com.jht.doctor.ui.activity.mine;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.RelativeLayout;

import com.jht.doctor.R;
import com.jht.doctor.ui.adapter.RecyclerAdapter;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.widget.recycle_view.FooterState;
import com.jht.doctor.widget.recycle_view.RecycleWithEmpty;
import com.jht.doctor.widget.recycle_view.UiPauseOnScrollListner;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MyLoanActivity extends BaseAppCompatActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_recycle)
    RecycleWithEmpty idRecycle;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;

    // 服务器端一共多少条数据
    private static final int TOTAL_COUNTER = 50;
    // 每一页展示多少条数据
    private static final int REQUEST_COUNT = 12;
    // 已经获取到多少条数据了
    private int mCurrentCounter = 0;

    private List<String> mDatas;

    private int mState = FooterState.NORMAL;

    private RecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_loan);
        ButterKnife.bind(this);
        initToolbar();
        initEvent();
    }

    private void initEvent() {
        mDatas = new ArrayList<>();
        adapter = new RecyclerAdapter(MyLoanActivity.this);
        idRecycle.setAdapter(adapter);
        idRecycle.setLayoutManager(new LinearLayoutManager(this));
        RelativeLayout emptyView = findViewById(R.id.id_empty_view);
        idRecycle.setEmptyView(emptyView);
        idRecycle.addOnScrollListener(mScrollListener);
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData(true);
                idSwipe.setRefreshing(false);
            }
        });
    }

    private UiPauseOnScrollListner mScrollListener = new UiPauseOnScrollListner(new UiPauseOnScrollListner.LoadMoreListener() {
        @Override
        public void onLoadNextPage(RecyclerView recyclerView) {
            if (mState == FooterState.LOADING) {
                Log.d("@TAG", "the state is Loading, just wait..");
                return;
            }
            if (mCurrentCounter < TOTAL_COUNTER) {
                // loading more
                requestData(false);
                Log.d("TAG", "请求数据");
            } else {
                //the end
                changeState(FooterState.NOMORE);
            }
        }
    });

    @Override
    protected void setupActivityComponent() {

    }

    private void changeState(int state) {
        this.mState = state;
        if (adapter != null && adapter.mFooterHolder != null) {
            adapter.mFooterHolder.setState(state);
        }
    }

    private void requestData(boolean isFirst) {
        if (!isFirst){
            changeState(FooterState.LOADING);
        }
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0x110);
            }
        }.start();
    }

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            changeState(FooterState.NORMAL);
            adapter.addAll(getRemoteData());
        }
    };

    private List<String> getRemoteData() {
        if (mDatas == null) {
            mDatas = new ArrayList<>();
        }
        mDatas.clear();
        for (int i = 0; i < REQUEST_COUNT; i++) {
            if (mDatas.size() + mCurrentCounter >= TOTAL_COUNTER)
                break;
            mDatas.add("数据" + (mCurrentCounter + i));
        }
        mCurrentCounter += mDatas.size();
        return mDatas;
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setTitle("我的借款")
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
}
