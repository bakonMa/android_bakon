package com.junhetang.doctor.ui.activity.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhetang.doctor.BuildConfig;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.config.H5Config;
import com.junhetang.doctor.config.SPConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.injection.components.DaggerFragmentComponent;
import com.junhetang.doctor.injection.modules.FragmentModule;
import com.junhetang.doctor.nim.NimManager;
import com.junhetang.doctor.nim.message.SessionHelper;
import com.junhetang.doctor.nim.message.extension.FirstMessageAttachment;
import com.junhetang.doctor.receiver.XGInitManager;
import com.junhetang.doctor.ui.activity.WebViewActivity;
import com.junhetang.doctor.ui.activity.home.CheckPaperActivity;
import com.junhetang.doctor.ui.activity.home.CommUsePaperActivity;
import com.junhetang.doctor.ui.activity.home.LogoutActivity;
import com.junhetang.doctor.ui.activity.home.OpenPaperCameraActivity;
import com.junhetang.doctor.ui.activity.home.OpenPaperOnlineActivity;
import com.junhetang.doctor.ui.activity.home.PaperHistoryActivity;
import com.junhetang.doctor.ui.activity.mine.AuthStep1Activity;
import com.junhetang.doctor.ui.activity.mine.UserNoticeActivity;
import com.junhetang.doctor.ui.base.BaseFragment;
import com.junhetang.doctor.ui.bean.BannerBean;
import com.junhetang.doctor.ui.contact.WorkRoomContact;
import com.junhetang.doctor.ui.nimview.PaperH5Activity;
import com.junhetang.doctor.ui.nimview.RecentActivity;
import com.junhetang.doctor.ui.presenter.WorkRoomPresenter;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.utils.imageloader.BannerImageLoader;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.common.util.sys.TimeUtil;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.StatusCode;
import com.netease.nimlib.sdk.auth.AuthServiceObserver;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.MsgServiceObserve;
import com.netease.nimlib.sdk.msg.constant.MsgStatusEnum;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomMessageConfig;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.msg.model.RecentContact;
import com.netease.nimlib.sdk.uinfo.model.UserInfo;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * WorkRoomFragment
 * Create at 2018/3/31 上午9:27 by mayakun
 */

public class WorkRoomFragment extends BaseFragment implements WorkRoomContact.View {

