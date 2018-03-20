package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

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
