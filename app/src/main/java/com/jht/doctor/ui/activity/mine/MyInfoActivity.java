package com.jht.doctor.ui.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.ui.activity.mine.myinfo.ShowMyInfoActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.ConfigBean;
import com.jht.doctor.ui.bean.MyInfoBean;
import com.jht.doctor.ui.presenter.MyInfoPresenter;
import com.jht.doctor.widget.RelativeWithText;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyInfoActivity extends BaseAppCompatActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_basic_info)
    RelativeWithText idBasicInfo;
    @BindView(R.id.id_job_info)
    RelativeWithText idJobInfo;
    @BindView(R.id.id_house_info)
    RelativeWithText idHouseInfo;

    @Inject
    MyInfoPresenter mPresenter;
    private MyInfoBean myInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_info);
        ButterKnife.bind(this);
        initToolbar();

        myInfoBean = getIntent().getParcelableExtra(PersonalActivity.KEY_INFO);
        if (myInfoBean == null) {
            CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("数据异常，请返回重试");
        }
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setTitle("我的资料")
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

    @Override
    protected void setupActivityComponent() {

    }

    ConfigBean bean;
    Intent intent = new Intent();

    @OnClick({R.id.id_basic_info, R.id.id_job_info, R.id.id_house_info})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_basic_info:
                intent.setClass(this, ShowMyInfoActivity.class);
                intent.putExtra("type", 0);
                intent.putExtra(PersonalActivity.KEY_INFO, myInfoBean);
                startActivity(intent);
                break;
            case R.id.id_job_info:
                intent.setClass(this, ShowMyInfoActivity.class);
                intent.putExtra("type", 1);
                intent.putExtra(PersonalActivity.KEY_INFO, myInfoBean);
                startActivity(intent);
                break;
            case R.id.id_house_info:
                intent.setClass(this, ShowMyInfoActivity.class);
                intent.putExtra("type", 2);
                intent.putExtra(PersonalActivity.KEY_INFO, myInfoBean);
                startActivity(intent);
                break;
        }
    }

}
