package com.junhetang.doctor.ui.presenter;

import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.base.BaseObserver;
import com.junhetang.doctor.ui.bean.BankCardBean;
import com.junhetang.doctor.ui.bean.BankTypeBean;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.DealDetailBean;
import com.junhetang.doctor.ui.bean.WalletBean;
import com.junhetang.doctor.ui.contact.WalletContact;
import com.junhetang.doctor.utils.M;
import com.junhetang.doctor.widget.dialog.LoadingDialog;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * WalletPresenter 钱包
 * Create at 2018/4/10 下午2:49 by mayakun
 */
public class WalletPresenter implements WalletContact.Presenter {

    public static final int GET_WALLET_OK = 0x110;
    public static final int GET_BANKCARD_OK = 0x111;
    public static final int GET_BANKTYPE_OK = 0x112;
    public static final int ADD_BANKCARD_OK = 0x113;
    public static final int DELETE_BANKCARD_OK = 0x114;
    public static final int WITHDRAW_OK = 0x115;
    public static final int GET_DEAL_LIST_OK = 0x116;

    private WalletContact.View mView;
    private CompositeDisposable mDisposable;
    private LoadingDialog mDialog;

    @Inject
    public WalletPresenter(WalletContact.View view) {
        this.mView = view;
        mDisposable = new CompositeDisposable();
        mDialog = new LoadingDialog(mView.provideContext());
    }

    @Override
    public void unsubscribe() {
        if (!mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        if (null != mDialog) {
            mDialog = null;
        }
        mView = null;
    }

    @Override
    public void getWallet() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getWallet(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<WalletBean>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<WalletBean> response) {
                        mView.onSuccess(M.createMessage(response.data, GET_WALLET_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void userBankList() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().userbanklist(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<List<BankCardBean>>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<List<BankCardBean>> response) {
                        mView.onSuccess(M.createMessage(response.data, GET_BANKCARD_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void getBankType() {
        Params params = new Params();
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getBankType(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<List<BankTypeBean>>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<List<BankTypeBean>> response) {
                        mView.onSuccess(M.createMessage(response.data, GET_BANKTYPE_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void addBankcard(String user_name, int bank_id, String bank_number, String openingbank) {
        Params params = new Params();
        params.put("user_name", user_name);
        params.put("bank_id", bank_id);
        params.put("bank_number", bank_number);
        params.put("openingbank", openingbank);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().useraddbank(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.data, ADD_BANKCARD_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void deleteBankCard(int id) {
        Params params = new Params();
        params.put("id", id);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().deleteBankCard(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.data, DELETE_BANKCARD_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void witdraw(int bound_bank_id, String money) {
        Params params = new Params();
        params.put("bound_bank_id", bound_bank_id);
        params.put("money", money);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().exmoneySubmit(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(disposable -> {
                    if (mDialog != null) mDialog.show();
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<String> response) {
                        mView.onSuccess(M.createMessage(response.data, WITHDRAW_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }

    @Override
    public void getDealFlow(int pageNum) {
        Params params = new Params();
        params.put("page", pageNum);
        params.put(HttpConfig.SIGN_KEY, params.getSign(params));
        DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getDealFlow(params))
                .compose(mView.toLifecycle())
                .subscribe(new BaseObserver<HttpResponse<BasePageBean<DealDetailBean>>>(null) {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onSuccess(HttpResponse<BasePageBean<DealDetailBean>> response) {
                        mView.onSuccess(M.createMessage(response.data, GET_DEAL_LIST_OK));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });

    }
}
