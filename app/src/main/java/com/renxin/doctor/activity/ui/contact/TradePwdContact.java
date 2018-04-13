package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BasePresenter;
import com.renxin.doctor.activity.ui.base.BaseView;

/**
 * mayakun 2017/11/29
 * TradePwdContact 设置
 */
public interface TradePwdContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        //判断是否有交易密码
        void tradePwdStatus();
        //发送验证码
        void sendVerifyCode(String phone);
        //初次设置交易密码
        void setTradePwd(String mobilePhone, String code, String pwd);
        //修改交易密码
        void resetTradePwd(String mobilePhone, String code, String pwd);
    }

}
