package com.renxin.doctor.activity.config;

import android.content.Context;

/**
 * @author: ZhaoYun
 * @date: 2017/11/1
 * @project: customer-android-2th
 * @detail:
 */
public interface SPConfig {
    String APP_SP_NAME = "sp_app";

    String SP_STR_TOKEN = "token";
    String SP_INT_SUTH_STATUS = "auth_status";
    String SP_STR_PHONE = "phone";
    String SP_NIM_ACCID = "accid";
    String SP_NIM_ACCTOKEN = "acctoken";

    String SP_USERBEAN = "userbean";
    //消息提醒状态
    String SP_MESSAGE_STATUS = "message_status";



    int GENERAL_SP_MODE = Context.MODE_PRIVATE;
    String FIRST_ENTER = "isFirstEnter";
    String SP_KEY_BASE_CONFIG = "base_config";//科室、职称、擅长等信息

    String BASE_CONFIG = "{\"COMPANY_TYPE\":[{\"colName\":\"\",\"colNameCn\":\"机关、事业\",\"id\":1,\"itemDesc\":\"公司类型\",\"itemName\":\"COMPANY_TYPE\",\"itemVal\":\"01\"},{\"colName\":\"\",\"colNameCn\":\"国有企业\",\"id\":2,\"itemDesc\":\"公司类型\",\"itemName\":\"COMPANY_TYPE\",\"itemVal\":\"02\"},{\"colName\":\"\",\"colNameCn\":\"外资\",\"id\":3,\"itemDesc\":\"公司类型\",\"itemName\":\"COMPANY_TYPE\",\"itemVal\":\"03\"},{\"colName\":\"\",\"colNameCn\":\"合资\",\"id\":4,\"itemDesc\":\"公司类型\",\"itemName\":\"COMPANY_TYPE\",\"itemVal\":\"04\"},{\"colName\":\"\",\"colNameCn\":\"民营\",\"id\":5,\"itemDesc\":\"公司类型\",\"itemName\":\"COMPANY_TYPE\",\"itemVal\":\"05\"},{\"colName\":\"\",\"colNameCn\":\"个体\",\"id\":6,\"itemDesc\":\"公司类型\",\"itemName\":\"COMPANY_TYPE\",\"itemVal\":\"06\"},{\"colName\":\"\",\"colNameCn\":\"其他\",\"id\":7,\"itemDesc\":\"公司类型\",\"itemName\":\"COMPANY_TYPE\",\"itemVal\":\"07\"}],\"HOUSE_TYPE\":[{\"colName\":\"\",\"colNameCn\":\"住宅\",\"id\":40,\"itemDesc\":\"住宅/房屋类型\",\"itemName\":\"HOUSE_TYPE\",\"itemVal\":\"01\"},{\"colName\":\"\",\"colNameCn\":\"酒店公寓\",\"id\":41,\"itemDesc\":\"住宅/房屋类型\",\"itemName\":\"HOUSE_TYPE\",\"itemVal\":\"02\"},{\"colName\":\"\",\"colNameCn\":\"商住两用\",\"id\":42,\"itemDesc\":\"住宅/房屋类型\",\"itemName\":\"HOUSE_TYPE\",\"itemVal\":\"03\"},{\"colName\":\"\",\"colNameCn\":\"别墅\",\"id\":43,\"itemDesc\":\"住宅/房屋类型\",\"itemName\":\"HOUSE_TYPE\",\"itemVal\":\"04\"},{\"colName\":\"\",\"colNameCn\":\"商铺\",\"id\":44,\"itemDesc\":\"住宅/房屋类型\",\"itemName\":\"HOUSE_TYPE\",\"itemVal\":\"05\"},{\"colName\":\"\",\"colNameCn\":\"办公楼/写字楼\",\"id\":45,\"itemDesc\":\"住宅/房屋类型\",\"itemName\":\"HOUSE_TYPE\",\"itemVal\":\"06\"}],\"INDUSTRY\":[{\"colName\":\"\",\"colNameCn\":\"IT/通讯/电子/互联网\",\"id\":10,\"itemDesc\":\"行业\",\"itemName\":\"INDUSTRY\",\"itemVal\":\"01\"},{\"colName\":\"\",\"colNameCn\":\"会计/互联网/银行/保险\",\"id\":11,\"itemDesc\":\"行业\",\"itemName\":\"INDUSTRY\",\"itemVal\":\"02\"},{\"colName\":\"\",\"colNameCn\":\"贸易/消费/制造/营运\",\"id\":12,\"itemDesc\":\"行业\",\"itemName\":\"INDUSTRY\",\"itemVal\":\"03\"},{\"colName\":\"\",\"colNameCn\":\"制药/医疗\",\"id\":13,\"itemDesc\":\"行业\",\"itemName\":\"INDUSTRY\",\"itemVal\":\"04\"},{\"colName\":\"\",\"colNameCn\":\"广告/媒体\",\"id\":14,\"itemDesc\":\"行业\",\"itemName\":\"INDUSTRY\",\"itemVal\":\"05\"},{\"colName\":\"\",\"colNameCn\":\"房地产/建筑\",\"id\":15,\"itemDesc\":\"行业\",\"itemName\":\"INDUSTRY\",\"itemVal\":\"06\"},{\"colName\":\"\",\"colNameCn\":\"专业服务/教育/培训\",\"id\":16,\"itemDesc\":\"行业\",\"itemName\":\"INDUSTRY\",\"itemVal\":\"07\"},{\"colName\":\"\",\"colNameCn\":\"服务业\",\"id\":17,\"itemDesc\":\"行业\",\"itemName\":\"INDUSTRY\",\"itemVal\":\"08\"},{\"colName\":\"\",\"colNameCn\":\"物流/运输\",\"id\":18,\"itemDesc\":\"行业\",\"itemName\":\"INDUSTRY\",\"itemVal\":\"09\"},{\"colName\":\"\",\"colNameCn\":\"能源/原材料\",\"id\":19,\"itemDesc\":\"行业\",\"itemName\":\"INDUSTRY\",\"itemVal\":\"10\"},{\"colName\":\"\",\"colNameCn\":\"政府/非赢利机构\",\"id\":20,\"itemDesc\":\"行业\",\"itemName\":\"INDUSTRY\",\"itemVal\":\"11\"},{\"colName\":\"\",\"colNameCn\":\"其他\",\"id\":21,\"itemDesc\":\"行业\",\"itemName\":\"INDUSTRY\",\"itemVal\":\"12\"}],\"LOAN_USER\":[{\"colName\":\"\",\"colNameCn\":\"日常消费\",\"id\":50,\"itemDesc\":\"借款用途\",\"itemName\":\"LOAN_USER\",\"itemVal\":\"01\"},{\"colName\":\"\",\"colNameCn\":\"资金周转\",\"id\":51,\"itemDesc\":\"借款用途\",\"itemName\":\"LOAN_USER\",\"itemVal\":\"02\"},{\"colName\":\"\",\"colNameCn\":\"教育培训\",\"id\":52,\"itemDesc\":\"借款用途\",\"itemName\":\"LOAN_USER\",\"itemVal\":\"03\"},{\"colName\":\"\",\"colNameCn\":\"医疗\",\"id\":53,\"itemDesc\":\"借款用途\",\"itemName\":\"LOAN_USER\",\"itemVal\":\"04\"}],\"PERIOD_NUMBER\":[{\"colName\":\"\",\"colNameCn\":\"6个月\",\"id\":60,\"itemDesc\":\"借款期数\",\"itemName\":\"PERIOD_NUMBER\",\"itemVal\":\"6\"},{\"colName\":\"\",\"colNameCn\":\"12个月\",\"id\":61,\"itemDesc\":\"借款期数\",\"itemName\":\"PERIOD_NUMBER\",\"itemVal\":\"12\"},{\"colName\":\"\",\"colNameCn\":\"24个月\",\"id\":62,\"itemDesc\":\"借款期数\",\"itemName\":\"PERIOD_NUMBER\",\"itemVal\":\"24\"},{\"colName\":\"\",\"colNameCn\":\"36个月\",\"id\":63,\"itemDesc\":\"借款期数\",\"itemName\":\"PERIOD_NUMBER\",\"itemVal\":\"36\"},{\"colName\":\"\",\"colNameCn\":\"40个月\",\"id\":64,\"itemDesc\":\"借款期数\",\"itemName\":\"PERIOD_NUMBER\",\"itemVal\":\"40\"}],\"POSITION_LEVEL\":[{\"colName\":\"\",\"colNameCn\":\"法人代表\",\"id\":25,\"itemDesc\":\"职位级别\",\"itemName\":\"POSITION_LEVEL\",\"itemVal\":\"01\"},{\"colName\":\"\",\"colNameCn\":\"股东\",\"id\":26,\"itemDesc\":\"职位级别\",\"itemName\":\"POSITION_LEVEL\",\"itemVal\":\"02\"},{\"colName\":\"\",\"colNameCn\":\"个体/私营负责人\",\"id\":27,\"itemDesc\":\"职位级别\",\"itemName\":\"POSITION_LEVEL\",\"itemVal\":\"03\"},{\"colName\":\"\",\"colNameCn\":\"高层管理者\",\"id\":28,\"itemDesc\":\"职位级别\",\"itemName\":\"POSITION_LEVEL\",\"itemVal\":\"04\"},{\"colName\":\"\",\"colNameCn\":\"中层管理者\",\"id\":29,\"itemDesc\":\"职位级别\",\"itemName\":\"POSITION_LEVEL\",\"itemVal\":\"05\"},{\"colName\":\"\",\"colNameCn\":\"基层管理者\",\"id\":30,\"itemDesc\":\"职位级别\",\"itemName\":\"POSITION_LEVEL\",\"itemVal\":\"06\"},{\"colName\":\"\",\"colNameCn\":\"一般正式员工\",\"id\":31,\"itemDesc\":\"职位级别\",\"itemName\":\"POSITION_LEVEL\",\"itemVal\":\"07\"},{\"colName\":\"\",\"colNameCn\":\"派遣员工\",\"id\":32,\"itemDesc\":\"职位级别\",\"itemName\":\"POSITION_LEVEL\",\"itemVal\":\"08\"},{\"colName\":\"\",\"colNameCn\":\"退休人员\",\"id\":33,\"itemDesc\":\"职位级别\",\"itemName\":\"POSITION_LEVEL\",\"itemVal\":\"09\"},{\"colName\":\"\",\"colNameCn\":\"其他\",\"id\":34,\"itemDesc\":\"职位级别\",\"itemName\":\"POSITION_LEVEL\",\"itemVal\":\"10\"}],\"RELATION\":[{\"colName\":\"\",\"colNameCn\":\"父母\",\"id\":110,\"itemDesc\":\"和主借人关系\",\"itemName\":\"RELATION\",\"itemVal\":\"01\"},{\"colName\":\"\",\"colNameCn\":\"配偶\",\"id\":111,\"itemDesc\":\"和主借人关系\",\"itemName\":\"RELATION\",\"itemVal\":\"02\"},{\"colName\":\"\",\"colNameCn\":\"子女\",\"id\":112,\"itemDesc\":\"和主借人关系\",\"itemName\":\"RELATION\",\"itemVal\":\"03\"},{\"colName\":\"\",\"colNameCn\":\"亲属\",\"id\":113,\"itemDesc\":\"和主借人关系\",\"itemName\":\"RELATION\",\"itemVal\":\"04\"},{\"colName\":\"\",\"colNameCn\":\"朋友\",\"id\":114,\"itemDesc\":\"和主借人关系\",\"itemName\":\"RELATION\",\"itemVal\":\"05\"}],\"REPAYMENT_TYPE\":[{\"colName\":\"\",\"colNameCn\":\"先息后本\",\"id\":56,\"itemDesc\":\"还款方式\",\"itemName\":\"REPAYMENT_TYPE\",\"itemVal\":\"01\"},{\"colName\":\"\",\"colNameCn\":\"等额本息\",\"id\":57,\"itemDesc\":\"还款方式\",\"itemName\":\"REPAYMENT_TYPE\",\"itemVal\":\"02\"}]}";

    String SP_BOOL_LASTCHECK_FORCEUPDATE_NAME = "isForceUpdate";//上一次检查更新时，是否是检查到要强制更新
    String SP_LONG_LASTCHECKUPDATE_TIME_NAME = "lastCheckUpdateTime";//上一次检查更新的时间戳

}