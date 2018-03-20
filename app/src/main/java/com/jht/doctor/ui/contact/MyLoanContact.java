package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * Created by table on 2017/12/4.
 * description:
 */

public interface MyLoanContact {
    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void getLoanList();
    }
}
