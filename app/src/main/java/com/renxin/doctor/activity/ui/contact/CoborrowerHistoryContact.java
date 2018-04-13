package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * Created by table on 2017/12/15.
 * description:
 */

public interface CoborrowerHistoryContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void reusingBank(int debtorId, String idCareNo, String oldOrderNo,
                         String orderNo, String userName);

        void unbind(String bankCardNo,String orderNo);

        void ensureCard(String bankCardNo,String orderNo);


        void contributiveTypeJudge(String idCardNo,String mobile,String orderNo,String realName);

        void ensureBankCardOfCunGuan(String bankCard,String bankMobile,String bankName,String idCard,
                                     String orderNo,String otherPlatformId,String userName,String userType);

        void ifBankJoint(String idCareNo, String orderNo, String userName);

        void addMainCard(String orderNo, String bankCardNo, String userName,
                    String bankPhone, String userType, String bankCardName,
                    String idCard, String password);
    }
}
