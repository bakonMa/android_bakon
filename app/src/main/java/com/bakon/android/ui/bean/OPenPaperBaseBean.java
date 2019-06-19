package com.bakon.android.ui.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * OPenPaperBaseBean
 * Create at 2018/4/23 下午7:26 by mayakun
 */
public class OPenPaperBaseBean {

    public List<StoreBean> store;//药房
    public List<CommBean> drug_class;//剂型
    public List<CommBean> usage;//用法
    public List<CommBean> frequency;//用量
    public ArrayList<CommBean> drugremark;//药材煎法
    public ArrayList<DocadviceBean> docadvice;//医嘱

    public static class StoreBean {
        public int drug_store_id;
        public String drug_store_name;
    }

    public static class CommBean implements Parcelable{
        public int id;
        public String name;
        public String ename;

        protected CommBean(Parcel in) {
            id = in.readInt();
            name = in.readString();
            ename = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeString(ename);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<CommBean> CREATOR = new Creator<CommBean>() {
            @Override
            public CommBean createFromParcel(Parcel in) {
                return new CommBean(in);
            }

            @Override
            public CommBean[] newArray(int size) {
                return new CommBean[size];
            }
        };
    }


    public static class DocadviceBean implements Parcelable {
        public String title;
        public String[] content;

        protected DocadviceBean(Parcel in) {
            title = in.readString();
            content = in.createStringArray();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(title);
            dest.writeStringArray(content);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<DocadviceBean> CREATOR = new Creator<DocadviceBean>() {
            @Override
            public DocadviceBean createFromParcel(Parcel in) {
                return new DocadviceBean(in);
            }

            @Override
            public DocadviceBean[] newArray(int size) {
                return new DocadviceBean[size];
            }
        };
    }

}
