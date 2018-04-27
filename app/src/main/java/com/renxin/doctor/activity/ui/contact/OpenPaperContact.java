package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.data.http.Params;
import com.renxin.doctor.activity.ui.base.BasePresenter;
import com.renxin.doctor.activity.ui.base.BaseView;

/**
 * OpenPaperContact  开方相关
 * Create at 2018/4/23 下午7:21 by mayakun
 */
public interface OpenPaperContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getOPenPaperBaseData();//开方基础数据
        void uploadImg(String path, String type);
        void openPaperCamera(Params params);//拍照开方
        void searchSkillName(String name);//搜索疾病名称
        void searchDrugName(String name);//搜索药材名称

    }

}
