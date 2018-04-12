package com.jht.doctor.injection.components;

import android.content.Context;

import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.injection.qualifiers.ActivityContext;
import com.jht.doctor.injection.scopes.PerActivity;
import com.jht.doctor.ui.activity.WebViewActivity;
import com.jht.doctor.ui.activity.fragment.HomeFragment;
import com.jht.doctor.ui.activity.login.LoginActivity;
import com.jht.doctor.ui.activity.login.RegisteActivity;
import com.jht.doctor.ui.activity.mine.AuthStep1Activity;
import com.jht.doctor.ui.activity.mine.AuthStep2Activity;
import com.jht.doctor.ui.activity.mine.AuthStep4Activity;
import com.jht.doctor.ui.activity.mine.ResetPasswordActivity;
import com.jht.doctor.ui.activity.mine.SetPriceActivity;
import com.jht.doctor.ui.activity.mine.SettingActivity;
import com.jht.doctor.ui.activity.mine.UserExplainActivity;
import com.jht.doctor.ui.activity.mine.UserNoticeActivity;
import com.jht.doctor.ui.activity.mine.wallet.AddBankCardActivity;
import com.jht.doctor.ui.activity.mine.wallet.DealDetailListActivity;
import com.jht.doctor.ui.activity.mine.wallet.DeleteBankCardActivity;
import com.jht.doctor.ui.activity.mine.wallet.MyBankCardActivity;
import com.jht.doctor.ui.activity.mine.wallet.WalletActivity;
import com.jht.doctor.ui.activity.mine.wallet.WithdrawActivity;
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
