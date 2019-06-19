package com.bakon.android.di.modules;

import android.content.Context;

import com.bakon.android.di.qualifiers.FragmentContext;
import com.bakon.android.ui.base.BaseView;
import com.bakon.android.ui.base.BasicProvider;
import com.bakon.android.ui.contact.OpenPaperContact;
import com.bakon.android.ui.contact.PatientContact;
import com.bakon.android.ui.contact.LoginContact;
import com.bakon.android.ui.contact.WorkRoomContact;
import com.bakon.android.ui.presenter.OpenPaperPresenter;
import com.bakon.android.ui.presenter.PatientPresenter;
import com.bakon.android.ui.presenter.LoginPresenter;
import com.bakon.android.di.scopes.PerFragment;
import com.bakon.android.ui.contact.PersonalContact;
import com.bakon.android.ui.presenter.PersonalPresenter;
import com.bakon.android.ui.presenter.WorkRoomPresenter;

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

    @PerFragment
    @Provides
    public WorkRoomPresenter provideWorkRoomPresenter() {
        return new WorkRoomPresenter((WorkRoomContact.View) mView);
    }
    
    @PerFragment
    @Provides
    public PatientPresenter providePatientPresenter() {
        return new PatientPresenter((PatientContact.View) mView);
    }

    @PerFragment
    @Provides
    public OpenPaperPresenter provideOpenPaperPresenter() {
        return new OpenPaperPresenter((OpenPaperContact.View) mView);
    }


}
