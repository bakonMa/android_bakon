package com.renxin.doctor.activity.data.http;

import com.renxin.doctor.activity.data.response.HttpResponse;
import com.renxin.doctor.activity.ui.bean.AppUpdateBean;
import com.renxin.doctor.activity.ui.bean.ApplyInfoBean;
import com.renxin.doctor.activity.ui.bean.BankBean;
import com.renxin.doctor.activity.ui.bean.ConfigBean;
import com.renxin.doctor.activity.ui.bean.DealDetailBean;
import com.renxin.doctor.activity.ui.bean.HomeLoanBean;
import com.renxin.doctor.activity.ui.bean.HouseInfoResponse;
import com.renxin.doctor.activity.ui.bean.JudgeIfTiedBean;
import com.renxin.doctor.activity.ui.bean.LoanDetailBean;
import com.renxin.doctor.activity.ui.bean.MaxAmtBean;
import com.renxin.doctor.activity.ui.bean.MessageBean;
import com.renxin.doctor.activity.ui.bean.MyAccountInfoBean;
import com.renxin.doctor.activity.ui.bean.RechargeBean;
import com.renxin.doctor.activity.ui.bean.RepaymentHomeBean;
import com.renxin.doctor.activity.ui.bean.RepaymentOffLineBean;
import com.renxin.doctor.activity.ui.bean.SupportBankBean;
import com.renxin.doctor.activity.ui.bean_jht.AuthInfoBean;
import com.renxin.doctor.activity.ui.bean_jht.HospitalBean;
import com.renxin.doctor.activity.ui.bean_jht.WalletBean;
import com.renxin.doctor.activity.config.HttpConfig;
import com.renxin.doctor.activity.ui.bean.AddCoborrowerBean;
import com.renxin.doctor.activity.ui.bean.ApplyUserBean;
import com.renxin.doctor.activity.ui.bean.BankCardBean;
import com.renxin.doctor.activity.ui.bean.BankTypeBean;
import com.renxin.doctor.activity.ui.bean.ContributiveBean;
import com.renxin.doctor.activity.ui.bean.DealInfoBean;
import com.renxin.doctor.activity.ui.bean.IfBankOfJointBean;
import com.renxin.doctor.activity.ui.bean.LoginResponse;
import com.renxin.doctor.activity.ui.bean.MessageCountBean;
import com.renxin.doctor.activity.ui.bean.MyLoanBean;
import com.renxin.doctor.activity.ui.bean.OtherBean;
import com.renxin.doctor.activity.ui.bean.ReusingBean;
import com.renxin.doctor.activity.ui.bean_jht.BaseConfigBean;
import com.renxin.doctor.activity.ui.bean_jht.UploadImgBean;
import com.renxin.doctor.activity.ui.bean_jht.UserBaseInfoBean;
import com.renxin.doctor.activity.ui.bean_jht.VisitInfoBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by table on 2017/11/22.
 * description:
 */
public interface HttpAPI {

    //上传单个文件
    //key multipartFiles
    @Multipart
    @POST("uploadImage")
    Observable<HttpResponse<UploadImgBean>> uploadSingleFile(@Part MultipartBody.Part type,
                                                             @Part MultipartBody.Part upFileInfo,
                                                             @Part MultipartBody.Part time,
                                                             @Part MultipartBody.Part sign);

    //发送验证码
    @POST("sendcode")
    Observable<HttpResponse<String>> sendCode(@QueryMap Params params);

    //登录
    @POST("login")
    Observable<HttpResponse<LoginResponse>> login(@Body Params params);

    //注册
    @POST("register")
    Observable<HttpResponse<LoginResponse>> register(@Body Params params);

    //获取认证状态
    @POST("getUserIdentifyStatus")
    Observable<HttpResponse<OtherBean>> getUserIdentifyStatus(@QueryMap Params params);

    //获取个人中心资料，认证时的资料
    @GET("getUserBasicInfo")
    Observable<HttpResponse<UserBaseInfoBean>> getUserBasicInfo(@QueryMap Params params);

    //获取科室、职称、擅长等信息
    @GET("getDpAndTitles")
    Observable<HttpResponse<BaseConfigBean>> getDpAndTitles(@QueryMap Params params);

    //获取省市 医院列表
    @POST("getHospital")
    Observable<HttpResponse<List<HospitalBean>>> getHospital(@QueryMap Params params);

    //认证信息提交1
    @POST("userIdentify")
    Observable<HttpResponse<String>> userIdentify(@QueryMap Params params);

    //认证信息提交1
    @POST("userIdentifyNext")
    Observable<HttpResponse<String>> userIdentifyNext(@QueryMap Params params);

    //获取认证信息提交
    @POST("getUserIdentify")
    Observable<HttpResponse<AuthInfoBean>> getUserIdentify(@QueryMap Params params);

    //个人公告和简介的提交
    @POST("add_userbasic")
    Observable<HttpResponse<String>> addUserbasic(@QueryMap Params params);

