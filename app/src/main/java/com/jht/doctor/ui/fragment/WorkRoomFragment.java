package com.jht.doctor.ui.fragment;

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
    @BindView(R.id.id_rl_top)
    RelativeLayout idRlTop;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.id_rl_msg)
    RelativeLayout idRlMsg;
    @BindView(R.id.id_rl_setting)
    RelativeLayout idRlSetting;
    @BindView(R.id.id_iv_head)
    CircleImageView idIvHead;
    @BindView(R.id.id_tv_login)
    TextView idTvLogin;
    @BindView(R.id.id_tv_phone)
    TextView idTvPhone;
    @BindView(R.id.id_tv_balance)
    TextView idTvBalance;
    @BindView(R.id.id_tv_repaument)
    TextView idTvRepayment;
    @BindView(R.id.id_rl_account)
    LinearLayout idRlAccount;
    @BindView(R.id.id_tab_data)
    RelativeWithImage idTabData;
    @BindView(R.id.id_tab_question)
    RelativeWithImage idTabQuestion;
    @BindView(R.id.id_tab_feedback)
    RelativeWithImage idTabFeedback;
    @BindView(R.id.id_tab_about_us)
    RelativeWithImage idTabAboutUs;
    @BindView(R.id.id_btn_logout)
    TextView idBtnLogout;
    @BindView(R.id.id_top)
    RelativeLayout idTop;
    @BindView(R.id.id_ll_top)
    LinearLayout idLlTop;
    @BindView(R.id.id_red_point)
    View idRedPoint;

    @Inject
    PersonalPresenter mPresenter;

    //个人资料
    private MyInfoBean infoBean;
    public static final String KEY_INFO = "info";

    private String name;

    private String phone;

    private MyAccountInfoBean myAccountInfoBean;//记录账单号





    @Override
    protected View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_personal, null);
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
        idRlTop.setPadding(0, DensityUtils.getStatusBarHeight(actContext()), 0, 0);
        idTop.setPadding(0, DensityUtils.getStatusBarHeight(actContext()), 0, 0);
        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setEnabled(false);
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestData();
            }
        });
    }

    private void requestData() {
        //有token，请求个人资料
        if (!U.isNoToken()) {
            mPresenter.getPersonalInfo();
        }
    }

    @OnClick({R.id.id_rl_msg, R.id.id_rl_setting, R.id.id_iv_head, R.id.id_tv_login,
            R.id.id_tab_data, R.id.id_tab_question, R.id.id_rl_account,
            R.id.id_tab_feedback, R.id.id_tab_about_us, R.id.id_btn_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.id_rl_msg:
                //我的消息
                jumpTo(new Intent(actContext(), MessageActivity.class));
                break;
            case R.id.id_rl_setting:
                //设置页面
                jumpTo(new Intent(actContext(), SettingActivity.class));
                break;
            case R.id.id_iv_head:
                //修改头像
                Intent intent = new Intent(actContext(), MyImageInfoActivity.class);
                intent.putExtra(MyImageInfoActivity.NAME_KEY, name);
                intent.putExtra(MyImageInfoActivity.PHONE_KEY, phone);
                jumpTo(intent);
                break;
            case R.id.id_tv_login:
                //未登录跳转¬到登录页面
                jumpTo(new Intent(actContext(), LoginActivity.class));
                break;
            case R.id.id_tab_data:
                //我的资料
                if (U.isNoToken()) {
                    jumpTo(new Intent());
                } else {
                    if (infoBean == null) {
                        mPresenter.getUserInfo();
                    } else {
                        gotoMyinfo();
                    }
                }
                break;
            case R.id.id_tab_question:
                //常见问题
                Intent intent1 = new Intent(actContext(), WebViewActivity.class);
                intent1.putExtra("title", "常见问题");
                intent1.putExtra("url", PathConfig.H5_QUESTION);
                startActivity(intent1);
                break;
            case R.id.id_tab_feedback:
                //使用反馈
                jumpTo(new Intent(actContext(), FeedbackActivity.class));
                break;
            case R.id.id_tab_about_us:
                Intent intentAbout = new Intent(actContext(), WebViewActivity.class);
                intentAbout.putExtra("title", "关于我们");
                intentAbout.putExtra("url", PathConfig.H5_ABOUTUS);
                startActivity(intentAbout);
                break;
            case R.id.id_btn_logout:
                //退出
                HaveBalanceDialog haveBalanceDialog = new HaveBalanceDialog(getActivity(), HaveBalanceDialog.LOG_OUT, new HaveBalanceDialog.ClickListener() {
                    @Override
                    public void confirm() {
                        CustomerApplication.getAppComponent().dataRepo().appSP().remove(SPConfig.SP_STR_TOKEN);
                        CustomerApplication.getAppComponent().dataRepo().appSP().remove(SPConfig.SP_STR_PHONE);
                        startActivity(new Intent(actContext(), MainActivity.class));
                        CustomerApplication.getInstance().managerRepository.actMgr().finishAllActivity();
                    }
                });
                haveBalanceDialog.show();
                break;
            case R.id.id_rl_account:
                //我的账户信息
                if (myAccountInfoBean.getOrderNo() == null) {
                    CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast("你的当前订单已取消，无法查看你的账户信息");
                } else {
                    Intent intentAccount = new Intent(actContext(), MyAccountActivity.class);
                    intentAccount.putExtra("orderNo", myAccountInfoBean.getOrderNo());
                    startActivity(intentAccount);
                }
                break;
        }
    }

    /**
     * 是否跳转用户资料
     */
    private void gotoMyinfo() {
        if (infoBean == null || infoBean.userDTO == null
                || infoBean.userJobDTO == null || infoBean.userHouseDTO == null) {
            //未认证，弹出dialog
            CommonDialog dialog = new CommonDialog(getActivity(), R.layout.dialog_my_info, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.btn_ok) {
                        startActivity(new Intent(actContext(), BasicInfoActivity.class));
                    }
                }
            });
            dialog.show();
        } else {
            //已认证，跳转
            Intent intent = new Intent(actContext(), MyInfoActivity.class);
            intent.putExtra(KEY_INFO, infoBean);
            startActivity(intent);
        }
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.REFRESH_PERSONAL://刷新个人资料
                    requestData();
                    break;
                case EventConfig.REFRESH_MESSAGE_RED_POINT://红点消除
                    idRedPoint.setVisibility(View.GONE);
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
            CustomerApplication.getAppComponent().dataRepo().appSP().setString(SPConfig.SP_STR_PHONE, personalBean.getMobilePhone());
            idTvLogin.setText(RegexUtil.hideFirstName(personalBean.getUserName()));
            name = personalBean.getUserName();
            idTvPhone.setVisibility(View.VISIBLE);
            idTvPhone.setText(RegexUtil.hidePhone(personalBean.getMobilePhone()));
            phone = personalBean.getMobilePhone();
            idTvLogin.setEnabled(false);
            idBtnLogout.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 展示账户信息
     */
    private void showAccountInfo() {
        if (myAccountInfoBean.isOnline()) {
            //线上 需要显示账户信息
            idRlAccount.setVisibility(View.VISIBLE);
            //idTvBalance.setText(RegexUtil.formatMoney(myAccountInfoBean.getTotalAvailableCredit()));
            //idTvRepayment.setText(RegexUtil.formatMoney(myAccountInfoBean.getTotalRepayAmount()));
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) idLlTop.getLayoutParams();
            lp.height = DensityUtils.dp2Px(actContext(), 60);
            idLlTop.setLayoutParams(lp);
            idLlTop.setPadding(0, 20, 0, 0);
        } else {
            //线下不需要显示
            idRlAccount.setVisibility(View.GONE);
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) idLlTop.getLayoutParams();
            lp.height = DensityUtils.dp2Px(actContext(), 80);
            idLlTop.setLayoutParams(lp);
            idLlTop.setPadding(0, 60, 0, 0);
        }
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
        if (idSwipe.isRefreshing()) {
            idSwipe.setRefreshing(false);
        }
        CustomerApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case PersonalPresenter.GET_PERSONAL_INFO:
                idSwipe.setEnabled(true);
                showPersonalInfo((PersonalBean) message.obj);
//                mPresenter.getAccount();
                mPresenter.getMessageCount();//是否有消息
                break;
            case PersonalPresenter.GET_USER_INFO://我的资料数据
                infoBean = (MyInfoBean) message.obj;
                gotoMyinfo();
                break;
            case PersonalPresenter.MESSAGE_COUNT://是否有消息提示
                if (idSwipe.isRefreshing()) {
                    idSwipe.setRefreshing(false);
                }
                MessageCountBean bean = (MessageCountBean) message.obj;
                idRedPoint.setVisibility(bean.getUnreadCount() > 0 ? View.VISIBLE : View.GONE);
                break;
            case PersonalPresenter.ACCOUNT_INFO://我的账户
                if (idSwipe.isRefreshing()) {
                    idSwipe.setRefreshing(false);
                }
                myAccountInfoBean = (MyAccountInfoBean) message.obj;
                showAccountInfo();
                break;
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
