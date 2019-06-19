package com.bakon.android.ui.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * CommMessageBean 编辑常用语
 * Create at 2018/4/19 上午11:14 by mayakun
 */
public class CommMessageBean implements Parcelable{

    public boolean isCheck;//view选择使用，默认false
    public int id;
    public String content;

    public CommMessageBean() {
        this.isCheck = false;
    }

    protected CommMessageBean(Parcel in) {
        isCheck = in.readByte() != 0;
        id = in.readInt();
        content = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (isCheck ? 1 : 0));
        dest.writeInt(id);
        dest.writeString(content);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<CommMessageBean> CREATOR = new Creator<CommMessageBean>() {
        @Override
        public CommMessageBean createFromParcel(Parcel in) {
            return new CommMessageBean(in);
        }

        @Override
        public CommMessageBean[] newArray(int size) {
            return new CommMessageBean[size];
        }
    };
}
