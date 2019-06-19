package com.bakon.android.di.modules;

import android.content.Context;

import com.bakon.android.di.qualifiers.ActivityContext;
import com.bakon.android.di.scopes.PerActivity;
import com.bakon.android.ui.base.BaseView;
import com.bakon.android.ui.base.BasicProvider;
import com.bakon.android.ui.contact.AuthContact;
import com.bakon.android.ui.contact.ChatMessageContact;
import com.bakon.android.ui.contact.DemoContact;
import com.bakon.android.ui.contact.FindContact;
import com.bakon.android.ui.contact.LoginContact;
import com.bakon.android.ui.contact.OpenPaperContact;
import com.bakon.android.ui.contact.PatientContact;
import com.bakon.android.ui.contact.PersonalContact;
import com.bakon.android.ui.contact.TranslucentContact;
import com.bakon.android.ui.contact.WalletContact;
import com.bakon.android.ui.contact.WorkRoomContact;
import com.bakon.android.ui.presenter.AuthPresenter;
import com.bakon.android.ui.presenter.ChatMessagePresenter;
import com.bakon.android.ui.presenter.DemoPresenter;
import com.bakon.android.ui.presenter.FindPresenter;
import com.bakon.android.ui.presenter.LoginPresenter;
import com.bakon.android.ui.presenter.OpenPaperPresenter;
import com.bakon.android.ui.presenter.PatientPresenter;
import com.bakon.android.ui.presenter.PersonalPresenter;
import com.bakon.android.ui.presenter.TranslucentPresenter;
import com.bakon.android.ui.presenter.WalletPresenter;
import com.bakon.android.ui.presenter.WorkRoomPresenter;

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
