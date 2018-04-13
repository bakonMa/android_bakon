package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.bean.ApplyAuthorizationBean;
import com.renxin.doctor.activity.ui.base.BasePresenter;
import com.renxin.doctor.activity.ui.bean.UserAuthorizationBO;

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
