package com.bakon.android.ui.contact;

import com.bakon.android.data.http.Params;
import com.bakon.android.ui.base.BaseView;
import com.bakon.android.ui.base.BasePresenter;

/**
 * AuthContact
 * Create at 2018/3/24 下午7:37 by mayakun
 */
public interface AuthContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getHospital(String prov, String city);

        void uploadImg(String path, String type);

        void getDpAndTitles();

        void userIdentify(Params params);//认证1

        void userIdentifyNext(String idCard, String path1, String path2, String path3);//认证2 证书

        void getUserIdentify();//获取认证信息

    }

}
