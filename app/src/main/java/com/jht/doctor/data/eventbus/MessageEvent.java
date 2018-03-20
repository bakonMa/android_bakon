package com.jht.doctor.data.eventbus;

/**
 * Created by table on 2017/11/28.
 * description:
 */

public class MessageEvent {
    private String message;

    public MessageEvent(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
