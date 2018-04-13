package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * Created by table on 2017/11/27.
 * description: 用户基本信息
 */

public interface BasicInfoContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void commitInfo(String userName,String certNo);

        void requestInfo();
    }
}
