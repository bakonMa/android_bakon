package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.data.http.Params;
import com.renxin.doctor.activity.ui.base.BasePresenter;
import com.renxin.doctor.activity.ui.base.BaseView;

/**
 * OpenPaperContact  开方相关
 * Create at 2018/4/23 下午7:21 by mayakun
 */
public interface OpenPaperContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void uploadImg(String path, String type);

        void openPaperCamera(Params params);//拍照开方

        void openPaperOnline(Params params);//在线开方

        void searchSkillName(String name);//搜索疾病名称

        void searchDrugName(int store_id, String name);//搜索药材名称

        void searchDrugPaperById(int store_id, int id);//药材中的处方 开方需要store_id，添加常用处方不需要

        void getOftenmedList();//常用处方列表

        void addOftenmed(Params params);//添加常用处方

        void delOftenmed(String ids);//删除常用处方

        void addChatRecord(String d_accid, String p_accid, int type, int source);//后台统计 1:问诊单 2:随诊单 3:开方 记录

        void getCheckPaperList(int type);//审核处方列表


    }

}
