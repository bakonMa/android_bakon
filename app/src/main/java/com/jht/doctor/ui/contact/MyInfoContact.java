package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * mayakun 2017/11/27
 * MyInfoContact 用户信息
 */
public interface MyInfoContact {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void getMyInfo();
    }

}
