package com.renxin.doctor.activity.injection.modules;

import android.content.Context;

import com.renxin.doctor.activity.injection.qualifiers.FragmentContext;
import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasicProvider;
import com.renxin.doctor.activity.ui.contact.HomeLoanContact;
import com.renxin.doctor.activity.ui.contact.LoginContact;
import com.renxin.doctor.activity.ui.contact.MyLoanContact;
import com.renxin.doctor.activity.ui.presenter.MyLoanPresenter;
import com.renxin.doctor.activity.ui.presenter.RepaymentPresenter;
import com.renxin.doctor.activity.ui.presenter.HomeLoanPresenter;
import com.renxin.doctor.activity.ui.presenter.LoginPresenter;
import com.renxin.doctor.activity.injection.scopes.PerFragment;
import com.renxin.doctor.activity.ui.contact.PersonalContact;
import com.renxin.doctor.activity.ui.contact.RepaymentContact;
import com.renxin.doctor.activity.ui.presenter.PersonalPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * @author: mayakun
 * @date: 2017/11/27
 * @project: customer-android-2th
 * @detail:
 */
@Module
public final class FragmentModule {

    private final BaseView mView;

    public FragmentModule(BaseView mView) {
        this.mView = mView;
    }

    @PerFragment
    @FragmentContext
    @Provides
    public Context provideFragmentContext() {
        if (mView instanceof BasicProvider) {
            return ((BasicProvider) mView).actContext();
        }
        //如果是Frament等，再自行实现BaseFrament并impl BasicProvider
        return null;
    }


    @PerFragment
    @Provides
    public LoginPresenter provideLoginPresenter() {
        return new LoginPresenter((LoginContact.View) mView);
    }

    @PerFragment
    @Provides
    public PersonalPresenter providePersonalPresenter() {
        return new PersonalPresenter((PersonalContact.View) mView);
    }


    //********************************************************



    @PerFragment
    @Provides
    public RepaymentPresenter provideRepaymentPresenter() {
        return new RepaymentPresenter((RepaymentContact.View) mView);
    }

    @PerFragment
    @Provides
    public HomeLoanPresenter provideHomeLoanPresenter() {
        return new HomeLoanPresenter((HomeLoanContact.View) mView);
    }


    @PerFragment
    @Provides
    public MyLoanPresenter provideMyLoanPresenter() {
        return new MyLoanPresenter((MyLoanContact.View) mView);
    }


}
