package com.junhetang.doctor.injection.components;

import android.content.Context;

import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.injection.qualifiers.ActivityContext;
import com.junhetang.doctor.injection.scopes.PerActivity;
import com.junhetang.doctor.ui.activity.find.GuildNewsListActivity;
import com.junhetang.doctor.ui.activity.fragment.MainActivity;
import com.junhetang.doctor.ui.activity.home.AddDrugActivity;
import com.junhetang.doctor.ui.activity.home.ChooseCommActivity;
import com.junhetang.doctor.ui.activity.home.ChooseDocAdviceActivity;
import com.junhetang.doctor.ui.activity.home.CommUsePaperActivity;
import com.junhetang.doctor.ui.activity.home.HistoryPaperActivity;
import com.junhetang.doctor.ui.activity.home.JZRListActivity;
import com.junhetang.doctor.ui.activity.home.JobScheduleActivity;
import com.junhetang.doctor.ui.activity.home.OpenPaperCameraActivity;
import com.junhetang.doctor.ui.activity.home.OpenPaperOnlineActivity;
import com.junhetang.doctor.ui.activity.home.PersonCardActivity;
import com.junhetang.doctor.ui.activity.home.SearchSkillNameActivity;
import com.junhetang.doctor.ui.activity.home.SystemMsgListActivity;
import com.junhetang.doctor.ui.activity.login.LoginActivity;
import com.junhetang.doctor.ui.activity.login.RegisteActivity;
import com.junhetang.doctor.ui.activity.login.ResetPasswordActivity;
import com.junhetang.doctor.ui.activity.mine.AboutUsActivity;
import com.junhetang.doctor.ui.activity.mine.AuthStep1Activity;
import com.junhetang.doctor.ui.activity.mine.AuthStep2Activity;
import com.junhetang.doctor.ui.activity.mine.AuthStep4Activity;
import com.junhetang.doctor.ui.activity.mine.SetPriceActivity;
import com.junhetang.doctor.ui.activity.mine.SettingActivity;
import com.junhetang.doctor.ui.activity.mine.UserExplainActivity;
import com.junhetang.doctor.ui.activity.mine.UserNoticeActivity;
import com.junhetang.doctor.ui.activity.mine.wallet.AddBankCardActivity;
import com.junhetang.doctor.ui.activity.mine.wallet.DealDetailListActivity;
import com.junhetang.doctor.ui.activity.mine.wallet.DeleteBankCardActivity;
import com.junhetang.doctor.ui.activity.mine.wallet.MyBankCardActivity;
import com.junhetang.doctor.ui.activity.mine.wallet.WalletActivity;
import com.junhetang.doctor.ui.activity.mine.wallet.WithdrawActivity;
import com.junhetang.doctor.ui.activity.patient.AddPatientActivity;
import com.junhetang.doctor.ui.activity.patient.PatienHealthRecordActivity;
import com.junhetang.doctor.ui.activity.patient.PatientCenterActivity;
import com.junhetang.doctor.ui.activity.patient.PatientFamilyActivity;
import com.junhetang.doctor.ui.activity.patient.PatientListActivity;
import com.junhetang.doctor.ui.activity.patient.RemarkNameActivity;
import com.junhetang.doctor.ui.activity.patient.SearchPatientActivity;
import com.junhetang.doctor.ui.activity.welcome.TranslucentActivity;
import com.junhetang.doctor.ui.nimview.AddCommMessageActivity;
import com.junhetang.doctor.ui.nimview.CheckPaperH5Activity;
import com.junhetang.doctor.ui.nimview.CommMessageActivity;
import com.junhetang.doctor.ui.nimview.PaperH5Activity;

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
    @ActivityContext
    Context actContext();

    void inject(MainActivity mainActivity);

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

    void inject(PaperH5Activity paperH5Activity);

    void inject(HistoryPaperActivity historyPaperActivity);

    void inject(GuildNewsListActivity guildNewsListActivity);

    void inject(SystemMsgListActivity systemMsgListActivity);

    void inject(CheckPaperH5Activity checkPaperH5Activity);

    void inject(ChooseDocAdviceActivity chooseDocAdviceActivity);

    void inject(JZRListActivity jzrListActivity);

    void inject(SearchPatientActivity searchPatientActivity);

    void inject(ChooseCommActivity chooseCommActivity);

    void inject(AddPatientActivity addPatientActivity);

    void inject(JobScheduleActivity jobScheduleActivity);

    void inject(PatientCenterActivity patientCenterActivity);

    void inject(PersonCardActivity personCardActivity);

}
