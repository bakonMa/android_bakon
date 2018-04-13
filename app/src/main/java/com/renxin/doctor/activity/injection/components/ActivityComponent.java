package com.renxin.doctor.activity.injection.components;

import android.content.Context;

import com.renxin.doctor.activity.injection.qualifiers.ActivityContext;
import com.renxin.doctor.activity.ui.activity.mine.UserExplainActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.AddBankCardActivity;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.injection.scopes.PerActivity;
import com.renxin.doctor.activity.ui.activity.WebViewActivity;
import com.renxin.doctor.activity.ui.activity.fragment.HomeFragment;
import com.renxin.doctor.activity.ui.activity.login.LoginActivity;
import com.renxin.doctor.activity.ui.activity.login.RegisteActivity;
import com.renxin.doctor.activity.ui.activity.mine.AuthStep1Activity;
import com.renxin.doctor.activity.ui.activity.mine.AuthStep2Activity;
import com.renxin.doctor.activity.ui.activity.mine.AuthStep4Activity;
import com.renxin.doctor.activity.ui.activity.mine.ResetPasswordActivity;
import com.renxin.doctor.activity.ui.activity.mine.SetPriceActivity;
import com.renxin.doctor.activity.ui.activity.mine.SettingActivity;
import com.renxin.doctor.activity.ui.activity.mine.UserNoticeActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.DealDetailListActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.DeleteBankCardActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.MyBankCardActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.WalletActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.WithdrawActivity;
import com.renxin.doctor.activity.ui.activity.welcome.TranslucentActivity;

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

    void inject(AuthStep2Activity authStep2Activity);

    void inject(AuthStep4Activity authStep4Activity);

    void inject(RegisteActivity registeActivity);

    void inject(UserExplainActivity userExplainActivity);

    void inject(UserNoticeActivity userNoticeActivity);

    void inject(WalletActivity walletActivity);

    void inject(WithdrawActivity withdrawActivity);

    void inject(AddBankCardActivity addBankCardActivity);

    void inject(MyBankCardActivity myBankCardActivity);

    void inject(DeleteBankCardActivity deleteBankCardActivity);

    void inject(DealDetailListActivity dealDetailListActivity);

    void inject(SetPriceActivity setPriceActivity);

    /**********************JHT end ***********************/


    void inject(LoginActivity loginActivity);

    void inject(SettingActivity settingActivity);

    void inject(ResetPasswordActivity resetPasswordActivity);

    void inject(TranslucentActivity translucentActivity);

    void inject(WebViewActivity webViewActivity);

    void inject(HomeFragment homeFragment);
}
