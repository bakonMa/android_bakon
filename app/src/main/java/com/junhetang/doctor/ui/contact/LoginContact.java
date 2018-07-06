package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasePresenter;

/**
 * mayakun 2017/11/15
 * LoginContact
 */
public interface LoginContact {

    interface View extends BaseView<Presenter> {
    }

    interface Presenter extends BasePresenter {
        void sendMsgCode(String phone, int type);

        void login(String phone, String code, int type);

        void regist(String phone, String pwd, String code);

        void restPwd(String phone, String code, String pwd);

        void setPushStatus(int status);

        void setChatFlag(int flag);//是否开通在线咨询

    }

}
