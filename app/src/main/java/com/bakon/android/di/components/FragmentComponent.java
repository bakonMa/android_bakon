package com.bakon.android.di.components;

import android.content.Context;

import com.bakon.android.di.modules.FragmentModule;
import com.bakon.android.di.qualifiers.FragmentContext;
import com.bakon.android.di.scopes.PerFragment;
import com.bakon.android.ui.activity.fragment.FindFragment;
import com.bakon.android.ui.activity.fragment.MineFragment;
import com.bakon.android.ui.activity.fragment.PatientFragment;
import com.bakon.android.ui.activity.fragment.WorkRoomFragment;
import com.bakon.android.ui.activity.login.LoginFragment;

import dagger.Component;

/**
 * @author: ZhaoYun
 * @date: 2017/10/31
 * @project: customer-android-2th
 * @detail:
 */
@PerFragment
@Component(
        dependencies = ApplicationComponent.class,
        modules = {FragmentModule.class}
)
public interface FragmentComponent extends ApplicationComponent{

    @FragmentContext
    Context actContext();

    void inject(WorkRoomFragment workRoomFragment);

    void inject(LoginFragment loginFragment);

    void inject(PatientFragment patientFragment);

    void inject(MineFragment mineFragment);

    void inject(FindFragment findFragment);


}
