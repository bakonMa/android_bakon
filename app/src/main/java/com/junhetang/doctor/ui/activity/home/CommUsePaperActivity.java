package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
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
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.CommPaperBean;
import com.junhetang.doctor.ui.bean.CommPaperInfoBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * AddDrugActivity 编辑处方药材
 * Create at 2018/4/26 下午6:15 by mayakun
 */
public class CommUsePaperActivity extends BaseActivity implements OpenPaperContact.View {
    private final int REQUESR_CODE_ADD_COMMMEPAPER = 2020;
    private final int REQUESR_CODE_UPDATA_COMMMEPAPER = 2021;
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
    private BaseQuickAdapter adapter;
    private int formType = 0;//来源 0：普通 1：开方进来选择
    private boolean isEdite = false;//是否是编辑状态
    private CommonDialog commonDialog;
    private int drugStoreId;//药房id
    private int clickTempPos;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_message_commmessage;
    }

    @Override
    protected void initView() {
        //来源
        formType = getIntent().getIntExtra("form", 0);
        drugStoreId = getIntent().getIntExtra("store_id", 0);
        //头部处理
        initToolbar();

        //添加的药材列表处理
        adapter = new BaseQuickAdapter<CommPaperBean, BaseViewHolder>(R.layout.item_comm_usepaper, beans) {
            @Override
            protected void convert(BaseViewHolder helper, CommPaperBean item) {
                helper.setText(R.id.tv_papername, item.title)
                        .setText(R.id.tv_paperremark, item.m_explain)
                        .setChecked(R.id.cb_select, item.isCheck)
                        .setGone(R.id.cb_select, isEdite)
                        //checkbox状态
                        .setOnCheckedChangeListener(R.id.cb_select, new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                item.isCheck = b;
                            }
                        });
            }
        };
        recyclerview.setLayoutManager(new LinearLayoutManager(this));
        recyclerview.setAdapter(adapter);
        recyclerview.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isEdite) {//编辑状态
                    beans.get(position).isCheck = !beans.get(position).isCheck;
                    adapter.notifyItemChanged(position);
                } else {
                    clickTempPos = position;
                    mPresenter.searchDrugPaperById(drugStoreId, beans.get(position).id);
                }
            }
        });

        mPresenter.getOftenmedList();
    }

    //头部处理
    private ToolbarBuilder toolbarBuilder;

    private void initToolbar() {
        toolbarBuilder = ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("常用处方")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        updataDelStatus();
                    }
                }).bind();

        if (formType == 0) {
            toolbarBuilder.setRightText("编辑", true, R.color.color_main);
        }

        rltAdd.setVisibility(formType == 0 ? View.VISIBLE : View.GONE);
        tvAdd.setText("添加常用处方");
    }

    //更新编辑状态
    private void updataDelStatus() {
        isEdite = !isEdite;
        toolbarBuilder.setRightText(isEdite ? "取消" : "编辑", true, R.color.color_main);
        tvDelete.setVisibility(isEdite ? View.VISIBLE : View.GONE);

        if (isEdite == false) {//取消编辑状态
            for (CommPaperBean bean : beans) {
                bean.isCheck = false;
            }
        }
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.rlt_add, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlt_add://添加
                Intent intent = new Intent(actContext(), AddDrugActivity.class);
                intent.putExtra("formtype", 0);
                startActivityForResult(intent, REQUESR_CODE_ADD_COMMMEPAPER);
                break;
            case R.id.tv_delete://删除
                StringBuffer stringBuffer = new StringBuffer();
                for (CommPaperBean bean : beans) {
                    if (bean.isCheck) {
                        stringBuffer.append(bean.id).append(",");
                    }
                }
                if (!TextUtils.isEmpty(stringBuffer)) {
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
            case OpenPaperPresenter.GET_COMMPAPER_LIST_OK://常用处方列表
                beans.clear();
                beans.addAll((List<CommPaperBean>) message.obj);
                adapter.notifyDataSetChanged();
                break;
            case OpenPaperPresenter.DEL_COMMPAPER_OK://删除常用处方 成功
                updataDelStatus();
                mPresenter.getOftenmedList();
                break;
            case OpenPaperPresenter.GET_COMMPAPER_INFO_OK://获取常用处方详情 成功
                Intent intent = new Intent();
                if (formType == 0) {//进入详情，编辑
                    intent.setClass(this, AddDrugActivity.class);
                    intent.putExtra("form", 2);//编辑
                    intent.putExtra("id", beans.get(clickTempPos).id);//id
                    intent.putExtra("title", beans.get(clickTempPos).title);//title
                    intent.putExtra("m_explain", beans.get(clickTempPos).m_explain);//explain
                    intent.putParcelableArrayListExtra("commbean", (ArrayList<CommPaperInfoBean>) message.obj);
                    startActivityForResult(intent, REQUESR_CODE_UPDATA_COMMMEPAPER);
                } else if (formType == 1) {//返回添加药材
                    intent.putParcelableArrayListExtra("commbean", (ArrayList<CommPaperInfoBean>) message.obj);
                    setResult(RESULT_OK, intent);
                    finish();
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUESR_CODE_ADD_COMMMEPAPER://添加药材 刷新
            case REQUESR_CODE_UPDATA_COMMMEPAPER://修改药材 刷新
                mPresenter.getOftenmedList();
                break;
        }
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
