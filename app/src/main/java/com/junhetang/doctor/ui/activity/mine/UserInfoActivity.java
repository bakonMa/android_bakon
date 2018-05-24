package com.junhetang.doctor.ui.activity.mine;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.config.H5Config;
import com.junhetang.doctor.ui.activity.WebViewActivity;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.UserBaseInfoBean;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.widget.EditTextlayout;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 认证信息
 * AuthStep4Activity
 * Create at 2018/4/3 下午3:58 by mayakun
 */
public class UserInfoActivity extends BaseActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.iv_head)
    ImageView ivHead;
    @BindView(R.id.tv_name)
    EditTextlayout tvName;
    @BindView(R.id.tv_sex)
    EditTextlayout tvSex;

    private UserBaseInfoBean baseInfoBean;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_userinfo;
    }

    @Override
    protected void initView() {
        initToolbar();

        baseInfoBean = U.getUserInfo();
        if (baseInfoBean != null) {
            ImageUtil.showCircleImage(baseInfoBean.header, ivHead);
            tvName.setText(TextUtils.isEmpty(baseInfoBean.name) ? "" : baseInfoBean.name);
            tvSex.setText(baseInfoBean.sex == 0 ? "男" : "女");
        }
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("认证信息")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setRightText("预览", true, R.color.color_main)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        //个人信息预览
                        WebViewActivity.startAct(actContext(), true, WebViewActivity.WEB_TYPE.WEB_TYPE_MYINFO, H5Config.H5_USERINFO_TITLE, H5Config.H5_USERINFO);
                    }
                }).bind();
    }

    @OnClick({R.id.rlt_head, R.id.tv_name, R.id.tv_sex, R.id.rlt_authinfo, R.id.rlt_addinfo})
    public void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.rlt_head:
            case R.id.tv_name:
            case R.id.tv_sex:
                CommonDialog commonDialog = new CommonDialog(this, "认证后头像、姓名、性别无法修改\n" +
                        "如需修改请重新认证");
                commonDialog.show();
                break;
            case R.id.rlt_authinfo:
                startActivity(new Intent(this, AuthStep4Activity.class));
                break;
            case R.id.rlt_addinfo:
                startActivity(new Intent(this, AddUseInfoActivity.class));
                break;
        }

    }

    @Override
    protected void setupActivityComponent() {

    }

}
