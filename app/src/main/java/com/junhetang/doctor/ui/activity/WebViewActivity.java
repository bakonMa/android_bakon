package com.junhetang.doctor.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.junhetang.doctor.R;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean_jht.H5JsonBean;
import com.junhetang.doctor.utils.FileUtil;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.ShareSDKUtils;
import com.junhetang.doctor.widget.ProgressWebView;
import com.junhetang.doctor.widget.popupwindow.MenuPopupView;
import com.junhetang.doctor.widget.popupwindow.SharePopupWindow;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * mayakun 2017/11/17
 * 共同webview画面
 */
public class WebViewActivity extends BaseActivity implements ProgressWebView.ErrorCallback {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_img_right)
    ImageView ivImgRight;
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

    //标题，url
    private String titleStr, urlStr, webType;
    private boolean hasTopBar;//默认带头部导航
    private SharePopupWindow sharePopupWindow;
    private ToolbarBuilder toolbarBuilder;

    //hasTopBar
    public static void startAct(Context context, boolean hasTopBar, String webType, String titel, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("hasTopBar", hasTopBar);//是否包含toolbar
        intent.putExtra("webType", webType);//URL的类型，自己定义
        intent.putExtra("title", titel);
        intent.putExtra("url", url);
        context.startActivity(intent);
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

        LogUtil.d(urlStr);
//        urlStr = "http://www.123qwesfasdf.com/";
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
        //js and java 交互
        wbWebview.addJavascriptInterface(new JSWebInterface(), "Android");
        wbWebview.loadUrl(urlStr);
    }

    @OnClick(R.id.btn_error_reload)
    void relodBtn() {
        wbWebview.reload();
    }

    //共同头部处理
    private void initToolbar() {
        toolbarBuilder = ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle(TextUtils.isEmpty(titleStr) ? wbWebview.getTitle() : titleStr)
                .setLeft(false)
                //.isShowClose(true)//是否显示close
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

                    @Override
                    public void rightClick() {
                        switch (webType) {
                            case WEB_TYPE.WEB_TYPE_MYCARD://我的卡片
                                MenuPopupView popupView = new MenuPopupView(actContext(), new MenuPopupView.OnClickListener() {
                                    @Override
                                    public void onClicked(View view) {
                                        switch (view.getId()) {
                                            case R.id.tv_share:
                                                //分享 获取share json数据
                                                //wbWebview.loadUrl("javascript:(function(){window.Android.jsEvent(document.getElementById('shareValue').value);})()");
                                                wbWebview.loadUrl("javascript:share_card()");
                                                break;
                                            case R.id.tv_save:
                                                //webview 生成图片保存相册
                                                FileUtil.saveWebviewToImage(actContext(), wbWebview);
                                                break;
                                        }
                                    }
                                });
                                popupView.show(ivImgRight);
                                break;
                        }

                    }
                }).bind();

        //是否toolbar 显示其他控件
        switch (webType) {
            case WEB_TYPE.WEB_TYPE_MYCARD://我的卡片 右边显示分享
                toolbarBuilder.setRightImg(R.drawable.icon_threepoint, true);
                break;
        }

    }

    @Override
    protected void setupActivityComponent() {

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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_USERINFO://我的信息卡片 分享
            case EventConfig.EVENT_KEY_H5_BOOKS_SHARE://书籍分享
                H5JsonBean bean = (H5JsonBean) event.getData();
                if (sharePopupWindow != null && sharePopupWindow.isShowing()) {
                    return;
                }
                sharePopupWindow = new SharePopupWindow(actContext(), new SharePopupWindow.ShareOnClickListener() {
                    @Override
                    public void onItemClick(SHARE_MEDIA shareType) {
                        ShareSDKUtils.share(WebViewActivity.this, shareType,
                                bean.img_url, bean.link, bean.title, bean.desc, null);
                    }
                });
                sharePopupWindow.showAtLocation(wbWebview, Gravity.BOTTOM, 0, 0);
                break;
        }
    }
    
    @Override
    protected boolean useEventBus() {
        return true;
    }

    /**
     * JSWebInterface webview和H5交互使用
     * 2018年05月02日15:34:46
     */
    public class JSWebInterface {
        //JS需要调用的方法
        @JavascriptInterface
        public void jsEvent(String json) {//使用统一的方法名
            LogUtil.d("jsEvent=" + json);
            H5JsonBean bean = new Gson().fromJson(json, H5JsonBean.class);
            if (bean == null) {
                return;
            }
            switch (bean.jstype) {
                case "share_card"://点击分享
                    //线程问题
                    EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_USERINFO, bean));
                    break;
                case "close"://发现 书籍
                    finish();
                    break;
                case "share_books"://发现 分享
                    EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_H5_BOOKS_SHARE, bean));
                    break;
            }
        }
    }

    //进来的类型
    public interface WEB_TYPE {
        String WEB_TYPE_MYCARD = "web_type_mycard";//添加患者 我的卡片
        String WEB_TYPE_MYINFO = "web_type_myinfo";//个人信息预览
        String WEB_TYPE_BOOKS = "books";//发现 书籍
        String WEB_TYPE_BAIKE = "baike";//发现 百科
        String WEB_TYPE_FUNCTION_INTRODUCTION = "Introduction";//功能介绍
        String WEB_TYPE_PRODUCT_INFO = "productinfo";//产品说明
        String WEB_TYPE_AGREEMENT = "agreement";//用户协议
        String WEB_TYPE_NEWS = "news";//发现 行业追踪，健康教育
        String WEB_TYPE_SYSTEMMSG = "system_msg";//系统消息
        String WEB_TYPE_BANNER = "banner";//首页 banner
    }


}
