package com.junhetang.doctor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

import com.junhetang.doctor.application.DocApplication;


public class NetUtil {


    /**
     * 没有连接网络
     */
    public static final int NETWORK_NONE = -1;
    /**
     * 移动网络
     */
    public static final int NETWORK_MOBILE = 0;
    /**
     * 无线网络
     */
    public static final int NETWORK_WIFI = 1;
    /**
     * 2G网络
     */
    public static final int NETWORK_2G = 2;
    /**
     * 3G网络
     */
    public static final int NETWORK_3G = 3;
    /**
     * 4G网络
     */
    public static final int NETWORK_4G = 4;
    /**
     * 未知
     */
    public static final int NETWORK_UNKNOW = -2;

    /**
     * 判断当前网络是否可用
     */
    public static boolean isNetWorkAviliable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) DocApplication.getInstance()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo info = connectivityManager.getActiveNetworkInfo();
            if (info != null) {
                return info.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断网络的类型
     *
     * @return 移动网络, wifi类型
     */
    public static int getNetWorkState() {
        // 得到连接管理器对象
        ConnectivityManager connectivityManager = (ConnectivityManager) DocApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if (activeNetworkInfo != null && activeNetworkInfo.isAvailable()) {
            if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
                return NETWORK_WIFI;
            } else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
                return NETWORK_MOBILE;
            }
        }
        return NETWORK_NONE;
    }


    /**
     * 判断网络速度的类型
     *
     * @return 移动网络类型
     */
    public static int getNetworkSpeedMode() {
        int netType = getNetWorkState();
        if (netType < 0) {
            //result = "网络不可用，请检测网络";
            return NETWORK_NONE;
        }
        //WIFI
        if (netType == NETWORK_WIFI) {//wifi
            //result = "当前WIFI网络";
            return NETWORK_WIFI;
        }
        //2G,3G,4G
        if (netType == NETWORK_MOBILE) {//移动网络
            TelephonyManager mTelephonyManager = (TelephonyManager) DocApplication.getInstance().getSystemService(Context.TELEPHONY_SERVICE);
            int networkType = mTelephonyManager.getNetworkType();
            switch (networkType) {
                case TelephonyManager.NETWORK_TYPE_GPRS:
                case TelephonyManager.NETWORK_TYPE_EDGE:
                case TelephonyManager.NETWORK_TYPE_CDMA:
                case TelephonyManager.NETWORK_TYPE_1xRTT:
                case TelephonyManager.NETWORK_TYPE_IDEN:
                    //result = "当前网络为2G网络，网速较慢";
                    return NETWORK_2G;
                case TelephonyManager.NETWORK_TYPE_UMTS:
                case TelephonyManager.NETWORK_TYPE_EVDO_0:
                case TelephonyManager.NETWORK_TYPE_EVDO_A:
                case TelephonyManager.NETWORK_TYPE_HSDPA:
                case TelephonyManager.NETWORK_TYPE_HSUPA:
                case TelephonyManager.NETWORK_TYPE_HSPA:
                case TelephonyManager.NETWORK_TYPE_EVDO_B:
                case TelephonyManager.NETWORK_TYPE_EHRPD:
                case TelephonyManager.NETWORK_TYPE_HSPAP:
                    //result = "当前网络为3G网络，网速较慢";
                    return NETWORK_3G;
                case TelephonyManager.NETWORK_TYPE_LTE://2G,3G网络 提示，其他不提示
                    //result = "当前网络为4G网络";
                    return NETWORK_4G;
            }
        }
        return NETWORK_UNKNOW;
    }

}