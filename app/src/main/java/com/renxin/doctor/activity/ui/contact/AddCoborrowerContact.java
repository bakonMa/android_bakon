package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * Created by table on 2017/12/14.
 * description:
 */

public interface AddCoborrowerContact {
    interface View extends BaseView<Presenter> {
        void ensureFailure(String errorMsg);
    }

    interface Presenter extends BasePresenter {
        void addCoborrower(String certAddressArea, String certAddressCity, String certAddressProvince,
                           String certDetailAddress, String certNo, String mobilePhone, String name,
                           String orderNo, String relation);

        void contributiveTypeJudge(String idCardNo,String mobile,String orderNo,String realName);

        void ensureBankCardOfCunGuan(String bankCard,String bankMobile,String bankName,String idCard,
                                     String orderNo,String otherPlatformId,String userName,String userType);
    }

}

