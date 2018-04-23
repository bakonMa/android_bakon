package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BasePresenter;
import com.renxin.doctor.activity.ui.base.BaseView;

/**
 * OpenPaperContact  开方相关
 * Create at 2018/4/23 下午7:21 by mayakun
 */
public interface OpenPaperContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getOPenPaperBaseData();//开方基础数据

    }

}
