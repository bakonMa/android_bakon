package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.bean.ApplyAuthorizationBean;
import com.junhetang.doctor.ui.base.BasePresenter;
import com.junhetang.doctor.ui.bean.UserAuthorizationBO;

/**
 * Created by table on 2017/12/13.
 * description:
 */

public interface AddBankCardVerifyContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void addMainCard(String orderNo, String bankCardNo, String userName,
                         String bankPhone, String userType, String bankCardName,
                         String idCard, String password);

        void userAuthorization(ApplyAuthorizationBean.ApplyAuthorizationDTOBean applyAuthorizationDTOBean,
                               UserAuthorizationBO userAuthorizationBO);
    }
}
