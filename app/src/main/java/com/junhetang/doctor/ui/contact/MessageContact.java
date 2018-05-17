package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasePresenter;

/**
 * mayakun 2017/11/27
 * MessageContact 消息
 */
public interface MessageContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getMessageList(int pageNum);
        void deleteMessage(int id);
    }

}
