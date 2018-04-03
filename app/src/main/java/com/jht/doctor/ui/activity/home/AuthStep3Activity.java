package com.jht.doctor.ui.activity.home;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jht.doctor.R;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * 认证审核中
 * AuthStep3Activity
 * Create at 2018/4/3 下午3:58 by mayakun
 */
public class AuthStep3Activity extends BaseActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_auth_step3_jht;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("认证")
                .setStatuBar(R.color.white)
                .setLeft(false)
//                .setRightText("认证", true, R.color.color_popup_btn)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                }).bind();
    }


    @OnClick({R.id.tv_back})
    public void tabOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_back:
                finish();
                break;


        }
    }


    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected void setupActivityComponent() {

    }


}
