package com.renxin.doctor.activity.injection.components;

import android.content.Context;

import com.renxin.doctor.activity.injection.qualifiers.FragmentContext;
import com.renxin.doctor.activity.ui.activity.fragment.FindFragment;
import com.renxin.doctor.activity.ui.activity.fragment.PatientFragment;
import com.renxin.doctor.activity.ui.activity.fragment.WorkRoomFragment;
import com.renxin.doctor.activity.ui.activity.login.LoginFragment;
import com.renxin.doctor.activity.injection.modules.FragmentModule;
import com.renxin.doctor.activity.injection.scopes.PerFragment;
import com.renxin.doctor.activity.ui.activity.fragment.MineFragment;

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
public interface FragmentComponent extends ApplicationComponent {

    @FragmentContext
    Context actContext();

    void inject(WorkRoomFragment workRoomFragment);

    void inject(LoginFragment loginFragment);

    void inject(PatientFragment patientFragment);

    void inject(MineFragment mineFragment);

    void inject(FindFragment findFragment);
}
