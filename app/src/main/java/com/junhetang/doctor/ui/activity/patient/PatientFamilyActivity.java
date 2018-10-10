package com.junhetang.doctor.ui.activity.patient;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.PatientFamilyBean;
import com.junhetang.doctor.ui.contact.PatientContact;
import com.junhetang.doctor.ui.presenter.PatientPresenter;
import com.junhetang.doctor.utils.Constant;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * PatientFamilyActivity 患者列表
 * v1.1.0  修改：只为选择就诊人使用
 * Create at 2018/4/21 下午10:23 by mayakun
 */
public class PatientFamilyActivity extends BaseActivity implements PatientContact.View {
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_class)
    TextView tvClass;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;

    @Inject
    PatientPresenter mPresenter;

    private String membNo;
    private PatientFamilyBean bean;
    private List<PatientFamilyBean.JiuzhenBean> jiuzhenBeans = new ArrayList<>();
    private BaseQuickAdapter mAdapter;
    private String im_accid;//选择就诊人时需要

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_patientfamily;
    }

    @Override
    protected void initView() {
        initToolbar();
        membNo = getIntent().getStringExtra("memb_no");
        im_accid = getIntent().getStringExtra("im_accid");

        recycleview.setLayoutManager(new LinearLayoutManager(actContext()));
        mAdapter = new BaseQuickAdapter<PatientFamilyBean.JiuzhenBean, BaseViewHolder>(R.layout.item_jiuzhen, jiuzhenBeans) {
            @Override
            protected void convert(BaseViewHolder helper, PatientFamilyBean.JiuzhenBean item) {
                helper.setText(R.id.tv_name, item.patient_name)
                        .setText(R.id.tv_relation, Constant.RELATION_TYPE[item.relationship]);
            }
        };
        recycleview.setAdapter(mAdapter);

        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                //选择就诊人
                PatientFamilyBean.JiuzhenBean jzrbean = jiuzhenBeans.get(position);
                jzrbean.id = membNo;//讨巧，后台需要的是患者的id
                jzrbean.phone = bean.patientinfo.phone;//统一使用患者手机号
                jzrbean.setIm_accid(im_accid);
                EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_CHOOSE_PATIENT, jzrbean));
                finish();
            }
        });

        mPresenter.getpatientFamily(membNo);
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("患者中心")
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

    //选择就诊人（默认就诊人）
    @OnClick(R.id.rlt_head)
    public void headOnClick() {
        //选择患者
        PatientFamilyBean.JiuzhenBean jzrBean = new PatientFamilyBean.JiuzhenBean();
        jzrBean.id = membNo;//讨巧，后台需要的是患者的id
        jzrBean.setIm_accid(im_accid);
        jzrBean.patient_name = TextUtils.isEmpty(bean.patientinfo.nick_name) ? "" : bean.patientinfo.nick_name;
        jzrBean.relationship = 0;//0：本人
        jzrBean.sex = bean.patientinfo.sex;
        jzrBean.age = bean.patientinfo.age;
        jzrBean.phone = bean.patientinfo.phone;
        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_CHOOSE_PATIENT, jzrBean));
        finish();
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
                bean = (PatientFamilyBean) message.obj;
                if (bean != null && bean.patientinfo != null) {
                    ImageUtil.showCircleImage(bean.patientinfo.head_url, ivHead);
                    tvName.setText(TextUtils.isEmpty(bean.patientinfo.remark_name) ? bean.patientinfo.nick_name : bean.patientinfo.remark_name);
                    tvPhone.setText("手机号：" + (TextUtils.isEmpty(bean.patientinfo.phone) ? "" : bean.patientinfo.phone));
                    tvClass.setText(TextUtils.isEmpty(bean.patientinfo.memb_class) ? "" : bean.patientinfo.memb_class);
                }
                if (bean != null && bean.jiuzhen != null) {
                    jiuzhenBeans.clear();
                    jiuzhenBeans.addAll(bean.jiuzhen);
                    mAdapter.notifyDataSetChanged();
                }
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
