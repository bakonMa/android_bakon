package com.junhetang.doctor.nim;

import android.content.Context;
import android.text.TextUtils;

import com.junhetang.doctor.nim.event.DocOnlineStateContentProvider;
import com.junhetang.doctor.nim.message.SessionHelper;
import com.junhetang.doctor.utils.LogUtil;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.contact.core.query.PinYin;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.LoginInfo;
import com.netease.nimlib.sdk.msg.MsgService;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.netease.nimlib.sdk.util.NIMUtil;

/**
 * 网易云IM相关
 * NimManager
 * Create by mayakun at 2018/3/30 下午1:36
 */
public class NimManager {

    private static NimManager nimManager;
    private Context context;

    /**
     * 对外唯一，单例
     */
    public static NimManager getInstance(Context context) {
        if (nimManager == null) {
            synchronized (NimManager.class) {
                if (nimManager == null) {
                    DocCache.setContext(context);
                    nimManager = new NimManager(context);
                }
            }
        }
        return nimManager;
    }

    public NimManager(Context context) {
        this.context = context;
        DocCache.setContext(context);
        // SDK初始化（启动后台服务，若已经存在用户登录信息， SDK 将完成自动登录）
        NIMClient.init(context, getLoginInfo(), NimSDKOptionConfig.getSDKOptions(context)); // init pinyin
//        NIMClient.init(context, getLoginInfo(), null); // init pinyin

        // ... your codes
        if (NIMUtil.isMainProcess(context)) {
            // 注意：以下操作必须在主进程中进行
            // 1、UI相关初始化操作
            // 2、相关Service调用
            // init pinyin
            PinYin.init(context);
            PinYin.validate();
            // 初始化消息提醒
            NIMClient.toggleNotification(UserPreferences.getNotificationToggle());
            // 云信sdk相关业务初始化
            NIMInitManager.getInstance().init(true);

            initUIKit();
        }

    }

    private void initUIKit() {
        // 初始化
        NimUIKit.init(context);

        // 可选定制项
        // 注册定位信息提供者类（可选）,如果需要发送地理位置消息，必须提供。
        // demo中使用高德地图实现了该提供者，开发者可以根据自身需求，选用高德，百度，google等任意第三方地图和定位SDK。
//        NimUIKit.setLocationProvider(new NimDemoLocationProvider());

        // 会话窗口的定制: 示例代码可详见demo源码中的SessionHelper类。
        // 1.注册自定义消息附件解析器（可选）
        // 2.注册各种扩展消息类型的显示ViewHolder（可选）
        // 3.设置会话中点击事件响应处理（一般需要）
        SessionHelper.init();

        // 通讯录列表定制：示例代码可详见demo源码中的ContactHelper类。
        // 1.定制通讯录列表中点击事响应处理（一般需要，UIKit 提供默认实现为点击进入聊天界面)
//        ContactHelper.init();

        // 添加自定义推送文案以及选项，请开发者在各端（Android、IOS、PC、Web）消息发送时保持一致，以免出现通知不一致的情况
//        NimUIKit.setCustomPushContentProvider(new DemoPushContentProvider());

        // 线状态定制初始化。
        NimUIKit.setOnlineStateContentProvider(new DocOnlineStateContentProvider());
    }

    //手动登录
    public void nimLogin() {
        NimUIKit.login(getLoginInfo(), new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo loginInfo) {
                NimU.setNimAccount(loginInfo.getAccount());
                NimU.setNimToken(loginInfo.getToken());
                LogUtil.d("nimLogin onSuccess=" + loginInfo.toString());
            }

            @Override
            public void onFailed(int i) {
                LogUtil.d("nimLogin onFailed i=" + i);
            }

            @Override
            public void onException(Throwable throwable) {
                LogUtil.d("nimLogin errMsg=" + throwable.getMessage());

            }
        });

    }

    // 如果已经存在用户登录信息，返回LoginInfo，否则返回null即可
    private LoginInfo getLoginInfo() {
        String accId = NimU.getNimAccount();
        String accToken = NimU.getNimToken();
        //DocCache 缓存
        DocCache.setAccount(accId.toLowerCase());
        LogUtil.d("nim accid=" + accId + "acctoken=" + accToken);

        if (!TextUtils.isEmpty(accId) && !TextUtils.isEmpty(accToken)) {
            DocCache.setAccount(accId.toLowerCase());
            return new LoginInfo(accId, accToken);
        } else {
            return null;
        }
    }

    /**
     * 外部 公共发送消息 统一发送消息
     *
     * @param imMessage
     * @param isReSend        失败后是否自动重发
     * @param requestCallback 结果回调
     */
    public static void sengChatMsg(IMMessage imMessage, boolean isReSend, RequestCallback<Void> requestCallback) {
        if (imMessage == null) {
            return;
        }
        if (requestCallback == null) {
            NIMClient.getService(MsgService.class).sendMessage(imMessage, isReSend);
        } else {
            NIMClient.getService(MsgService.class).sendMessage(imMessage, isReSend).setCallback(requestCallback);
        }
    }

    /**
     * 设置免扰推送
     */
    public static void setPushStatus(boolean checkState) {
        // 开启/关闭通知栏消息提醒 只有StatusBarNotificationConfig配置不为空时才有效
        UserPreferences.setNotificationToggle(checkState);
        NIMClient.toggleNotification(checkState);
    }

}