    //钱包首页
    @POST("wallet_home")
    Observable<HttpResponse<WalletBean>> getWallet(@QueryMap Params params);

    //我的银行卡
    @POST("userbanklist")
    Observable<HttpResponse<List<BankCardBean>>> userbanklist(@QueryMap Params params);

    //银行列表
    @GET("getBank")
    Observable<HttpResponse<List<BankTypeBean>>> getBankType(@QueryMap Params params);

    //添加银行卡
    @POST("useraddbank")
    Observable<HttpResponse<String>> useraddbank(@QueryMap Params params);
    //解绑
    @POST("userdel_bank")
    Observable<HttpResponse<String>> deleteBankCard(@QueryMap Params params);

    //解绑
    @POST("deal_flow")
    Observable<HttpResponse<List<DealDetailBean>>> getDealFlow(@QueryMap Params params);

    //获取资费信息
    @POST("getvisit_info")
    Observable<HttpResponse<VisitInfoBean>> getVisitInfo(@QueryMap Params params);

    //设置资费信息
    @POST("visitcost_set")
    Observable<HttpResponse<String>> setVisitPrice(@QueryMap Params params);





    //***************************************************
    //发送修改验证码
    @POST("customerApp/userCenter/sendTradeCode")
    Observable<HttpResponse<String>> sendTradeCode(@Body Params params);

    //获取基本资料
    @GET("customerApp/loanApply/dataDic/getConfig")
    Observable<HttpResponse<ConfigBean>> getConfig(@QueryMap Params params);

    //判断是否有交易密码
    @GET("customerApp/userCenter/tradePwdStatus")
    Observable<HttpResponse<OtherBean>> tradePwdStatus(@QueryMap Params params);

    //初次设置交易密码
    @POST("customerApp/userCenter/setTradePwd")
    Observable<HttpResponse<String>> setTradePwd(@Body Params params);

    //重置交易密码
    @POST("customerApp/userCenter/resetTradePwd")
    Observable<HttpResponse<String>> resetTradePwd(@Body Params params);

    //消息列表
    @POST("customerApp/userMessage/selectMessagePageList")
    Observable<HttpResponse<MessageBean>> getMessageList(@Body Params params);

    //删除消息
    @POST("customerApp/userMessage/delete")
    Observable<HttpResponse<String>> deleteMessage(@QueryMap Params params);

    //提交基本信息
    @POST("customerApp/loanApply/insertUserByAPP")
    Observable<HttpResponse<String>> commitBasicInfo(@Body Params params);

    //提交工作信息
    @POST("customerApp/loanApply/insertUserJobByAPP")
    Observable<HttpResponse<String>> commitJobInfo(@Body Params params);

    //提交房产信息
    @POST("customerApp/loanApply/insertUserHouseByAPP")
    Observable<HttpResponse<HouseInfoResponse>> commitHouseInfo(@Body Params params);

    //获取是否有未完成订单
    @GET("customerApp/loanApply/applyJudge")
    Observable<HttpResponse<HomeLoanBean>> applyStatus();

    //获取客户申请三步骤资料
    @GET("customerApp/userCenter/userInfo")
    Observable<HttpResponse<ApplyInfoBean>> getApplyInfo();

    //还款首页接口
    @GET("customerApp/applyHome/homePage")
    Observable<HttpResponse<RepaymentHomeBean>> getHomeRepayment();

    //线下还款列表
    @GET("customerApp/repayment/repaymentInfo")
    Observable<HttpResponse<RepaymentOffLineBean>> getOffLineRepaymentInfo(@QueryMap Params params);

    //我的账户
    @GET("customerApp/bankCard/selectOrderAccountInfo")
    Observable<HttpResponse<MyAccountInfoBean>> selectOrderAccountInfo(@QueryMap Params params);

    //交易明细
    @POST("customerApp/repayment/selectRechargeWithdrawPageList")
    Observable<HttpResponse<DealInfoBean>> transactionDetails(@Body Params params);

    //密码 充值
    @POST("customerApp/repayment/pwdRecharge")
    Observable<HttpResponse<RechargeBean>> pwdRecharge(@Body Params params);

    //确认充值
    @POST("customerApp/repayment/rechargeConfirm")
    Observable<HttpResponse<String>> recharegeConfirm(@Body Params params);

    //密码 提现
    @POST("customerApp/repayment/pwdWithDraw")
    Observable<HttpResponse<String>> pwdWithDraw(@Body Params params);

    //验证码提现
    @POST("customerApp/repayment/smsWithDraw")
    Observable<HttpResponse<String>> smsWithDraw(@Body Params params);

    //验证码充值
    @POST("customerApp/repayment/smsRecharge")
    Observable<HttpResponse<String>> smsRecharge(@Body Params params);

    //充值/提现验证方式查询
    @GET("customerApp/repayment/selectVerificationMethod")
    Observable<HttpResponse<OtherBean>> selectVerificationMethod(@QueryMap Params params);

    //充值 获取验证码
    @GET("customerApp/repayment/getRechargeSmsVerifyCode")
    Observable<HttpResponse<String>> getRechargeSmsVerifyCode(@QueryMap Params params);

