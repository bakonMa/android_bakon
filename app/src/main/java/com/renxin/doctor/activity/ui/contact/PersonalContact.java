package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BasePresenter;
import com.renxin.doctor.activity.ui.base.BaseView;

/**
 * PersonalContact 个人中心
 * Create at 2018/4/3 上午10:28 by mayakun 
 */

public interface PersonalContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getUserBasicInfo();
        void addUserbasic(String content, int type);//个人公告和简介的提交
        void setVisitInfo(String first, String again);//设置资费信息
    }
}
