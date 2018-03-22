package com.jht.doctor.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.injection.components.DaggerFragmentComponent;
import com.jht.doctor.injection.modules.FragmentModule;
import com.jht.doctor.ui.activity.mine.LoginActivity;
import com.jht.doctor.ui.activity.mine.bankcard.SupportBankActivity;
import com.jht.doctor.ui.base.BaseAppCompatFragment;
import com.jht.doctor.ui.bean.PersonalBean;
import com.jht.doctor.ui.contact.PersonalContact;
import com.jht.doctor.utils.U;
import com.jht.doctor.view.activity.AuthStep1Activity;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by table on 2018/1/8.
 * description:
 */

public class WorkRoomFragment extends BaseAppCompatFragment implements PersonalContact.View {

    @BindView(R.id.viewflipper)
    ViewFlipper viewflipper;
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;

    private String[] strings = {"fragment_workroom1", "fragment_workroom2", "fragment_workroom3"};

    @Override
    protected View setViewId(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workroom, null);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        initToolbar();
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_main, null);
            ((TextView) view.findViewById(R.id.tv_num)).setText(strings[i]);
            viewflipper.addView(view);
        }
        requestData();
    }

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build().inject(this);
    }


    private void requestData() {

    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(getActivity()))
                .setTitle("工作室")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setRightText("认证", true, R.color.color_popup_btn)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        startActivity(new Intent(getContext(), AuthStep1Activity.class));
                    }
                }).bind();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.REFRESH_PERSONAL://刷新个人资料
                    requestData();
                    break;
                case EventConfig.REFRESH_MESSAGE_RED_POINT://红点消除
//                    idRedPoint.setVisibility(View.GONE);
                    break;
            }
        }
    }

    /**
     * 显示手机号姓名
     *
     * @param personalBean
     */
    private void showPersonalInfo(PersonalBean personalBean) {
        if (personalBean != null) {
            //手机号存在sp中
        }
    }

    /**
     * 展示账户信息
     */
    private void showAccountInfo() {
    }

    private void jumpTo(Intent intent) {
        if (U.isNoToken()) {
            intent.setClass(actContext(), LoginActivity.class);
            intent.putExtra(LoginActivity.FROM_KEY, LoginActivity.PERSONAL_ACTIVITY);
        }
        startActivity(intent);
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
    }

    @Override
    public void onSuccess(Message message) {

    }

    @Override
    public Activity provideContext() {
        return getActivity();
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }

}
