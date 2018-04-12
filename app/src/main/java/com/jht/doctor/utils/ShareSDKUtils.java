package com.jht.doctor.utils;

import android.app.Activity;
import android.text.TextUtils;

import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.widget.dialog.CommonDialog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;

/**
 * Created by bakon on 2018/4/12.
 */

public class ShareSDKUtils {

    //默认图片 使用logo
    public static void shareURL(Activity context, SHARE_MEDIA shareType,
                                       String shareUrl, String shareTitle, String shareText,
                                       ShareCallBack shareCallBack) {
        share(context, shareType, "", shareUrl, shareTitle, shareText, shareCallBack);
    }

    //默认图片 使用logo，分享APP链接
    public static void shareApp(Activity context, SHARE_MEDIA shareType) {
        share(context,
                shareType,
                "",
                Constant.APP_SHARE_URL,
                UIUtils.getString(R.string.app_name),
                UIUtils.getString(R.string.str_share_app),
                null);
    }

    public static void share(Activity context, SHARE_MEDIA shareType,
                             String imgUrl, String shareUrl, String shareTitle, String shareText,
                             ShareCallBack shareCallBack) {
        //判断应用是否已经安装
        if (!DocApplication.umShareAPI.isInstall(context, shareType)) {
            CommonDialog commonDialog = new CommonDialog(context, "请先安装应用后再分享");
            commonDialog.show();
            return;
        }

        UMWeb web = new UMWeb(shareUrl);
        web.setTitle(TextUtils.isEmpty(shareTitle) ? UIUtils.getString(R.string.app_name) : shareTitle);//标题
        if (TextUtils.isEmpty(imgUrl)) {
            web.setThumb(new UMImage(context, R.mipmap.logo_round));  //缩略图
        } else {
            web.setThumb(new UMImage(context, imgUrl));  //缩略图
        }
        web.setDescription(shareText);//描述

        new ShareAction(context)
                .setPlatform(shareType)
                .withMedia(web)
                .setCallback(new UMShareListener() {
                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }
                    @Override
                    public void onResult(SHARE_MEDIA share_media) {
                        switch (share_media) {
                            case WEIXIN:
                                ToastUtil.showShort("分享微信好友成功");
                                break;
                            case WEIXIN_CIRCLE:
                                ToastUtil.showShort("分享微信朋友圈成功");
                                break;
                            case QQ:
                                ToastUtil.showShort("分享QQ成功");
                                break;
                            default:
                                ToastUtil.showShort("分享成功");
                                break;
                        }
                        if (shareCallBack != null) {
                            shareCallBack.shareOK();
                        }
                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                        switch (share_media) {
                            case WEIXIN:
                                ToastUtil.showShort("分享微信好友失败");
                                break;
                            case WEIXIN_CIRCLE:
                                ToastUtil.showShort("分享微信朋友圈失败");
                                break;
                            case QQ:
                                ToastUtil.showShort("分享QQ失败");
                                break;
                            default:
                                ToastUtil.showShort("分享失败");
                                break;
                        }
                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {
                        ToastUtil.showShort("取消分享");
                    }
                })
                .share();
    }


    interface ShareCallBack {
        void shareOK();
    }


}
