package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

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

