package com.jht.doctor.ui.contact.contact_jht;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * AuthContact
 * Create at 2018/3/24 下午7:37 by mayakun 
 */
public interface AuthContact {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void getBanks();
    }

}
