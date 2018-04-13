package com.renxin.doctor.activity.ui.activity.mine;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * AboutUsActivity 关于
 * Create at 2018/4/13 下午4:59 by mayakun
 */
public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;


    @Override
    protected int provideRootLayout() {
        return R.layout.activity_aboutus;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("关于")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                }).bind();
    }

    @OnClick({R.id.tv_function_introduction, R.id.tv_produce_info, R.id.tv_agreement})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_function_introduction://功能介绍
                break;
            case R.id.tv_produce_info://产品说明
                break;
            case R.id.tv_agreement://用户协议
                break;
        }
    }

    @Override
    protected void setupActivityComponent() {
    }

}
