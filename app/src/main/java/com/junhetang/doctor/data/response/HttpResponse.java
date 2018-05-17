package com.junhetang.doctor.data.response;

/**
 * description:
 */

public class HttpResponse<T> {
    public boolean success;

    public String code;

    public String msg;

    public T data;
}
