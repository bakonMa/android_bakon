package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * mayakun 2017/11/27
 *
 */
public interface LoanApplyContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter{
        void getOrderCreditStatus(String orderNo);
    }

}
