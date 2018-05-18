package com.junhetang.doctor.ui.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.injection.components.DaggerFragmentComponent;
import com.junhetang.doctor.injection.modules.FragmentModule;
import com.junhetang.doctor.ui.activity.patient.PatientFamilyActivity;
import com.junhetang.doctor.ui.adapter.PatientAdapter;
import com.junhetang.doctor.ui.base.BaseFragment;
import com.junhetang.doctor.ui.bean.PatientBean;
import com.junhetang.doctor.ui.contact.PatientContact;
import com.junhetang.doctor.ui.presenter.PatientPresenter;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.widget.SideBar;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * PatientFragment 患者fragment
 * Create at 2018/4/15 下午5:10 by mayakun
 */

public class PatientFragment extends BaseFragment implements PatientContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.recyvleview)
    RecyclerView recycleView;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.sideBar)
    SideBar sideBar;
    @BindView(R.id.indicator)
    TextView indicator;

    @Inject
    PatientPresenter mPresenter;

    private PatientAdapter mAdapter;
    private List<PatientBean> dataList = new ArrayList<>();

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_patient;
    }


    private LinearLayoutManager mLayoutManager;

    @Override
    protected void initView() {
        initToolBar();
        //下拉刷新
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //刷新数据
                mPresenter.getPatientlist();
            }
        });

        recycleView.setLayoutManager(mLayoutManager = new LinearLayoutManager(actContext()));
        mAdapter = new PatientAdapter(actContext(), dataList);
        recycleView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
//        recycleView.addItemDecoration(new DividerItemDecoration(actContext(), RecyclerView.VERTICAL));
        recycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(actContext(), PatientFamilyActivity.class);
                intent.putExtra("memb_no", dataList.get(position).memb_no);
                intent.putExtra("im_accid", dataList.get(position).im_accid);
                startActivity(intent);
            }
        });

        //是否显示侧边快捷栏
        sideBar.setTextView(indicator);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s, int offsetY) {
                int position = mAdapter.getPositionForSection(s.charAt(0));
                indicator.setText(s);
//                if (offsetY > indicator.getHeight() / 2 && offsetY + indicator.getHeight() / 2 < sideBar.getHeight())
//                    indicator.setTranslationY(offsetY - indicator.getHeight() / 2 + sideBar.getTop());
                if (position != -1) {
                    mLayoutManager.scrollToPositionWithOffset(position, 0);
                }
            }
        });

        mPresenter.getPatientlist();
    }

    /**
     * fragment 是否隐藏
     *
     * @param hidden false 前台显示 true 隐藏
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresenter.getPatientlist();
        }
    }

    private void initToolBar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(getActivity()))
                .setTitle("患者列表")
                .setStatuBar(R.color.white)
//                .setRightImg(R.drawable.icon_add_main, true)
                .blank()
                .setLeft(false)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void rightClick() {
                        super.rightClick();
                        ToastUtil.show("search");
                    }
                })
                .bind();
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
            case PatientPresenter.GET_PATIENTLIST_0K:
                List<PatientBean> beans = (List<PatientBean>) message.obj;
                if (beans != null) {
                    dataList.clear();
                    dataList.addAll(beans);
                    Collections.sort(dataList);
                    mAdapter.notifyDataSetChanged();
                }
                sideBar.setVisibility(dataList.isEmpty() ? View.GONE : View.VISIBLE);
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
            case EventConfig.EVENT_KEY_REMARKNAME://备注修改
                mPresenter.getPatientlist();
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
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }

}
