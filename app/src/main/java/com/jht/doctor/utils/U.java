package com.jht.doctor.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.ui.bean.ConfigBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Create by mayakun at 2018/3/30 下午3:01
 */

public class U {

    //获取sp中本系统token
    public static String getToken() {
        return DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, "");
    }
    //获取sp中认证状态
    public static int getAuthStatus() {
        return DocApplication.getAppComponent().dataRepo().appSP().getInteger(SPConfig.SP_INT_SUTH_STATUS, 0);
    }
    //获取sp中认证状态 认证状态 0：未认证 1：审核中；2：审核通过 3：审核失败
    public static void setAuthStatus(int status) {
        DocApplication.getAppComponent().dataRepo().appSP().setInteger(SPConfig.SP_INT_SUTH_STATUS, status);
    }
    /**
     * token是否为空，是否登录
     * true : 未登录
     * false： 已登录
     */
    public static boolean isNoToken() {
        return TextUtils.isEmpty(DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN));
    }


    //每页的size
    public static final int PAGE_SIZE = 15;

    //交易状态
    public final static HashMap<String, String> DEAL_STATUS = new HashMap<>();

    static {
        DEAL_STATUS.put("CONFIRMING", "在途中");
        DEAL_STATUS.put("SUCCESS", "交易成功");
        DEAL_STATUS.put("FAIL", "交易失败");
    }

    //交易状态_Color
    public final static HashMap<String, Integer> DEAL_STATUS_COLOR = new HashMap<>();

    static {
        DEAL_STATUS_COLOR.put("CONFIRMING", R.color.color_4f9ef3);
        DEAL_STATUS_COLOR.put("SUCCESS", R.color.color_ccc);
        DEAL_STATUS_COLOR.put("FAIL", R.color.color_trade_failure);
    }

    //交易状态
    public final static HashMap<String, String> RECHARGE_WITHRAW = new HashMap<>();

    static {
        RECHARGE_WITHRAW.put("2001", "充值");
        RECHARGE_WITHRAW.put("2003", "提现");
    }





    //获取base信息 ConfigBean
    public static ConfigBean getConfigData() {
        String json = DocApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_KEY_BASE_CONFIG);
        if (TextUtils.isEmpty(json)) {
            return null;
        } else {
            return new Gson().fromJson(json, ConfigBean.class);
        }
    }

    //配置信息转list
    public static List<String> configToStrs(List<ConfigBean.ConfigItem> configItems) {
        List<String> strs = new ArrayList<>();
        for (ConfigBean.ConfigItem item : configItems) {
            strs.add(item.colNameCn);
        }
        return strs;
    }

    //配置code转string
    public static String keyToValue(String code, List<ConfigBean.ConfigItem> configItems) {
        for (ConfigBean.ConfigItem item : configItems) {
            if (code.equals(item.itemVal))
                return item.colNameCn;
        }
        return "";
    }
}
