package com.jht.doctor.ui.activity.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.jht.doctor.BuildConfig;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.ui.bean_jht.UploadImgBean;
import com.jht.doctor.ui.contact.AuthContact;
import com.jht.doctor.ui.presenter.present_jht.AuthPresenter;
import com.jht.doctor.utils.ActivityUtil;
import com.jht.doctor.utils.ImageUtil;
import com.jht.doctor.utils.LogUtil;
import com.jht.doctor.utils.ToastUtil;
import com.jht.doctor.utils.UriUtil;
import com.jht.doctor.widget.popupwindow.CameraPopupView;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.File;
import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * 认证 上传图片
 * AuthStep2Activity
 * Create at 2018/4/3 下午3:59 by mayakun
 */

public class AuthStep2Activity extends BaseActivity implements AuthContact.View {

    private final int REQUEST_CAMERA_CODE = 101;//拍照
    private final int REQUEST_ALBUM_CODE = 102;//相册
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.iv_img1)
    ImageView ivImg1;
    @BindView(R.id.iv_img2)
    ImageView ivImg2;
    @BindView(R.id.iv_img3)
    ImageView ivImg3;


    @Inject
    AuthPresenter mPresenter;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_auth_step2;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("认证")
                .setStatuBar(R.color.white)
                .setLeft(false)
//                .setRightText("认证", true, R.color.color_popup_btn)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                }).bind();
    }


    private String imgPath1, imgPath2, imgPath3;
    private int currImg;

    @OnClick({R.id.iv_img1, R.id.iv_img2, R.id.iv_img3, R.id.tv_next_step})
    public void tabOnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_img1:
            case R.id.iv_img2:
            case R.id.iv_img3:
                CameraPopupView cameraPopupView = new CameraPopupView(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //标识点击的哪一个
                        currImg = view.getId();
                        openCameraOrPhoto(v.getId() == R.id.llt_camera);
                    }
                });
                cameraPopupView.show(scrollView);
                break;
            case R.id.tv_next_step:
//                startActivity(new Intent(this, AuthStep3Activity.class));
                break;


        }
    }

    //公用
    public File cameraPath;

    //1:打开相机拍照 2:打开相册
    private void openCameraOrPhoto(boolean isCamera) {
        //根据路径拍照并存储照片
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(BuildConfig.DEBUG);
        rxPermissions
                .request(isCamera ? new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA} : new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            if (isCamera) {//打开照相机
                                File dir;
                                if (Environment.MEDIA_MOUNTED.equals(DocApplication.getAppComponent().dataRepo().storage().externalRootDirState())) {
                                    dir = DocApplication.getAppComponent().dataRepo().storage().externalPublicDir(Environment.DIRECTORY_PICTURES);
                                } else {
                                    dir = DocApplication.getAppComponent().dataRepo().storage().internalCustomDir(Environment.DIRECTORY_PICTURES);
                                }
                                if (!dir.exists()) {
                                    dir.mkdirs();
                                }
                                cameraPath = new File(dir, UriUtil.headerFileName(actContext()));
                                ActivityUtil.openCamera(AuthStep2Activity.this, cameraPath, REQUEST_CAMERA_CODE);
                            } else {//打开相册
                                ActivityUtil.openAlbum(AuthStep2Activity.this, "image/*", REQUEST_ALBUM_CODE);
                            }

                        } else {
                            ToastUtil.show(isCamera ? "请求照相机权限失败" : "请求相册权限失败");
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //裁剪
//                case REQUEST_CROP_CODE:
//                break;
                //拍照
                case REQUEST_CAMERA_CODE:
                    LogUtil.d("cameraPath=" + cameraPath.getAbsolutePath());
                    if (cameraPath.exists()) {
                        switch (currImg) {
                            case R.id.iv_img1:
                                ImageUtil.showImage(cameraPath.getAbsolutePath(), ivImg1);
                                break;
                            case R.id.iv_img2:
                                ImageUtil.showImage(cameraPath.getAbsolutePath(), ivImg2);
                                break;
                            case R.id.iv_img3:
                                ImageUtil.showImage(cameraPath.getAbsolutePath(), ivImg3);
                                break;
                        }

                        mPresenter.uploadImg(cameraPath.getAbsolutePath());
                    }
                    break;
                //相册
                case REQUEST_ALBUM_CODE:
                    Uri uri = data.getData();
                    String headerPath;
                    if (uri != null && !TextUtils.isEmpty(headerPath = UriUtil.getRealFilePath(this, uri))) {
                        LogUtil.d("headerPath=" + headerPath);
                        switch (currImg) {
                            case R.id.iv_img1:
                                ImageUtil.showImage(headerPath, ivImg1);
                                break;
                            case R.id.iv_img2:
                                ImageUtil.showImage(headerPath, ivImg2);
                                break;
                            case R.id.iv_img3:
                                ImageUtil.showImage(headerPath, ivImg3);
                                break;
                        }
                        mPresenter.uploadImg(headerPath);
                    }
                    break;
                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
    public void onSuccess(Message message) {
        switch (message.what) {
            case AuthPresenter.UPLOADIMF_OK://上传成功
                UploadImgBean uploadImgBean = (UploadImgBean) message.obj;
                switch (currImg) {
                    case R.id.iv_img1:
                        imgPath1 = uploadImgBean.url;
                        break;
                    case R.id.iv_img2:
                        imgPath2 = uploadImgBean.url;
                        break;
                    case R.id.iv_img3:
                        imgPath3 = uploadImgBean.url;
                        break;
                }
                ToastUtil.showShort("上传成功");
                break;
            case AuthPresenter.UPLOADIMF_ERROR://上传失败
                switch (currImg) {
                    case R.id.iv_img1:
                        imgPath1 = "";
                        ivImg1.setImageResource(0);
                        break;
                    case R.id.iv_img2:
                        imgPath2 = "";
                        break;
                    case R.id.iv_img3:
                        imgPath3 = "";
                        break;
                }
                ToastUtil.showShort("上传失败，请重新选择");
                break;
            case AuthPresenter.USERIDENTIFY_OK://认证信息提交
                startActivity(new Intent(this, AuthStep2Activity.class));
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}
