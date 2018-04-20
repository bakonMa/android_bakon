package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BasePresenter;
import com.renxin.doctor.activity.ui.base.BaseView;

/**
 * ChatMessageContact 聊天相关画面
 * Create at 2018/4/19 上午11:44 by mayakun
 */
public interface ChatMessageContact {
    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getuseful(int type);//用户获取常用语

        void adduseful(int type, String message);//添加常用语

        void deluseful(String ids);//删除常用语
    }
}
