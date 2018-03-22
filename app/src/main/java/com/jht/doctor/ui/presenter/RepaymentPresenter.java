package com.jht.doctor.ui.presenter;

import com.jht.doctor.application.DocApplication;
import com.jht.doctor.data.api.http.Params;
import com.jht.doctor.data.response.HttpResponse;
import com.jht.doctor.ui.base.BaseObserver;
import com.jht.doctor.ui.bean.DealInfoBean;
import com.jht.doctor.ui.bean.MyAccountInfoBean;
import com.jht.doctor.ui.bean.OtherBean;
import com.jht.doctor.ui.bean.RechargeBean;
import com.jht.doctor.ui.bean.RepaymentOffLineBean;
import com.jht.doctor.ui.contact.RepaymentContact;
import com.jht.doctor.utils.M;
import com.jht.doctor.utils.U;
import com.jht.doctor.widget.dialog.LoadingDialog;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by mayakun on 2017/12/7
 * 还款（线下，我的账户，充值，提现）
 */
public class RepaymentPresenter implements RepaymentContact.Presenter {

    private RepaymentContact.View mView;
    private CompositeSubscription compositeSubscription;
    private LoadingDialog mDialog;

    public static final int TYPE_RECHARGE = 0;//充值
    public static final int TYPE_WITHDRAW = 1;//提现

    public static final int REPAYMENT_OFFLINE = 0x110;
    public static final int REPAYMENT_MYACCOUNT = 0x111;
    public static final int REPAYMENT_RECHARGE = 0x112;
    public static final int REPAYMENT_WITHDRAW = 0x113;
    public static final int REPAYMENT_DETAIL = 0x114;

    public static final int REPAYMENT_PAYWAY = 0x115;
    public static final int REPAYMENT_SMS_CODE = 0x116;
    public static final int REPAYMENT_SMS_CODE_AGAIN = 0x119;

    public static final int REPAYMENT_SMS_RECHARGE = 0x117;
    public static final int REPAYMENT_SMS_WITHDRAW = 0x118;

    public RepaymentPresenter(RepaymentContact.View mView) {
        this.mView = mView;
        compositeSubscription = new CompositeSubscription();
        mDialog = new LoadingDialog(mView.provideContext());
    }

    @Override
    public void unsubscribe() {
        if (!compositeSubscription.isUnsubscribed()) {
            compositeSubscription.unsubscribe();
        }
    }

