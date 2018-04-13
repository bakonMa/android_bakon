package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasePresenter;

/**
 * Created by table on 2017/11/27.
 * description:房产信息
 */

public interface HouseInfoContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void commitHouseInfo(String houseType, double houseArea, String communityName, String provinceCode,
                             String cityCode, String areaCode, String detailAddress, String hasLoan,
                             String loanOrg, double loanAmt,double tradingAmt,double monthRentalAmount);

        void requestInfo();
    }
}
