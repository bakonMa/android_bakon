package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * Created by table on 2017/12/13.
 * description:
 */

public interface AddMainCardContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getBankName(String cardNo);

        void tradePwdStatus();

        void bindCardTradePwdStatus(String orderNo,String otherPlatformId);

        void bindCardSetTradePwd(String orderNo,String otherPlatformId,String pwd);

        void addMainCard(String orderNo, String bankCardNo, String userName,
                         String bankPhone, String userType, String bankCardName,
                         String idCard, String password);
    }
}

