package com.junhetang.doctor.injection.modules;

import android.content.Context;

import com.junhetang.doctor.injection.qualifiers.FragmentContext;
import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasicProvider;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.contact.PatientContact;
import com.junhetang.doctor.ui.contact.LoginContact;
import com.junhetang.doctor.ui.contact.WorkRoomContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.ui.presenter.PatientPresenter;
import com.junhetang.doctor.ui.presenter.LoginPresenter;
import com.junhetang.doctor.injection.scopes.PerFragment;
import com.junhetang.doctor.ui.contact.PersonalContact;
import com.junhetang.doctor.ui.presenter.PersonalPresenter;
import com.junhetang.doctor.ui.presenter.WorkRoomPresenter;

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
