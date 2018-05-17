package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasePresenter;

/**
 * Created by table on 2017/12/7.
 * description:
 */

public interface LoanDetailContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getLoanDetail(String orderNO);
    }
}
