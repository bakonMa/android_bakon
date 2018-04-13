package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

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
