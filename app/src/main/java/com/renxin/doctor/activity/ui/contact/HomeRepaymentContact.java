package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BasePresenter;
import com.renxin.doctor.activity.ui.base.BaseView;

/**
 * Created by table on 2017/12/4.
 * description:
 */

public interface HomeRepaymentContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getHomeRepayment();

//        void getMessageCount();
    }
}
