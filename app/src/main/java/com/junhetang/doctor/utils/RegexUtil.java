package com.junhetang.doctor.utils;

import android.text.TextUtils;

import java.math.RoundingMode;
import java.text.DecimalFormat;


/**
 * Created by mayakun on 2017/11/21.
 */

public class RegexUtil {

    public static String STYLE_MONEY_TWO = "##,##0.00";
    public static String STYLE_MONEY_ZERO = "##,##0";

    /**
     * 格式化金额  "123,456.12"
     */
    public static String formatMoney(String price) {
        return formatMoney(price, false, STYLE_MONEY_TWO);
    }

    /**
     * 格式化金额  "123,456.12"
     */
    public static String formatMoney(double price) {
        return formatMoney(String.valueOf(price), false, STYLE_MONEY_TWO);
    }

    /**
     * 格式化金额  "123456.12 -> 123,456.12"
     */
    public static String formatMoneyNoZero(double price) {
        return formatMoney(String.valueOf(price), false, STYLE_MONEY_ZERO);
    }

    /**
     * 格式化金额
     *
     * @param price  12345678901234.12645678，小数点前最多14位
     * @param halfUp false  强制向下取值 true 四舍五入
     * @return "123,456.12"
     */
    public static String formatMoney(String price, boolean halfUp, String style) {
        DecimalFormat formater = new DecimalFormat();
        formater.applyPattern(style);//style形式
//        formater.setMaximumFractionDigits(2);//保留2位小数
//        formater.setGroupingSize(3);//3位一组用逗号分隔
        formater.setRoundingMode(halfUp ? RoundingMode.HALF_UP : RoundingMode.FLOOR);//四舍五入的模式
        return formater.format(Double.parseDouble(price));
    }

    /**
     * 格式化金额
     *
     * @param price 12345678901234.12645678，小数点前最多14位
     * @return "123456.12"
     */
    public static String formatDoubleMoney(Double price) {
        DecimalFormat formater = new DecimalFormat();
        formater.applyPattern("#.##");//style形式
        return formater.format(price);
    }
    /**
     * 格式化金额
     *
     * @param price 12345678901234.12645678，小数点前最多14位
     * @return "123456.12"
     */
    public static String formatDoubleMoney(String price) {
       return formatDoubleMoney(Double.parseDouble(price));
    }

    /**
     * 手机号码，中间4位星号替换
     *
     * @param phone 手机号
     * @return 星号替换的手机号
     */
    public static String hidePhone(String phone) {
        if (TextUtils.isEmpty(phone)) {
            return "";
        }
        // 括号表示组，被替换的部分$n表示第n组的内容
        // 正则表达式中，替换字符串，括号的意思是分组，在replace()方法中，
        // 参数二中可以使用$n(n为数字)来依次引用模式串中用括号定义的字串。
        // "(\d{3})\d{4}(\d{4})", "$1****$2"的这个意思就是用括号，
        // 分为(前3个数字)中间4个数字(最后4个数字)替换为(第一组数值，保持不变$1)(中间为*)(第二组数值，保持不变$2)
        return phone.replaceAll("(\\d{3})\\d{4}(\\d{4})", "$1****$2");
    }

    /**
     * 姓名，姓 用星号替换
     *
     * @param name 姓名
     * @return 星号替换的姓名
     */
    public static String hideFirstName(String name) {
        if (TextUtils.isEmpty(name)) {
            return "";
        }
        return name.replaceFirst("\\S{1}", "*");
    }

    /**
     * 银行卡号，保留最后4位，其他星号替换
     *
     * @param cardId 卡号
     * @return 星号替换的银行卡号
     */
    public static String hideBankCardId(String cardId) {
        if (TextUtils.isEmpty(cardId)) {
            return "";
        }
        int len = cardId.length();
        switch (len) {
            case 16:
                return cardId.replaceAll("\\d{13}(\\d{3})", "**** **** **** *$1");
            case 17:
                return cardId.replaceAll("\\d{14}(\\d{2})(\\d{1})", "**** **** **** **$1 $2");
            case 18:
                return cardId.replaceAll("\\d{15}(\\d{1})(\\d{2})", "**** **** **** ***$1 $2");
            case 19:
                return cardId.replaceAll("\\d{16}(\\d{3})", "**** **** **** **** $1");
            default:
                StringBuffer format = new StringBuffer("\\d{");
                format.append(len - 3).append("}(\\d{3})");
                //format = "\\d{16}(\\d{3})"
                return cardId.replaceAll(format.toString(), "**** **** **** **** $1");
        }
    }

    /**
     * 身份证号，中间 星号替换
     *
     * @param id 身份证号
     * @return 星号替换的身份证号
     */
    public static String hideID(String id) {
        if (TextUtils.isEmpty(id)) {
            return "";
        }
        if (id.length() == 18) {
            return id.replaceAll("(\\d{1})\\d{16}(\\S{1})", "$1***** ******** ***$2");
        } else {//老的身份证15位
            return id.replaceAll("(\\d{1})\\d{13}(\\S{1})", "$1** ******** ***$2");
        }
    }

    /**
     * 身份证号，中间 替换
     *
     * @param id 身份证号
     * @return 星号替换的身份证号
     */
    public static String hideIDNormal(String id) {
        if (TextUtils.isEmpty(id)) {
            return "";
        }
        StringBuffer format = new StringBuffer("(\\d{1})\\d{");
        format.append(id.length() - 2).append("}(\\S{1})");
        //format="(\\d{1})\d{16}(\S{1})"
        return id.replaceAll(format.toString(), "$1****************$2");
    }
}
