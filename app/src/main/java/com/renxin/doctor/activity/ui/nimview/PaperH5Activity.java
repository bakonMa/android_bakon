package com.renxin.doctor.activity.ui.nimview;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.config.EventConfig;
import com.renxin.doctor.activity.data.eventbus.Event;
import com.renxin.doctor.activity.data.eventbus.EventBusUtil;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean_jht.H5JsonBean;
import com.renxin.doctor.activity.utils.LogUtil;
import com.renxin.doctor.activity.utils.ShareSDKUtils;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.widget.ProgressWebView;
import com.renxin.doctor.activity.widget.popupwindow.SharePopupWindow;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
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
public class PaperH5Activity extends BaseActivity implements ProgressWebView.ErrorCallback {

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

    //标题，url
    private String titleStr, urlStr;
    private int h5Type;//0：默认带头部导航栏 1：不带

    //带有返回的startActivityForResult-仅限nim中使用
    public static void startResultActivity(Context context, int requestCode, int type, String title, String url) {
        Intent intent = new Intent(context, PaperH5Activity.class);
        intent.putExtra("type", type);
        intent.putExtra("title", title);
        intent.putExtra("url", url);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_h5paper;
    }

    @Override
    protected void initView() {
        h5Type = getIntent().getIntExtra("type", 0);
        titleStr = getIntent().getStringExtra("title");
        urlStr = getIntent().getStringExtra("url");
        LogUtil.d(urlStr);
        wbWebview.setErrorCallback(this);
        if (h5Type == 0) {
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_H5_BOOKS_SHARE://书籍分享
                H5JsonBean bean = (H5JsonBean) event.getData();
                SharePopupWindow sharePopupWindow = new SharePopupWindow(actContext(), new SharePopupWindow.ShareOnClickListener() {
                    @Override
                    public void onItemClick(SHARE_MEDIA shareType) {
                        ShareSDKUtils.share(PaperH5Activity.this, shareType,
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
//            case 1://无网络
//                wbWebview.setVisibility(View.GONE);
//                rltError.setVisibility(View.VISIBLE);
//                ivErrorImage.setImageResource(R.drawable.wangluoyichang);
//                tvErrorText.setText("啊哦～网络异常");
//                break;
//            case 2://其他-加载失败
//                wbWebview.setVisibility(View.GONE);
//                rltError.setVisibility(View.VISIBLE);
//                ivErrorImage.setImageResource(R.drawable.wangyechucuo);
//                tvErrorText.setText("啊哦～页面出错了");
//                break;
        }
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
                    Intent intent = new Intent();
                    intent.putExtra("typeID", bean.id);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case "inquiry"://问诊单详情医生【给他开方】
                    ToastUtil.showShort("给他开方");
                    finish();
                    break;
                case "web_checkups"://医生【发送随诊单】
                    setResult(RESULT_OK, new Intent());
                    finish();
                    break;
                case "web_checkupsinfo"://随诊单详情 医生
                    ToastUtil.showShort("给他开方");
                    finish();
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

}
