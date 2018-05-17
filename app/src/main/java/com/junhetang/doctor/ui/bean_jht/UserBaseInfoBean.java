package com.junhetang.doctor.ui.bean_jht;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * UserBaseInfoBean 用户基本信息
 * Create at 2018/4/9 下午6:40 by mayakun
 */
public class UserBaseInfoBean implements Parcelable {

    /**
     * name : 哈哈
     * phone : 13011096420
     * sex : 0
     * header : https://jhtpub.oss-cn-shanghai.aliyuncs.com/app/header/20180421/1524291240805.jpg
     * hospital : 安庆市石化医院
     * department : 外科
     * title : 主治医师
     * skills : 代谢综合征,甲状腺病
     * push_status : 1
     * fee_explain : 1.您可通过图文、语音与患者交流，首次回复需在24小时内，默认单次交流时间72小时，与患者沟通后，经双方同意可随时结束对话。
     * 2.患者首次咨询后，下次咨询将按复诊价格收取，您可自定义初诊、复诊价格，建议为老患者提供适当的优惠，您还可以进入患者列表，给某个患者自定义价格。
     * 3.若患者未在线上进行咨询，您直接为患者开方，则可在开方时补充服务费。
     * 4.咨询过程中给患者发送问诊单或随诊单不收取任何费用。若咨询结束或给从未在线咨询的患者发送问诊单或随诊单时，按初诊或复诊价格向患者收取咨询费用。
     * notice : 本人近期停诊，恢复时间待定，给您造成不便，敬请谅解，如有疑问，请联系君和客服，电话：15871357371。
     * my_explain : 简介
     * first_diagnose : 101
     * second_diagnose : 51
     */

    public String name;
    public String phone;
    public int sex;
    public String header;
    public String hospital;
    public String department;
    public String title;
    public String skills;
    public int push_status;
    public String fee_explain;
    public String notice;
    public String my_explain;
    public String first_diagnose;
    public String second_diagnose;

    protected UserBaseInfoBean(Parcel in) {
        name = in.readString();
        phone = in.readString();
        sex = in.readInt();
        header = in.readString();
        hospital = in.readString();
        department = in.readString();
        title = in.readString();
        skills = in.readString();
        push_status = in.readInt();
        fee_explain = in.readString();
        notice = in.readString();
        my_explain = in.readString();
        first_diagnose = in.readString();
        second_diagnose = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(phone);
        dest.writeInt(sex);
        dest.writeString(header);
        dest.writeString(hospital);
        dest.writeString(department);
        dest.writeString(title);
        dest.writeString(skills);
        dest.writeInt(push_status);
        dest.writeString(fee_explain);
        dest.writeString(notice);
        dest.writeString(my_explain);
        dest.writeString(first_diagnose);
        dest.writeString(second_diagnose);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<UserBaseInfoBean> CREATOR = new Creator<UserBaseInfoBean>() {
        @Override
        public UserBaseInfoBean createFromParcel(Parcel in) {
            return new UserBaseInfoBean(in);
        }

        @Override
        public UserBaseInfoBean[] newArray(int size) {
            return new UserBaseInfoBean[size];
        }
    };
}
