package com.jht.doctor.ui.activity.mine;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.ui.bean_jht.VisitInfoBean;
import com.jht.doctor.ui.contact.PersonalContact;
import com.jht.doctor.ui.presenter.PersonalPresenter;
import com.jht.doctor.utils.ToastUtil;
import com.jht.doctor.widget.dialog.CommonDialog;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * SetPriceActivity 资费设置
 * Create at 2018/4/12 上午9:43 by mayakun
 */
public class SetPriceActivity extends BaseActivity implements PersonalContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.et_firstprice)
    EditText etFirstprice;
    @BindView(R.id.et_againprice)
    EditText etAgainprice;
    @BindView(R.id.tv_topicinfo)
    TextView tvTopicinfo;

    @Inject
    PersonalPresenter mPresenter;
    private VisitInfoBean bean;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_setprice;
    }

    @Override
    protected void initView() {
        initToolbar();
        mPresenter.getVisitInfo();
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("资费设置")
                .setLeft(false)
                .setStatuBar(R.color.white)
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
                        checkData();
                    }
                }).bind();
    }


    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .applicationComponent(DocApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    private void checkData() {
        if (TextUtils.isEmpty(etFirstprice.getText().toString().trim())
                && Double.parseDouble(etFirstprice.getText().toString().trim()) > 0) {
            ToastUtil.showShort("请输入初诊资费");
            return;
        }
        if (TextUtils.isEmpty(etAgainprice.getText().toString().trim())
                && Double.parseDouble(etAgainprice.getText().toString().trim()) > 0) {
            ToastUtil.showShort("请输入复诊资费");
            return;
        }
        mPresenter.setVisitInfo(etAgainprice.getText().toString().trim(),
                etAgainprice.getText().toString().trim());
    }

    @Override
    public void onSuccess(Message message) {
        if (message != null) {
            switch (message.what) {
                case PersonalPresenter.GET_VISITINFO_OK:
                    bean = (VisitInfoBean) message.obj;
                    if (bean != null) {
                        tvTopicinfo.setText("\u3000\u3000" + bean.fee_explain);
                        etFirstprice.setText(bean.first_diagnose);
                        etAgainprice.setText(bean.second_diagnose);
                    }
                    break;
                case PersonalPresenter.SET_VISITINFO_OK:
                    ToastUtil.showShort("资费设置成功");
                    finish();
                    break;
            }
        }
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
