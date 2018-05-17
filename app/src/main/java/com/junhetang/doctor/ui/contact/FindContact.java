package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BasePresenter;
import com.junhetang.doctor.ui.base.BaseView;

/**
 * FindContact
 * Create at 2018/5/10 上午10:44 by mayakun
 */
public interface FindContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        //行业追踪，健康教育
        void getNewsList(int type, int page);
    }

}
