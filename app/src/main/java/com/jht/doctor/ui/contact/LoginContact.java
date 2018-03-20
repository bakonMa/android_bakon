package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * mayakun 2017/11/15
 * LoginContact
 */
public interface LoginContact {

    interface View extends BaseView<Presenter>{
        void bindError(String errorMsg);
    }

    interface Presenter extends BasePresenter{
        void sendVerifyCode(String phone);

        void login(String phone,String code);

        void bind(String phone);
    }

}
