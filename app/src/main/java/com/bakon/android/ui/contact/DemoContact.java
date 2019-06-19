package com.bakon.android.ui.contact;

import com.bakon.android.ui.base.BaseView;
import com.bakon.android.ui.base.BasePresenter;

/**
 * Tang
 */
public interface DemoContact {

    interface View extends BaseView<Presenter> {

    }

    interface Presenter extends BasePresenter{
        void login();
    }

}
