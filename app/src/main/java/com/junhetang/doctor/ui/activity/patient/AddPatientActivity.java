package com.junhetang.doctor.ui.activity.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.OtherBean;
import com.junhetang.doctor.ui.contact.PatientContact;
import com.junhetang.doctor.ui.presenter.PatientPresenter;
import com.junhetang.doctor.widget.EditTextlayout;
import com.junhetang.doctor.widget.dialog.CommSuperDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * AddPatientActivity 手动添加患者（就诊人）
 * Create at 2018/6/13 下午3:30 by mayakun
 */
public class AddPatientActivity extends BaseActivity implements PatientContact.View {

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

    private int sexType = 0;
    private CommSuperDialog commSuperDialog;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_add_jzr;
    }

    @Override
    protected void initView() {
        initToolbar();
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
                .setTitle("添加患者")
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

            commSuperDialog = new CommSuperDialog(this, "请输入患者的全部信息");
            commSuperDialog.show();
        } else {
            Params params = new Params();
            params.put("name", etName.getEditText().getText().toString().trim());
            params.put("age", etAge.getEditText().getText().toString().trim());
            params.put("phone", etPhone.getEditText().getText().toString().trim());
            params.put("sex", sexType);

            mPresenter.addPatient(params);
        }
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case PatientPresenter.ADD_PATIENT_OK://提交后
                OtherBean bean = (OtherBean) message.obj;
                //通知添加患者成功
                EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_ADD_PATIENT));
                if (bean.rcode == 1 || bean.rcode == -2) {//成功、已存在
                    commSuperDialog = new CommSuperDialog(this, bean.msg,
                            "关  闭", "查  看", new CommSuperDialog.ClickListener() {
                        @Override
                        public void btnOnClick(int btnId) {
                            if (btnId == R.id.btn_right) {//进入患者中心
                                Intent intent = new Intent(actContext(), PatientCenterActivity.class);
                                intent.putExtra("memb_no", bean.memb_no);
                                intent.putExtra("isnew", bean.rcode == 1);//重复添加不显示new
                                intent.putExtra("patientname", etName.getEditText().getText().toString().trim());//患者姓名
                                startActivity(intent);
                            }
                            finish();
                        }
                    });
                } else {//只有【确定】
                    commSuperDialog = new CommSuperDialog(this, bean.msg);
                }
                commSuperDialog.show();
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        commSuperDialog = new CommSuperDialog(this, errorMsg);
        commSuperDialog.show();
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(etName.getEditText().getText().toString().trim())
                || !TextUtils.isEmpty(etAge.getEditText().getText().toString().trim())
                || !TextUtils.isEmpty(etPhone.getEditText().getText().toString().trim())) {

            commSuperDialog = new CommSuperDialog(this, "患者信息尚未保存，是否确定退出？", new CommSuperDialog.ClickListener() {
                @Override
                public void btnOnClick(int btnId) {
                    if (btnId == R.id.btn_right) {//点击【确定】
                        finish();
                    }
                }
            });
            commSuperDialog.show();
        } else {
            finish();
        }
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
