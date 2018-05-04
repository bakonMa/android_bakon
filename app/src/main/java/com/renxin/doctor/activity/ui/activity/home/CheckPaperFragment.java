package com.renxin.doctor.activity.ui.activity.home;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerFragmentComponent;
import com.renxin.doctor.activity.injection.modules.FragmentModule;
import com.renxin.doctor.activity.ui.base.BaseFragment;
import com.renxin.doctor.activity.ui.bean_jht.CheckPaperBean;
import com.renxin.doctor.activity.ui.contact.OpenPaperContact;
import com.renxin.doctor.activity.ui.presenter.OpenPaperPresenter;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.trello.rxlifecycle.LifecycleTransformer;

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
                        .setText(R.id.tv_phone, TextUtils.isEmpty(item.phone) ? "" : item.phone)
                        .setText(R.id.tv_date, "开方日期：" + (TextUtils.isEmpty(item.create_time) ? "" : item.create_time));
            }
        };

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter mAdapter, View view, int position) {
                ToastUtil.showShort(checkPaperBeans.get(position).patient_name);
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
                if (checkPaperBeans.isEmpty()) {
                    mAdapter.setEmptyView(R.layout.empty_view);
                } else {
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        ToastUtil.show(errorMsg);
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
