package com.renxin.doctor.activity.nim.message.extension;


import com.alibaba.fastjson.JSONObject;

/**
 * CloseChatAttachment 问诊结束
 * Create at 2018/4/17 下午5:50 by mayakun
 */
public class CloseChatAttachment extends CustomAttachment {

    private final String MESSAGE_KEY = "message";


    private String message;//消息内容

    protected CloseChatAttachment() {
        super(CustomAttachmentType.MESSAGE_ClOSE);
    }

    public CloseChatAttachment(String message) {
        this();
        this.message = message;
    }

    @Override
    protected void parseData(JSONObject data) {
        this.message = data.getString(MESSAGE_KEY);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(MESSAGE_KEY, message);
        return data;
    }

    @Override
    public int getType() {
        return super.getType();
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}