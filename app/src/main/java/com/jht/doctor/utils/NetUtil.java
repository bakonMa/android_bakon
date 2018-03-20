package com.jht.doctor.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.jht.doctor.application.CustomerApplication;


public class NetUtil {

	/**
	 * 判断当前网络是否可用
	 */
	public static boolean isNetWorkAviliable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) CustomerApplication.getInstance().getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo info = connectivityManager.getActiveNetworkInfo();
			if (info != null) {
				return info.isAvailable();
			}
		}
		return false;
	}

	/**
	 * 没有连接网络
	 */
	private static final int NETWORK_NONE = -1;
	/**
	 * 移动网络
	 */
	private static final int NETWORK_MOBILE = 0;
	/**
	 * 无线网络
	 */
	private static final int NETWORK_WIFI = 1;

	public static int getNetWorkState(Context context) {
		// 得到连接管理器对象
		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {

			if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_WIFI)) {
				return NETWORK_WIFI;
			} else if (activeNetworkInfo.getType() == (ConnectivityManager.TYPE_MOBILE)) {
				return NETWORK_MOBILE;
			}
		} else {
			return NETWORK_NONE;
		}
		return NETWORK_NONE;
	}

}
