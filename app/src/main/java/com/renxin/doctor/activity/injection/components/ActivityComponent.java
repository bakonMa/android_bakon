package com.renxin.doctor.activity.injection.components;

import android.content.Context;

import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.injection.qualifiers.ActivityContext;
import com.renxin.doctor.activity.injection.scopes.PerActivity;
import com.renxin.doctor.activity.ui.activity.home.AddDrugActivity;
import com.renxin.doctor.activity.ui.activity.home.CommUsePaperActivity;
import com.renxin.doctor.activity.ui.activity.home.OpenPaperCameraActivity;
import com.renxin.doctor.activity.ui.activity.home.OpenPaperOnlineActivity;
import com.renxin.doctor.activity.ui.activity.home.SearchSkillNameActivity;
import com.renxin.doctor.activity.ui.activity.login.LoginActivity;
import com.renxin.doctor.activity.ui.activity.login.RegisteActivity;
import com.renxin.doctor.activity.ui.activity.login.ResetPasswordActivity;
import com.renxin.doctor.activity.ui.activity.mine.AboutUsActivity;
import com.renxin.doctor.activity.ui.activity.mine.AuthStep1Activity;
import com.renxin.doctor.activity.ui.activity.mine.AuthStep2Activity;
import com.renxin.doctor.activity.ui.activity.mine.AuthStep4Activity;
import com.renxin.doctor.activity.ui.activity.mine.SetPriceActivity;
import com.renxin.doctor.activity.ui.activity.mine.SettingActivity;
import com.renxin.doctor.activity.ui.activity.mine.UserExplainActivity;
import com.renxin.doctor.activity.ui.activity.mine.UserNoticeActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.AddBankCardActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.DealDetailListActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.DeleteBankCardActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.MyBankCardActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.WalletActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.WithdrawActivity;
import com.renxin.doctor.activity.ui.activity.patient.PatienHealthRecordActivity;
import com.renxin.doctor.activity.ui.activity.patient.PatientFamilyActivity;
import com.renxin.doctor.activity.ui.activity.patient.PatientListActivity;
import com.renxin.doctor.activity.ui.activity.patient.RemarkNameActivity;
import com.renxin.doctor.activity.ui.activity.welcome.TranslucentActivity;
import com.renxin.doctor.activity.ui.nimview.AddCommMessageActivity;
import com.renxin.doctor.activity.ui.nimview.CommMessageActivity;

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

    void inject(SettingActivity settingActivity);

    void inject(ResetPasswordActivity resetPasswordActivity);

    void inject(AboutUsActivity aboutUsActivity);

    void inject(TranslucentActivity translucentActivity);

    void inject(LoginActivity loginActivity);

    void inject(CommMessageActivity commMessageActivity);

    void inject(AddCommMessageActivity addCommMessageActivity);

    void inject(PatientFamilyActivity patientFamilyActivity);

    void inject(RemarkNameActivity remarkNameActivity);

    void inject(PatienHealthRecordActivity patienHealthRecordActivity);

    void inject(OpenPaperCameraActivity openPaperCameraActivity);

    void inject(OpenPaperOnlineActivity openPaperOnlineActivity);

    void inject(PatientListActivity patientListActivity);

    void inject(SearchSkillNameActivity searchSkillNameActivity);

    void inject(AddDrugActivity addDrugActivity);

    void inject(CommUsePaperActivity commUsePaperActivity);

}
