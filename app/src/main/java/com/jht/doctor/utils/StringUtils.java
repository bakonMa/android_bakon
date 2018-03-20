package com.jht.doctor.utils;

/**
 * Created by table on 2017/11/28.
 * description:
 */

public class StringUtils {
    public static boolean isEmpty(String str) {
        if (str == null) {
            return true;
        }
        if (str.length() == 0) {
            return true;
        }
        if (str.equals("")) {
            return true;
        }
        return false;
    }
}
