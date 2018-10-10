package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.JobScheduleBean;
import com.junhetang.doctor.ui.bean.JobSchedulePatientBean;
import com.junhetang.doctor.ui.contact.WorkRoomContact;
import com.junhetang.doctor.ui.presenter.WorkRoomPresenter;
import com.junhetang.doctor.widget.EmptyView;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.dialog.PatientListDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * JobScheduleActivity 坐诊信息
 * Create at 2018/6/26 上午11:23 by mayakun
 */
public class JobScheduleActivity extends BaseActivity implements WorkRoomContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @Inject
    WorkRoomPresenter mPresenter;

    private List<JobScheduleBean> jobScheduleBeans = new ArrayList<>();
    private BaseQuickAdapter mAdapter;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_comm_recycleview;
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
                mPresenter.getJobScheduleList();
            }
        });

        recycleview.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BaseQuickAdapter<JobScheduleBean, BaseViewHolder>(R.layout.item_job_schedule, jobScheduleBeans) {
            @Override
            protected void convert(BaseViewHolder helper, JobScheduleBean item) {
                helper.setText(R.id.tv_date, TextUtils.isEmpty(item.date) ? "" : item.date)
                        .setText(R.id.tv_hospital_name, TextUtils.isEmpty(item.hospital) ? "" : item.hospital)
                        .setText(R.id.tv_complete_num, item.count + "人")
                        .setText(R.id.tv_cancle_num, item.uncount + "人")
                        .addOnClickListener(R.id.tv_complete_num)
                        .addOnClickListener(R.id.tv_cancle_num);
            }
        };
        //添加title
        mAdapter.addHeaderView(getLayoutInflater().inflate(R.layout.item_job_schedule_title, (ViewGroup) recycleview.getParent(), false));

        //item点击
        mAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                //1：已预约 -1：已取消
                switch (view.getId()) {
                    case R.id.tv_complete_num:
                        if (jobScheduleBeans.get(position).count > 0) {
                            mPresenter.getJobSchedulePatientList(jobScheduleBeans.get(position).date, jobScheduleBeans.get(position).store_id, 1);
                        }
                        break;
                    case R.id.tv_cancle_num:
                        if (jobScheduleBeans.get(position).uncount > 0) {
                            mPresenter.getJobSchedulePatientList(jobScheduleBeans.get(position).date, jobScheduleBeans.get(position).store_id, -1);
                        }
                        break;
                }
            }
        });

        recycleview.setAdapter(mAdapter);

        //请求数据
        mPresenter.getJobScheduleList();
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("我的门诊")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                }).bind();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    public void showListDialog(int type, List<JobSchedulePatientBean> beanList) {
        PatientListDialog patientListDialog = new PatientListDialog(this, type, beanList);
        patientListDialog.show();
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
            case WorkRoomPresenter.GET_JOBSCHEDULE_LIST_OK:
                jobScheduleBeans.clear();
                List<JobScheduleBean> tempBean = (List<JobScheduleBean>) message.obj;
                if (tempBean != null) {
                    jobScheduleBeans.addAll(tempBean);
                    mAdapter.notifyDataSetChanged();
                }

                if (jobScheduleBeans.isEmpty()) {
//                    mAdapter.setEmptyView(R.layout.empty_view, recycleview);
                    mAdapter.setEmptyView(getEmptyView());
                }
                break;
            case WorkRoomPresenter.GET_JOBSCHEDULE_PATIENT_COMPLETE:
                List<JobSchedulePatientBean> beans = (List<JobSchedulePatientBean>) message.obj;
                if (beans != null) {
                    showListDialog(1, beans);
                }
                break;
            case WorkRoomPresenter.GET_JOBSCHEDULE_PATIENT_CANCLE:
                List<JobSchedulePatientBean> cancelBean = (List<JobSchedulePatientBean>) message.obj;
                if (cancelBean != null) {
                    showListDialog(-1, cancelBean);
                }
                break;
        }
    }

    private View getEmptyView() {
        EmptyView emptyView = new EmptyView(this, "暂无门诊数据", true);
        emptyView.setSubTitelText("可能是您近一周无坐诊排班或尚无用户预约您的门诊");
        return emptyView;
    }


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
