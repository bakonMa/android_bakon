package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * Created by table on 2017/11/30.
 * description:
 */

public interface LoanMoneyContact {
    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void loanMoney(double loanAmt,String loanUse,String orderNo,int periodNumber,String repaymentType);
    }
}
