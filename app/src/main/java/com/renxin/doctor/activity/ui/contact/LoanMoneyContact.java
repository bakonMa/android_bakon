package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * Created by table on 2017/11/30.
 * description:
 */

public interface LoanMoneyContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter{
        void loanMoney(double loanAmt,String loanUse,String orderNo,int periodNumber,String repaymentType);
    }
}
