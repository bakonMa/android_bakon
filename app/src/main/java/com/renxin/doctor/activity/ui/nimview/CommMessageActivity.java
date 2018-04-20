package com.renxin.doctor.activity.ui.nimview;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.EventConfig;
import com.renxin.doctor.activity.data.eventbus.Event;
import com.renxin.doctor.activity.data.eventbus.EventBusUtil;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean.CommMessageBean;
import com.renxin.doctor.activity.ui.contact.ChatMessageContact;
import com.renxin.doctor.activity.ui.presenter.ChatMessagePresenter;
import com.renxin.doctor.activity.utils.UIUtils;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * QuickReplyActivity  快速回复
 * Create at 2018/4/19 上午10:42 by mayakun
 */
public class CommMessageActivity extends BaseActivity implements ChatMessageContact.View {

    private int REQUESR_CODE_ADD_COMMMESSAGE = 100;
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.tv_delete)
    TextView tvDelete;

    @BindView(R.id.recycleview_message)
    RecyclerView recycleview;

    @Inject
    ChatMessagePresenter mPresenter;

    private int type;//1:药物常用 2:咨询常用语
    private BaseQuickAdapter adapter;
    private List<CommMessageBean> commMessageBeanList = new ArrayList<>();
    private boolean isEdite = false;
    private ToolbarBuilder toolbarBuilder;
    private CommonDialog commonDialog;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_message_commmessage;
    }

    @Override
    protected void initView() {
        type = getIntent().getIntExtra("type", 1);
        initToolbar();

        recycleview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BaseQuickAdapter<CommMessageBean, BaseViewHolder>(R.layout.item_comm_message, commMessageBeanList) {
            @Override
            protected void convert(BaseViewHolder helper, CommMessageBean item) {
                helper.setText(R.id.tv_messge, item.content)
                        .setChecked(R.id.cb_select, item.isCheck)
                        .setGone(R.id.cb_select, isEdite)
                        //checkbox状态
                        .setOnCheckedChangeListener(R.id.cb_select, new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                                item.isCheck = b;
                            }
                        });
                if (isEdite) {//编辑状态
                    UIUtils.setCompoundDrawable(helper.getView(R.id.tv_messge), 0, 0, R.drawable.icon_dian, Gravity.LEFT);
                } else {
                    UIUtils.setCompoundDrawable(helper.getView(R.id.tv_messge), 10, 5, R.drawable.icon_dian, Gravity.LEFT);
                }
            }
        };
        recycleview.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                if (isEdite) {
                    commMessageBeanList.get(position).isCheck = !commMessageBeanList.get(position).isCheck;
                    adapter.notifyItemChanged(position);
                    return;
                }
                //选中常用语
                EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_CHAT_SELECT_COMMMSG, commMessageBeanList.get(position).content));
                finish();
            }
        });

        mPresenter.getuseful(type);
    }

    //共同头部处理
    private void initToolbar() {
        toolbarBuilder = ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle(type == 1 ? "药物常用语" : "咨询常用语")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setRightText("编辑", true, R.color.color_main)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        onBackPressed();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        updataDelStatus();
                    }
                }).bind();
    }

    //更新编辑状态
    private void updataDelStatus() {
        isEdite = !isEdite;
        toolbarBuilder.setRightText(isEdite ? "取消" : "编辑", true, R.color.color_main);
        tvDelete.setVisibility(isEdite ? View.VISIBLE : View.GONE);
        adapter.notifyDataSetChanged();
    }

    @OnClick({R.id.rlt_add, R.id.tv_delete})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rlt_add://添加
                Intent intent = new Intent(actContext(), AddCommMessageActivity.class);
                intent.putExtra("type", type);
                startActivityForResult(intent, REQUESR_CODE_ADD_COMMMESSAGE);
                break;
            case R.id.tv_delete://删除
                StringBuffer stringBuffer = new StringBuffer();
                for (CommMessageBean bean : commMessageBeanList) {
                    if (bean.isCheck) {
                        stringBuffer.append(bean.id).append(",");
                    }
                }
                if (!TextUtils.isEmpty(stringBuffer)) {
                    commonDialog = new CommonDialog(provideContext(), 1, "确定删除选中的常用语？", btnview -> {
                        if (btnview.getId() == R.id.btn_ok) {
                            mPresenter.deluseful(stringBuffer.toString());
                        }
                    });
                    commonDialog.show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUESR_CODE_ADD_COMMMESSAGE) {
            if (null == data) {
                return;
            }
            CommMessageBean bean = data.getParcelableExtra("commmessage");
            if (bean != null) {
                commMessageBeanList.add(bean);
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case ChatMessagePresenter.GET_USERFUL_OK:
                List<CommMessageBean> beanList = (List<CommMessageBean>) message.obj;
                if (beanList != null) {
                    commMessageBeanList.clear();
                    commMessageBeanList.addAll(beanList);
                    adapter.notifyDataSetChanged();
                }
                break;
            case ChatMessagePresenter.DEL_USERFUL_OK:
                updataDelStatus();
                mPresenter.getuseful(type);
                break;
        }

    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
    }

    //点击返回
    @Override
    public void onBackPressed() {
        if (isEdite) {
            commonDialog = new CommonDialog(provideContext(), 1, "放弃编辑常用语？", view -> {
                if (view.getId() == R.id.btn_ok) {
                    finish();
                }
            });
            commonDialog.show();
        } else {
            finish();
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .applicationComponent(DocApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
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
