package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * PersonalContact 个人中心
 * Create at 2018/4/3 上午10:28 by mayakun 
 */

public interface PersonalContact {
    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void getUserBasicInfo();
        void getUserIdentifyStatus();
        void addUserbasic(String content, int type);//个人公告和简介的提交
        void getVisitInfo();//资费信息
        void setVisitInfo(String first, String again);//设置资费信息
    }
}
