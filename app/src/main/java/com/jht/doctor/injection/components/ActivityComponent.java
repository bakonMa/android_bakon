package com.jht.doctor.injection.components;

import android.content.Context;

import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.injection.qualifiers.ActivityContext;
import com.jht.doctor.injection.scopes.PerActivity;
import com.jht.doctor.ui.activity.login.LoginActivity;
import com.jht.doctor.ui.activity.WebViewActivity;
import com.jht.doctor.ui.activity.fragment.HomeFragment;
import com.jht.doctor.ui.activity.mine.AuthStep1Activity;
import com.jht.doctor.ui.activity.login.RegisteActivity;
import com.jht.doctor.ui.activity.mine.ResetPasswordActivity;
import com.jht.doctor.ui.activity.mine.SettingActivity;
import com.jht.doctor.ui.activity.welcome.TranslucentActivity;

import dagger.Component;

/**
 * @author: ZhaoYun
 * @date: 2017/10/31
 * @project: customer-android-2th
 * @detail:
 */
@PerActivity
@Component(
        dependencies = ApplicationComponent.class,
        modules = {ActivityModule.class}
)
public interface ActivityComponent extends ApplicationComponent {
    /**********************JHT Start ***********************/
    @ActivityContext
    Context actContext();

    void inject(AuthStep1Activity authStep1Activity);
    void inject(RegisteActivity registeActivity);

    /**********************JHT end ***********************/


    void inject(LoginActivity loginActivity);

    void inject(SettingActivity settingActivity);

    void inject(ResetPasswordActivity resetPasswordActivity);

    void inject(TranslucentActivity translucentActivity);

    void inject(WebViewActivity webViewActivity);

    void inject(HomeFragment homeFragment);
}
