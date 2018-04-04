package com.jht.doctor.injection.modules;

import android.content.Context;

import com.jht.doctor.injection.qualifiers.ActivityContext;
import com.jht.doctor.injection.scopes.PerActivity;
import com.jht.doctor.ui.base.BaseView;
import com.jht.doctor.ui.base.BasicProvider;
import com.jht.doctor.ui.contact.AddBankCardVerifyContact;
import com.jht.doctor.ui.contact.AddCoborrowerContact;
import com.jht.doctor.ui.contact.AddMainCardContact;
import com.jht.doctor.ui.contact.BankCardSettingContact;
import com.jht.doctor.ui.contact.BasicInfoContact;
import com.jht.doctor.ui.contact.CoborrowerHistoryContact;
import com.jht.doctor.ui.contact.DemoContact;
import com.jht.doctor.ui.contact.FeedBackContact;
import com.jht.doctor.ui.contact.HomeLoanContact;
import com.jht.doctor.ui.contact.HomeRepaymentContact;
import com.jht.doctor.ui.contact.HouseInfoContact;
import com.jht.doctor.ui.contact.JobInfoContact;
import com.jht.doctor.ui.contact.LoanApplyContact;
import com.jht.doctor.ui.contact.LoanDetailContact;
import com.jht.doctor.ui.contact.LoanMoneyContact;
import com.jht.doctor.ui.contact.LoginContact;
import com.jht.doctor.ui.contact.MessageContact;
import com.jht.doctor.ui.contact.MyBankCardContact;
import com.jht.doctor.ui.contact.MyInfoContact;
import com.jht.doctor.ui.contact.MyLoanContact;
import com.jht.doctor.ui.contact.RegisteContact;
import com.jht.doctor.ui.contact.RepaymentContact;
import com.jht.doctor.ui.contact.SettingContract;
import com.jht.doctor.ui.contact.SupportBankContact;
import com.jht.doctor.ui.contact.TradePwdContact;
import com.jht.doctor.ui.contact.TranslucentContact;
import com.jht.doctor.ui.contact.WebViewContact;
import com.jht.doctor.ui.contact.AuthContact;
import com.jht.doctor.ui.presenter.AddBankCardVerifyPresenter;
import com.jht.doctor.ui.presenter.AddCoborrowerPresenter;
import com.jht.doctor.ui.presenter.AddMainCardPresenter;
import com.jht.doctor.ui.presenter.BankCardSettingPresenter;
import com.jht.doctor.ui.presenter.BasicInfoPresenter;
import com.jht.doctor.ui.presenter.CoborrowerHistoryPresenter;
import com.jht.doctor.ui.presenter.DemoPresenter;
import com.jht.doctor.ui.presenter.FeedBackPresenter;
import com.jht.doctor.ui.presenter.HomeLoanPresenter;
import com.jht.doctor.ui.presenter.HomeRepaymentPresenter;
import com.jht.doctor.ui.presenter.HouseInfoPresenter;
import com.jht.doctor.ui.presenter.JobInfoPresenter;
import com.jht.doctor.ui.presenter.LoanApplyPresenter;
import com.jht.doctor.ui.presenter.LoanDetailPresenter;
import com.jht.doctor.ui.presenter.LoanMoneyPresenter;
import com.jht.doctor.ui.presenter.LoginPresenter;
import com.jht.doctor.ui.presenter.MessagePresenter;
import com.jht.doctor.ui.presenter.MyBankCardPresenter;
import com.jht.doctor.ui.presenter.MyInfoPresenter;
import com.jht.doctor.ui.presenter.MyLoanPresenter;
import com.jht.doctor.ui.presenter.RegistePresenter;
import com.jht.doctor.ui.presenter.RepaymentPresenter;
import com.jht.doctor.ui.presenter.SettingPresenter;
import com.jht.doctor.ui.presenter.SupportBankPresenter;
import com.jht.doctor.ui.presenter.TradePwdPresenter;
import com.jht.doctor.ui.presenter.TranslucentPresenter;
import com.jht.doctor.ui.presenter.WebviewPresenter;
import com.jht.doctor.ui.presenter.present_jht.AuthPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * ActivityModule
 * Create at 2018/3/24 下午7:37 by mayakun
 */
@Module
public final class ActivityModule {

    private final BaseView mView;

    public ActivityModule(BaseView mView) {
        this.mView = mView;
    }

    @PerActivity
    @Provides
    public DemoPresenter provideDemoActivityPresenter() {
        return new DemoPresenter((DemoContact.View) mView);
    }

    /**********************JHT Start ***********************/
    @PerActivity
    @Provides
    public AuthPresenter provideAuthPresenter() {
        return new AuthPresenter((AuthContact.View) mView);
    }
    @PerActivity
    @Provides
    public RegistePresenter provideRegistePresenter() {
        return new RegistePresenter((RegisteContact.View) mView);
    }


    /**********************JHT end ***********************/















