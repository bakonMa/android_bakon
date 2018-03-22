package com.jht.doctor.ui.activity.mine.webview;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.contact.WebViewContact;
import com.jht.doctor.ui.presenter.WebviewPresenter;
import com.jht.doctor.utils.LogUtil;
import com.jht.doctor.widget.ProgressWebView;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * mayakun 2017/11/17
 * 共同webview画面
 */
public class WebViewActivity extends BaseAppCompatActivity implements ProgressWebView.ErrorCallback, WebViewContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.iv_error_image)
    ImageView ivErrorImage;
    @BindView(R.id.tv_error_text)
    TextView tvErrorText;
    @BindView(R.id.btn_error_reload)
    Button btnErrorReload;
    @BindView(R.id.rlt_error)
    RelativeLayout rltError;
    @BindView(R.id.wb_webview)
    ProgressWebView wbWebview;
    @Inject
    WebviewPresenter mPresenter;

    //标题，url
    private String orderNo, titleStr, urlStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        ButterKnife.bind(this);

        orderNo = getIntent().getStringExtra("orderNo");
        titleStr = getIntent().getStringExtra("title");
        urlStr = getIntent().getStringExtra("url");

        wbWebview.setErrorCallback(this);
        //设置title
        initToolbar();

        if (TextUtils.isEmpty(orderNo)) {
            //加载url
            wbWebview.loadUrl(urlStr);
            LogUtil.d("url=", urlStr);
        } else {
            mPresenter.getCreditUrl(orderNo);
        }
    }

    @OnClick(R.id.btn_error_reload)
    void relodBtn() {
        wbWebview.reload();
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle(TextUtils.isEmpty(titleStr) ? "" : titleStr)
                .setLeft(false)
                .isShowClose(true)//是否显示close
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

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .applicationComponent(DocApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
    public void onError(String errorCode, String errorMsg) {
        if (!TextUtils.isEmpty(errorMsg)) {
            DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
        }
        finish();
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null || message.obj == null) {
            return;
        }
        switch (message.what) {
            case WebviewPresenter.GET_CREDIT_URL://获取征信url后跳转
                urlStr = message.obj.toString();
                LogUtil.d(urlStr);
                wbWebview.loadUrl(urlStr);
                break;
        }
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }
}
