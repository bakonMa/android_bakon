package com.junhetang.doctor.ui.activity.mine;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.junhetang.doctor.R;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.UserBaseInfoBean;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.widget.EditableLayout;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * AddUseInfoActivity 完善资料
 * Create at 2018/4/10 上午9:54 by mayakun
 */
public class AddUseInfoActivity extends BaseActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.et_notice)
    EditableLayout etNotice;
    @BindView(R.id.et_intro)
    EditableLayout etIntro;

    private UserBaseInfoBean baseInfoBean;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_adduserinfo;
    }

    @Override
    protected void initView() {


        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("完善资料")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                }).bind();
        //显示公告 简介
        showNoticeAndInro();
    }

    //显示公告 简介
    private void showNoticeAndInro() {
        baseInfoBean = U.getUserInfo();
        if (baseInfoBean != null) {
            if (!TextUtils.isEmpty(baseInfoBean.notice)) {
                etNotice.setText(baseInfoBean.notice);
            }
            if (!TextUtils.isEmpty(baseInfoBean.my_explain)) {
                etIntro.setText(baseInfoBean.my_explain);
            }
        }
    }

    @OnClick({R.id.et_notice, R.id.et_intro})
    public void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.et_notice://公告
                startActivity(new Intent(this, UserNoticeActivity.class));
                break;
            case R.id.et_intro://简介
                startActivity(new Intent(this, UserExplainActivity.class));
                break;
        }
    }

    @Override
    protected void setupActivityComponent() {

    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_UPDATE_NOTICE://公告修改成功
            case EventConfig.EVENT_KEY_UPDATE_EXPLAIN://简介修改成功
                showNoticeAndInro();
                break;
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }
}
