package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * mayakun 2017/11/27
 *
 */
public interface LoanApplyContact {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void getOrderCreditStatus(String orderNo);
    }

}
