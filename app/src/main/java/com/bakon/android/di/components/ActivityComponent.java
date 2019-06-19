package com.bakon.android.di.components;

import android.content.Context;

import com.bakon.android.di.modules.ActivityModule;
import com.bakon.android.di.qualifiers.ActivityContext;
import com.bakon.android.di.scopes.PerActivity;
import com.bakon.android.ui.activity.find.GuildNewsListActivity;
import com.bakon.android.ui.activity.fragment.MainActivity;
import com.bakon.android.ui.activity.login.LoginActivity;
import com.bakon.android.ui.activity.login.RegisteActivity;
import com.bakon.android.ui.activity.login.ResetPasswordActivity;
import com.bakon.android.ui.activity.welcome.TranslucentActivity;

import dagger.Component;

/**
 * ActivityComponent
 * Create at 2018/4/13 下午3:44 by mayakun
 */
@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class}
)
public interface ActivityComponent extends ApplicationComponent{
    @ActivityContext
    Context actContext();

    void inject(MainActivity mainActivity);


    void inject(RegisteActivity registeActivity);


    void inject(ResetPasswordActivity resetPasswordActivity);


    void inject(TranslucentActivity translucentActivity);

    void inject(LoginActivity loginActivity);



    void inject(GuildNewsListActivity guildNewsListActivity);


}
