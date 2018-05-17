package com.junhetang.doctor.ui.contact;

import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasePresenter;

/**
 * PatientContact
 * Create at 2018/4/14 下午11:41 by mayakun
 */
public interface PatientContact {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {
        void getPatientlist();//患者列表
        void getpatientFamily(String memb_no);//患者信息和关系列表
        void setRemarkName(String accID, String memb_no, String remarkName);//设置备注
        void setPrice(String memb_no, String advisory_fee);//设置咨询价格
    }
}