package com.jht.doctor.injection.modules;

import android.content.Context;

import com.jht.doctor.ui.contact.LoginContact;
import com.jht.doctor.ui.presenter.HomeLoanPresenter;
import com.jht.doctor.ui.presenter.LoginPresenter;
import com.jht.doctor.ui.presenter.RepaymentPresenter;
import com.jht.doctor.injection.qualifiers.FragmentContext;
import com.jht.doctor.injection.scopes.PerFragment;
import com.jht.doctor.ui.base.BaseView;
import com.jht.doctor.ui.base.BasicProvider;
import com.jht.doctor.ui.contact.HomeLoanContact;
import com.jht.doctor.ui.contact.MyLoanContact;
import com.jht.doctor.ui.contact.PersonalContact;
import com.jht.doctor.ui.contact.RepaymentContact;
import com.jht.doctor.ui.presenter.MyLoanPresenter;
import com.jht.doctor.ui.presenter.PersonalPresenter;

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
