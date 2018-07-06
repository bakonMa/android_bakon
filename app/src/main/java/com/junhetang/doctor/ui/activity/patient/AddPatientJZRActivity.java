package com.junhetang.doctor.ui.activity.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.activity.home.JiuZhenHistoryActivity;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.JiuZhenHistoryBean;
import com.junhetang.doctor.ui.contact.PatientContact;
import com.junhetang.doctor.ui.presenter.PatientPresenter;
import com.junhetang.doctor.utils.RegexUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.widget.EditTextlayout;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * AddPatientJZRActivity 手动添加患者（就诊人）
 * Create at 2018/6/13 下午3:30 by mayakun
 */
public class AddPatientJZRActivity extends BaseActivity implements PatientContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.et_name)
    EditTextlayout etName;
    @BindView(R.id.rb_nan)
    RadioButton rbNan;
    @BindView(R.id.rb_nv)
    RadioButton rbNv;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.et_age)
    EditTextlayout etAge;
    @BindView(R.id.et_phone)
    EditTextlayout etPhone;

    @Inject
    PatientPresenter mPresenter;

    private boolean isEdite = false;//是否是编辑
    private JiuZhenHistoryBean bean;
    private CommonDialog commonDialog;
    private int sexType = 0;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_add_jzr;
    }

    @Override
    protected void initView() {
        isEdite = getIntent().getBooleanExtra("isEdite", false);
        bean = getIntent().getParcelableExtra("bean");
        initToolbar();

        //编辑状态 数据展示
        if (isEdite && bean != null) {
            etName.setEditeText(RegexUtil.getNameSubString(bean.patient_name));
            etPhone.setEditeText(TextUtils.isEmpty(bean.phone) ? "" : bean.phone);
            etAge.setEditeText(bean.age > 0 ? bean.age + "" : "");
            sexType = bean.sex;
            rgSex.check(bean.sex == 0 ? R.id.rb_nan : R.id.rb_nv);
            etPhone.setClearImageVisible(false);
            etAge.setClearImageVisible(false);
        }
        //性别
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sexType = (i == R.id.rb_nan ? 0 : 1);
            }
        });
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle(isEdite ? "患者信息" : "添加患者")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        onBackPressed();
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

    @OnClick(R.id.tv_save)
    public void saveOnClick() {
        if (TextUtils.isEmpty(etName.getEditText().getText().toString().trim())
                || TextUtils.isEmpty(etAge.getEditText().getText().toString().trim())
                || TextUtils.isEmpty(etPhone.getEditText().getText().toString().trim())
                ) {
            commonDialog = new CommonDialog(this, true, "请输入患者的全部信息", null);
            commonDialog.show();
        } else {
            Params params = new Params();
            if (isEdite) {
                params.put("id", bean.id);
            }
            params.put("name", etName.getEditText().getText().toString().trim());
            params.put("age", etAge.getEditText().getText().toString().trim());
            params.put("phone", etPhone.getEditText().getText().toString().trim());
            params.put("sex", sexType);

            mPresenter.addPatientJZR(params);
        }
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case PatientPresenter.ADD_PATIENT_JZR_OK:
                ToastUtil.show(isEdite ? "患者信息保存成功" : "添加患者成功");
                startActivity(new Intent(this, JiuZhenHistoryActivity.class));
                finish();
                break;

        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CommonDialog commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (isEdite) {
            if (!etName.getEditText().getText().toString().trim().equals(bean.patient_name)
                    || !etAge.getEditText().getText().toString().trim().equals(bean.age + "")
                    || !etPhone.getEditText().getText().toString().trim().equals(bean.phone)
                    || sexType != bean.sex
                    ) {
                showDialog("患者信息尚未保存，是否确定退出？");
            } else {
                finish();
            }
        } else {
            if (!TextUtils.isEmpty(etName.getEditText().getText().toString().trim())
                    || !TextUtils.isEmpty(etAge.getEditText().getText().toString().trim())
                    || !TextUtils.isEmpty(etPhone.getEditText().getText().toString().trim())) {
                showDialog("患者信息尚未保存，是否确定退出？");
            } else {
                finish();
            }
        }
    }

    /**
     * 展示dialog
     */
    private void showDialog(String dialogStr) {
        commonDialog = new CommonDialog(this, false, dialogStr, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_ok) {
                    finish();
                }
            }
        });
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

}
