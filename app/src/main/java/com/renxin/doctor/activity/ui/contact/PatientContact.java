package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * PatientContact
 * Create at 2018/4/14 下午11:41 by mayakun
 */
public interface PatientContact {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {
        void getpatientlist();
    }
}
