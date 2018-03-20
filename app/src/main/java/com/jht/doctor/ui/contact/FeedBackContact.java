package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * Created by mayakun on 2017/12/15
 * description: 反馈
 */

public interface FeedBackContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void confirmMessage(String content);
    }
}
