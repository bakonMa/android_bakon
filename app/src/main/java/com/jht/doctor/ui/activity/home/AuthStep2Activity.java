package com.jht.doctor.ui.activity.home;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ScrollView;

import com.jht.doctor.BuildConfig;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.utils.ActivityUtil;
import com.jht.doctor.utils.UriUtil;
import com.jht.doctor.widget.popupwindow.CommonBottomPopupView;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.tbruyelle.rxpermissions.RxPermissions;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.Arrays;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

public class AuthStep2Activity extends BaseActivity {

    private final int REQUEST_CAMERA_CODE = 101;//拍照
    private final int REQUEST_ALBUM_CODE = 102;//相册
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    private CommonBottomPopupView popupView;


    @Override
    protected int provideRootLayout() {
        return R.layout.activity_auth_step2_jht;
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


    private File cameraPath1, cameraPath2, cameraPath3;

    @OnClick({R.id.iv_img, R.id.tv_next_step})
    public void tabOnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_img:
                popupView = new CommonBottomPopupView(this,
                        Arrays.asList(new String[]{"拍照", "从手机相册选择"}), new CommonBottomPopupView.CommOnClickListener() {
                    @Override
                    public void OnItemOnClick(Object o) {
                        if (o == null) {
                            return;
                        }
                        if (o.toString().equals("拍照")) {
                            openCamera(cameraPath1);
                        } else {

                        }
                    }
                });
                popupView.show(scrollView);
                break;
            case R.id.tv_next_step:
                startActivity(new Intent(this, AuthStep3Activity.class));
                break;


        }
    }


    private void openCamera(File file) {
        //根据路径拍照并存储照片
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(BuildConfig.DEBUG);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            File dir;
                            if (Environment.MEDIA_MOUNTED.equals(DocApplication.getAppComponent().dataRepo().storage().externalRootDirState())) {
                                dir = DocApplication.getAppComponent().dataRepo().storage().externalPublicDir(Environment.DIRECTORY_PICTURES);
                            } else {
                                dir = DocApplication.getAppComponent().dataRepo().storage().internalCustomDir(Environment.DIRECTORY_PICTURES);
                            }
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }
                            cameraPath1 = new File(dir, UriUtil.headerFileName(actContext()));
                            ActivityUtil.openCamera(AuthStep2Activity.this, cameraPath1, REQUEST_CAMERA_CODE);
                        } else {
                            DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("请求权限失败");
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

    ////从相册选取图片
    private void openAlbum() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(BuildConfig.DEBUG);
        rxPermissions
                .request(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA)
                .subscribe(new Observer<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            ActivityUtil.openAlbum(AuthStep2Activity.this, "image/jpeg", REQUEST_ALBUM_CODE);
                        } else {
                            DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast("请求权限失败");
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
                //拍照
                case REQUEST_CAMERA_CODE:
                    if (cameraPath1.exists()) {
//                        headerImageUpload(cameraPath);
                    }
                    break;

                //相册
                case REQUEST_ALBUM_CODE:
                    Uri uri = data.getData();
                    String headerPath;
                    if (uri != null && !TextUtils.isEmpty(headerPath = UriUtil.getRealFilePath(this, uri))) {
//                        headerImageUpload(new File(headerPath));
                    }
                    break;


                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    @Override
    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected void setupActivityComponent() {

    }


}
