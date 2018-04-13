package com.renxin.doctor.activity.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnKeyListener;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.utils.UIUtils;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Observer;
import rx.Subscription;
import rx.functions.Action0;

/**
 * mayakun 2017/11/17
 * 基本的webview设置统一处理
 * 1：带Progress的WebView
 * 2：添加监听错误的回调
 * 3：内部多级url历史处理
 * 4：加载超时需要完善（暂不要使用）
 */
public class ProgressWebView extends WebView implements OnKeyListener {

    public static int ERROR_NONETWORK = 1;
    public static int ERROR_LOADFAIL = 2;

    private Context context;
    private int TIMEOUT = 30;//超时时间30s
    //webview设置
    private WebSettings settings;
    private ErrorCallback errorCallback;
    private ProgressBar mProgressbar;

    public ProgressWebView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        //加载进度条
        mProgressbar = new ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, UIUtils.dp2px(context, 2));
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
//        mProgressbar.setLayoutParams(layoutParams);

        mProgressbar.setProgressDrawable(getResources().getDrawable(R.drawable.progress_bg_webview));
        addView(mProgressbar, layoutParams);

        settings = this.getSettings();
        //优化，图片后加载
        settings.setLoadsImagesAutomatically(Build.VERSION.SDK_INT >= 19 ? true : false);
        //在安卓5.0之后，默认不允许加载http与https混合内容，需要设置webview允许其加载混合网络协议内容
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //缩放
        settings.setSupportZoom(false);
        //设置适应Html5的一些方法
        settings.setDomStorageEnabled(true);
        //启用JavaScript
        settings.setJavaScriptEnabled(true);
        //屏幕自适应
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);
        //是否使用缓存
        settings.setAppCacheEnabled(true);
        //优先缓存
        settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        //不要缓存
//        settings.setCacheMode(WebSettings.LOAD_NO_CACHE);

        //UserAgent添加appName,appVersion,channel,device_token
//		StringBuilder stringBuilder = new StringBuilder(settings.getUserAgentString());
//		stringBuilder.append(" isaplus/Android")
//                     .append(" token/" + CommUtil.getCurrentUser().plat_token);
//		settings.setUserAgentString(stringBuilder.toString());
//        LogUtil.d("UserAgent" + settings.getUserAgentString());

        setOnKeyListener(this);
        setWebViewClient(new MyWebViewClient());
        setWebChromeClient(new MyWebChromeClient());
    }

    public void loadUrlWithHeader(String url) {
        HashMap<String, String> map = new HashMap<String, String>();
//		map.put("APPVERSION", ICommonUtil.getAppVersionName(context));
//		map.put("CHANNEL", SharedPerUtil.getConfigInfo(context, Contants.SharedConfigKey.channel));
//		map.put("OS", "Android");
//		map.put("uid", SharedPerUtil.getUserInfo(context, Contants.SharedUserKey.uid));
        //添加header数据
        loadUrl(url, map);
        //开始计时
//        startTime();
    }


    private int isError = 0;

    private class MyWebViewClient extends WebViewClient {
        //内部打开连接
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                loadUrlWithHeader(request.getUrl().toString());
            } else {
                loadUrlWithHeader(request.toString());
            }
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            isError = 0;
        }

        //加载完成
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (subscription != null) {
                subscription.unsubscribe();
            }
            //加载失败
            if (errorCallback != null) {
                errorCallback.onError(isError);
            }
            ProgressWebView.this.setEnabled(true);
            //页面finish后再发起图片加载
            if (!settings.getLoadsImagesAutomatically()) {
                settings.setLoadsImagesAutomatically(true);
            }
        }

        //旧版本，会在新版本中也可能被调用，所以加上一个判断，防止重复显示
        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                return;
            }
            //-2没有网络
            isError = (errorCode == -2 ? ERROR_NONETWORK : ERROR_LOADFAIL);
        }

        // 新版本，只会在Android6及以上调用
        @TargetApi(Build.VERSION_CODES.M)
        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
            super.onReceivedError(view, request, error);
            //-2没有网络
            isError = (error.getErrorCode() == -2 ? ERROR_NONETWORK : ERROR_LOADFAIL);
        }

        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            if (errorResponse.getStatusCode() != 200) {
                isError = ERROR_LOADFAIL;
            }
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            handler.proceed();
        }
    }

    public class MyWebChromeClient extends android.webkit.WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressbar.setVisibility(View.GONE);
            } else {
                if (mProgressbar.getVisibility() == View.GONE) {
                    mProgressbar.setVisibility(View.VISIBLE);
                }
                mProgressbar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }

    /**
     * 加载超时计数
     */
    private Subscription subscription;

    //需要调试完善，暂时不用（加载超时）
    private void startTime() {
        subscription = null;
        subscription = Observable.timer(TIMEOUT, TimeUnit.SECONDS)
//                .observeOn(Schedulers.newThread())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        ProgressWebView.this.setEnabled(false);//不可点击
                    }
                })
//                .observeOn(AndroidSchedulers.mainThread())//操作UI主要在UI线程
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onCompleted() {
                        //超时
                        if (errorCallback != null) {
                            errorCallback.onError(ERROR_LOADFAIL);
                        }
                        subscription.unsubscribe();
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                        subscription.unsubscribe();
                    }

                    @Override
                    public void onNext(Long aLong) { //接受到一条就是会操作一次UI
                    }
                });
    }

    //监听webview KeyEvent
    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_UP) {
            // 表示按返回键，返回历史
            if (keyCode == KeyEvent.KEYCODE_BACK && this.canGoBack()) {
                this.goBack();
                return true;
            }
        }
        return false;
    }

    public void setErrorCallback(ErrorCallback errorCallback) {
        this.errorCallback = errorCallback;
    }

    //错误的监听回调
    public interface ErrorCallback {
        void onError(int type);
    }

}