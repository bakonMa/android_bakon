package com.renxin.doctor.activity.receiver;

import android.content.Context;
import android.text.TextUtils;

import com.renxin.doctor.activity.BuildConfig;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.EventConfig;
import com.renxin.doctor.activity.config.SPConfig;
import com.renxin.doctor.activity.data.eventbus.Event;
import com.renxin.doctor.activity.data.eventbus.EventBusUtil;
import com.renxin.doctor.activity.utils.LogUtil;
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
