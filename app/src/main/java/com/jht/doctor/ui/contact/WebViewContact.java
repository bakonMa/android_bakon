package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * mayakun 2017/12/8
 *
 */
public interface WebViewContact {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void getCreditUrl(String orderNo);
    }

}
