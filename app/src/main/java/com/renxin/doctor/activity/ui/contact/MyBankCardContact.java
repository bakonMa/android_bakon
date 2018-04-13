package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BasePresenter;
import com.renxin.doctor.activity.ui.base.BaseView;

/**
 * Created by table on 2017/12/11.
 * description:
 */

public interface MyBankCardContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getBankList(String orderNo);

        void contributiveTypeJudge(String idCardNo,String mobile,String orderNo,String realName,String type);

        void ensureBankCardOfCunGuan(String bankCard,String bankMobile,String bankName,String idCard,
                                     String orderNo,String otherPlatformId,String userName,String userType);

        void judgeIsTiedCard(String orderNo, String userType);

        void unbind(String bankCardNo,String orderNo);

        void ensureCard(String bankCardNo,String orderNo);

        void ifBankJoint(String idCareNo, String orderNo, String userName);

        void bindCardSetTradePwd(String orderNo,String otherPlatformId,String pwd);

        void addMainCard(String orderNo, String bankCardNo, String userName,
                         String bankPhone, String userType, String bankCardName,
                         String idCard, String password);
    }
}
