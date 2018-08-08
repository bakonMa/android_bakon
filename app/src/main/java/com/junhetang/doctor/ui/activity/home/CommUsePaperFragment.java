package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.injection.components.DaggerFragmentComponent;
import com.junhetang.doctor.injection.modules.FragmentModule;
import com.junhetang.doctor.ui.base.BaseFragment;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.CommPaperBean;
import com.junhetang.doctor.ui.bean.CommPaperInfoBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.Constant;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.UmengKey;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.umeng.analytics.MobclickAgent;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * CommUsePaperActivity 常用处方
 * Create at 2018/4/26 下午6:15 by mayakun
 */
public class CommUsePaperFragment extends BaseFragment implements OpenPaperContact.View {
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.rlt_add)
    RelativeLayout rltAdd;
    @BindView(R.id.recycleview)
    RecyclerView recyclerview;
    @BindView(R.id.tv_add)
    TextView tvAdd;
    @BindView(R.id.tv_delete)
    TextView tvDelete;

    @Inject
    OpenPaperPresenter mPresenter;

    private List<CommPaperBean> beans = new ArrayList<>();
    private BaseQuickAdapter mAdapter;
    //    private boolean isEdite = false;//是否是编辑状态
    private CommonDialog commonDialog;
    private int clickTempPos;
    private int pageNum = 1;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_message_commmessage;
    }

    @Override
    protected void initView() {
        idToolbar.setVisibility(View.GONE);
        rltAdd.setVisibility(View.VISIBLE);
        tvAdd.setText("添加常用处方");

        //添加的药材列表处理
        mAdapter = new BaseQuickAdapter<CommPaperBean, BaseViewHolder>(R.layout.item_comm_usepaper, beans) {
            @Override
            protected void convert(BaseViewHolder helper, CommPaperBean item) {
                helper.setText(R.id.tv_papername, item.title)
                        .setText(R.id.tv_paperremark, item.m_explain)
                        .setChecked(R.id.cb_select, item.isCheck)
                        .setGone(R.id.cb_select, getIsEdite())
                        //checkbox状态
                        .setOnCheckedChangeListener(R.id.cb_select, new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                item.isCheck = b;
                            }
                        });
            }
        };
        //上拉加载
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                mPresenter.getOftenmedList(pageNum + 1, 1, "");
            }
        }, recyclerview);

        recyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerview.setAdapter(mAdapter);
        recyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (getIsEdite()) {//编辑状态
                    beans.get(position).isCheck = !beans.get(position).isCheck;
                    adapter.notifyItemChanged(position);
                } else {
                    clickTempPos = position;
                    mPresenter.searchDrugPaperById(0, beans.get(position).id, Constant.PAPER_TYPE_2);
                }
            }
        });

        mPresenter.getOftenmedList(pageNum, 1, "");
    }

    //更新编辑状态
    public void updataDelStatus() {
        if (beans == null || beans.isEmpty()) {
            ToastUtil.show("您还没有常用处方数据");
            return;
        }
        //状态取反
        setIsEdite(!getIsEdite());
        tvDelete.setVisibility(getIsEdite() ? View.VISIBLE : View.GONE);
        rltAdd.setVisibility(getIsEdite() ? View.GONE : View.VISIBLE);
        if (getIsEdite() == false) {//取消编辑状态
            for (CommPaperBean bean : beans) {
                bean.isCheck = false;
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    //获取activity isEdite 状态
    private boolean getIsEdite() {
        return ((CommPaperActivity) getActivity()).isEdite;
    }

    //修改activity isEdite 状态 统一使用
    private void setIsEdite(boolean newIsEdite) {
        ((CommPaperActivity) getActivity()).isEdite = newIsEdite;
        ((CommPaperActivity) getActivity()).updateRightTitle();
    }

    @OnClick({R.id.rlt_add, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlt_add://添加
                //Umeng 埋点
                MobclickAgent.onEvent(getActivity(), UmengKey.commpaper_save);
                Intent intent = new Intent(actContext(), AddDrugActivity.class);
                intent.putExtra("formtype", 0);
                startActivity(intent);
                break;
            case R.id.tv_delete://删除
                StringBuffer stringBuffer = new StringBuffer();
                for (CommPaperBean bean : beans) {
                    if (bean.isCheck) {
                        stringBuffer.append(bean.id).append(",");
                    }
                }
                if (TextUtils.isEmpty(stringBuffer)) {
                    ToastUtil.showShort("请选择要删除的处方");
                } else {
                    commonDialog = new CommonDialog(provideContext(), false, "确定删除选中的常用语处方吗？", btnview -> {
                        if (btnview.getId() == R.id.btn_ok) {
                            mPresenter.delOftenmed(stringBuffer.toString());
                        }
                    });
                    commonDialog.show();
                }
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
        if (message == null) {
            return;
        }
        switch (message.what) {
            case OpenPaperPresenter.GET_COMMPAPER_LIST_OK://常用处方列表
                BasePageBean<CommPaperBean> currBeans = (BasePageBean<CommPaperBean>) message.obj;
                if (beans != null && currBeans.list != null) {
                    pageNum = currBeans.page;
                    if (pageNum == 1) {
                        beans.clear();
                    }
                    beans.addAll(currBeans.list);
                    mAdapter.notifyDataSetChanged();

                    if (currBeans.is_last == 1) {//最后一页
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.loadMoreComplete();
                    }
                }
                if (beans.isEmpty()) {
                    mAdapter.setEmptyView(R.layout.empty_view);
                }
                break;
            case OpenPaperPresenter.DEL_COMMPAPER_OK://删除常用处方 成功
                //删除成功，修改状态
                updataDelStatus();
                pageNum = 1;
                mPresenter.getOftenmedList(pageNum, 1, "");
                break;
            case OpenPaperPresenter.GET_COMMPAPER_INFO_OK://获取常用处方详情 成功
                Intent intent = new Intent();
                intent.setClass(getActivity(), AddDrugActivity.class);
                intent.putExtra("form", 2);//编辑
                intent.putExtra("id", beans.get(clickTempPos).id);//id
                intent.putExtra("title", beans.get(clickTempPos).title);//title
                intent.putExtra("m_explain", beans.get(clickTempPos).m_explain);//explain
                intent.putParcelableArrayListExtra("commbean", (ArrayList<CommPaperInfoBean>) message.obj);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CommonDialog commonDialog = new CommonDialog(getActivity(), errorMsg);
        commonDialog.show();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_ADD_COMMMEPAPER://添加常用处方
                pageNum = 1;
                mPresenter.getOftenmedList(pageNum, 1, "");
                break;
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
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
