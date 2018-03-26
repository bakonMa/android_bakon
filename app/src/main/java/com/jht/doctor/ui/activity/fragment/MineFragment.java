package com.jht.doctor.ui.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.ui.activity.home.AuthStep1Activity;
import com.jht.doctor.ui.base.BaseFragment;
import com.jht.doctor.ui.contact.PersonalContact;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * MineFragment
 * Create at 2018/3/25 下午5:37 by mayakun
 */

public class MineFragment extends BaseFragment implements PersonalContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;

    @Override
    protected void setupActivityComponent() {
//        DaggerFragmentComponent.builder()
//                .fragmentModule(new FragmentModule(this))
//                .applicationComponent(DocApplication.getAppComponent())
//                .build().inject(this);
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initView() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(getActivity()))
                .setTitle("个人中心")
                .setStatuBar(R.color.white)
                .blank()
                .setLeft(false)
//                .setRightText("认证", true, R.color.color_popup_btn)
                .bind();
    }


//    private void requestData() {
//        //有token，请求个人资料
//        if (!U.isNoToken()) {
//            mPresenter.getPersonalInfo();
//        }
//    }

    @OnClick({R.id.id_check_status, R.id.id_collect, R.id.id_wallet, R.id.id_set_price,
            R.id.id_setting, R.id.id_recommend, R.id.id_about, R.id.id_btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_check_status:
                startActivity(new Intent(actContext(), AuthStep1Activity.class));
                break;
            case R.id.id_collect:
                break;
            case R.id.id_wallet:
                break;
            case R.id.id_set_price:
                break;
            case R.id.id_setting:
                break;
            case R.id.id_recommend:
                break;
            case R.id.id_about:
                break;
            case R.id.id_btn_logout:
                break;
        }
    }


    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.REFRESH_PERSONAL://刷新个人资料
                    break;
                case EventConfig.REFRESH_MESSAGE_RED_POINT://红点消除
                    break;
            }
        }
    }


    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public void onError(String errorCode, String errorMsg) {

        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }

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
