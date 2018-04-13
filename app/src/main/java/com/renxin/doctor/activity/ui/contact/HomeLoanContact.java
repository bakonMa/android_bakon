package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

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
