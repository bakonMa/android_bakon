package com.junhetang.doctor.data.http;

import com.junhetang.doctor.config.HttpConfig;
import com.junhetang.doctor.data.response.HttpResponse;
import com.junhetang.doctor.ui.bean.AppUpdateBean;
import com.junhetang.doctor.ui.bean.AuthInfoBean;
import com.junhetang.doctor.ui.bean.BankCardBean;
import com.junhetang.doctor.ui.bean.BankTypeBean;
import com.junhetang.doctor.ui.bean.BannerBean;
import com.junhetang.doctor.ui.bean.BaseConfigBean;
import com.junhetang.doctor.ui.bean.BasePageBean;
import com.junhetang.doctor.ui.bean.CheckPaperBean;
import com.junhetang.doctor.ui.bean.CommMessageBean;
import com.junhetang.doctor.ui.bean.CommPaperBean;
import com.junhetang.doctor.ui.bean.CommPaperInfoBean;
import com.junhetang.doctor.ui.bean.DealDetailBean;
import com.junhetang.doctor.ui.bean.HospitalBean;
import com.junhetang.doctor.ui.bean.JiuZhenHistoryBean;
import com.junhetang.doctor.ui.bean.JobScheduleBean;
import com.junhetang.doctor.ui.bean.JobSchedulePatientBean;
import com.junhetang.doctor.ui.bean.LoginResponse;
import com.junhetang.doctor.ui.bean.NewsInfoBean;
import com.junhetang.doctor.ui.bean.OPenPaperBaseBean;
import com.junhetang.doctor.ui.bean.OnlinePaperBackBean;
import com.junhetang.doctor.ui.bean.OtherBean;
import com.junhetang.doctor.ui.bean.PaperInfoBean;
import com.junhetang.doctor.ui.bean.PatientBean;
import com.junhetang.doctor.ui.bean.PatientFamilyBean;
import com.junhetang.doctor.ui.bean.SearchDrugBean;
import com.junhetang.doctor.ui.bean.SystemMsgBean;
import com.junhetang.doctor.ui.bean.UploadImgBean;
import com.junhetang.doctor.ui.bean.UserBaseInfoBean;
import com.junhetang.doctor.ui.bean.WalletBean;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.QueryMap;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * HttpAPI
 * Create at 2018/5/22 下午3:58 by mayakun
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

    //登录
    @POST("logout")
    Observable<HttpResponse<String>> logout(@Body Params params);

    //注册
    @POST("register")
    Observable<HttpResponse<LoginResponse>> register(@Body Params params);//注册

    //重设密码
    @POST("updatePwd")
    Observable<HttpResponse<String>> updatePwd(@Body Params params);

    //是否接收推送消息设置
    @POST("set_pushstatus")
    Observable<HttpResponse<String>> setPushStatus(@Body Params params);

    //获取认证状态
    @POST("getUserIdentifyStatus")
    Observable<HttpResponse<OtherBean>> getUserIdentifyStatus(@Body Params params);

    //获取首页红点状态
    @POST("dot_status")
    Observable<HttpResponse<OtherBean>> getHomeRedPointStatus(@Body Params params);

    //获取个人中心资料，认证时的资料
    @GET("getUserBasicInfo")
    Observable<HttpResponse<UserBaseInfoBean>> getUserBasicInfo(@QueryMap Params params);

    //获取科室、职称、擅长等信息
    @GET("getDpAndTitles")
    Observable<HttpResponse<BaseConfigBean>> getDpAndTitles(@QueryMap Params params);

    //获取省市 医院列表
    @POST("getHospital")
    Observable<HttpResponse<List<HospitalBean>>> getHospital(@Body Params params);

    //认证信息提交1
    @POST("userIdentify")
    Observable<HttpResponse<String>> userIdentify(@QueryMap Params params);

    //认证信息提交1
    @POST("userIdentifyNext")
    Observable<HttpResponse<String>> userIdentifyNext(@Body Params params);

    //获取认证信息提交
    @POST("getUserIdentify")
    Observable<HttpResponse<AuthInfoBean>> getUserIdentify(@Body Params params);

    //个人公告和简介的提交
    @POST("add_userbasic")
    Observable<HttpResponse<String>> addUserbasic(@Body Params params);

    //钱包首页
    @POST("wallet_home")
    Observable<HttpResponse<WalletBean>> getWallet(@Body Params params);

    //我的银行卡
    @POST("userbanklist")
    Observable<HttpResponse<List<BankCardBean>>> userbanklist(@Body Params params);

    //银行列表
    @GET("getBank")
    Observable<HttpResponse<List<BankTypeBean>>> getBankType(@QueryMap Params params);

    //提现
    @POST("exmoney_submit")
    Observable<HttpResponse<String>> exmoneySubmit(@Body Params params);

    //添加银行卡
    @POST("useraddbank")
    Observable<HttpResponse<String>> useraddbank(@Body Params params);

    //解绑
    @POST("userdel_bank")
    Observable<HttpResponse<String>> deleteBankCard(@Body Params params);

    //交易详情
    @POST("deal_flow")
    Observable<HttpResponse<BasePageBean<DealDetailBean>>> getDealFlow(@Body Params params);

    //设置资费信息
    @POST("visitcost_set")
    Observable<HttpResponse<String>> setVisitPrice(@Body Params params);

    //用户获取常用语
    @POST("getuseful")
    Observable<HttpResponse<List<CommMessageBean>>> getuseful(@Body Params params);

    //添加常用语
    @POST("adduseful")
    Observable<HttpResponse<String>> adduseful(@Body Params params);

    //删除常用语
    @POST("deluseful")
    Observable<HttpResponse<String>> deluseful(@Body Params params);

    //患者列表
    @POST("getPatientlist")
    Observable<HttpResponse<List<PatientBean>>> getpatientlist(@Body Params params);

    //患者详情 和就诊人列表
    @POST("getpatientinfo")
    Observable<HttpResponse<PatientFamilyBean>> getpatientinfo(@Body Params params);

    //设置患者备注
    @POST("set_remarkname")
    Observable<HttpResponse<String>> setRemarkName(@Body Params params);

    //设置咨询价格
    @POST("set_advisoryfee")
    Observable<HttpResponse<String>> setAdvisoryfee(@Body Params params);

    //开方基础数据
    @POST("getsomeadvisory")
    Observable<HttpResponse<OPenPaperBaseBean>> getSomeadvisory(@Body Params params);

    //首页 banner
    @POST("get_homebanner")
    Observable<HttpResponse<List<BannerBean>>> getHomeBanner(@Body Params params);

    //发现 行业追踪，健康教育
    @POST("infolist")
    Observable<HttpResponse<BasePageBean<NewsInfoBean>>> getNewslist(@Body Params params);

    //发现 行业追踪，健康教育
    @POST("msgList")
    Observable<HttpResponse<BasePageBean<SystemMsgBean>>> getSystemMsglist(@Body Params params);

    //更新token
    @POST("updateToken")
    Observable<HttpResponse<OtherBean>> updateToken(@Body Params params);

    //拍照开方
    @POST("newphoto_extraction")
    Observable<HttpResponse<String>> photoExtractionNew(@QueryMap Params params);

    //在线开方
    @POST("line_extraction")
    Observable<HttpResponse<OnlinePaperBackBean>> lineExtraction(@QueryMap Params params);

    //accid 换 memb_no
    @POST("getmemb_no")
    Observable<HttpResponse<OtherBean>> getMembNo(@Body Params params);

    //toTalk 医生主动聊天
    @POST("toTalk")
    Observable<HttpResponse<String>> docToTalk(@Body Params params);

    //绑定信鸽token
    @POST("binding_token")
    Observable<HttpResponse<String>> bindXGToken(@Body Params params);

    //疾病名称搜索
    @POST("searchicd10")
    Observable<HttpResponse<List<BaseConfigBean.Skill>>> searchSkillName(@Body Params params);

    //药材名称搜索
    @POST("searchgetmedicine")
    Observable<HttpResponse<List<SearchDrugBean>>> searchDrugName(@Body Params params);

    //常用方列表
    @POST("oftenmed_list")
    Observable<HttpResponse<List<CommPaperBean>>> getOftenmedList(@Body Params params);

    //常用方列表
    @POST("oftenmed_list")
    Observable<HttpResponse<BasePageBean<CommPaperBean>>> getOftenmedList2(@Body Params params);

    //添加常用方列表
    @POST("often_medicinal")
    Observable<HttpResponse<String>> addOftenmed(@Body Params params);

    //经典处方【置顶、取消置顶】
    @POST("stickclassics")
    Observable<HttpResponse<String>> classicsPaperUp(@Body Params params);

    //删除常用方列表
    @POST("del_oftenmed")
    Observable<HttpResponse<String>> delOftenmed(@Body Params params);

    //常用方详情
    @POST("oftenmed_info")
    Observable<HttpResponse<List<CommPaperInfoBean>>> getOftenmedInfo(@Body Params params);

    //医生发送自定义消息记录
    @POST("addchatsendflow")
    Observable<HttpResponse<String>> addChatRecord(@Body Params params);

    //审核处方列表
    @POST("my_checkextra")
    Observable<HttpResponse<List<CheckPaperBean>>> getCheckPaperList(@Body Params params);

    //审核某个处方
    @POST("check_extra")
    Observable<HttpResponse<String>> checkPape(@Body Params params);

    //历史处方列表
    @POST("my_historyextra")
    Observable<HttpResponse<BasePageBean<CheckPaperBean>>> getPaperHistoryList(@Body Params params);

    //历史处方详情
    @POST("getPrescInfo")
    Observable<HttpResponse<PaperInfoBean>> getPaperInfo(@Body Params params);

    //历史就诊人处方列表
    @POST("getpresc_patient")
    Observable<HttpResponse<BasePageBean<JiuZhenHistoryBean>>> getJiuZhenHistoryList(@Body Params params);

    //添加患者（处方联系人）
    @POST("addprescpatient")
    Observable<HttpResponse<String>> addPatientJZR(@Body Params params);

    //坐诊信息
    @POST("visit_list")
    Observable<HttpResponse<List<JobScheduleBean>>> getJobScheduleList(@Body Params params);//坐诊信息

    @POST("visit_info")
    Observable<HttpResponse<List<JobSchedulePatientBean>>> getJobSchedulePatientList(@Body Params params);

    //检查App更新
    @POST("getVersion")
    Observable<HttpResponse<AppUpdateBean>> appUpdateCheck(@Body Params params);

    //下载Apk文件
    @GET
    @Streaming
    Observable<ResponseBody> downloadApk(@Url String url, @Header(HttpConfig.HTTP_HEADER_DOWNLOAD_APK) String identifier);


}
