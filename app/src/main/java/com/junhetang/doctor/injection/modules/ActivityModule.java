package com.junhetang.doctor.injection.modules;

import android.content.Context;

import com.junhetang.doctor.injection.qualifiers.ActivityContext;
import com.junhetang.doctor.injection.scopes.PerActivity;
import com.junhetang.doctor.ui.base.BaseView;
import com.junhetang.doctor.ui.base.BasicProvider;
import com.junhetang.doctor.ui.contact.AddBankCardVerifyContact;
import com.junhetang.doctor.ui.contact.AddCoborrowerContact;
import com.junhetang.doctor.ui.contact.AddMainCardContact;
import com.junhetang.doctor.ui.contact.AuthContact;
import com.junhetang.doctor.ui.contact.BankCardSettingContact;
import com.junhetang.doctor.ui.contact.BasicInfoContact;
import com.junhetang.doctor.ui.contact.ChatMessageContact;
import com.junhetang.doctor.ui.contact.CoborrowerHistoryContact;
import com.junhetang.doctor.ui.contact.DemoContact;
import com.junhetang.doctor.ui.contact.FeedBackContact;
import com.junhetang.doctor.ui.contact.FindContact;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.contact.PatientContact;
import com.junhetang.doctor.ui.contact.HomeRepaymentContact;
import com.junhetang.doctor.ui.contact.HouseInfoContact;
import com.junhetang.doctor.ui.contact.JobInfoContact;
import com.junhetang.doctor.ui.contact.LoanApplyContact;
import com.junhetang.doctor.ui.contact.LoanDetailContact;
import com.junhetang.doctor.ui.contact.LoanMoneyContact;
import com.junhetang.doctor.ui.contact.LoginContact;
import com.junhetang.doctor.ui.contact.MessageContact;
import com.junhetang.doctor.ui.contact.MyBankCardContact;
import com.junhetang.doctor.ui.contact.MyInfoContact;
import com.junhetang.doctor.ui.contact.MyLoanContact;
import com.junhetang.doctor.ui.contact.PersonalContact;
import com.junhetang.doctor.ui.contact.RepaymentContact;
import com.junhetang.doctor.ui.contact.SettingContract;
import com.junhetang.doctor.ui.contact.SupportBankContact;
import com.junhetang.doctor.ui.contact.TradePwdContact;
import com.junhetang.doctor.ui.contact.TranslucentContact;
import com.junhetang.doctor.ui.contact.WalletContact;
import com.junhetang.doctor.ui.contact.WebViewContact;
import com.junhetang.doctor.ui.contact.WorkRoomContact;
import com.junhetang.doctor.ui.presenter.AddBankCardVerifyPresenter;
import com.junhetang.doctor.ui.presenter.AddCoborrowerPresenter;
import com.junhetang.doctor.ui.presenter.AddMainCardPresenter;
import com.junhetang.doctor.ui.presenter.BankCardSettingPresenter;
import com.junhetang.doctor.ui.presenter.BasicInfoPresenter;
import com.junhetang.doctor.ui.presenter.ChatMessagePresenter;
import com.junhetang.doctor.ui.presenter.CoborrowerHistoryPresenter;
import com.junhetang.doctor.ui.presenter.DemoPresenter;
import com.junhetang.doctor.ui.presenter.FeedBackPresenter;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.ui.presenter.PatientPresenter;
import com.junhetang.doctor.ui.presenter.HomeRepaymentPresenter;
import com.junhetang.doctor.ui.presenter.JobInfoPresenter;
import com.junhetang.doctor.ui.presenter.LoanApplyPresenter;
import com.junhetang.doctor.ui.presenter.LoanDetailPresenter;
import com.junhetang.doctor.ui.presenter.LoanMoneyPresenter;
import com.junhetang.doctor.ui.presenter.LoginPresenter;
import com.junhetang.doctor.ui.presenter.MessagePresenter;
import com.junhetang.doctor.ui.presenter.MyBankCardPresenter;
import com.junhetang.doctor.ui.presenter.MyInfoPresenter;
import com.junhetang.doctor.ui.presenter.MyLoanPresenter;
import com.junhetang.doctor.ui.presenter.PersonalPresenter;
import com.junhetang.doctor.ui.presenter.RepaymentPresenter;
import com.junhetang.doctor.ui.presenter.SettingPresenter;
import com.junhetang.doctor.ui.presenter.SupportBankPresenter;
import com.junhetang.doctor.ui.presenter.TradePwdPresenter;
import com.junhetang.doctor.ui.presenter.TranslucentPresenter;
import com.junhetang.doctor.ui.presenter.WebviewPresenter;
import com.junhetang.doctor.ui.presenter.WorkRoomPresenter;
import com.junhetang.doctor.ui.presenter.present_jht.AuthPresenter;
import com.junhetang.doctor.ui.presenter.present_jht.FindPresenter;
import com.junhetang.doctor.ui.presenter.present_jht.WalletPresenter;
import com.junhetang.doctor.ui.presenter.HouseInfoPresenter;

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
