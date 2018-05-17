package com.junhetang.doctor.ui.activity.patient;

import android.app.Activity;
import android.content.Intent;
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
import com.junhetang.doctor.nim.message.SessionHelper;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.PatientFamilyBean;
import com.junhetang.doctor.ui.contact.PatientContact;
import com.junhetang.doctor.ui.presenter.PatientPresenter;
import com.junhetang.doctor.utils.Constant;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.widget.EditableLayout;
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
 * PatientFamilyActivity 患者列表
 * Create at 2018/4/21 下午10:23 by mayakun
 */
public class PatientFamilyActivity extends BaseActivity implements PatientContact.View {
    public static int REQUEST_CODE_REMARKNAME = 2010;
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
    @BindView(R.id.tv_remark)
    TextView tvRemark;
    @BindView(R.id.et_price)
    EditableLayout etPrice;
    @BindView(R.id.recycleview)
    RecyclerView recycleview;
    @BindView(R.id.tv_gotochat)
    TextView tvGotochat;

    @Inject
    PatientPresenter mPresenter;

    private int formType = 0;//是否来自选择患者(0 默认不是 1：选择患者)
    private String membNo;
    private PatientFamilyBean bean;
    private List<PatientFamilyBean.JiuzhenBean> jiuzhenBeans = new ArrayList<>();
    private BaseQuickAdapter mAdapter;
    private CommonDialog commonDialog;
    private String im_accid;//选择就诊人时需要

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_patientfamily;
    }

    @Override
    protected void initView() {
        initToolbar();
        membNo = getIntent().getStringExtra("memb_no");
        formType = getIntent().getIntExtra("formtype", 0);
        im_accid = getIntent().getStringExtra("im_accid");
        //进入咨询
        tvGotochat.setVisibility(formType == 1 ? View.GONE : View.VISIBLE);

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
                //选择患者
                if (formType == 1) {
                    PatientFamilyBean.JiuzhenBean bean = jiuzhenBeans.get(position);
                    bean.id = membNo;//讨巧，后台需要的是患者的id
                    bean.setIm_accid(im_accid);
                    EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_CHOOSE_PATIENT, bean));
                    finish();
                }
                //可能可以点击进入健康档案，需求待定
//                Intent intent = new Intent(actContext(), PatienHealthRecordActivity.class);
//                intent.putExtra("bean", jiuzhenBeans.get(position));
//                startActivity(intent);
            }
        });

        mPresenter.getpatientFamily(membNo);
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("患者列表")
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

    private String tempPrice;

    @OnClick({R.id.et_price, R.id.rlt_head, R.id.tv_gotochat})
    public void btnOnClick(View view) {
        //选择患者时不能修改其他都行
        if (formType == 1) {
            return;
        }
        switch (view.getId()) {
            case R.id.tv_gotochat://进入聊天
                SessionHelper.startP2PSession(actContext(), im_accid);
                break;
            case R.id.rlt_head://设置备注
                Intent intent = new Intent(this, RemarkNameActivity.class);
                intent.putExtra("patientinfo", bean.patientinfo);
                startActivityForResult(intent, REQUEST_CODE_REMARKNAME);
                break;
            case R.id.et_price:
                commonDialog = new CommonDialog(this, false, true,
                        "设置咨询价格(元)", view1 -> {
                    if (view1.getId() == R.id.btn_ok) {
                        tempPrice = commonDialog.getCommonEditText();
                        if (!tempPrice.equals(bean.patientinfo.advisory_fee)) {
                            mPresenter.setPrice(bean.patientinfo.memb_no, TextUtils.isEmpty(tempPrice) ? "0" : tempPrice);
                        }
                    }
                });
                commonDialog.show();
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
            case PatientPresenter.GET_PATIENTFAMILY_0K:
                bean = (PatientFamilyBean) message.obj;
                if (bean != null && bean.patientinfo != null) {
                    ImageUtil.showCircleImage(bean.patientinfo.head_url, ivHead);
                    tvName.setText(TextUtils.isEmpty(bean.patientinfo.nick_name) ? "" : bean.patientinfo.nick_name);
                    tvRemark.setText(TextUtils.isEmpty(bean.patientinfo.remark_name) ? "" : bean.patientinfo.remark_name);
                    tvPhone.setText("手机号：" + (TextUtils.isEmpty(bean.patientinfo.phone) ? "" : bean.patientinfo.phone));
                    tvClass.setText(TextUtils.isEmpty(bean.patientinfo.memb_class) ? "" : bean.patientinfo.memb_class);
                    etPrice.setText(TextUtils.isEmpty(bean.patientinfo.advisory_fee) ? "" : (bean.patientinfo.advisory_fee + "元"));
                }
                if (bean != null && bean.jiuzhen != null) {
                    jiuzhenBeans.clear();
                    jiuzhenBeans.addAll(bean.jiuzhen);
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case PatientPresenter.SET_PRICE_0K://设置咨询价格
                bean.patientinfo.advisory_fee = tempPrice;
                etPrice.setText(TextUtils.isEmpty(bean.patientinfo.advisory_fee) ? "" : (bean.patientinfo.advisory_fee + "元"));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_REMARKNAME) {
            if (data != null) {
                bean.patientinfo.remark_name = getIntent().getStringExtra("remarkname");
                tvRemark.setText(TextUtils.isEmpty(bean.patientinfo.remark_name) ? "" : bean.patientinfo.remark_name);
            }
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
