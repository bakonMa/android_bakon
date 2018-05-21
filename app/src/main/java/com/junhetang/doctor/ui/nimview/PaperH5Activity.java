package com.junhetang.doctor.ui.nimview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.nim.NimManager;
import com.junhetang.doctor.nim.NimU;
import com.junhetang.doctor.nim.message.extension.AskPaperAttachment;
import com.junhetang.doctor.nim.message.extension.FollowPaperAttachment;
import com.junhetang.doctor.ui.activity.patient.PatientListActivity;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean_jht.H5JsonBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.Constant;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.widget.ProgressWebView;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * mayakun 2017/11/17
 * 共同webview画面
 */
public class PaperH5Activity extends BaseActivity implements ProgressWebView.ErrorCallback, OpenPaperContact.View {
    private static final int REQUEST_CODE_CHOOSEPATIENT_ASKPAPER = 2040;//问诊单 选择患者
    private static final int REQUEST_CODE_CHOOSEPATIENT_FOLLOWPAPER = 2041;//随诊单 选择患者

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.iv_error_image)
    ImageView ivErrorImage;
    @BindView(R.id.tv_error_text)
    TextView tvErrorText;
    @BindView(R.id.rlt_error)
    RelativeLayout rltError;
    @BindView(R.id.wb_webview)
    ProgressWebView wbWebview;

    @Inject
    OpenPaperPresenter mPresenter;

    //标题，url
    private String titleStr, urlStr, webType;
    private String paccid;
    private boolean hasTopBar;//带头部导航栏
    private int formParent = 0;//是否来自聊天(0 默认不是 1：聊天)
    private String askPapertypeID = "";//男性，女性，儿童  问诊单需要

    //带有返回的startActivityForResult-仅限nim中使用
    //paccid 患者accid
    public static void startResultActivity(Context context, int requestCode,
                                           boolean hasTopBar, String webType,
                                           String title, String url, String paccid) {
        Intent intent = new Intent(context, PaperH5Activity.class);
        intent.putExtra("hasTopBar", hasTopBar);//是否包含toolbar
        intent.putExtra("webType", webType);//URL的类型，自己定义
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        intent.putExtra("paccid", paccid);//聊天才有
        intent.putExtra("formParent", 1);//来自聊天
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_webview;
    }

    @Override
    protected void initView() {
        //是否包含toolbar
        hasTopBar = getIntent().getBooleanExtra("hasTopBar", true);
        titleStr = getIntent().getStringExtra("title");
        urlStr = getIntent().getStringExtra("url");
        webType = getIntent().getStringExtra("webType");
        paccid = getIntent().getStringExtra("paccid");//聊天才有
        formParent = getIntent().getIntExtra("formParent", 0);

        LogUtil.d(urlStr);
        wbWebview.setErrorCallback(this);
        //是否有导航头
        if (hasTopBar) {
            initToolbar();
        } else {
            idToolbar.setVisibility(View.GONE);
            ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                    .setStatuBar(R.color.white)
                    .bind();
        }
        wbWebview.addJavascriptInterface(new JSInterface(), "Android");
        wbWebview.loadUrl(urlStr);
    }

    @OnClick({R.id.btn_error_reload})
    void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_error_reload:
                wbWebview.reload();
                break;
        }
        wbWebview.reload();
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle(TextUtils.isEmpty(titleStr) ? wbWebview.getTitle() : titleStr)
                .setLeft(false)
                .isShowClose(false)//是否显示close
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void closeClick() {//关闭
                        super.closeClick();
                        finish();
                    }

                    @Override
                    public void leftClick() {//返回
                        super.leftClick();
                        if (wbWebview.canGoBack()) {
                            wbWebview.goBack();
                        } else {
                            finish();
                        }
                    }
                }).bind();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
