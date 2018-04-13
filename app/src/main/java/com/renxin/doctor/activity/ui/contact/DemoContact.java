package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * Tang
 */
public interface DemoContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter{
        void login();
    }

}
