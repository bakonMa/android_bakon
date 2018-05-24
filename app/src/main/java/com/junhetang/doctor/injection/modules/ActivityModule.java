package com.junhetang.doctor.injection.modules;

import android.content.Context;

import com.junhetang.doctor.injection.qualifiers.ActivityContext;
import com.junhetang.doctor.injection.scopes.PerActivity;
import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasicProvider;
import com.junhetang.doctor.ui.contact.AuthContact;
import com.junhetang.doctor.ui.contact.ChatMessageContact;
import com.junhetang.doctor.ui.contact.DemoContact;
import com.junhetang.doctor.ui.contact.FindContact;
import com.junhetang.doctor.ui.contact.LoginContact;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.contact.PatientContact;
import com.junhetang.doctor.ui.contact.PersonalContact;
import com.junhetang.doctor.ui.contact.TranslucentContact;
import com.junhetang.doctor.ui.contact.WalletContact;
import com.junhetang.doctor.ui.contact.WorkRoomContact;
import com.junhetang.doctor.ui.presenter.AuthPresenter;
import com.junhetang.doctor.ui.presenter.ChatMessagePresenter;
import com.junhetang.doctor.ui.presenter.DemoPresenter;
import com.junhetang.doctor.ui.presenter.FindPresenter;
import com.junhetang.doctor.ui.presenter.LoginPresenter;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.ui.presenter.PatientPresenter;
import com.junhetang.doctor.ui.presenter.PersonalPresenter;
import com.junhetang.doctor.ui.presenter.TranslucentPresenter;
import com.junhetang.doctor.ui.presenter.WalletPresenter;
import com.junhetang.doctor.ui.presenter.WorkRoomPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * ActivityModule
 * Create at 2018/3/24 下午7:37 by mayakun
 */
@Module
public final class ActivityModule {

    private final BaseView mView;

    public ActivityModule(BaseView mView) {
        this.mView = mView;
    }

    @PerActivity
    @Provides
    public DemoPresenter provideDemoActivityPresenter() {
        return new DemoPresenter((DemoContact.View) mView);
    }

    @PerActivity
    @Provides
    public AuthPresenter provideAuthPresenter() {
        return new AuthPresenter((AuthContact.View) mView);
    }

    @PerActivity
    @Provides
    public PersonalPresenter providePersonalPresenter() {
        return new PersonalPresenter((PersonalContact.View) mView);
    }

    @PerActivity
    @Provides
    public WalletPresenter provideWalletPresenter() {
        return new WalletPresenter((WalletContact.View) mView);
    }

    @PerActivity
    @Provides
    public ChatMessagePresenter provideChatMessagePresenter() {
        return new ChatMessagePresenter((ChatMessageContact.View) mView);
    }

    @PerActivity
    @Provides
    public OpenPaperPresenter provideOpenPaperPresenter() {
        return new OpenPaperPresenter((OpenPaperContact.View) mView);
    }

    @PerActivity
    @Provides
    public FindPresenter provideFindPresenter() {
        return new FindPresenter((FindContact.View) mView);
    }

    @PerActivity
    @Provides
    public WorkRoomPresenter provideWorkRoomPresenter() {
        return new WorkRoomPresenter((WorkRoomContact.View) mView);
    }

    @PerActivity
    @Provides
    public LoginPresenter provideLoginActivityPresenter() {
        return new LoginPresenter((LoginContact.View) mView);
    }

    @PerActivity
    @Provides
    public PatientPresenter provideHomeLoanPresenter() {
        return new PatientPresenter((PatientContact.View) mView);
    }


    @PerActivity
    @Provides
    public TranslucentPresenter provideTranslucentPresenter() {
        return new TranslucentPresenter((TranslucentContact.View) mView);
    }


    @PerActivity
    @ActivityContext
    @Provides
    public Context provideActivityContext() {
        if (mView instanceof BasicProvider) {
            return ((BasicProvider) mView).actContext();
        }
        //如果是Frament等，再自行实现BaseFrament并impl BasicProvider
        return null;
    }


}
