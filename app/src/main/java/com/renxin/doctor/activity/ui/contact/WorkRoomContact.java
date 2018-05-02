package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BasePresenter;
import com.renxin.doctor.activity.ui.base.BaseView;

/**
 * WorkRoomContact  工作室
 * Create at 2018/4/16 上午10:03 by mayakun
 */

public interface WorkRoomContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getHomeData();

        void getOPenPaperBaseData();//开方基础数据
    }
}
