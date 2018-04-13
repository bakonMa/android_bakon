package com.renxin.doctor.activity.ui.contact;

import com.renxin.doctor.activity.ui.base.BasePresenter;
import com.renxin.doctor.activity.ui.base.BaseView;

/**
 * WalletContact 钱包
 * Create at 2018/4/10 上午11:57 by mayakun
 */
public interface WalletContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter {
        void getWallet();

        void userBankList();

        void getBankType();

        void addBankcard(String user_name, int bank_id, String bank_number, String openingbank);

        void deleteBankCard(int id);

        void witdraw(int bound_bank_id, String exmoney_submit);

        void getDealFlow(int pageNum);
    }

}
