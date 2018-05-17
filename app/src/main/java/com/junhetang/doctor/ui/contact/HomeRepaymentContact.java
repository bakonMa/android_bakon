package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BasePresenter;
import com.junhetang.doctor.ui.base.BaseView;

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
