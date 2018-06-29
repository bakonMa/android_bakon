package com.junhetang.doctor.injection.components;

import android.content.Context;

import com.junhetang.doctor.injection.modules.FragmentModule;
import com.junhetang.doctor.injection.qualifiers.FragmentContext;
import com.junhetang.doctor.injection.scopes.PerFragment;
import com.junhetang.doctor.ui.activity.fragment.FindFragment;
import com.junhetang.doctor.ui.activity.fragment.MineFragment;
import com.junhetang.doctor.ui.activity.fragment.PatientFragment;
import com.junhetang.doctor.ui.activity.fragment.WorkRoomFragment;
import com.junhetang.doctor.ui.activity.home.CheckPaperFragment;
import com.junhetang.doctor.ui.activity.home.ChooseCommFragment;
import com.junhetang.doctor.ui.activity.home.CommUsePaperFragment;
import com.junhetang.doctor.ui.activity.home.HistoryPaperFragment;
import com.junhetang.doctor.ui.activity.login.LoginFragment;

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

    void inject(CheckPaperFragment checkPaperFragment);

    void inject(ChooseCommFragment chooseCommFragment);

    void inject(CommUsePaperFragment commUsePaperFragment);

    void inject(HistoryPaperFragment historyPaperFragment);

}
