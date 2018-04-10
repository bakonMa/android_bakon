package com.jht.doctor.ui.contact;

import com.jht.doctor.ui.base.BasePresenter;
import com.jht.doctor.ui.base.BaseView;

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
    }

}
