package com.jht.doctor.injection.components;

import android.content.Context;

import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.injection.qualifiers.ActivityContext;
import com.jht.doctor.injection.scopes.PerActivity;
import com.jht.doctor.ui.activity.loan.BasicInfoActivity;
import com.jht.doctor.ui.activity.loan.DemoActivity;
import com.jht.doctor.ui.activity.loan.HomeLoanActivity;
import com.jht.doctor.ui.activity.loan.HouseInfoActivity;
import com.jht.doctor.ui.activity.loan.JobInfoActivity;
import com.jht.doctor.ui.activity.loan.LoanApplyStateActivity;
import com.jht.doctor.ui.activity.loan.LoanMoneyActivity;
import com.jht.doctor.ui.activity.mine.FeedbackActivity;
import com.jht.doctor.ui.activity.mine.LoanDetailActivity;
import com.jht.doctor.ui.activity.mine.LoginActivity;
import com.jht.doctor.ui.activity.mine.MyInfoActivity;
import com.jht.doctor.ui.activity.mine.MyLoanListActivity;
import com.jht.doctor.ui.activity.mine.PersonalActivity;
import com.jht.doctor.ui.activity.mine.bankcard.AddBankCardVerifyActivity;
import com.jht.doctor.ui.activity.mine.bankcard.AddCoborrowerActivity;
import com.jht.doctor.ui.activity.mine.bankcard.AddMainCardActivity;
import com.jht.doctor.ui.activity.mine.bankcard.BankCardSettingActivity;
import com.jht.doctor.ui.activity.mine.bankcard.CoborrowerHistoryActivity;
import com.jht.doctor.ui.activity.mine.bankcard.MyBankCardActivity;
import com.jht.doctor.ui.activity.mine.bankcard.SupportBankActivity;
import com.jht.doctor.ui.activity.mine.setting.ResetPasswordActivity;
import com.jht.doctor.ui.activity.mine.setting.SettingActivity;
import com.jht.doctor.ui.activity.mine.webview.WebViewActivity;
import com.jht.doctor.ui.activity.repayment.HomeRepaymentActivity;
import com.jht.doctor.ui.activity.repayment.MessageActivity;
import com.jht.doctor.ui.activity.repayment.MyAccountActivity;
import com.jht.doctor.ui.activity.repayment.OfflineRepaymentActivity;
import com.jht.doctor.ui.activity.repayment.RechageActivity;
import com.jht.doctor.ui.activity.repayment.RepaymentVerifyCodeActivity;
import com.jht.doctor.ui.activity.repayment.TradeDetailActivity;
import com.jht.doctor.ui.activity.repayment.WithdrawCashActivity;
import com.jht.doctor.ui.activity.welcome.TranslucentActivity;
import com.jht.doctor.view.activity.AuthStep1Activity;
import com.jht.doctor.view.fragment.HomeFragment;

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
public interface ActivityComponent extends ApplicationComponent{
    /**********************JHT Start ***********************/
    @ActivityContext
    Context actContext();
    void inject(AuthStep1Activity authStep1Activity);

    /**********************JHT end ***********************/


    void inject(DemoActivity demoActivity);
    void inject(LoginActivity loginActivity);
    void inject(BasicInfoActivity basicInfoActivity);
    void inject(JobInfoActivity jobInfoActivity);
    void inject(MyInfoActivity myInfoActivity);
    void inject(MessageActivity messageActivity);
    void inject(HomeLoanActivity homeLoanActivity);
    void inject(PersonalActivity personalActivity);
    void inject(SettingActivity settingActivity);
    void inject(ResetPasswordActivity resetPasswordActivity);
    void inject(TranslucentActivity translucentActivity);
    void inject(HouseInfoActivity houseInfoActivity);
    void inject(LoanMoneyActivity loanMoneyActivity);
    void inject(MyLoanListActivity myLoanListActivity);
    void inject(HomeRepaymentActivity homeRepaymentActivity);
    void inject(MyAccountActivity myAccountActivity);
    void inject(OfflineRepaymentActivity offlineRepaymentActivity);
    void inject(LoanApplyStateActivity loanApplyStateActivity);
    void inject(WebViewActivity webViewActivity);
    void inject(LoanDetailActivity loanDetailActivity);
    void inject(MyBankCardActivity myBankCardActivity);
    void inject(RechageActivity rechageActivity);
    void inject(RepaymentVerifyCodeActivity repaymentVerifyCodeActivity);
    void inject(WithdrawCashActivity withdrawCashActivity);
    void inject(SupportBankActivity supportBankActivity);
    void inject(TradeDetailActivity tradeDetailActivity);
    void inject(BankCardSettingActivity bankCardSettingActivity);
    void inject(AddMainCardActivity addMainCardActivity);
    void inject(AddBankCardVerifyActivity addBankCardVerifyActivity);
    void inject(AddCoborrowerActivity addCoborrowerActivity);
    void inject(CoborrowerHistoryActivity coborrowerHistoryActivity);
    void inject(FeedbackActivity feedbackActivity);

    void inject(HomeFragment homeFragment);
}
