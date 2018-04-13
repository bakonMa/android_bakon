package com.renxin.doctor.activity.ui.bean_jht;

import android.os.Parcel;
import android.os.Parcelable;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 科室、职称、擅长等信息
 * BaseConfigBean
 * Create at 2018/4/4 上午9:32 by mayakun
 */

public class BaseConfigBean {


    /**
     * department : [{"id":1,"category":"内科"},{"id":2,"category":"外科"}]
     * title : ["住院医师","主治医师","副主任医师","主任医师"]
     * skills : {"category" :"内分泌科", "name" : [{"id":1,"category":"糖尿病"},{"id":2,"category":"甲亢"},{"id":3,"category":"甲减"},{"id":4,"category":"甲状腺病"},{"id":5,"category":"痛风"},{"id":6,"category":"内分泌失调"},{"id":7,"category":"代谢综合征"}]}
     */

    public ArrayList<SkillsBean> skills;
    public List<DepartmentBean> department;
    public List<String> title;

    public static class DepartmentBean {
        public int id;
        public String name;

    }

    //疾病
    public static class SkillsBean implements Parcelable {
        public String category;
        public List<Skill> name;


        protected SkillsBean(Parcel in) {
            category = in.readString();
            name = in.createTypedArrayList(Skill.CREATOR);
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(category);
            dest.writeTypedList(name);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<SkillsBean> CREATOR = new Creator<SkillsBean>() {
            @Override
            public SkillsBean createFromParcel(Parcel in) {
                return new SkillsBean(in);
            }

            @Override
            public SkillsBean[] newArray(int size) {
                return new SkillsBean[size];
            }
        };
    }

    //具体疾病名称
    public static class Skill implements MultiItemEntity,Parcelable {

        public int id;
        public String name;
        //区分ui使用
        public int itemType;
        public boolean isSelect;

        public Skill(String name, int itemType) {
            this.name = name;
            this.itemType = itemType;
        }

        protected Skill(Parcel in) {
            id = in.readInt();
            name = in.readString();
            itemType = in.readInt();
        }

        @Override
        public int getItemType() {
            return itemType;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(id);
            dest.writeString(name);
            dest.writeInt(itemType);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Skill> CREATOR = new Creator<Skill>() {
            @Override
            public Skill createFromParcel(Parcel in) {
                return new Skill(in);
            }

            @Override
            public Skill[] newArray(int size) {
                return new Skill[size];
            }
        };


    }


}
