package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * Tang
 */
public interface DemoContact {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void login();
    }

}
