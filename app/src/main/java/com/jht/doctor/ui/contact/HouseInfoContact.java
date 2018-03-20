package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

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
