package com.jht.doctor.ui.activity.mine;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.jht.doctor.R;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.ui.bean_jht.UserBaseInfoBean;
import com.jht.doctor.utils.ImageUtil;
import com.jht.doctor.utils.ToastUtil;
import com.jht.doctor.widget.EditTextlayout;
import com.jht.doctor.widget.dialog.CommonDialog;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 认证信息
 * AuthStep4Activity
 * Create at 2018/4/3 下午3:58 by mayakun
 */
public class UseInfoActivity extends BaseActivity {

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

        baseInfoBean = getIntent().getParcelableExtra("userinfo");

        ImageUtil.showCircleImage(baseInfoBean.header, ivHead);
        tvName.setText(TextUtils.isEmpty(baseInfoBean.name) ? "" : baseInfoBean.name);
        tvSex.setText(baseInfoBean.sex == 0 ? "男" : "女");
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("认证信息")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setRightText("预览", true, R.color.color_999)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        ToastUtil.showShort("预览");
                    }
                }).bind();
    }

    private CommonDialog commonDialog;

    @OnClick({R.id.rlt_head, R.id.tv_name, R.id.tv_sex, R.id.rlt_authinfo, R.id.rlt_addinfo})
    public void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.rlt_head:
            case R.id.tv_name:
            case R.id.tv_sex:
                commonDialog = new CommonDialog(this, "认证后头像、姓名、性别无法修改\n" +
                        "如需修改请重新认证");
                commonDialog.show();
                break;
            case R.id.rlt_authinfo:
                startActivity(new Intent(this, AuthStep4Activity.class));
                break;
            case R.id.rlt_addinfo:
                ToastUtil.showShort("完善资料");
                break;

        }

    }

    @Override
    protected void setupActivityComponent() {

    }

}
