package com.renxin.doctor.activity.receiver;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;

import com.renxin.doctor.activity.utils.LogUtil;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.tencent.android.tpush.XGPushBaseReceiver;
import com.tencent.android.tpush.XGPushClickedResult;
import com.tencent.android.tpush.XGPushRegisterResult;
import com.tencent.android.tpush.XGPushShowedResult;
import com.tencent.android.tpush.XGPushTextMessage;

import org.json.JSONException;
import org.json.JSONObject;

public class XGMessageReceiver extends XGPushBaseReceiver {
    private Intent intent = new Intent("com.renxin.doctor.activity.UPDATE_LISTVIEW");
    public static final String LogTag = "TPushReceiver";

    private void show(Context context, String text) {
        ToastUtil.showShort(text);
    }

    // 通知展示
    @Override
    public void onNotifactionShowedResult(Context context, XGPushShowedResult notifiShowedRlt) {
        if (context == null || notifiShowedRlt == null) {
            return;
        }
        context.sendBroadcast(intent);
//        show(context, "您有1条新消息, " + "通知被展示 ， " + notifiShowedRlt.toString());
        LogUtil.d("LC", "+++++++++++++++++++++++++++++展示通知的回调---" + notifiShowedRlt.toString());
    }

    // 通知点击回调 actionType=1为该消息被清除，actionType=0为该消息被点击。
    // 此处不能做点击消息跳转，详细方法请参照官网的Android常见问题文档
    @Override
    public void onNotifactionClickedResult(Context context, XGPushClickedResult message) {
        LogUtil.d("+++++++++++++++ 通知被点击 跳转到指定页面。");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.cancelAll();
        if (context == null || message == null) {
            return;
        }
        if (message.getActionType() == XGPushClickedResult.NOTIFACTION_CLICKED_TYPE) {
            // APP自己处理点击的相关动作
            // 获取自定义key-value
            String customContent = message.getCustomContent();
            LogUtil.d("xgmsg", customContent);
            if (customContent != null && customContent.length() != 0) {
                try {
                    JSONObject obj = new JSONObject(customContent);
                    // key1为前台配置的key
                    if (!obj.isNull("type")) { //提醒红点使用
                        int type = obj.getInt("type");
//                        switch (type) {
//                            case 1://认证失败
//                                context.startActivity(new Intent(context, AuthStep1Activity.class));
//                                break;
//                            case 2://审方
//                                context.startActivity(new Intent(context, CheckPaperActivity.class));
//                                break;
//                            case 3://系统消息
//                                context.startActivity(new Intent(context, SystemMsgListActivity.class));
//                                break;
//                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    //反注册的回调
    @Override
    public void onUnregisterResult(Context context, int errorCode) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "反注册成功";
        } else {
            text = "反注册失败" + errorCode;
        }
        LogUtil.d(LogTag, text);
        show(context, text);
    }

    //设置tag的回调
    @Override
    public void onSetTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"设置成功";
        } else {
            text = "\"" + tagName + "\"设置失败,错误码：" + errorCode;
        }
        LogUtil.d(LogTag, text);
//        show(context, text);

    }

    //删除tag的回调
    @Override
    public void onDeleteTagResult(Context context, int errorCode, String tagName) {
        if (context == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = "\"" + tagName + "\"删除成功";
        } else {
            text = "\"" + tagName + "\"删除失败,错误码：" + errorCode;
        }
        LogUtil.d(LogTag, text);
        show(context, text);

    }

    //注册的回调
    @Override
    public void onRegisterResult(Context context, int errorCode,
                                 XGPushRegisterResult message) {
        // TODO Auto-generated method stub
        if (context == null || message == null) {
            return;
        }
        String text = "";
        if (errorCode == XGPushBaseReceiver.SUCCESS) {
            text = message + "注册成功";
            // 在这里拿token
            String token = message.getToken();
        } else {
            text = message + "注册失败错误码：" + errorCode;
        }
        LogUtil.d(LogTag, text);
//        show(context, text);
    }

    // 消息透传的回调
    @Override
    public void onTextMessage(Context context, XGPushTextMessage message) {
        // TODO Auto-generated method stub
        String text = "收到消息:" + message.toString();
        // 获取自定义key-value
        String customContent = message.getCustomContent();
        if (customContent != null && customContent.length() != 0) {
            try {
                JSONObject obj = new JSONObject(customContent);
                // key1为前台配置的key
                if (!obj.isNull("key")) {
                    String value = obj.getString("key");
                    LogUtil.d(LogTag, "get custom value:" + value);
                }
                // ...
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        LogUtil.d("LC", "++++++++++++++++透传消息");
        // APP自主处理消息的过程...
        LogUtil.d(LogTag, text);
//        show(context, text);
    }

}
