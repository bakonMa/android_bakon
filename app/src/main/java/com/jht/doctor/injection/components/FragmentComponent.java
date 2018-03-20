package com.jht.doctor.injection.components;

import android.content.Context;

import com.jht.doctor.injection.modules.FragmentModule;
import com.jht.doctor.injection.qualifiers.FragmentContext;
import com.jht.doctor.injection.scopes.PerFragment;
import com.jht.doctor.ui.activity.mine.home.OrderFragment;
import com.jht.doctor.ui.activity.mine.home.TestFragment;
import com.jht.doctor.ui.fragment.HomeFragment;
import com.jht.doctor.ui.fragment.MineFragment;
import com.jht.doctor.ui.fragment.WorkRoomFragment;

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
    void inject(OrderFragment orderFragment);
    void inject(TestFragment testFragment );

    void inject(HomeFragment homeFragment);

    void inject(MineFragment mineFragment);

    void inject(com.jht.doctor.ui.fragment.OrderFragment orderFragment);
}