    @Override
    public void getOffLineRepayment(String orderId, int pageNum) {
        Params params = new Params();
        params.put("orderNo", orderId);
        params.put("page", pageNum);
        Subscription subscription =
                DocApplication.getAppComponent().dataRepo().http()
                        .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getOffLineRepaymentInfo(params))
                        .compose(mView.toLifecycle())
                        .doOnSubscribe(() -> {
                            if (mDialog != null)
                                mDialog.show();
                        })
                        .subscribe(
                                new BaseObserver<HttpResponse<RepaymentOffLineBean>>(mDialog) {
                                    @Override
                                    public void onSuccess(HttpResponse<RepaymentOffLineBean> httpResponse) {
                                        mView.onSuccess(M.createMessage(httpResponse.result, REPAYMENT_OFFLINE));
                                    }

                                    @Override
                                    public void onError(String errorCode, String errorMsg) {
                                        mView.onError(errorCode, errorMsg);
                                    }
                                }
                        );
        compositeSubscription.add(subscription);

    }

    @Override
    public void getMyAccountRepayment(String orderId) {
        Params params = new Params();
        params.put("orderNo", orderId);
        Subscription subscription =
                DocApplication.getAppComponent().dataRepo().http()
                        .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().selectOrderAccountInfo(params))
                        .compose(mView.toLifecycle())
                        .doOnSubscribe(() -> {
                            if (mDialog != null)
                                mDialog.show();
                        })
                        .subscribe(
                                new BaseObserver<HttpResponse<MyAccountInfoBean>>(mDialog) {
                                    @Override
                                    public void onSuccess(HttpResponse<MyAccountInfoBean> httpResponse) {
                                        mView.onSuccess(M.createMessage(httpResponse.result, REPAYMENT_MYACCOUNT));
                                    }

                                    @Override
                                    public void onError(String errorCode, String errorMsg) {
                                        mView.onError(errorCode, errorMsg);
                                    }
                                }
                        );
        compositeSubscription.add(subscription);
    }

    @Override
    public void transactionDetails(String platformUserNo, int type, int pagNum) {
        Params params = new Params();
        params.put("platformUserNo", platformUserNo);
        switch (type) {
            case 0:
                break;
            case 1:
                params.put("orderType", "2001");
                break;
            case 2:
                params.put("orderType", "2003");
                break;
            case 3:
                params.put("resStatus", "SUCCESS");
                break;
            case 4:
                params.put("resStatus", "FAIL");
                break;
            case 5:
                params.put("resStatus", "CONFIRMING");
                break;
        }
        params.put("pageSize", U.PAGE_SIZE);
        params.put("pageNo", pagNum);
        Subscription subscription =
                DocApplication.getAppComponent().dataRepo().http()
                        .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().transactionDetails(params))
                        .compose(mView.toLifecycle())
                        .doOnSubscribe(() -> {
                            if (mDialog != null)
                                mDialog.show();
                        })
                        .subscribe(
                                new BaseObserver<HttpResponse<DealInfoBean>>(mDialog) {
                                    @Override
                                    public void onSuccess(HttpResponse<DealInfoBean> httpResponse) {
                                        mView.onSuccess(M.createMessage(httpResponse.result, REPAYMENT_DETAIL));
                                    }

                                    @Override
                                    public void onError(String errorCode, String errorMsg) {
                                        mView.onError(errorCode, errorMsg);
                                    }
                                }
                        );
        compositeSubscription.add(subscription);
    }

    /**
     * 交易密码 充值
     *
     * @param params
     */
    @Override
    public void pwdRecharge(Params params) {
        Subscription subscription =
                DocApplication.getAppComponent().dataRepo().http()
                        .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().pwdRecharge(params))
                        .compose(mView.toLifecycle())
                        .doOnSubscribe(() -> {
                            if (mDialog != null) {
                                mDialog.show();
                            }
                        })
                        .subscribe(
                                new BaseObserver<HttpResponse<RechargeBean>>(mDialog) {
                                    @Override
                                    public void onSuccess(HttpResponse<RechargeBean> httpResponse) {
                                        mView.onSuccess(M.createMessage(httpResponse.result, REPAYMENT_RECHARGE));
                                    }

                                    @Override
                                    public void onError(String errorCode, String errorMsg) {
                                        mView.onError(errorCode, errorMsg);
                                    }
                                }
                        );
        compositeSubscription.add(subscription);
    }

    /**
     * 交易密码 提现
     *
     * @param params
     */
    @Override
    public void pwdWithDraw(Params params) {
        Subscription subscription =
                DocApplication.getAppComponent().dataRepo().http()
                        .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().pwdWithDraw(params))
                        .compose(mView.toLifecycle())
                        .doOnSubscribe(() -> {
                            if (mDialog != null) {
                                mDialog.show();
                            }
                        })
                        .subscribe(
                                new BaseObserver<HttpResponse<String>>(mDialog) {
                                    @Override
                                    public void onSuccess(HttpResponse<String> httpResponse) {
                                        mView.onSuccess(M.createMessage(httpResponse.result, REPAYMENT_WITHDRAW));
                                    }

                                    @Override
                                    public void onError(String errorCode, String errorMsg) {
                                        mView.onError(errorCode, errorMsg);
                                    }
                                }
                        );
        compositeSubscription.add(subscription);
    }


    /**
     * 充值&提现 获取交易方式
     * 验证码/支付密码
     */
    @Override
    public void getPayWay(String orderNo) {
        Params params = new Params();
        params.put("orderNo", orderNo);
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().selectVerificationMethod(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                }).subscribe(new BaseObserver<HttpResponse<OtherBean>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<OtherBean> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.result, REPAYMENT_PAYWAY));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    /**
     * 充值/提现 获取短信验证码
     *
     * @param form           0:充值/提现画面 1：验证码画面 (为了区分请求的画面不同，event事件问题)
     * @param type           0:充值 1：提现
     * @param platformUserNo
     * @param orderNo
     * @param amount 金额
     */
    @Override
    public void getSmsVerifyCode(int form, int type, String platformUserNo, String orderNo, String amount) {
        Params params = new Params();
        params.put("otherPlatformId", platformUserNo);
        params.put("orderNo", orderNo);
        //后台匹配"连连" 充值添加金额参数
        if (type == 0) {
            params.put("amount", amount);
        }
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(type == RepaymentPresenter.TYPE_RECHARGE ?
                        DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getRechargeSmsVerifyCode(params)
                        : DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().getWithdrawSmsVerifyCode(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.result, form == 0 ? REPAYMENT_SMS_CODE : REPAYMENT_SMS_CODE_AGAIN));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }

    /**
     * 验证码 充值
     *
     * @param params
     */
    @Override
    public void smsRecharge(Params params) {
        Subscription subscription =
                DocApplication.getAppComponent().dataRepo().http()
                        .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().smsRecharge(params))
                        .compose(mView.toLifecycle())
                        .doOnSubscribe(() -> {
                            if (mDialog != null) {
                                mDialog.show();
                            }
                        })
                        .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                            @Override
                            public void onSuccess(HttpResponse<String> httpResponse) {
                                mView.onSuccess(M.createMessage(httpResponse.result, REPAYMENT_SMS_RECHARGE));
                            }

                            @Override
                            public void onError(String errorCode, String errorMsg) {
                                mView.onError(errorCode, errorMsg);
                            }
                        });
        compositeSubscription.add(subscription);
    }

    /**
     * 验证码方式提现
     *
     * @param params
     */
    @Override
    public void smsWithDraw(Params params) {
        Subscription subscription = DocApplication.getAppComponent().dataRepo().http()
                .wrapper(DocApplication.getAppComponent().dataRepo().http().provideHttpAPI().smsWithDraw(params))
                .compose(mView.toLifecycle())
                .doOnSubscribe(() -> {
                    if (mDialog != null) {
                        mDialog.show();
                    }
                })
                .subscribe(new BaseObserver<HttpResponse<String>>(mDialog) {
                    @Override
                    public void onSuccess(HttpResponse<String> httpResponse) {
                        mView.onSuccess(M.createMessage(httpResponse.result, REPAYMENT_SMS_WITHDRAW));
                    }

                    @Override
                    public void onError(String errorCode, String errorMsg) {
                        mView.onError(errorCode, errorMsg);
                    }
                });
        compositeSubscription.add(subscription);
    }
}
