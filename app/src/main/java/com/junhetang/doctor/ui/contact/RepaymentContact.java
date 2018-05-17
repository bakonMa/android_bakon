package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasePresenter;

/**
 * Created by mayakun on 2017/12/7
 * 还款（线下，线上）
 */

public interface RepaymentContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getOffLineRepayment(String orderId, int pageNum);
        void getMyAccountRepayment(String orderId);
        void transactionDetails(String platformUserNo, int type, int pagNum);

        void getPayWay(String orderNo);
        void getSmsVerifyCode(int form, int type, String platformUserNo, String orderNo, String amount);
        void smsWithDraw(Params params);
        void smsRecharge(Params params);
        void pwdWithDraw(Params params);
        void pwdRecharge(Params params);
    }
}