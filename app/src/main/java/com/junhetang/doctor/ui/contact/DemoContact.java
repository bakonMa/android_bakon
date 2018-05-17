package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasePresenter;

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
