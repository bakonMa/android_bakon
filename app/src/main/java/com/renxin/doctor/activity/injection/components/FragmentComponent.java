package com.renxin.doctor.activity.injection.components;

import android.content.Context;

import com.renxin.doctor.activity.injection.qualifiers.FragmentContext;
import com.renxin.doctor.activity.ui.activity.fragment.OrderFragment;
import com.renxin.doctor.activity.ui.activity.fragment.WorkRoomFragment;
import com.renxin.doctor.activity.ui.activity.login.LoginFragment;
import com.renxin.doctor.activity.injection.modules.FragmentModule;
import com.renxin.doctor.activity.injection.scopes.PerFragment;
import com.renxin.doctor.activity.ui.activity.fragment.HomeFragment;
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

    void inject(HomeFragment homeFragment);

    void inject(MineFragment mineFragment);

    void inject(OrderFragment orderFragment);
}
