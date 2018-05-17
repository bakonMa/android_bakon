package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasePresenter;

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
