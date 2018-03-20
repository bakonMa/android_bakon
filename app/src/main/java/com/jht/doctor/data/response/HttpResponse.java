package com.jht.doctor.data.response;

/**
 * Created by table on 2017/11/22.
 * description:
 */

public class HttpResponse<T> {
    public boolean success;

    public String errorCode;

    public String errorMsg;

    public T result;
}
