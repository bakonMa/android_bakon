package com.renxin.doctor.activity.ui.activity.mine;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean_jht.UserBaseInfoBean;
import com.renxin.doctor.activity.ui.contact.PersonalContact;
import com.renxin.doctor.activity.ui.presenter.PersonalPresenter;
import com.renxin.doctor.activity.utils.RegexUtil;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.utils.U;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
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
            etAgainprice.setText(TextUtils.isEmpty(bean.second_diagnose) ? "" : bean.second_diagnose);
            etAgainprice.setSelection(etAgainprice.getText().length());
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
                && Double.parseDouble(etFirstprice.getText().toString().trim()) >= 0) {
            ToastUtil.showShort("请输入初诊资费");
            return;
        }
        if (TextUtils.isEmpty(etAgainprice.getText().toString().trim())
                && Double.parseDouble(etAgainprice.getText().toString().trim()) >= 0) {
            ToastUtil.showShort("请输入复诊资费");
            return;
        }
        mPresenter.setVisitInfo(RegexUtil.formatDoubleMoney(etFirstprice.getText().toString().trim()),
                RegexUtil.formatDoubleMoney(etAgainprice.getText().toString().trim()));
    }

    @Override
    public void onSuccess(Message message) {
        if (message != null) {
            switch (message.what) {
                case PersonalPresenter.SET_VISITINFO_OK:
                    bean.first_diagnose = RegexUtil.formatDoubleMoney(etFirstprice.getText().toString().trim());
                    bean.second_diagnose = RegexUtil.formatDoubleMoney(etAgainprice.getText().toString().trim());
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
