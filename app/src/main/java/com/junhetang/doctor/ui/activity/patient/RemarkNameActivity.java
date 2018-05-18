package com.junhetang.doctor.ui.activity.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;

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
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * RemarkNameActivity 设置患者备注
 * Create at 2018/4/23 上午10:18 by mayakun
 */
public class RemarkNameActivity extends BaseActivity implements PatientContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.ed_remarkname)
    EditText edRemarkname;

    @Inject
    PatientPresenter mPresenter;

    private PatientFamilyBean.PatientinfoBean patientinfoBean;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_remarkname;
    }

    @Override
    protected void initView() {
        initToolbar();
        patientinfoBean = getIntent().getParcelableExtra("patientinfo");
        if (patientinfoBean != null) {
            edRemarkname.setText(TextUtils.isEmpty(patientinfoBean.remark_name) ? "" : patientinfoBean.remark_name);
        }
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("设置备注")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setRightText("保存", true, R.color.color_main)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        mPresenter.setRemarkName(patientinfoBean.im_accid, patientinfoBean.memb_no, edRemarkname.getText().toString().trim());
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
        if(message == null) {
            return;
        }
        ToastUtil.showShort("设置备注成功");
        Intent intent = new Intent();
        intent.putExtra("remarkname", edRemarkname.getText().toString().trim());
        setResult(RESULT_OK, intent);
        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_REMARKNAME));
        finish();
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
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }

}
