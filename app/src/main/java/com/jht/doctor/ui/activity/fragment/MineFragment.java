package com.jht.doctor.ui.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.injection.components.DaggerFragmentComponent;
import com.jht.doctor.injection.modules.FragmentModule;
import com.jht.doctor.ui.activity.mine.AuthStep1Activity;
import com.jht.doctor.ui.activity.mine.AuthStep3Activity;
import com.jht.doctor.ui.base.BaseFragment;
import com.jht.doctor.ui.bean.OtherBean;
import com.jht.doctor.ui.contact.PersonalContact;
import com.jht.doctor.ui.presenter.PersonalPresenter;
import com.jht.doctor.utils.ToastUtil;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;

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
    @BindView(R.id.tv_authstatus)
    TextView tvAuthstatus;
    @BindView(R.id.tv_authmsg)
    TextView tvAuthmsg;
    @BindView(R.id.llt_auth_status)
    LinearLayout lltAuthStatus;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_usersex)
    TextView tvUsersex;
    @BindView(R.id.tv_userinfo)
    TextView tvUserinfo;
    @BindView(R.id.llt_user_info)
    LinearLayout lltUserInfo;

    @Inject
    PersonalPresenter mPresenter;

    private List<String> carTagBeans = new LinkedList<>();//全部选择的标签


    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build().inject(this);
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


        mPresenter.getUserIdentifyStatus();
    }


    @OnClick({R.id.llt_auth_status, R.id.id_collect, R.id.id_wallet, R.id.id_set_price,
            R.id.id_setting, R.id.id_recommend, R.id.id_about, R.id.id_btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llt_auth_status:
                switch (U.getAuthStatus()) {//状态值，0：未认证 1：审核中；2：审核通过 3：审核失败
                    case 0:
                    case 3:
                        startActivity(new Intent(actContext(), AuthStep1Activity.class));
                        break;
                    case 1:
                        startActivity(new Intent(actContext(), AuthStep3Activity.class));
                        break;
                }
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

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case PersonalPresenter.GET_AUTH_STATUS:
                //状态值，0：未认证 1：审核中；2：审核通过 3：审核失败
                OtherBean bean = (OtherBean) message.obj;
                switch (bean.status) {
                    case 0:
                        lltAuthStatus.setVisibility(View.VISIBLE);
                        lltUserInfo.setVisibility(View.GONE);
                        tvAuthstatus.setText(R.string.str_auth_0);
                        tvAuthmsg.setText(R.string.str_auth_msg_0);
                        break;
                    case 1:
                        lltAuthStatus.setVisibility(View.VISIBLE);
                        lltUserInfo.setVisibility(View.GONE);
                        tvAuthstatus.setText(R.string.str_auth_1);
                        tvAuthmsg.setText(R.string.str_auth_msg_1);
                        break;
                    case 2://请求个人资料
                        lltAuthStatus.setVisibility(View.GONE);
                        lltUserInfo.setVisibility(View.VISIBLE);
                        mPresenter.getPersonalInfo();
                        break;
                    case 3:
                        lltAuthStatus.setVisibility(View.VISIBLE);
                        lltUserInfo.setVisibility(View.GONE);
                        tvAuthstatus.setText(R.string.str_auth_3);
                        tvAuthmsg.setText(R.string.str_auth_msg_3);
                        break;
                }
                break;
            case PersonalPresenter.GET_PERSONAL_INFO:

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
    public void onError(String errorCode, String errorMsg) {
        ToastUtil.show(errorMsg);
    }


    @Override
    public boolean useEventBus() {
        return true;
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
