package com.renxin.doctor.activity.ui.bean_jht;

/**
 * Created by admin on 2017/2/13.
 */

public class XGNotificationBean {
    private Integer id;
    private long msg_id;
    private String title;
    private String content;
    private String activity;
    private int notificationActionType;
    private String update_time;
    private String custom_content;

    public String getCustom_content() {
        return custom_content;
    }

    public void setCustom_content(String custom_content) {
        this.custom_content = custom_content;
    }


    public void setId(Integer id) {
        this.id = id;
    }

    public void setMsg_id(long msg_id) {
        this.msg_id = msg_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public void setNotificationActionType(int notificationActionType) {
        this.notificationActionType = notificationActionType;
    }

    public void setUpdate_time(String update_time) {
        this.update_time = update_time;
    }


    public Integer getId() {
        return id;
    }

    public long getMsg_id() {
        return msg_id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getActivity() {
        return activity;
    }

    public int getNotificationActionType() {
        return notificationActionType;
    }

    public String getUpdate_time() {
        return update_time;
    }


    public XGNotificationBean() {

    }

}
