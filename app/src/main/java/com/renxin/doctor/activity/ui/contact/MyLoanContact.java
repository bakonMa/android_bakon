package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * Created by table on 2017/12/4.
 * description:
 */

public interface MyLoanContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter{
        void getLoanList();
    }
}
