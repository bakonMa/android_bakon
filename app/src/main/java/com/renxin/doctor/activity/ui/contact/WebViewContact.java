package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * mayakun 2017/12/8
 *
 */
public interface WebViewContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter{
        void getCreditUrl(String orderNo);
    }

}
