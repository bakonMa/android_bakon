package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * Created by table on 2017/12/13.
 * description:
 */

public interface BankCardSettingContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter{
        void getBankList(String orderNo);
        void unbind(String bankCardNo,String orderNo);
    }
}
