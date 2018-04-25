package com.renxin.doctor.activity.nim.message.extension;


import com.alibaba.fastjson.JSONObject;

/**
 * FirstMessageAttachment 聊天第一天消息（患者信息）
 * Create at 2018/4/24 上午9:27 by mayakun
 */
public class FirstMessageAttachment extends CustomAttachment {

    private final String PATIENTS_TITLE = "patients_title";
    private final String PATIENTS_NAME = "patients_name";
    private final String PATIENTS_SEX = "patients_sex";
    private final String PATIENTS_AGE = "patients_age";

    //患者发给医生数据
    private String patients_title;//标题
    private String patients_name;// 名字
    private String patients_sex;// 性别
    private String patients_age;// 年龄


    protected FirstMessageAttachment() {
        super(CustomAttachmentType.MESSAGE_PAY_OK);
    }


    @Override
    protected void parseData(JSONObject data) {
        this.patients_title = data.getString(PATIENTS_TITLE);
        this.patients_name = data.getString(PATIENTS_NAME);
        this.patients_sex = data.getString(PATIENTS_SEX);
        this.patients_age = data.getString(PATIENTS_AGE);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(PATIENTS_TITLE, patients_title);
        data.put(PATIENTS_NAME, patients_name);
        data.put(PATIENTS_SEX, patients_sex);
        data.put(PATIENTS_AGE, patients_age);
        return data;
    }


    @Override
    public int getType() {
        return super.getType();
    }

    public String getPatients_title() {
        return patients_title;
    }

    public void setPatients_title(String patients_title) {
        this.patients_title = patients_title;
    }

    public String getPatients_name() {
        return patients_name;
    }

    public void setPatients_name(String patients_name) {
        this.patients_name = patients_name;
    }

    public String getPatients_sex() {
        return patients_sex;
    }

    public void setPatients_sex(String patients_sex) {
        this.patients_sex = patients_sex;
    }

    public String getPatients_age() {
        return patients_age;
    }

    public void setPatients_age(String patients_age) {
        this.patients_age = patients_age;
    }
}