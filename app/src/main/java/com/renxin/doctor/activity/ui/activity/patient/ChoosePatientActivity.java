package com.renxin.doctor.activity.ui.activity.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.ui.adapter.PatientAdapter;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean.PatientBean;
import com.renxin.doctor.activity.ui.contact.PatientContact;
import com.renxin.doctor.activity.ui.presenter.PatientPresenter;
import com.renxin.doctor.activity.widget.SideBar;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * PatientFragment 患者fragment
 * Create at 2018/4/15 下午5:10 by mayakun
 */

public class ChoosePatientActivity extends BaseActivity implements PatientContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.recyvleview)
    RecyclerView recycleView;
    @BindView(R.id.sideBar)
    SideBar sideBar;
    @BindView(R.id.indicator)
    TextView indicator;

    @Inject
    PatientPresenter mPresenter;

    private PatientAdapter mAdapter;
    private List<PatientBean> dataList = new ArrayList<>();
    private CommonDialog commonDialog;

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
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

        recycleView.setLayoutManager(mLayoutManager = new LinearLayoutManager(actContext()));
        mAdapter = new PatientAdapter(actContext(), dataList);
        recycleView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
//        recycleView.addItemDecoration(new DividerItemDecoration(actContext(), RecyclerView.VERTICAL));
        recycleView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                commonDialog = new CommonDialog(ChoosePatientActivity.this,
                        false,
                        ("确认发送给：\n" + dataList.get(position).nick_name),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (view.getId() == R.id.btn_ok) {
                                    Intent intent = new Intent();
                                    intent.putExtra("accid", dataList.get(position).im_accid);
                                    setResult(RESULT_OK, intent);
                                    finish();
                                }
                            }
                        }
                );
                commonDialog.show();
            }
        });

        //是否显示侧边快捷栏
        if (sideBar.getVisibility() == View.VISIBLE) {
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
        }

        mPresenter.getpatientlist();
    }

    private void initToolBar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("发送给患者")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                })
                .bind();
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case PatientPresenter.GET_PATIENTLIST_0K:
                List<PatientBean> beans = (List<PatientBean>) message.obj;
                if (beans != null) {
                    dataList.clear();
                    dataList.addAll(beans);
                    mAdapter.notifyDataSetChanged();
                }
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (commonDialog != null) {
            commonDialog.dismiss();
            commonDialog = null;
        }
    }
}
