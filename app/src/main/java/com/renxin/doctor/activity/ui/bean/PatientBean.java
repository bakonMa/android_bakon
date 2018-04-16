package com.renxin.doctor.activity.ui.bean;

import android.support.annotation.NonNull;

import com.renxin.doctor.activity.utils.CharacterParser;

/**
 * PatientBean
 * Create at 2018/4/14 下午11:57 by mayakun
 */
public class PatientBean implements Comparable<PatientBean> {

    public int id;
    public String headImg;
    public String nickname;

    public PatientBean(int id, String headImg, String nickname) {
        this.id = id;
        this.headImg = headImg;
        this.nickname = nickname;
    }

    @Override
    public int compareTo(@NonNull PatientBean bean) {
        String string = CharacterParser.getInstance().getSelling(nickname);
        if (CharacterParser.getInstance().getInitials(string).equals("#")) {
            return 1;
        }
        char[] chars = string.toCharArray();
        String antherString = CharacterParser.getInstance().getSelling(bean.nickname);
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
