package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * mayakun 2017/11/27
 * MessageContact 消息
 */
public interface MessageContact {

    interface View extends BaseView<Presenter>{

    }

    interface Presenter extends BasePresenter{
        void getMessageList(int pageNum);
        void deleteMessage(int id);
    }

}
