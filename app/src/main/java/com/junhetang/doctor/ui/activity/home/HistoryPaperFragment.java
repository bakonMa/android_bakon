package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.junhetang.doctor.config.H5Config;
import com.junhetang.doctor.injection.components.DaggerFragmentComponent;
import com.junhetang.doctor.injection.modules.FragmentModule;
import com.junhetang.doctor.ui.base.BaseFragment;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.CheckPaperBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.nimview.PaperH5Activity;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.KeyBoardUtils;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * HistoryPaperFragment 历史处方
 * Create at 2018/6/26 上午10:22 by mayakun
 */
public class HistoryPaperFragment extends BaseFragment implements OpenPaperContact.View {
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

    private List<CheckPaperBean> checkPaperBeans = new ArrayList<>();
    private BaseQuickAdapter mAdapter;
    private int pageNum = 1;
    private int status = 0;//status：0：全部 1：已支付 -1：未支付 -2：已关闭
    private String searchStr = "";

    //根据type，构造fragment
    //status：0：全部 1：已支付 -1：未支付 -2：已关闭
    public static HistoryPaperFragment newInstance(int status) {
        HistoryPaperFragment fragment = new HistoryPaperFragment();
        Bundle args = new Bundle();
        args.putInt("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_paper_history;
    }

    @Override
    protected void initView() {
        status = getArguments().getInt("status", 0);
        //下拉刷新
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                pageNum = 1;
                mPresenter.getPaperHistoryList(pageNum, status, etSerch.getText().toString().trim());
            }
        });

        recyvleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new BaseQuickAdapter<CheckPaperBean, BaseViewHolder>(R.layout.item_checkpaper, checkPaperBeans) {
            @Override
            protected void convert(BaseViewHolder helper, CheckPaperBean item) {
                if (item.presc_type == 2) {//拍照开方
                    helper.setText(R.id.tv_date, "开方日期：" + (TextUtils.isEmpty(item.create_time) ? "" : item.create_time))
                            .setImageResource(R.id.iv_papertype, R.drawable.icon_camera)
                            .setText(R.id.tv_checkstatus, TextUtils.isEmpty(item.status_name) ? "" : item.status_name);

                    if (item.z_status == 1) {
                        helper.setText(R.id.tv_name, item.patient_name + "    " + (item.sex == 0 ? "男" : "女") + "    " + item.age + "岁")
                                .setText(R.id.tv_phone, TextUtils.isEmpty(item.phone) ? "" : item.phone);
                    } else {
                        helper.setText(R.id.tv_name, "处方正在处理中...")
                                .setText(R.id.tv_phone, "");
                    }
                } else {//在线开发
                    helper.setText(R.id.tv_name, item.patient_name + "    " + (item.sex == 0 ? "男" : "女") + "    " + item.age + "岁")
                            .setText(R.id.tv_phone, TextUtils.isEmpty(item.phone) ? "" : item.phone)
                            .setText(R.id.tv_date, "开方日期：" + (TextUtils.isEmpty(item.create_time) ? "" : item.create_time))
                            .setImageResource(R.id.iv_papertype, R.drawable.icon_phone)
                            .setText(R.id.tv_checkstatus, TextUtils.isEmpty(item.status_name) ? "" : item.status_name);
                }
            }
        };

        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getPaperHistoryList(pageNum + 1, status, searchStr);
            }
        }, recyvleview);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter mAdapter, View view, int position) {
                CheckPaperBean paperBean = checkPaperBeans.get(position);
                boolean canuse = (paperBean.presc_type == 1 || (paperBean.presc_type == 2 && paperBean.z_status == 1));
                Intent intent = new Intent(actContext(), PaperH5Activity.class);
                intent.putExtra("hasTopBar", true);//是否包含toolbar
                intent.putExtra("canuse", canuse);//是否显示【调用此方】
                intent.putExtra("webType", PaperH5Activity.FORM_TYPE.H5_PAPER_DETAIL);
                intent.putExtra("title", UIUtils.getString(R.string.str_paper_detail));
                intent.putExtra("url", H5Config.H5_PAPER_DETAIL + paperBean.id);
                intent.putExtra("checkid", paperBean.id);
                startActivity(intent);
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
                    mPresenter.getPaperHistoryList(pageNum, status, searchStr);
                    return true;
                }
                return false;
            }
        });
        //请求数据
        mPresenter.getPaperHistoryList(pageNum, status, etSerch.getText().toString().trim());
    }

    //搜索监听
    @OnTextChanged(value = R.id.et_serch, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterSearchTextChanged(Editable s) {
        ivClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        if (s.length() == 0 && !TextUtils.isEmpty(searchStr)) {
            searchStr = "";
            //请求数据
            pageNum = 1;
            mPresenter.getPaperHistoryList(pageNum, status, etSerch.getText().toString().trim());
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
                mPresenter.getPaperHistoryList(pageNum, status, searchStr);
                break;
        }
    }


    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
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
            case OpenPaperPresenter.GET_PAPER_HISTORYLIST_OK:
                BasePageBean<CheckPaperBean> beans = (BasePageBean<CheckPaperBean>) message.obj;
                if (beans != null && beans.list != null) {
                    pageNum = beans.page;
                    if (pageNum == 1) {
                        checkPaperBeans.clear();
                    }
                    checkPaperBeans.addAll(beans.list);
                    mAdapter.notifyDataSetChanged();

                    if (beans.is_last == 1) {//最后一页
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.loadMoreComplete();
                    }
                }
                if (checkPaperBeans.isEmpty()) {
                    mAdapter.setEmptyView(R.layout.empty_view);
                }
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CommonDialog commonDialog = new CommonDialog(getActivity(), errorMsg);
        commonDialog.show();
    }


    @Override
    public Activity provideContext() {
        return getActivity();
    }

    @Override
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }

}
