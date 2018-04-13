package com.renxin.doctor.activity.data.response;

/**
 * description:
 */

public class HttpResponse<T> {
    public boolean success;

    public String code;

    public String msg;

    public T data;
}
