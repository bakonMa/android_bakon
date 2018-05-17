package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasePresenter;

/**
 * Created by table on 2017/12/13.
 * description:
 */

public interface BankCardSettingContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getBankList(String orderNo);
        void unbind(String bankCardNo,String orderNo);
    }
}
