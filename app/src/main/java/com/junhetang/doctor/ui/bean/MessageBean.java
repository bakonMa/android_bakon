package com.junhetang.doctor.ui.bean;

import java.util.List;

/**
 * Created by mayakun on 2017/11/20.
 * 消息中心-消息bean
 */

public class MessageBean {

    public int pageNo;
    public int pageSize;
    public int totalCount;
    public List<MessageItem> data;
    
    public static class MessageItem {

        public String createAt;
        public int id;
        public String msgContent;
        public String msgTitle;
        public boolean readStatus;

        public MessageItem(String createAt, int id, String msgContent, String msgTitle, boolean readStatus) {
            this.createAt = createAt;
            this.id = id;
            this.msgContent = msgContent;
            this.msgTitle = msgTitle;
            this.readStatus = readStatus;
        }
    }
}
