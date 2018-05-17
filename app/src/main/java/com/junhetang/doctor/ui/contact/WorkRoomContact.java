package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BasePresenter;
import com.junhetang.doctor.ui.base.BaseView;

/**
 * WorkRoomContact  工作室
 * Create at 2018/4/16 上午10:03 by mayakun
 */

public interface WorkRoomContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getUserIdentifyStatus();

        void getRedPointStatus();

        void getHomeBanner();

        void getOPenPaperBaseData();//开方基础数据

        void updataToken();//为了保持登录态，和安全性，更新token

        void bindXGToken(String xgToken);//把注册的信鸽token给后台

        void getSystemMsgList(int page);//系统消息列表
    }
}
