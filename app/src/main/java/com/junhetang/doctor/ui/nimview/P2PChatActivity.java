package com.junhetang.doctor.ui.nimview;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.SPConfig;
import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.presenter.CommonPresenter;
import com.junhetang.doctor.utils.SoftHideKeyBoardUtil;
import com.junhetang.doctor.utils.StatusBarUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.api.model.contact.ContactChangedObserver;
import com.netease.nim.uikit.api.model.main.OnlineStateChangeObserver;
import com.netease.nim.uikit.api.model.session.SessionCustomization;
import com.netease.nim.uikit.api.model.user.UserInfoObserver;
import com.netease.nim.uikit.business.session.activity.BaseMessageActivity;
import com.netease.nim.uikit.business.session.constant.Extras;
import com.netease.nim.uikit.business.session.fragment.MessageFragment;
import com.netease.nim.uikit.business.uinfo.UserInfoHelper;
import com.netease.nim.uikit.impl.NimUIKitImpl;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.CustomNotification;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleTransformer;

import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Set;

import io.reactivex.functions.Consumer;

/**
 * P2PChatActivity  定制p2p单聊
 * Create at 2018/4/17 下午3:24 by mayakun
 */
public class P2PChatActivity extends BaseMessageActivity implements BaseView {

    private Toolbar idToolbar;
    private ToolbarBuilder toolbarBuilder;
    private boolean isResume = false;
    private CommonPresenter commonPresenter;

    public static void start(Context context, String contactId, SessionCustomization customization, IMMessage anchor) {
        Intent intent = new Intent();
        intent.putExtra(Extras.EXTRA_ACCOUNT, contactId);
        intent.putExtra(Extras.EXTRA_CUSTOMIZATION, customization);
        if (anchor != null) {
            intent.putExtra(Extras.EXTRA_ANCHOR, anchor);
        }

        intent.setClass(context, P2PChatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //请求权限
        requestPermissions();

        commonPresenter = new CommonPresenter(this);
        String accid = getIntent().getStringExtra(Extras.EXTRA_ACCOUNT);
        //不为空，不是客服才获取患者的momb_no
        if (!TextUtils.isEmpty(accid)
                && !DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_SERVICE_ACCID).equals(accid)) {
            commonPresenter.getMembNo(accid);
        }
        // 单聊特例话数据，包括个人信息，
        setTitleText();//这里才拿到数据
        registerObservers(true);

        //显示在线状态
        //displayOnlineState();
        //对方在线状态监听 这哪是不需要
        //registerOnlineStateChangeListener(true);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.doc_p2pchat_activity;
    }

    @Override
    protected void initToolBar() {
        StatusBarUtil.initStatusBar(this);
        SoftHideKeyBoardUtil.assistActivity(this);
        idToolbar = findViewById(R.id.id_toolbar);
        toolbarBuilder = ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
//                .setTitle("")//这时候还拿不到数据
                .setLeft(false)
                .setStatuBar(R.color.white)
//                .setRightText("健康档案", true, R.color.color_main)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        ToastUtil.show("健康档案");
                    }
                }).bind();
    }

    //修改title文字
    private void setTitleText() {
        toolbarBuilder.setTitle(UserInfoHelper.getUserTitleName(sessionId, SessionTypeEnum.P2P));
    }

    //显示对方在线状态
    OnlineStateChangeObserver onlineStateChangeObserver = new OnlineStateChangeObserver() {
        @Override
        public void onlineStateChange(Set<String> accounts) {
            // 更新 toolbar
            if (accounts.contains(sessionId)) {
                // 按照交互来展示
                displayOnlineState();
            }
        }
    };

    private void displayOnlineState() {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
//        String detailContent = NimUIKitImpl.getOnlineStateContentProvider().getDetailDisplay(sessionId);
//        ToastUtil.show("online 状态变化了=" + detailContent);
//        LogUtil.d("logcat", detailContent);
//        setSubTitle(detailContent);
    }

    private void registerOnlineStateChangeListener(boolean register) {
        if (!NimUIKitImpl.enableOnlineState()) {
            return;
        }
        NimUIKitImpl.getOnlineStateChangeObservable().registerOnlineStateChangeListeners(onlineStateChangeObserver, register);
    }

    private void registerObservers(boolean register) {
        if (register) {
            registerUserInfoObserver();
        } else {
            unregisterUserInfoObserver();
        }
        //显示对方正在输入。。。的监听 暂时关闭
        //NIMClient.getService(MsgServiceObserve.class).observeCustomNotification(commandObserver, register);
        NimUIKit.getContactChangedObservable().registerObserver(friendDataChangedObserver, register);
    }

    //注册用户信息变化监听
    private UserInfoObserver uinfoObserver;

    private void registerUserInfoObserver() {
        if (uinfoObserver == null) {
            uinfoObserver = new UserInfoObserver() {
                @Override
                public void onUserInfoChanged(List<String> accounts) {
                    if (accounts.contains(sessionId)) {
                        setTitleText();
                    }
                }
            };
        }
        NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, true);
    }

    //解除监听
    private void unregisterUserInfoObserver() {
        if (uinfoObserver != null) {
            NimUIKit.getUserInfoObservable().registerObserver(uinfoObserver, false);
        }
    }

    /**
     * 好友信息状态监听
     */
    ContactChangedObserver friendDataChangedObserver = new ContactChangedObserver() {
        @Override
        public void onAddedOrUpdatedFriends(List<String> accounts) {
            setTitleText();
        }

        @Override
        public void onDeletedFriends(List<String> accounts) {
            setTitleText();
        }

        @Override
        public void onAddUserToBlackList(List<String> account) {
            setTitleText();
        }

        @Override
        public void onRemoveUserFromBlackList(List<String> account) {
            setTitleText();
        }
    };

    /**
     * 命令消息接收观察者
     */
    Observer<CustomNotification> commandObserver = new Observer<CustomNotification>() {
        @Override
        public void onEvent(CustomNotification message) {
            if (!sessionId.equals(message.getSessionId()) || message.getSessionType() != SessionTypeEnum.P2P) {
                return;
            }
            showCommandMessage(message);
        }
    };

    //对方输入状态
    protected void showCommandMessage(CustomNotification message) {
        if (!isResume) {
            return;
        }
        String content = message.getContent();
        try {
            JSONObject json = JSON.parseObject(content);
            int id = json.getIntValue("id");
            if (id == 1) {
                // 正在输入
                ToastUtil.showShort("对方正在输入...");
            } else {
                ToastUtil.showShort("command: " + content);
            }

        } catch (Exception e) {

        }
    }

    @Override
    protected MessageFragment fragment() {
        Bundle arguments = getIntent().getExtras();
        arguments.putSerializable(Extras.EXTRA_TYPE, SessionTypeEnum.P2P);
        MessageFragment fragment = new MessageFragment();
        fragment.setArguments(arguments);
        fragment.setContainerId(com.netease.nim.uikit.R.id.message_fragment_container);
        return fragment;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        registerObservers(false);
//        registerOnlineStateChangeListener(false);
        commonPresenter.unsubscribe();
    }

    //录音权限
    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(new String[]{Manifest.permission.RECORD_AUDIO})
                .subscribe(new Consumer<Boolean>() {
                               @Override
                               public void accept(Boolean aBoolean) throws Exception {
                                   if (!aBoolean) {
                                       ToastUtil.show("请求语音权限失败");
                                   }
                               }
                           }
                );
    }

    @Override
    protected void onResume() {
        super.onResume();
        isResume = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isResume = false;
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case CommonPresenter.GET_MOMBNO_OK://获取memb_no
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {

    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return null;
    }
}
