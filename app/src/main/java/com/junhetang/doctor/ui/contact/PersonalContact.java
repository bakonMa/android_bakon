package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BasePresenter;
import com.junhetang.doctor.ui.base.BaseView;

/**
 * PersonalContact 个人中心
 * Create at 2018/4/3 上午10:28 by mayakun 
 */

public interface PersonalContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getUserIdentifyStatus();//认证状态
        void getUserBasicInfo();
        void addUserbasic(String content, int type);//个人公告和简介的提交
        void setVisitInfo(String first, String again);//设置资费信息
    }
}
