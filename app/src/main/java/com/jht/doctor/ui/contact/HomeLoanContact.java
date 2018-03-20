package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * Created by table on 2017/11/28.
 * description:
 */

public interface HomeLoanContact {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {
        void applyStatus();

        void getMaxAmt();
    }
}
