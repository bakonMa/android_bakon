package com.renxin.doctor.activity.utils;

import android.os.Message;

/**
 * Created by table on 2017/11/24.
 * description:
 */

public class M {
    public static Message createMessage(Object object,int what){
        Message message = Message.obtain();
        message.what = what;
        message.obj = object;
        return message;
    }
}
