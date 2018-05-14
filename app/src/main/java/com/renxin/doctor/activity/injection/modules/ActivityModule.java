package com.renxin.doctor.activity.injection.modules;

import android.content.Context;

import com.renxin.doctor.activity.injection.qualifiers.ActivityContext;
import com.renxin.doctor.activity.injection.scopes.PerActivity;
import com.renxin.doctor.activity.ui.base.BaseView;
import com.renxin.doctor.activity.ui.base.BasicProvider;
import com.renxin.doctor.activity.ui.contact.AddBankCardVerifyContact;
import com.renxin.doctor.activity.ui.contact.AddCoborrowerContact;
import com.renxin.doctor.activity.ui.contact.AddMainCardContact;
import com.renxin.doctor.activity.ui.contact.AuthContact;
import com.renxin.doctor.activity.ui.contact.BankCardSettingContact;
import com.renxin.doctor.activity.ui.contact.BasicInfoContact;
import com.renxin.doctor.activity.ui.contact.ChatMessageContact;
import com.renxin.doctor.activity.ui.contact.CoborrowerHistoryContact;
import com.renxin.doctor.activity.ui.contact.DemoContact;
import com.renxin.doctor.activity.ui.contact.FeedBackContact;
import com.renxin.doctor.activity.ui.contact.FindContact;
import com.renxin.doctor.activity.ui.contact.OpenPaperContact;
import com.renxin.doctor.activity.ui.contact.PatientContact;
import com.renxin.doctor.activity.ui.contact.HomeRepaymentContact;
import com.renxin.doctor.activity.ui.contact.HouseInfoContact;
import com.renxin.doctor.activity.ui.contact.JobInfoContact;
import com.renxin.doctor.activity.ui.contact.LoanApplyContact;
import com.renxin.doctor.activity.ui.contact.LoanDetailContact;
import com.renxin.doctor.activity.ui.contact.LoanMoneyContact;
import com.renxin.doctor.activity.ui.contact.LoginContact;
import com.renxin.doctor.activity.ui.contact.MessageContact;
import com.renxin.doctor.activity.ui.contact.MyBankCardContact;
import com.renxin.doctor.activity.ui.contact.MyInfoContact;
import com.renxin.doctor.activity.ui.contact.MyLoanContact;
import com.renxin.doctor.activity.ui.contact.PersonalContact;
import com.renxin.doctor.activity.ui.contact.RepaymentContact;
import com.renxin.doctor.activity.ui.contact.SettingContract;
import com.renxin.doctor.activity.ui.contact.SupportBankContact;
import com.renxin.doctor.activity.ui.contact.TradePwdContact;
import com.renxin.doctor.activity.ui.contact.TranslucentContact;
import com.renxin.doctor.activity.ui.contact.WalletContact;
import com.renxin.doctor.activity.ui.contact.WebViewContact;
import com.renxin.doctor.activity.ui.contact.WorkRoomContact;
import com.renxin.doctor.activity.ui.presenter.AddBankCardVerifyPresenter;
import com.renxin.doctor.activity.ui.presenter.AddCoborrowerPresenter;
import com.renxin.doctor.activity.ui.presenter.AddMainCardPresenter;
import com.renxin.doctor.activity.ui.presenter.BankCardSettingPresenter;
import com.renxin.doctor.activity.ui.presenter.BasicInfoPresenter;
import com.renxin.doctor.activity.ui.presenter.ChatMessagePresenter;
import com.renxin.doctor.activity.ui.presenter.CoborrowerHistoryPresenter;
import com.renxin.doctor.activity.ui.presenter.DemoPresenter;
import com.renxin.doctor.activity.ui.presenter.FeedBackPresenter;
import com.renxin.doctor.activity.ui.presenter.OpenPaperPresenter;
import com.renxin.doctor.activity.ui.presenter.PatientPresenter;
import com.renxin.doctor.activity.ui.presenter.HomeRepaymentPresenter;
import com.renxin.doctor.activity.ui.presenter.JobInfoPresenter;
import com.renxin.doctor.activity.ui.presenter.LoanApplyPresenter;
import com.renxin.doctor.activity.ui.presenter.LoanDetailPresenter;
import com.renxin.doctor.activity.ui.presenter.LoanMoneyPresenter;
import com.renxin.doctor.activity.ui.presenter.LoginPresenter;
import com.renxin.doctor.activity.ui.presenter.MessagePresenter;
import com.renxin.doctor.activity.ui.presenter.MyBankCardPresenter;
import com.renxin.doctor.activity.ui.presenter.MyInfoPresenter;
import com.renxin.doctor.activity.ui.presenter.MyLoanPresenter;
import com.renxin.doctor.activity.ui.presenter.PersonalPresenter;
import com.renxin.doctor.activity.ui.presenter.RepaymentPresenter;
import com.renxin.doctor.activity.ui.presenter.SettingPresenter;
import com.renxin.doctor.activity.ui.presenter.SupportBankPresenter;
import com.renxin.doctor.activity.ui.presenter.TradePwdPresenter;
import com.renxin.doctor.activity.ui.presenter.TranslucentPresenter;
import com.renxin.doctor.activity.ui.presenter.WebviewPresenter;
import com.renxin.doctor.activity.ui.presenter.WorkRoomPresenter;
import com.renxin.doctor.activity.ui.presenter.present_jht.AuthPresenter;
import com.renxin.doctor.activity.ui.presenter.present_jht.FindPresenter;
import com.renxin.doctor.activity.ui.presenter.present_jht.WalletPresenter;
import com.renxin.doctor.activity.ui.presenter.HouseInfoPresenter;

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
    public PersonalPresenter providePersonalPresenter() {
        return new PersonalPresenter((PersonalContact.View) mView);
    }

    @PerActivity
    @Provides
    public WalletPresenter provideWalletPresenter() {
        return new WalletPresenter((WalletContact.View) mView);
    }

    @PerActivity
    @Provides
    public ChatMessagePresenter provideChatMessagePresenter() {
        return new ChatMessagePresenter((ChatMessageContact.View) mView);
    }

    @PerActivity
    @Provides
    public OpenPaperPresenter provideOpenPaperPresenter() {
        return new OpenPaperPresenter((OpenPaperContact.View) mView);
    }

    @PerActivity
    @Provides
    public FindPresenter provideFindPresenter() {
        return new FindPresenter((FindContact.View) mView);
    }

    @PerActivity
    @Provides
    public WorkRoomPresenter provideWorkRoomPresenter() {
        return new WorkRoomPresenter((WorkRoomContact.View) mView);
    }


    /**********************JHT end ***********************/
    /**********************JHT end ***********************/
    /**********************JHT end ***********************/
    /**********************JHT end ***********************/
    /**********************JHT end ***********************/
    /**********************JHT end ***********************/
    /**********************JHT end ***********************/
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
    public PatientPresenter provideHomeLoanPresenter() {
        return new PatientPresenter((PatientContact.View) mView);
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
