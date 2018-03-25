package com.jht.doctor.view.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jht.doctor.R;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AuthStep3Activity extends BaseAppCompatActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_step3_jht);
        ButterKnife.bind(this);
        initToolbar();
        initView();
    }

    private void initView() {

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
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void setupActivityComponent() {

    }


}
