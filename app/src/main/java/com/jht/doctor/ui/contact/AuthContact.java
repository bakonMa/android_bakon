package com.jht.doctor.ui.contact;

import com.jht.doctor.data.http.Params;
import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

/**
 * AuthContact
 * Create at 2018/3/24 下午7:37 by mayakun
 */
public interface AuthContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getHospital(String prov, String city);

        void uploadImg(String path);

        void getDpAndTitles();

        void userIdentify(Params params);//认证1


    }

}
