package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.PatientFamilyBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.KeyBoardUtils;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * HistoryPaperActivity 历史处方
 * Create at 2018/5/7 下午3:11 by mayakun
 */
public class JZRListActivity extends BaseActivity implements OpenPaperContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.recycleview)
    RecyclerView recyvleview;
    @BindView(R.id.et_serch)
    EditText etSerch;
    @BindView(R.id.iv_clear)
    ImageView ivClear;

    @Inject
    OpenPaperPresenter mPresenter;

    private List<PatientFamilyBean.JiuzhenBean> jiuzhenBeans = new ArrayList<>();
    private BaseQuickAdapter mAdapter;
    private int pageNum = 1;
    private String searchStr = "";

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_choose_jzr;
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
                mPresenter.getJZRList(pageNum, etSerch.getText().toString().trim());
            }
        });

        recyvleview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<PatientFamilyBean.JiuzhenBean, BaseViewHolder>(R.layout.item_jiuzhen_history, jiuzhenBeans) {
            @Override
            protected void convert(BaseViewHolder helper, PatientFamilyBean.JiuzhenBean item) {
                helper.setText(R.id.tv_name, TextUtils.isEmpty(item.patient_name) ? "" : item.patient_name)
                        .setText(R.id.tv_sex_age, (item.sex == 0 ? "男" : "女") + "        " + item.age + "岁")
                        .setText(R.id.tv_phone, TextUtils.isEmpty(item.phone) ? "" : item.phone);
            }
        };

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getJZRList(pageNum + 1, searchStr);
            }
        }, recyvleview);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter mAdapter, View view, int position) {
                EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_CHOOSE_JZR, jiuzhenBeans.get(position)));
                finish();

            }
        });

        recyvleview.setAdapter(mAdapter);

        etSerch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                KeyBoardUtils.hideKeyBoard(etSerch, actContext());
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    if (TextUtils.isEmpty(etSerch.getText().toString().trim())
                            || !searchStr.equals(etSerch.getText().toString().trim())) {
                        pageNum = 1;
                    }
                    searchStr = etSerch.getText().toString().trim();
                    mPresenter.getJZRList(pageNum, searchStr);
                    return true;
                }
                return false;
            }
        });
        //请求数据
        mPresenter.getJZRList(pageNum, etSerch.getText().toString().trim());
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("选择就诊人")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {//返回
                        super.leftClick();
                        finish();
                    }
                }).bind();
    }

    //搜索监听
    @OnTextChanged(value = R.id.et_serch, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterSearchTextChanged(Editable s) {
        ivClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        if (s.length() == 0 && !TextUtils.isEmpty(searchStr)) {
            searchStr = "";
            //请求数据
            pageNum = 1;
            mPresenter.getJZRList(pageNum, etSerch.getText().toString().trim());
        }
    }

    @OnClick({R.id.iv_clear, R.id.tv_search})
    public void cleanClick(View view) {
        switch (view.getId()) {
            case R.id.iv_clear:
                etSerch.setText("");
                break;
            case R.id.tv_search:
                KeyBoardUtils.hideKeyBoard(etSerch, actContext());
                if (TextUtils.isEmpty(etSerch.getText().toString().trim())
                        || !searchStr.equals(etSerch.getText().toString().trim())) {
                    pageNum = 1;
                }
                searchStr = etSerch.getText().toString().trim();
                mPresenter.getJZRList(pageNum, searchStr);
                break;
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
        if (idSwipe.isRefreshing()) {
            idSwipe.setRefreshing(false);
        }
        if (message == null) {
            return;
        }
        switch (message.what) {
            case OpenPaperPresenter.GET_JZR_LIST:
                BasePageBean<PatientFamilyBean.JiuzhenBean> beans = (BasePageBean<PatientFamilyBean.JiuzhenBean>) message.obj;
                if (beans != null && beans.list != null) {
                    pageNum = beans.page;
                    if (pageNum == 1) {
                        jiuzhenBeans.clear();
                    }
                    jiuzhenBeans.addAll(beans.list);
                    mAdapter.notifyDataSetChanged();

                    if (beans.is_last == 1) {//最后一页
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.loadMoreComplete();
                    }
                }
                if (jiuzhenBeans.isEmpty()) {
                    mAdapter.setEmptyView(R.layout.empty_view, recyvleview);
                }
                break;
        }
    }

//    @Override
//    protected void onNewIntent(Intent intent) {
//        super.onNewIntent(intent);
//        //刷新数据
//        pageNum = 1;
//        mPresenter.getJZRList(pageNum, etSerch.getText().toString().trim());
//    }

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
