package com.bakon.android.data.eventbus;

import org.greenrobot.eventbus.EventBus;

public class EventBusUtil {
    //注册
    public static void register(Object subscriber) {
        EventBus.getDefault().register(subscriber);
    }

    //解绑
    public static void unregister(Object subscriber) {
        EventBus.getDefault().unregister(subscriber);
    }

    //发送event
    public static void sendEvent(Event event) {
        EventBus.getDefault().post(event);
    }

    //发送粘滞时间（1先发送event，2后注册，3接受处理event）
    public static void sendStickyEvent(Event event) {
        EventBus.getDefault().postSticky(event);
    }

}