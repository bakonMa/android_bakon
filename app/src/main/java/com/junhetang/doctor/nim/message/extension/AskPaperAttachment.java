package com.junhetang.doctor.nim.message.extension;


import com.alibaba.fastjson.JSONObject;
import com.junhetang.doctor.nim.NimU;

/**
 * AskPaperAttachment 问诊单
 * Create at 2018/4/17 下午5:50 by mayakun
 */
public class AskPaperAttachment extends CustomAttachment {

    private final String TYPEID = "typeID";
    private final String DOCTOR_ACCID = "doctor_accid";

    private final String PATIENTS_TITLE = "patients_title";
    private final String PATIENTS_NAME = "patients_name";
    private final String PATIENTS_SEX = "patients_sex";
    private final String PATIENTS_AGE = "patients_age";
    private final String PATIENTS_DESCRIBE = "patients_describe";
    private final String LINKID = "linkID";

    //医生发给患者数据
    private String typeID;//问诊单h5中【男性-0，女性-1，儿童-2】
    private String doctor_accid;// 医生云信账号
    //患者发给医生数据
    private String patients_title;//标题
    private String patients_name;// 名字
    private String patients_sex;// 性别
    private String patients_age;// 年龄
    private String patients_describe;// 症状描述
    private String linkID;// 跳转问诊单ID


    protected AskPaperAttachment() {
        super(CustomAttachmentType.MESSAGE_ASK_PAPER);
    }

    public AskPaperAttachment(String typeID) {
        this();
        this.typeID = typeID;
        this.doctor_accid = NimU.getNimAccount();
    }

    @Override
    protected void parseData(JSONObject data) {
        this.typeID = data.getString(TYPEID);
        this.doctor_accid = data.getString(DOCTOR_ACCID);

        this.patients_title = data.getString(PATIENTS_TITLE);
        this.patients_name = data.getString(PATIENTS_NAME);
        this.patients_sex = data.getString(PATIENTS_SEX);
        this.patients_age = data.getString(PATIENTS_AGE);
        this.patients_describe = data.getString(PATIENTS_DESCRIBE);
        this.linkID = data.getString(LINKID);
    }

    @Override
    protected JSONObject packData() {
        JSONObject data = new JSONObject();
        data.put(TYPEID, typeID);
        data.put(DOCTOR_ACCID, doctor_accid);

        data.put(PATIENTS_TITLE, patients_title);
        data.put(PATIENTS_NAME, patients_name);
        data.put(PATIENTS_SEX, patients_sex);
        data.put(PATIENTS_AGE, patients_age);
        data.put(PATIENTS_DESCRIBE, patients_describe);
        data.put(LINKID, linkID);
        return data;
    }


    @Override
    public int getType() {
        return super.getType();
    }

    public String getTypeID() {
        return typeID;
    }

    public void setTypeID(String typeID) {
        this.typeID = typeID;
    }

    public String getDoctor_accid() {
        return doctor_accid;
    }

    public void setDoctor_accid(String doctor_accid) {
        this.doctor_accid = doctor_accid;
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

    public String getPatients_describe() {
        return patients_describe;
    }

    public void setPatients_describe(String patients_describe) {
        this.patients_describe = patients_describe;
    }

    public String getLinkID() {
        return linkID;
    }

    public void setLinkID(String linkID) {
        this.linkID = linkID;
    }
}