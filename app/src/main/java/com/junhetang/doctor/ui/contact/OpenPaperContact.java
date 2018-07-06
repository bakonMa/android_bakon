package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.ui.base.BasePresenter;
import com.junhetang.doctor.ui.base.BaseView;

/**
 * OpenPaperContact  开方相关
 * Create at 2018/4/23 下午7:21 by mayakun
 */
public interface OpenPaperContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void uploadImg(String path, String type);

        void openPaperCamera(Params params);//拍照开方 v1.0.3开始使用

        void openPaperOnline(Params params);//在线开方

        void searchSkillName(String name);//搜索疾病名称

        void searchDrugName(int store_id, String name);//搜索药材名称

        void searchDrugPaperById(int store_id, int id, int type);//药材中的处方 开方需要store_id，添加常用处方不需要

        void getOftenmedList(int page, int type, String searchStr);//常用/经典处方列表

        void addOftenmed(Params params);//添加常用处方

        void delOftenmed(String ids);//删除常用处方

        void addChatRecord(String d_accid, String p_accid, int type, int source);//后台统计 1:问诊单 2:随诊单 3:开方 记录

        void getCheckPaperList(int type);//审核处方列表

        void checkPaper(int id, int status, String remark);//医生审核处方提交接口

        void getPaperHistoryList(int page, int status, String searchStr);//历史处方和搜索

        void getJiuZhenHistoryList(int page, String searchStr);//历史就诊人和搜索

        void classicsPaperUp(int id);//经典处方 置顶/取消置顶

        void getPaperInfo(int id);//处方详情数据（调用此方 使用）

        void changeDrugType(int storId, int type, String drugJson);//普药 精品转换

    }

}
