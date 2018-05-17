package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasePresenter;

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
