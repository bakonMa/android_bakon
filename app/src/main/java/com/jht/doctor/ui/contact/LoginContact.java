package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

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
