package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.config.H5Config;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.injection.components.DaggerFragmentComponent;
import com.junhetang.doctor.injection.modules.FragmentModule;
import com.junhetang.doctor.ui.base.BaseFragment;
import com.junhetang.doctor.ui.bean_jht.CheckPaperBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.nimview.CheckPaperH5Activity;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * CheckPaperFragment 审核列表
 * Create at 2018/5/4 下午3:52 by mayakun
 */
public class CheckPaperFragment extends BaseFragment implements OpenPaperContact.View {

    @BindView(R.id.recyvleview)
    RecyclerView recyvleview;

    @Inject
    OpenPaperPresenter mPresenter;

    private List<CheckPaperBean> checkPaperBeans = new ArrayList<>();
    private int type = 1;//1：我的处方 2：患者处方
    private BaseQuickAdapter mAdapter;

    //根据type，构造fragment
    public static CheckPaperFragment newInstance(int type) {
        CheckPaperFragment fragment = new CheckPaperFragment();
        Bundle args = new Bundle();
        args.putInt("type", type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_checkpaper;
    }

    @Override
    protected void initView() {
        //1：我的处方 2：患者处方
        type = getArguments().getInt("type", 1);

        recyvleview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new BaseQuickAdapter<CheckPaperBean, BaseViewHolder>(R.layout.item_checkpaper, checkPaperBeans) {
            @Override
            protected void convert(BaseViewHolder helper, CheckPaperBean item) {
                helper.setText(R.id.tv_name, item.patient_name + "    " + (item.sex == 0 ? "男" : "女") + "    " + item.age + "岁")
                        .setImageResource(R.id.iv_papertype, item.presc_type == 1 ? R.drawable.icon_phone : R.drawable.icon_camera)
                        .setText(R.id.tv_phone, TextUtils.isEmpty(item.phone) ? "" : item.phone)
                        .setText(R.id.tv_date, "开方日期：" + (TextUtils.isEmpty(item.create_time) ? "" : item.create_time));
            }
        };

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter mAdapter, View view, int position) {
                Intent intent = new Intent(actContext(), CheckPaperH5Activity.class);
                intent.putExtra("hasTopBar", true);//是否包含toolbar
                intent.putExtra("webType", CheckPaperH5Activity.FORM_TYPE.H5_CHECKPAPER);
                intent.putExtra("title", UIUtils.getString(R.string.str_check_paper));
                intent.putExtra("url", H5Config.H5_CHECKPAPER + checkPaperBeans.get(position).id);
                intent.putExtra("checkid", checkPaperBeans.get(position).id);
                startActivity(intent);
            }
        });
//        recyvleview.setAdapter(mAdapter);
        mAdapter.bindToRecyclerView(recyvleview);

        //请求数据
        mPresenter.getCheckPaperList(type);
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
            case OpenPaperPresenter.GET_CHECKPAPERLIST_OK:
                checkPaperBeans.clear();
                List<CheckPaperBean> beans = (List<CheckPaperBean>) message.obj;
                if (beans != null) {
                    checkPaperBeans.addAll(beans);
                }
                mAdapter.notifyDataSetChanged();

                if (checkPaperBeans.isEmpty()) {
                    mAdapter.setEmptyView(R.layout.empty_view);
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.EVENT_KEY_CHECKPAPER_OK://审核提交成功，刷新列表
                    mPresenter.getCheckPaperList(type);
                    break;
            }
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
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
