package com.jht.doctor.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.config.PathConfig;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.injection.components.DaggerFragmentComponent;
import com.jht.doctor.injection.modules.FragmentModule;
import com.jht.doctor.ui.activity.loan.BasicInfoActivity;
import com.jht.doctor.ui.activity.mine.FeedbackActivity;
import com.jht.doctor.ui.activity.mine.LoginActivity;
import com.jht.doctor.ui.activity.mine.MyInfoActivity;
import com.jht.doctor.ui.activity.mine.myinfo.MyImageInfoActivity;
import com.jht.doctor.ui.activity.mine.setting.SettingActivity;
import com.jht.doctor.ui.activity.mine.webview.WebViewActivity;
import com.jht.doctor.ui.activity.repayment.MessageActivity;
import com.jht.doctor.ui.activity.repayment.MyAccountActivity;
import com.jht.doctor.ui.activity.welcome.MainActivity;
import com.jht.doctor.ui.base.BaseAppCompatFragment;
import com.jht.doctor.ui.bean.MessageCountBean;
import com.jht.doctor.ui.bean.MyAccountInfoBean;
import com.jht.doctor.ui.bean.MyInfoBean;
import com.jht.doctor.ui.bean.PersonalBean;
import com.jht.doctor.ui.contact.PersonalContact;
import com.jht.doctor.ui.presenter.PersonalPresenter;
import com.jht.doctor.utils.DensityUtils;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.RelativeWithImage;
import com.jht.doctor.widget.dialog.CommonDialog;
import com.jht.doctor.widget.dialog.HaveBalanceDialog;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by table on 2018/1/8.
 * description:
 */

public class WorkRoomFragment extends BaseAppCompatFragment implements PersonalContact.View {

    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_workroom, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        requestData();
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(CustomerApplication.getAppComponent())
                .build().inject(this);
    }

    private void initView() {
    }

    private void requestData() {
        //有token，请求个人资料
        if (!U.isNoToken()) {
        }
    }

    /**
     * 是否跳转用户资料
     */
    private void gotoMyinfo() {

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
