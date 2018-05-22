package com.junhetang.doctor.receiver;

import android.app.Notification;
import android.content.Context;
import android.media.RingtoneManager;
import android.text.TextUtils;

import com.junhetang.doctor.BuildConfig;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.config.SPConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.utils.LogUtil;
import com.tencent.android.tpush.XGCustomPushNotificationBuilder;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

/**
 * XGInitManager 信鸽初始化，其他方法
 * Create at 2018/5/11 下午2:12 by mayakun
 */
public class XGInitManager {

    private static XGInitManager nimManager;

    /**
     * 对外唯一，单例
     */
    public static XGInitManager getInstance(Context context) {

        if (nimManager == null) {
            synchronized (XGInitManager.class) {
                if (nimManager == null) {
                    nimManager = new XGInitManager(context);
                }
            }
        }
        return nimManager;
    }

    public XGInitManager(Context context) {
        //设置日志标签
        XGPushManager.setTag(context, BuildConfig.VERSION_NAME);
        //开启debug日志数据
        XGPushConfig.enableDebug(context, BuildConfig.DEBUG);

        //小米推送
        XGPushConfig.setMiPushAppId(context, "2882303761517794100");
        XGPushConfig.setMiPushAppKey(context, "5831779458100");
        //打开第三方推送
        XGPushConfig.enableOtherPush(context, true);


        //自定义通知
        XGCustomPushNotificationBuilder build = new XGCustomPushNotificationBuilder();
        build.setSound(RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM)) // 设置声音
                .setDefaults(Notification.DEFAULT_VIBRATE) // 振动
                .setFlags(Notification.FLAG_NO_CLEAR); // 是否可清除
//        // 设置自定义通知layout,通知背景等可以在layout里设置
//        build.setLayoutId(R.layout.layout_notification);
//        // 设置自定义通知内容id
//        build.setLayoutTextId(R.id.ssid);
//        // 设置自定义通知标题id
//        build.setLayoutTitleId(R.id.title);
//        // 设置自定义通知图片id
//        build.setLayoutIconId(R.id.icon);
//        // 设置自定义通知图片资源
//        build.setLayoutIconDrawableId(R.mipmap.logo);
        // 设置时间id
        build.setLayoutTimeId(R.id.time);
        // 设置状态栏的通知小图标
        build.setIcon(R.drawable.icon_notify_smallicon);
        // 若不设定以上自定义layout，又想简单指定通知栏图片资源
        build.setNotificationLargeIcon(R.mipmap.logo);

        XGPushManager.setDefaultNotificationBuilder(context, build);
    }

    //约定使用手机号注册
    public void registerXG() {
        // 注册应用（必须调用本接口，否则APP将无法接收到通知和消息）
        // 使用绑定账号的注册接口（可针对账号下发通知和消息）
        // 可以重复注册，以最后一次注册为准
        String phone = DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_PHONE, "");
        if (!TextUtils.isEmpty(phone)) {
            XGPushManager.registerPush(DocApplication.getInstance(), phone, new XGIOperateCallback() {
                @Override
                public void onSuccess(Object data, int errCode) {
                    //token在设备卸载重装的时候有可能会变
                    LogUtil.d("TPush", "注册成功，设备token为：" + data);
                    EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_XG_BINDTOKEN, data.toString()));
                }

                @Override
                public void onFail(Object data, int errCode, String msg) {
                    LogUtil.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                }
            });
        }
    }


}
