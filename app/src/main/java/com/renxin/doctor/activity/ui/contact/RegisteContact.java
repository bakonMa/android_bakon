package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * RegisteContact 注册
 * Create at 2018/4/2 下午6:26 by mayakun 
 */
public interface RegisteContact {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
        void sendMsgCode(String phone, int type);
        void regist(String phone, String pwd, String code);
    }

}
