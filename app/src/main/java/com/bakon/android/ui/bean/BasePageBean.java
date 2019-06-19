package com.bakon.android.ui.bean;

import java.util.List;

/**
 * 分页bean
 */
public class BasePageBean<T> {
    public int is_last;//是否最后一页 1：是 0：否
    public int page;//当前页码
    public List<T> list;
}
