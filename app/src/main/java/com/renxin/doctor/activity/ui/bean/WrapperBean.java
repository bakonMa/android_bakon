package com.renxin.doctor.activity.ui.bean;

/**
 * Created by table on 2017/12/9.
 * description:实体类进行包装 保存item的选中和未选中状态
 */

public class WrapperBean<T> {
    private T t;

    private boolean isChecked = false;

    public WrapperBean(T t) {
        this.t = t;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }
}
