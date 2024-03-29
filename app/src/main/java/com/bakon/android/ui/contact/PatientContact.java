package com.bakon.android.ui.contact;

import com.bakon.android.data.http.Params;
import com.bakon.android.ui.base.BasePresenter;
import com.bakon.android.ui.base.BaseView;

/**
 * PatientContact
 * Create at 2018/4/14 下午11:41 by mayakun
 */
public interface PatientContact {
    interface View extends BaseView {

    }

    interface Presenter extends BasePresenter {
        void getPatientlist();//患者列表

        void searchPatient(String search);//搜索患者

        void getpatientFamily(String memb_no);//患者信息和关系列表

        void setRemarkName(String accID, String memb_no, String remarkName);//设置备注

        void setPrice(String memb_no, String advisory_fee);//设置咨询价格

        void docToTalk(String accid);//医生主动发起聊天

        void addPatient(Params params);//添加患者

        void addPatientJZR(Params params);//添加患者（处方联系人）

        void getPatientPaper(int page, String patient_id, String memb_no);//患者处方列表

    }
}
