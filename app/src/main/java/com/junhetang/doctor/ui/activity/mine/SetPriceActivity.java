package com.junhetang.doctor.ui.activity.mine;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.UserBaseInfoBean;
import com.junhetang.doctor.ui.contact.PersonalContact;
import com.junhetang.doctor.ui.presenter.PersonalPresenter;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
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
    @BindView(R.id.tv_topicinfo)
    TextView tvTopicinfo;

    @Inject
    PersonalPresenter mPresenter;
    private UserBaseInfoBean bean;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_setprice;
    }

    @Override
    protected void initView() {
        initToolbar();
        //读取sp中持久化
        bean = U.getUserInfo();
        if (bean != null) {
            String str = bean.fee_explain.replace("\\n", "\n");
            tvTopicinfo.setText(str);
            etFirstprice.setText(TextUtils.isEmpty(bean.first_diagnose) ? "" : bean.first_diagnose);
            etFirstprice.setSelection(etFirstprice.getText().length());
        }
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
                || Integer.parseInt(etFirstprice.getText().toString().trim()) < 0) {
            ToastUtil.showShort("请输入初诊资费");
            return;
        }
        mPresenter.setVisitInfo(Integer.parseInt(etFirstprice.getText().toString().trim()) + "");
    }

    @Override
    public void onSuccess(Message message) {
        if (message != null) {
            switch (message.what) {
                case PersonalPresenter.SET_VISITINFO_OK:
                    bean.first_diagnose = Integer.parseInt(etFirstprice.getText().toString().trim()) + "";
                    //修改sp本地数据
                    U.saveUserInfo(new Gson().toJson(bean));
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
