package com.junhetang.doctor.ui.bean;

import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.junhetang.doctor.utils.CharacterParser;

/**
 * PatientBean
 * Create at 2018/4/14 下午11:57 by mayakun
 */
public class PatientBean implements Comparable<PatientBean> {

//    id	int	患者id
//    memb_no	int	患者编号
//    nick_name	string	微信昵称
//    im_accid	string	云信accid
//    head_url	string	头像
//    memb_class	string	会员性质

    public int id;
    public String head_url;
    public String nick_name;
    public String memb_no;
    public String im_accid;
    public String remark_name;
    public String memb_class;
    public int is_new;//是否是新添加的 显示红点 1：新患者 0：老患者

    @Override
    public int compareTo(@NonNull PatientBean bean) {
        //当前对象
        String string = CharacterParser.getInstance().getSelling(TextUtils.isEmpty(nick_name) ? "" : nick_name);
        if (CharacterParser.getInstance().getInitials(string).equals("#")) {
            return 1;
        }
        char[] chars = string.toCharArray();
        //当前bean
        String antherString = CharacterParser.getInstance().getSelling(TextUtils.isEmpty(bean.nick_name) ? "" : bean.nick_name);
        if (CharacterParser.getInstance().getInitials(antherString).equals("#")) {
            return -1;
        }
        char[] anotherChars = antherString.toCharArray();

        int length = chars.length > anotherChars.length ? anotherChars.length : chars.length;
        for (int i = 0; i < length; i++) {
            if (chars[i] < anotherChars[i]) {
                return -1;
            } else if (chars[i] > anotherChars[i]) {
                return 1;
            }
        }
        return 0;
    }
}