    //提现 获取验证码
    @GET("customerApp/repayment/getWithdrawSmsVerifyCode")
    Observable<HttpResponse<String>> getWithdrawSmsVerifyCode(@QueryMap Params params);

    //获取首页最大金额
    @GET("customerApp/loanApply/dataConfig/getMaxAmt")
    Observable<HttpResponse<MaxAmtBean>> getMAxAmt();

    //提交借款金额
    @POST("customerApp/loanApply/loanApply")
    Observable<HttpResponse<String>> loanMoney(@Body Params params);

    //我的借款列表
    @GET("customerApp/userCenter/myLoan")
    Observable<HttpResponse<List<MyLoanBean>>> getMyLoan();//我的借款列表

    //获取征信url
    @GET("customerApp/loanApply/credit")
    Observable<HttpResponse<String>> getCreditUrl(@QueryMap Params params);

    //是否已经申请征信验证
    @GET("customerApp/loanApply/getOrderCreditStatus")
    Observable<HttpResponse<String>> getOrderCreditStatus(@QueryMap Params params);

    //借款详情
    @GET("customerApp/userCenter/loanDetail")
    Observable<HttpResponse<LoanDetailBean>> getLoanDetail(@QueryMap Params params);

    //获取银行卡列表
    @GET("customerApp/bankCard/selectBankList")
    Observable<HttpResponse<BankCardBean>> getBankList(@QueryMap Params params);

    //判断用户是否绑过卡
    @POST("customerApp/bankCard/ifTileBank")
    Observable<HttpResponse<JudgeIfTiedBean>> ifTileBank(@Body Params params);

    //获取支持银行列表
    @GET("customerApp/bankCard/supportBankList")
    Observable<HttpResponse<List<SupportBankBean>>> supportBankList(@QueryMap Params params);

    //根据银行卡号获取银行卡名称
    @GET("customerApp/bankCard/selectBankLogo")
    Observable<HttpResponse<BankBean>> getBankName(@QueryMap Params params);

    //开户
    @POST("customerApp/bankCard/applyAuthorization")
    Observable<HttpResponse> applyAuthorization(@Body Params params);

    //授权
    @POST("customerApp/bankCard/userAuthorization")
    Observable<HttpResponse<ApplyUserBean>> userAuthorization(@Body Params params);



    //添加共借人
    @POST("customerApp/bankCard/insertDebtorByAPP")
    Observable<HttpResponse<AddCoborrowerBean>> insertDebtorByAPP(@Body Params params);

    //用户绑定销售员
    @GET("customerApp/userLogin/userBindSale")
    Observable<HttpResponse<String>> userBindSale(@QueryMap Params params);

    //确认使用历史银行卡
    @GET("customerApp/bankCard/ensureBankCard")
    Observable<HttpResponse<String>> ensureBankCard(@QueryMap Params params);

    //复用历史共借人信息
    @POST("customerApp/bankCard/ifResuingBankOfJoint")
    Observable<HttpResponse<ReusingBean>> ifResuingBankOfJoint(@Body Params params);

    //反馈
    @POST("customerApp/userFeedback/insert")
    Observable<HttpResponse<String>> feedBack(@QueryMap Params params);

    //获取是否有共借人银行卡
    @POST("customerApp/bankCard/ifBankOfJoint")
    Observable<HttpResponse<IfBankOfJointBean>> ifBankOfJoint(@Body Params params);

    //获取个人中心账户总计
    @GET("customerApp/bankCard/selectUserAccountInfo")
    Observable<HttpResponse<MyAccountInfoBean>> selectUserAccountInfo();

    //消息列表已读未读统计
    @GET("customerApp/userMessage/selectCount")
    Observable<HttpResponse<MessageCountBean>> selectCount();

    //判断是否为存管，有存管时返回卡
    @POST("customerApp/bankCard/contributiveTypeJudge")
    Observable<HttpResponse<ContributiveBean>> contributiveTypeJudge(@Body Params params);

    //确认使用存管下的卡
    @POST("customerApp/bankCard/ensureBankCardOfCunGuan")
    Observable<HttpResponse<String>> ensureBankCardOfCunGuan(@Body Params params);

    //设置绑卡时交易密码
    @POST("customerApp/userCenter/bindCardSetTradePwd")
    Observable<HttpResponse<String>> bindCardSetTradePwd(@Body Params params);

    //检查App更新
    @GET("customerApp/clientVersion/search")
    Observable<HttpResponse<AppUpdateBean>> appUpdateCheck(@Query("type") int type);

    //下载Apk文件
    @GET
    @Streaming
    Observable<ResponseBody> downloadApk(@Url String url, @Header(HttpConfig.HTTP_HEADER_DOWNLOAD_APK) String identifier);

    //绑卡时判断是否有交易密码
    @GET("customerApp/userCenter/bindCardTradePwdStatus")
    Observable<HttpResponse<OtherBean>> bindCardTradePwdStatus(@QueryMap Params params);

}
