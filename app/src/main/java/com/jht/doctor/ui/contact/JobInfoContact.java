package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * Created by table on 2017/11/27.
 * description: 工作信息
 */

public interface JobInfoContact {
    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void commitJobInfo(String companyName,String companyType,String industry,String position,double monthlyIncome);

        void requestInfo();
    }
}