    @BindView(R.id.id_ll_top)
    LinearLayout idLlTop;
    @BindView(R.id.llt_shownotice)
    LinearLayout lltShownotice;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.iv_service_img)
    ImageView ivServiceImg;
    @BindView(R.id.tv_service_name)
    TextView tvServiceName;
    @BindView(R.id.tv_service_message)
    TextView tvServiceMessage;
    @BindView(R.id.tv_service_num)
    TextView tvServiceNum;
    @BindView(R.id.tv_service_time)
    TextView tvServiceTime;
    @BindView(R.id.tv_notification)
    TextView tvNotification;
    @BindView(R.id.tv_checkredpoint)
    TextView tvCheckredpoint;//审核处方-红点
    @BindView(R.id.tv_chatunreadnum)
    TextView tvChatunreadnum;//消息通知-数字红点
    @BindView(R.id.tv_chatredpoint)
    TextView tvChatReadPoint;//消息通知-红点

    @Inject
    WorkRoomPresenter mPresenter;
    private CommonDialog commonDialog;
    private List<BannerBean> bannerBeans = new ArrayList<>();
    private List<String> imgUrl = new ArrayList<>();

    //客服accid
    private String accid;

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_workroom;
    }

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    protected void initView() {
        //获取客服accid login接口获得
        accid = DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_SERVICE_ACCID);
        //请求权限
        requestPermissions();

        //nim手动登录
        NimManager.getInstance(DocApplication.getInstance()).nimLogin();
        //信鸽注册
        XGInitManager.getInstance(DocApplication.getInstance()).registerXG();

        //Banner初始化
        initBanner();

        //请求数据
        mPresenter.updataToken();//更新token
        mPresenter.getUserIdentifyStatus();//认证状态
        mPresenter.getOPenPaperBaseData();//开方基础数据
        mPresenter.getRedPointStatus();//红点状态
        //首页Banner数据
        mPresenter.getHomeBanner();

        //登录状态监听 nim互踢
        NIMClient.getService(AuthServiceObserver.class).observeOnlineStatus(new Observer<StatusCode>() {
            public void onEvent(StatusCode status) {
                LogUtil.d("observeOnlineStatus = " + status);
                if (status.wontAutoLogin()) {
                    // 被踢出、账号被禁用、密码错误等情况，自动登录失败，需要返回到登录界面进行重新登录操作
                    LogUtil.d("observeOnlineStatus :你被踢下线了");
                    //重新登录
                    EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_NIM_LOGOUT));
                } else if (status == StatusCode.UNLOGIN) {
                    //未登录就登录
                    NimManager.getInstance(DocApplication.getInstance()).nimLogin();
                } else if (status == StatusCode.LOGINED) {
                    //登录成功 初始化service
                    EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_NIM_LOGIN));
                }
            }
        }, true);

    }


    /**
     * fragment 是否隐藏
     *
     * @param hidden false 前台显示 true 隐藏
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresenter.getUserIdentifyStatus();//认证状态
            mPresenter.getRedPointStatus();//红点状态
        }
    }

    //登录成功后初始化 客服数据
    private void initService() {
        //最近联系人列表变化观察者
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, true);
        NIMClient.getService(MsgServiceObserve.class).observeReceiveMessage(messageReceiverObserver, true);

        //第一次主动 发起
        showServiceInfo();//客服资料数据
        showLastServiceMessage();//最后一条消息内容
        showAllUnreadMessageNum();//[消息通知]是否显示数字 和 系统消息红点
        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_REDPOINT_HOME));//通知【工作室】 是否显示红点
    }

    //最后一条消息内容
    private void showLastServiceMessage() {
        // 查询最近联系人列表数据
        NIMClient.getService(MsgService.class).queryRecentContacts().setCallback(new RequestCallback<List<RecentContact>>() {
            @Override
            public void onSuccess(List<RecentContact> recentContacts) {
                // 初次加载，更新离线的消息中是否有@我的消息
                for (RecentContact loadedRecent : recentContacts) {
                    if (loadedRecent.getSessionType() == SessionTypeEnum.P2P
                            && loadedRecent.getContactId().equals(accid)) {
                        //显示客服最后一条消息
                        showLastMessage(loadedRecent);
                    }
                }
            }

            @Override
            public void onFailed(int i) {

            }

            @Override
            public void onException(Throwable throwable) {
            }
        });
    }

    //显示客服数据
    private void showServiceInfo() {
        //客服个人资料
        String serviceName = UserInfoHelper.getServiceName(accid);
        LogUtil.d("serviceName=" + serviceName);
        //name
        tvServiceName.setText(TextUtils.isEmpty(serviceName) ? "君和客服" : serviceName);
        //head img
        ImageUtil.showCircleImage(UserInfoHelper.getUserHeadImg(accid), ivServiceImg);

        //本地是空的话，去云信服务器拿数据
        if (TextUtils.isEmpty(serviceName)) {
            UserInfoHelper.getServiceInfo(accid, new RequestCallback<List<UserInfo>>() {
                @Override
                public void onSuccess(List<UserInfo> userInfos) {
                    if (userInfos != null && !userInfos.isEmpty()) {
                        //显示客服资料
                        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_SERVICE_INFO, userInfos.get(0)));
                    }
                }

                @Override
                public void onFailed(int i) {

                }

                @Override
                public void onException(Throwable throwable) {

                }
            });
        }

    }

    /**
     * [消息通知]是否显示数字 和 系统消息红点
     * ps：优先未读消息数字心事显示
     */
    private void showAllUnreadMessageNum() {
        int unreadNum = NIMClient.getService(MsgService.class).getTotalUnreadCount();
        if (unreadNum > 99) {
            unreadNum = 99;
        } else if (unreadNum < 0) {
            unreadNum = 0;
        }
        if (unreadNum > 0) {
            tvChatunreadnum.setText(unreadNum + "");
            tvChatunreadnum.setVisibility(View.VISIBLE);
            tvChatReadPoint.setVisibility(View.GONE);
        } else {
            tvChatunreadnum.setVisibility(View.GONE);
            tvChatReadPoint.setVisibility(U.getRedPointSys() > 0 ? View.VISIBLE : View.GONE);
        }
    }

    //最近联系人列表变化观察者
    Observer<List<RecentContact>> messageObserver = new Observer<List<RecentContact>>() {
        @Override
        public void onEvent(List<RecentContact> recentContacts) {
            for (RecentContact loadedRecent : recentContacts) {
                if (loadedRecent.getSessionType() == SessionTypeEnum.P2P
                        && loadedRecent.getContactId().equals(accid)) {
                    //显示客服最后一条消息
                    EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_SERVICE_MESSAGE, loadedRecent));
                }
            }
            //工作室内部红点 未读消息数和系统消息红点
            EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_REDPOINT_HOME_SYSMSG));
            //通知【工作室是否显示红点】和 logo角标处理
            EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_REDPOINT_HOME));
        }
    };
    //监听在线消息中是否有 患者第一条消息 自动回复
    private Observer<List<IMMessage>> messageReceiverObserver = new Observer<List<IMMessage>>() {
        @Override
        public void onEvent(List<IMMessage> imMessages) {
            if (imMessages != null) {
                for (IMMessage imMessage : imMessages) {
                    //第一条消息，自动回复一条，tip
                    if (imMessage.getAttachment() instanceof FirstMessageAttachment) {
//                    if (imMessage.getContent().equals("123456789")) {//测试
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                IMMessage msg = MessageBuilder.createTipMessage(imMessage.getFromAccount(), SessionTypeEnum.P2P);
                                msg.setContent("请给患者发送问诊单或者随诊单，待患者填写完成，详细了解患者的情况");
                                CustomMessageConfig config = new CustomMessageConfig();
                                config.enablePush = false; // 不推送
                                config.enableUnreadCount = false; // 消息不计入未读
                                // 消息发送状态设置为success
                                msg.setStatus(MsgStatusEnum.success);
                                msg.setConfig(config);
                                // 保存消息到本地数据库，但不发送到服务器
                                NIMClient.getService(MsgService.class).saveMessageToLocal(msg, true);
                            }
                        }, 2000);
                    }
                    break;
                }
            }
        }
    };

    //显示客服最后一条消息
    private void showLastMessage(RecentContact loadedRecent) {
        tvServiceMessage.setText(loadedRecent.getContent());
        tvServiceTime.setText(TimeUtil.getTimeShowString(loadedRecent.getTime(), true));
        tvServiceNum.setVisibility(loadedRecent.getUnreadCount() > 0 ? View.VISIBLE : View.GONE);

        if (loadedRecent.getUnreadCount() > 0) {
            tvServiceNum.setText(loadedRecent.getUnreadCount() > 99 ? "99+" : loadedRecent.getUnreadCount() + "");
        }
    }

    //初始化banner
    private void initBanner() {
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoader());
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //URL为空不跳转
                if (TextUtils.isEmpty(bannerBeans.get(position).url)) {
                    return;
                }
                //Banner点击 跳转
                WebViewActivity.startAct(actContext(),
                        true,
                        WebViewActivity.WEB_TYPE.WEB_TYPE_BANNER,
                        "",
                        bannerBeans.get(position).url);
            }
        });
    }

    @OnClick({R.id.tv_add_patient, R.id.tv_online_paper, R.id.tv_camera_patient, R.id.tv_comm_paper,
            R.id.tv_ask_paper, R.id.tv_flow_paper, R.id.tv_checkpaper, R.id.tv_notice})
    void btnOnClick(View view) {
        //认证是否通过
        if (!U.isHasAuthOK()) {
            commonDialog = new CommonDialog(getActivity(),
                    R.layout.dialog_auth,
                    U.getAuthStatusMsg(),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (view.getId() == R.id.btn_gotuauth) {
                                startActivity(new Intent(getActivity(), AuthStep1Activity.class));
                            }
                        }
                    });

            commonDialog.show();
            return;
        }

        switch (view.getId()) {
            case R.id.tv_add_patient://添加患者
                //个人卡片
                WebViewActivity.startAct(actContext(), true, WebViewActivity.WEB_TYPE.WEB_TYPE_MYCARD, H5Config.H5_USERCARD_TITLE, H5Config.H5_USERCARD);
                break;
            case R.id.tv_online_paper://在线开方
                startActivity(new Intent(actContext(), OpenPaperOnlineActivity.class));
                break;
            case R.id.tv_camera_patient://拍照开方
                startActivity(new Intent(actContext(), OpenPaperCameraActivity.class));
                break;
            case R.id.tv_ask_paper://问诊单
                Intent intent = new Intent(actContext(), PaperH5Activity.class);
                intent.putExtra("hasTopBar", true);//是否包含toolbar
                intent.putExtra("webType", PaperH5Activity.FORM_TYPE.H5_ASKPAPER);
                intent.putExtra("title", UIUtils.getString(R.string.input_panel_askpaper));
                intent.putExtra("url", H5Config.H5_ASKPAPER);
                startActivity(intent);
                break;
            case R.id.tv_flow_paper://随诊单
                Intent intentFollow = new Intent(actContext(), PaperH5Activity.class);
                intentFollow.putExtra("hasTopBar", true);//是否包含toolbar
                intentFollow.putExtra("webType", PaperH5Activity.FORM_TYPE.H5_FOLLOWPAPER);
                intentFollow.putExtra("title", UIUtils.getString(R.string.input_panel_followpaper));
                intentFollow.putExtra("url", H5Config.H5_FOLLOWPAPER);
                startActivity(intentFollow);
                break;
            case R.id.tv_checkpaper://审核开方
                startActivity(new Intent(actContext(), CheckPaperActivity.class));
                break;
            case R.id.tv_notice://公告
                startActivity(new Intent(actContext(), UserNoticeActivity.class));
                break;
            case R.id.tv_comm_paper://常用处方
                startActivity(new Intent(actContext(), CommUsePaperActivity.class));
                break;
        }

    }

    @OnClick({R.id.rlt_service, R.id.id_history, R.id.id_notification})
    void unCheckBtnOnClick(View view) {
        switch (view.getId()) {
            case R.id.rlt_service://客服
                SessionHelper.startP2PSession(actContext(), accid, true);
                break;
            case R.id.id_history://历史处方
                startActivity(new Intent(actContext(), PaperHistoryActivity.class));
                break;
            case R.id.id_notification://消息通知
                startActivity(new Intent(actContext(), RecentActivity.class));
                break;
        }
    }


    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case WorkRoomPresenter.GET_AUTH_STATUS://认证状态
                lltShownotice.setVisibility(U.getAuthStatus() == 2 ? View.GONE : View.VISIBLE);
                tvNotification.setText(U.getAuthStatusMsg());
                break;
            case WorkRoomPresenter.GET_BANNER_OK:
                imgUrl.clear();
                bannerBeans.clear();
                bannerBeans = (List<BannerBean>) message.obj;
                for (BannerBean bannerBean : bannerBeans) {
                    imgUrl.add(bannerBean.img_url);
                }
                banner.setImages(imgUrl);
                //banner设置方法全部调用完毕时最后调用
                banner.start();
                break;
        }

    }

    @Override
    public void onError(String errorCode, String errorMsg) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_REDPOINT_HOME_CHECK://红点 审核处方
                //是否有审核处方
                tvCheckredpoint.setVisibility(U.getRedPointExt() > 0 ? View.VISIBLE : View.GONE);
                break;
            case EventConfig.EVENT_KEY_NIM_LOGIN://nim 登录成功
                //客服 初始化
                initService();
                break;
            case EventConfig.EVENT_KEY_SERVICE_INFO://客服资料
                UserInfo userInfo = (UserInfo) event.getData();
                tvServiceName.setText(TextUtils.isEmpty(userInfo.getName()) ? "君和客服" : userInfo.getName());
                ImageUtil.showCircleImage(userInfo.getAvatar(), ivServiceImg);
                break;
            case EventConfig.EVENT_KEY_REDPOINT_HOME_SYSMSG://消息通知 未读消息数和系统消息红点
                showAllUnreadMessageNum();
                break;
            case EventConfig.EVENT_KEY_SERVICE_MESSAGE://客服数据 变化
                showServiceInfo();
                showLastMessage((RecentContact) event.getData());
                break;
            case EventConfig.EVENT_KEY_NIM_LOGOUT://踢掉 进入登录画面
                U.logout();
                NimUIKit.logout();
                startActivity(new Intent(getActivity(), LogoutActivity.class));
                break;
            case EventConfig.EVENT_KEY_XG_BINDTOKEN://绑定信鸽token
                mPresenter.bindXGToken(event.getData().toString());
                break;
        }
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

    @Override
    public void onDestroyView() {
        if (commonDialog != null) {
            commonDialog.dismiss();
            commonDialog = null;
        }
        //注销监听
        NIMClient.getService(MsgServiceObserve.class).observeRecentContact(messageObserver, true);
        super.onDestroyView();
    }

    /**
     * 下面的代码很关键,没有下面的代码会出现切换tab的时候重影现象：
     *
     * @param menuVisible
     */
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.setLogging(BuildConfig.DEBUG);
        rxPermissions
                .request(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .subscribe(new rx.Observer<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            ToastUtil.show("请求存储权限失败");
                        } else {
                            showServiceInfo();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                });
    }
}
