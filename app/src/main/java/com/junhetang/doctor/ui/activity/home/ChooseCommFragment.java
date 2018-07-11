package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
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
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.injection.components.DaggerFragmentComponent;
import com.junhetang.doctor.injection.modules.FragmentModule;
import com.junhetang.doctor.ui.base.BaseFragment;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.CommPaperBean;
import com.junhetang.doctor.ui.bean.DrugBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.Constant;
import com.junhetang.doctor.utils.KeyBoardUtils;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;

/**
 * CheckPaperFragment 审核列表
 * Create at 2018/5/4 下午3:52 by mayakun
 */
public class ChooseCommFragment extends BaseFragment implements OpenPaperContact.View {

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

    private List<CommPaperBean> commPaperBeans = new ArrayList<>();
    private int type = 1;//1：常用处方 2：经典处方
    private int drugStoreId;//药房id
    private boolean isCanEdite = false;//经典处方 是否可【置顶】
    private int pageNum = 1;
    private String searchStr = "";
    private BaseQuickAdapter mAdapter;
    private int tempPos;

    //根据type，构造fragment
    public static ChooseCommFragment newInstance(int type, int drugStoreId) {
        ChooseCommFragment fragment = new ChooseCommFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        args.putInt("drugStoreId", drugStoreId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_comm_paper;
    }

    @Override
    protected void initView() {
        //1：常用处方 2：经典处方
        type = getArguments().getInt("type", 1);
        drugStoreId = getArguments().getInt("drugStoreId", 0);
        isCanEdite = getArguments().getBoolean("isCanEdite", false);
        //搜索
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
                    mPresenter.getOftenmedList(pageNum, type, etSerch.getText().toString().trim());
                    return true;
                }
                return false;
            }
        });

        //下拉刷新
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                pageNum = 1;
                mPresenter.getOftenmedList(pageNum, type, etSerch.getText().toString().trim());
            }
        });
        //recycleview展示数据
        recyvleview.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (type == 1) {//常用处方
            mAdapter = new BaseQuickAdapter<CommPaperBean, BaseViewHolder>(R.layout.item_comm_usepaper, commPaperBeans) {
                @Override
                protected void convert(BaseViewHolder helper, CommPaperBean item) {
                    helper.setText(R.id.tv_papername, item.title)
                            .setText(R.id.tv_paperremark, item.m_explain)
                            .setGone(R.id.cb_select, false)
                            .setGone(R.id.iv_right, false);
                }
            };
        } else {//经典处方
            mAdapter = new BaseQuickAdapter<CommPaperBean, BaseViewHolder>(R.layout.item_classics_usepaper, commPaperBeans) {
                @Override
                protected void convert(BaseViewHolder helper, CommPaperBean item) {
                    helper.setText(R.id.tv_papername, item.title)
                            .setGone(R.id.iv_star, isCanEdite)//星是否显示
                            .addOnClickListener(R.id.iv_star);
                    helper.getView(R.id.iv_star).setSelected(item.is_star == 1);//星是否显示

                    //详细药品展示
                    RecyclerView recyclerView = helper.getView(R.id.drug_recycle);
                    recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
                    BaseQuickAdapter adapter = new BaseQuickAdapter<DrugBean, BaseViewHolder>(R.layout.item_classic_text, item.druglist) {
                        @Override
                        protected void convert(BaseViewHolder helper, DrugBean bean) {
                            helper.setText(R.id.tv_drug_name, bean.drug_name + " " + bean.drug_num + bean.unit);
                        }
                    };
                    recyclerView.setAdapter(adapter);
                }
            };
        }


        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getOftenmedList(pageNum + 1, type, searchStr);
            }
        }, recyvleview);

        recyvleview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (!isCanEdite) {
                    mPresenter.searchDrugPaperById(drugStoreId, commPaperBeans.get(position).id,
                            type == 1 ? Constant.PAPER_TYPE_2 : Constant.PAPER_TYPE_3);
                }
            }
        });

        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (isCanEdite) {
                    tempPos = position;
                    mPresenter.classicsPaperUp(commPaperBeans.get(position).id);
                }
            }
        });
        recyvleview.setAdapter(mAdapter);
        //请求数据
        mPresenter.getOftenmedList(pageNum, type, "");
    }

    //搜索监听
    @OnTextChanged(value = R.id.et_serch, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterSearchTextChanged(Editable s) {
        ivClear.setVisibility(s.length() > 0 ? View.VISIBLE : View.GONE);
        if (s.length() == 0 && !TextUtils.isEmpty(searchStr)) {
            searchStr = "";
            //请求数据
            pageNum = 1;
            mPresenter.getOftenmedList(pageNum, type, etSerch.getText().toString().trim());
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
                mPresenter.getOftenmedList(pageNum, type, etSerch.getText().toString().trim());
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
            case OpenPaperPresenter.GET_COMMPAPER_LIST_OK:
                BasePageBean<CommPaperBean> currBeans = (BasePageBean<CommPaperBean>) message.obj;
                if (commPaperBeans != null && currBeans.list != null) {
                    pageNum = currBeans.page;
                    if (pageNum == 1) {
                        commPaperBeans.clear();
                    }
                    commPaperBeans.addAll(currBeans.list);
                    mAdapter.notifyDataSetChanged();

                    if (currBeans.is_last == 1) {//最后一页
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.loadMoreComplete();
                    }
                }
                if (commPaperBeans.isEmpty()) {
                    mAdapter.setEmptyView(R.layout.empty_view);
                }
                break;
            case OpenPaperPresenter.CLASSICSPAPER_UP://经典处方【置顶、取消置顶】
                if (commPaperBeans.get(tempPos).is_star == 0) {
                    ToastUtil.showShort("已置顶处方");
                    commPaperBeans.get(tempPos).is_star = 1;
                } else {
                    ToastUtil.showShort("已取消置顶");
                    commPaperBeans.get(tempPos).is_star = 0;
                }
                mAdapter.notifyItemChanged(tempPos);
                break;
            case OpenPaperPresenter.GET_COMMPAPER_INFO_OK://获取常用处方详情 成功
                EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_CHOOSE_COMM_PAPER, message.obj));
                getActivity().finish();
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
