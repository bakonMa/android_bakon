package com.renxin.doctor.activity.ui.activity.patient;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean.PatientFamilyBean;
import com.renxin.doctor.activity.ui.contact.PatientContact;
import com.renxin.doctor.activity.ui.presenter.PatientPresenter;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
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
                .setRightText("保存", true, R.color.color_999)
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
        ToastUtil.showShort("设置备注成功");
        Intent intent = new Intent();
        intent.putExtra("remarkname", edRemarkname.getText().toString().trim());
        setResult(RESULT_OK, intent);
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
