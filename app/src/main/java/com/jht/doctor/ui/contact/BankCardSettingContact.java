package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * Created by table on 2017/12/13.
 * description:
 */

public interface BankCardSettingContact {
    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void getBankList(String orderNo);
        void unbind(String bankCardNo,String orderNo);
    }
}
