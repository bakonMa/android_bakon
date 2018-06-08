package com.junhetang.doctor.ui.nimview;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.H5JsonBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.widget.ProgressWebView;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
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
public class CheckPaperH5Activity extends BaseActivity implements ProgressWebView.ErrorCallback, OpenPaperContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.iv_error_image)
    ImageView ivErrorImage;
    @BindView(R.id.tv_error_text)
    TextView tvErrorText;
    @BindView(R.id.rlt_error)
    RelativeLayout rltError;
    @BindView(R.id.llt_checkpaper)
    LinearLayout lltCheckpaper;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.llt_web)
    LinearLayout lltWeb;
    @BindView(R.id.wb_webview)
    ProgressWebView wbWebview;

    @Inject
    OpenPaperPresenter mPresenter;

    //标题，url
    private String titleStr, urlStr, webType;
    private boolean hasTopBar;//带头部导航栏
    private int checkPaperID = -1;//审核处方 id
    private ToolbarBuilder toolbarBuilder;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_checkpaperh5;
    }

    @Override
    protected void initView() {
        //是否包含toolbar
        hasTopBar = getIntent().getBooleanExtra("hasTopBar", true);
        titleStr = getIntent().getStringExtra("title");
        urlStr = getIntent().getStringExtra("url");
        webType = getIntent().getStringExtra("webType");
        checkPaperID = getIntent().getIntExtra("checkid", -1);

        LogUtil.d(urlStr);
        wbWebview.setErrorCallback(this);
        //是否有导航头
        if (hasTopBar) {
            initToolbar();
        } else {
            idToolbar.setVisibility(View.GONE);
            toolbarBuilder = ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                    .setStatuBar(R.color.white)
                    .bind();
        }
        wbWebview.addJavascriptInterface(new JSInterface(), "Android");
        wbWebview.loadUrl(urlStr);
    }

    @OnClick({R.id.btn_error_reload, R.id.tv_checkpaper_commite})
    void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.btn_error_reload:
                wbWebview.reload();
                break;
            case R.id.tv_checkpaper_commite://审核处方 提交
                mPresenter.checkPaper(checkPaperID,
                        rbYes.isChecked() ? 1 : -1,
                        etRemark.getText().toString().trim());
                break;
        }
        wbWebview.reload();
    }

    //共同头部处理
    private void initToolbar() {
        toolbarBuilder = ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
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
    public void onError(int type, String webTitle) {
        switch (type) {
            case 0://正常页面
                lltWeb.setVisibility(View.VISIBLE);
                rltError.setVisibility(View.GONE);
                //title
                if (hasTopBar && toolbarBuilder != null) {
                    toolbarBuilder.setTitle(TextUtils.isEmpty(titleStr) ? webTitle : titleStr);
                }
                lltWeb.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //审核处方 下部布局才显示，其他时候隐藏
                        lltCheckpaper.setVisibility(webType.equals(FORM_TYPE.H5_CHECKPAPER) ? View.VISIBLE : View.GONE);
                    }
                }, 1000);
                break;
            case 1://无网络
                lltWeb.setVisibility(View.GONE);
                rltError.setVisibility(View.VISIBLE);
                ivErrorImage.setImageResource(R.drawable.wangluoyichang);
                tvErrorText.setText("啊哦～网络异常");
                break;
            case 2://其他-加载失败
                lltWeb.setVisibility(View.GONE);
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
                case "web_checkupsinfo"://随诊单详情 医生
                    ToastUtil.showShort("给他开方");
                    finish();
                    break;
            }
        }
    }


    //进来的类型
    public interface FORM_TYPE {
        String H5_CHECKPAPER = "check_extraction";//审核处方
    }


}
