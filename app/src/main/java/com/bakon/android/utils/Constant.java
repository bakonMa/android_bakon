package com.bakon.android.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Constant 常量
 * Create at 2018/4/9 下午4:43 by mayakun
 */
public interface Constant {
    //分页 每页数量
    int PAGE_SIZE_5 = 5;
    int PAGE_SIZE_DEFAULT_15 = 15;

    //上传图片 0：头像 1：其他认证图片 2:拍照开方照片
    String UPLOADIMG_TYPE_0 = "0";
    String UPLOADIMG_TYPE_1 = "1";
    String UPLOADIMG_TYPE_2 = "2";

    int PAPER_TYPE_2 = 2;//常用处方-查询药物列表详情
    int PAPER_TYPE_3 = 3;//经典处方-查询药物列表详情


    //下载链接（可以使用应用宝的推广链接）
//    String APP_SHARE_URL = "http://a.app.qq.com/o/simple.jsp?pkgname=com.bakon.android";

    //就诊人与患者的关系
    String[] RELATION_TYPE = {"本人", "父母", "子女", "其他亲属", "其他"};

    //发送自定义消息记录 1:问诊单 2:随诊单 3:开方 4:技术咨询
    int CHAT_RECORD_TYPE_1 = 1;
    int CHAT_RECORD_TYPE_2 = 2;
    int CHAT_RECORD_TYPE_3 = 3;
    int CHAT_RECORD_TYPE_4 = 4;

    //药材类型
    Map<String, String> DRUG_TYPE = new HashMap<String, String>() {
        {
            put("ZY", "中草药");
            put("KLJ", "颗粒剂");
            put("ZCY", "中成药");
            put("XY", "西药");
            put("QC", "器材");
        }
    };

    //提现类型 0:待受理 1：受理中 2：提现成功 -1:拒绝受理
    Map<Integer, String> WITHDRAW_TYPE = new HashMap<Integer, String>() {
        {
            put(0, "待受理");
            put(1, "受理中");
            put(2, "提现成功");
            put(-1, "拒绝受理");
        }
    };
}
