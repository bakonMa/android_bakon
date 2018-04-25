package com.renxin.doctor.activity.nim.message.extension;


import com.alibaba.fastjson.JSONObject;

/**
 * OPenPaperAttachment 开方子
 * Create at 2018/4/24 下午6:56 by mayakun
 */
public class OPenPaperAttachment extends CustomAttachment {

    private final String MESSAGE_KEY = "message";

    private String message;//消息内容

    protected OPenPaperAttachment() {
        super(CustomAttachmentType.MESSAGE_DOC_OPENPAPER);
    }

    public OPenPaperAttachment(String message) {
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