//            case EventConfig.EVENT_KEY_H5_BOOKS_SHARE://书籍分享
//                break;
        }
    }


    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        wbWebview.removeJavascriptInterface("Android");
        wbWebview.clearHistory();
        wbWebview.removeAllViews();
        wbWebview.destroy();
    }

    @Override
    public void onError(int type) {
        switch (type) {
            case 0://正常页面
                wbWebview.setVisibility(View.VISIBLE);
                rltError.setVisibility(View.GONE);
                break;
            case 1://无网络
                wbWebview.setVisibility(View.GONE);
                rltError.setVisibility(View.VISIBLE);
                ivErrorImage.setImageResource(R.drawable.wangluoyichang);
                tvErrorText.setText("啊哦～网络异常");
                break;
            case 2://其他-加载失败
                wbWebview.setVisibility(View.GONE);
                rltError.setVisibility(View.VISIBLE);
                ivErrorImage.setImageResource(R.drawable.wangyechucuo);
                tvErrorText.setText("啊哦～页面出错了");
                break;
        }
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case OpenPaperPresenter.CHECKPAPER_OK://审核处方 提交成功
                ToastUtil.showShort("审方提交成功");
                EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_CHECKPAPER_OK));
                finish();
                break;
            case OpenPaperPresenter.ADD_CHAT_RECORD_OK://提交发送记录成功
                LogUtil.d("提交发送记录ok");
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {

    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }

    /**
     * JSInterface webview和H5交互使用
     * Create at 2018/4/18 下午2:35 by mayakun
     */
    public class JSInterface {
        //JS需要调用的方法
        @JavascriptInterface
        public void jsEvent(String json) {//使用同一的发方法名
            LogUtil.d("jsEvent=" + json);
            H5JsonBean bean = new Gson().fromJson(json, H5JsonBean.class);
            if (bean == null) {
                return;
            }
            switch (bean.jstype) {
                case "edit_inquiry"://医生【发送问诊单】
                    if (formParent == 1) {//聊天列表
                        //发送自定义消息记录
                        mPresenter.addChatRecord(NimU.getNimAccount(),
                                paccid,
                                Constant.CHAT_RECORD_TYPE_1,
                                formParent);
                        //返回聊天
                        Intent intent = new Intent();
                        intent.putExtra("typeID", bean.id);
                        setResult(RESULT_OK, intent);
                        finish();
                    } else {//首页进入，选择患者发送
                        askPapertypeID = bean.id;
                        Intent intentChoose = new Intent(PaperH5Activity.this, PatientListActivity.class);
                        startActivityForResult(intentChoose, REQUEST_CODE_CHOOSEPATIENT_ASKPAPER);
                    }
                    break;
                case "inquiry"://问诊单详情医生【给他开方】
//                    ToastUtil.showShort("给他开方");
                    finish();
                    break;
                case "web_checkups"://医生【发送随诊单】
                    if (formParent == 1) {//聊天列表
                        //发送自定义消息记录
                        mPresenter.addChatRecord(NimU.getNimAccount(),
                                paccid,
                                Constant.CHAT_RECORD_TYPE_2,
                                formParent);
                        //返回聊天
                        setResult(RESULT_OK, new Intent());
                        finish();
                    } else {//首页进入，选择患者发送
                        Intent intentChoose = new Intent(PaperH5Activity.this, PatientListActivity.class);
                        startActivityForResult(intentChoose, REQUEST_CODE_CHOOSEPATIENT_FOLLOWPAPER);
                    }
                    break;
                case "web_checkupsinfo"://随诊单详情 医生
//                    ToastUtil.showShort("给他开方");
                    finish();
                    break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_CODE_CHOOSEPATIENT_ASKPAPER://问诊单 选择患者
                if (data == null) {
                    return;
                }
                //发送问诊单
                AskPaperAttachment customAttachment = new AskPaperAttachment(TextUtils.isEmpty(askPapertypeID) ? "0" : askPapertypeID);
                IMMessage askPaperMessage = MessageBuilder.createCustomMessage(
                        data.getStringExtra("accid"),//患者accid
                        SessionTypeEnum.P2P,
                        UIUtils.getString(R.string.input_panel_askpaper),
                        customAttachment);
                NimManager.sengChatMsg(askPaperMessage, true, null);
                //发送自定义消息记录
                mPresenter.addChatRecord(NimU.getNimAccount(),
                        data.getStringExtra("accid"),
                        Constant.CHAT_RECORD_TYPE_1,
                        formParent);
                ToastUtil.showShort("问诊单已发送");
                finish();
                break;
            case REQUEST_CODE_CHOOSEPATIENT_FOLLOWPAPER://随诊单 选择患者
                if (data == null) {
                    return;
                }
                //发送随诊单
                FollowPaperAttachment attachment = new FollowPaperAttachment();
                IMMessage message = MessageBuilder.createCustomMessage(
                        data.getStringExtra("accid"),
                        SessionTypeEnum.P2P,
                        UIUtils.getString(R.string.input_panel_followpaper),
                        attachment);
                NimManager.sengChatMsg(message, true, null);
                //发送自定义消息记录
                mPresenter.addChatRecord(NimU.getNimAccount(),
                        data.getStringExtra("accid"),
                        Constant.CHAT_RECORD_TYPE_2,
                        formParent);
                ToastUtil.showShort("随诊单已发送");
                finish();
                break;
        }
    }

    //进来的类型
    public interface FORM_TYPE {
        String H5_ASKPAPER = "web_edit_inquiry";//问诊单
        String H5_FOLLOWPAPER = "web_checkups";//随诊单
        String H5_PAPER_DETAIL = "extraction_info";//处方详情
    }


}
