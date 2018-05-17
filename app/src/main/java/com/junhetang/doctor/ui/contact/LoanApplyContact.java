package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasePresenter;

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
