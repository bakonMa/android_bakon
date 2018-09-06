package com.junhetang.doctor.ui.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * PatientFamilyBean 患者和就诊人详情
 * Create at 2018/4/23 上午9:21 by mayakun 
 */
public class PatientFamilyBean {


    /**
     * patientinfo : {"memb_no":"5AC19BAD4E085","advisory_fee":"123","remark_name":"ceshi","head_url":"http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoiazJgwLh28iaK8BYp3QB69uXSFQcVYu0UibSyhqJKica6VEqcm65X9gHVV7Kop0uuK642zWwLvpmXYA/132","nick_name":"雷雷雷雷雷～","memb_name":"李雷","phone":"17625952532","memb_class":"君和堂","im_accid":"sszg001"}
     * jiuzhen : [{"id":1,"patient_name":"啊啊啊","sex":1,"age":20,"relationship":2},{"id":2,"patient_name":"lala","sex":0,"age":15,"relationship":1},{"id":35,"patient_name":"开心果","sex":1,"age":7,"relationship":4}]
     */

    public PatientinfoBean patientinfo;
    public List<JiuzhenBean> jiuzhen;

    public static class PatientinfoBean implements Parcelable{
        /**
         * memb_no : 5AC19BAD4E085
         * advisory_fee : 123
         * remark_name : ceshi
         * head_url : http://thirdwx.qlogo.cn/mmopen/vi_32/DYAIOgq83eoiazJgwLh28iaK8BYp3QB69uXSFQcVYu0UibSyhqJKica6VEqcm65X9gHVV7Kop0uuK642zWwLvpmXYA/132
         * nick_name : 雷雷雷雷雷～
         * memb_name : 李雷
         * phone : 17625952532
         * memb_class : 君和堂
         * im_accid : sszg001
         */

        public String memb_no;
        public String advisory_fee;//咨询费用
        public String remark_name;
        public String head_url;
        public String nick_name;
        public int sex;//0 男 1 女
        public int age;
        public String memb_name;
        public String phone;
        public String memb_class;
        public String valid_name;
        public String im_accid;//云信accid


        protected PatientinfoBean(Parcel in) {
            memb_no = in.readString();
            advisory_fee = in.readString();
            remark_name = in.readString();
            head_url = in.readString();
            nick_name = in.readString();
            sex = in.readInt();
            age = in.readInt();
            memb_name = in.readString();
            phone = in.readString();
            memb_class = in.readString();
            valid_name = in.readString();
            im_accid = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(memb_no);
            dest.writeString(advisory_fee);
            dest.writeString(remark_name);
            dest.writeString(head_url);
            dest.writeString(nick_name);
            dest.writeInt(sex);
            dest.writeInt(age);
            dest.writeString(memb_name);
            dest.writeString(phone);
            dest.writeString(memb_class);
            dest.writeString(valid_name);
            dest.writeString(im_accid);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<PatientinfoBean> CREATOR = new Creator<PatientinfoBean>() {
            @Override
            public PatientinfoBean createFromParcel(Parcel in) {
                return new PatientinfoBean(in);
            }

            @Override
            public PatientinfoBean[] newArray(int size) {
                return new PatientinfoBean[size];
            }
        };
    }

    public static class JiuzhenBean implements Parcelable {
        public String id;
        public String memb_no;//phone_getmempatient接口出现  暂时不使用
        public String patient_name;
        public String phone;
        public int sex;//0 男 1 女
        public int age;
        public int relationship;//就诊人与患者的关系 0：本人 1：父母 2：子女 3：其他亲属 4：其他
        public int is_memb;//select_patient就出现，暂时不使用 1：患者 0：就诊人
        private String im_accid;//就诊人归属的患者的im_accid 业务是使用（自己添加，json中没有）

        protected JiuzhenBean(Parcel in) {
            id = in.readString();
            memb_no = in.readString();
            patient_name = in.readString();
            phone = in.readString();
            sex = in.readInt();
            age = in.readInt();
            relationship = in.readInt();
            is_memb = in.readInt();
            im_accid = in.readString();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(id);
            dest.writeString(memb_no);
            dest.writeString(patient_name);
            dest.writeString(phone);
            dest.writeInt(sex);
            dest.writeInt(age);
            dest.writeInt(relationship);
            dest.writeInt(is_memb);
            dest.writeString(im_accid);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<JiuzhenBean> CREATOR = new Creator<JiuzhenBean>() {
            @Override
            public JiuzhenBean createFromParcel(Parcel in) {
                return new JiuzhenBean(in);
            }

            @Override
            public JiuzhenBean[] newArray(int size) {
                return new JiuzhenBean[size];
            }
        };

        public String getIm_accid() {
            return im_accid;
        }
        public void setIm_accid(String im_accid) {
            this.im_accid = im_accid;
        }

        public JiuzhenBean() {
        }

    }
}