    @PerActivity
    @Provides
    public LoginPresenter provideLoginActivityPresenter() {
        return new LoginPresenter((LoginContact.View) mView);
    }

    @PerActivity
    @Provides
    public MyInfoPresenter provideMyInfoPresenter() {
        return new MyInfoPresenter((MyInfoContact.View) mView);
    }

    @PerActivity
    @Provides
    public MessagePresenter provideMessagePresenter() {
        return new MessagePresenter((MessageContact.View) mView);
    }

    @PerActivity
    @Provides
    public BasicInfoPresenter provideBasicInfoPresenter() {
        return new BasicInfoPresenter((BasicInfoContact.View) mView);
    }

    @PerActivity
    @Provides
    public JobInfoPresenter provideJobInfoPresenter() {
        return new JobInfoPresenter((JobInfoContact.View) mView);
    }

    @PerActivity
    @Provides
    public HomeLoanPresenter provideHomeLoanPresenter() {
        return new HomeLoanPresenter((HomeLoanContact.View) mView);
    }

    @PerActivity
    @Provides
    public TradePwdPresenter provideTradePwdPresenter() {
        return new TradePwdPresenter((TradePwdContact.View) mView);

    }

    @PerActivity
    @Provides
    public SettingPresenter provideSettingPresenter() {
        return new SettingPresenter((SettingContract.View) mView);
    }

    @PerActivity
    @Provides
    public TranslucentPresenter provideTranslucentPresenter() {
        return new TranslucentPresenter((TranslucentContact.View) mView);
    }

    @PerActivity
    @Provides
    public HouseInfoPresenter provideHouseInfoPresenter() {
        return new HouseInfoPresenter((HouseInfoContact.View) mView);
    }

    @PerActivity
    @Provides
    public LoanMoneyPresenter provideLoanMoneyPresenter() {
        return new LoanMoneyPresenter((LoanMoneyContact.View) mView);
    }

    @PerActivity
    @Provides
    public MyLoanPresenter provideMyLoanPresenter() {
        return new MyLoanPresenter((MyLoanContact.View) mView);
    }

    @PerActivity
    @Provides
    public HomeRepaymentPresenter provideHomeRepaymentPresenter() {
        return new HomeRepaymentPresenter((HomeRepaymentContact.View) mView);
    }

    @PerActivity
    @Provides
    public LoanDetailPresenter provideLoanDetailPresenter() {
        return new LoanDetailPresenter((LoanDetailContact.View) mView);
    }


    @PerActivity
    @Provides
    public RepaymentPresenter provideRepaymentPresenter() {
        return new RepaymentPresenter((RepaymentContact.View) mView);
    }

    @PerActivity
    @Provides
    public LoanApplyPresenter provideLoanApplyPresenter() {
        return new LoanApplyPresenter((LoanApplyContact.View) mView);
    }

    @PerActivity
    @Provides
    public WebviewPresenter provideWebviewPresenter() {
        return new WebviewPresenter((WebViewContact.View) mView);
    }

    @PerActivity
    @Provides
    public MyBankCardPresenter provideMyBankCardPresenter() {
        return new MyBankCardPresenter((MyBankCardContact.View) mView);
    }

    @PerActivity
    @Provides
    public SupportBankPresenter provideSupportBankPresenter() {
        return new SupportBankPresenter((SupportBankContact.View) mView);
    }

    @PerActivity
    @Provides
    public BankCardSettingPresenter provideBankCardSettingPresenter() {
        return new BankCardSettingPresenter((BankCardSettingContact.View) mView);
    }

    @PerActivity
    @Provides
    public AddMainCardPresenter provideAddMainCardPresenter() {
        return new AddMainCardPresenter((AddMainCardContact.View) mView);
    }

    @PerActivity
    @Provides
    public AddBankCardVerifyPresenter provideAddBankCardVerifyPresenter() {
        return new AddBankCardVerifyPresenter((AddBankCardVerifyContact.View) mView);
    }

    @PerActivity
    @Provides
    public FeedBackPresenter provideFeedBackPresenter() {
        return new FeedBackPresenter((FeedBackContact.View) mView);
    }

    @PerActivity
    @Provides
    public AddCoborrowerPresenter provideAddCoborrowerPresenter() {
        return new AddCoborrowerPresenter((AddCoborrowerContact.View) mView);
    }

    @PerActivity
    @Provides
    public CoborrowerHistoryPresenter CoborrowerHistoryPresenter() {
        return new CoborrowerHistoryPresenter((CoborrowerHistoryContact.View) mView);
    }

    @PerActivity
    @ActivityContext
    @Provides
    public Context provideActivityContext() {
        if (mView instanceof BasicProvider) {
            return ((BasicProvider) mView).actContext();
        }
        //如果是Frament等，再自行实现BaseFrament并impl BasicProvider
        return null;
    }

//    @PerFragment
//    @Provides
//    public XXXContract.Presenter provideXXXFragmentPresenter(){
//        return new XXXPresenter();
//    }

}
