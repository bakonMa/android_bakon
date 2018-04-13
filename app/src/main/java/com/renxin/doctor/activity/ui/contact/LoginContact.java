package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * mayakun 2017/11/15
 * LoginContact
 */
public interface LoginContact {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
        void sendMsgCode(String phone, int type);

        void login(String phone, String code, int type);
    }

}
