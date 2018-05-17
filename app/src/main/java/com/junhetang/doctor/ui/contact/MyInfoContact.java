package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasePresenter;

/**
 * mayakun 2017/11/27
 * MyInfoContact 用户信息
 */
public interface MyInfoContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getMyInfo();
    }

}
