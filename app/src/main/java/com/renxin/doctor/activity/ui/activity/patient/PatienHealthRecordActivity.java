package com.renxin.doctor.activity.ui.activity.patient;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean.PatientFamilyBean;
import com.renxin.doctor.activity.ui.contact.PatientContact;
import com.renxin.doctor.activity.ui.presenter.PatientPresenter;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * PatienHealthRecordActivity  健康档案
 * Create at 2018/4/23 下午1:56 by mayakun
 */
public class PatienHealthRecordActivity extends BaseActivity implements PatientContact.View {
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sex_age)
    TextView tvSexAge;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @Inject
    PatientPresenter mPresenter;

    private PatientFamilyBean.JiuzhenBean bean;
    private BaseQuickAdapter mAdapter;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_patient_health_record;
    }

    @Override
    protected void initView() {
        initToolbar();
        bean = getIntent().getParcelableExtra("bean");
        if (bean != null) {
            tvName.setText(TextUtils.isEmpty(bean.patient_name) ? "" : bean.patient_name);
            tvSexAge.setText((bean.sex == 0 ? "男" : "女") + "\\u3000\\u3000\\u3000\\u3000" + bean.age + "岁");
        }

//        recycleview.setLayoutManager(new LinearLayoutManager(actContext()));
//        mAdapter = new BaseQuickAdapter<PatientFamilyBean.JiuzhenBean, BaseViewHolder>(R.layout.item_jiuzhen, jiuzhenBeans) {
//            @Override
//            protected void convert(BaseViewHolder helper, PatientFamilyBean.JiuzhenBean item) {
//                helper.setText(R.id.tv_name, item.patient_name)
//                        .setText(R.id.tv_relation, Constant.RELATION_TYPE[item.relationship]);
//            }
//        };
//        recycleview.setAdapter(mAdapter);
//        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//
//            }
//        });

    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("健康档案")
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

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case PatientPresenter.GET_PATIENTFAMILY_0K:
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {

    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }
}
