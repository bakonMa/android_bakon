package com.junhetang.doctor.ui.activity.mine;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.manager.OSSManager;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.contact.AuthContact;
import com.junhetang.doctor.ui.presenter.AuthPresenter;
import com.junhetang.doctor.utils.ActivityUtil;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.UmengKey;
import com.junhetang.doctor.utils.UriUtil;
import com.junhetang.doctor.widget.EditTextlayout;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.dialog.LoadingDialog;
import com.junhetang.doctor.widget.popupwindow.BottomChoosePopupView;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.functions.Consumer;

/**
 * 认证 上传图片
 * AuthStep2Activity
 * Create at 2018/4/3 下午3:59 by mayakun
 */

public class AuthStep2Activity extends BaseActivity implements AuthContact.View, OSSManager.OSSUploadCallback {

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
    @BindView(R.id.et_sfz)
    EditTextlayout etSfz;

    @Inject
    AuthPresenter mPresenter;
    private BottomChoosePopupView bottomChoosePopupView;
    private LoadingDialog loadingDialog;
    private OSSAsyncTask upLoadTask;

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
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                }).bind();
    }

    private String imgPath1, imgPath2, imgPath3;
    private int currImg;//记录点击的图片

    @OnClick({R.id.iv_img1, R.id.iv_img2, R.id.iv_img3, R.id.tv_next_step})
    public void tabOnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_img1:
            case R.id.iv_img2:
            case R.id.iv_img3:
                bottomChoosePopupView = new BottomChoosePopupView(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //标识点击的哪一个
                        currImg = view.getId();
                        openCameraOrPhoto(v.getId() == R.id.dtv_one);
                    }
                });
                bottomChoosePopupView.show(scrollView);
                break;
            case R.id.tv_next_step:
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.auth_step2);

                if (TextUtils.isEmpty(etSfz.getEditText().getText().toString().trim())) {
                    ToastUtil.showCenterToast("请输入身份证号码");
                    return;
                }
                if (TextUtils.isEmpty(imgPath1) || TextUtils.isEmpty(imgPath2) || TextUtils.isEmpty(imgPath3)) {
                    ToastUtil.showCenterToast("请选择全部证件照片");
                    return;
                }
                mPresenter.userIdentifyNext(etSfz.getEditText().getText().toString().trim(),
                        imgPath1, imgPath2, imgPath3);
                break;
        }
    }

    //照相机公用file
    public File cameraPath;

    //1:打开相机拍照 2:打开相册
    private void openCameraOrPhoto(boolean isCamera) {
        //根据路径拍照并存储照片
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(isCamera ? new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA} : new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .subscribe(new Consumer<Boolean>() {
                    @Override
                    public void accept(Boolean aBoolean) throws Exception {
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
                            ToastUtil.showCenterToast(isCamera ? "请求照相机权限失败" : "请求相册权限失败");
                        }
                    }


                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照
                case REQUEST_CAMERA_CODE:
                    LogUtil.d("cameraPath=" + cameraPath.getAbsolutePath());
                    upLoadTask = OSSManager.getInstance().uploadImageAsync(1, cameraPath.getAbsolutePath(), this);
                    break;
                //相册
                case REQUEST_ALBUM_CODE:
                    Uri uri = data.getData();
                    String imagePath;
                    if (uri != null && !TextUtils.isEmpty(imagePath = UriUtil.getRealFilePath(this, uri))) {
                        LogUtil.d("imagePath=" + imagePath);
                        upLoadTask = OSSManager.getInstance().uploadImageAsync(1, imagePath, this);
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
            case AuthPresenter.USER_CREDENTIAL_OK://认证信息提交
                ToastUtil.showCenterToast("认证提交成功");
                startActivity(new Intent(this, AuthStep3Activity.class));
                finish();
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        CommonDialog commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
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
    public void uploadStatus(int type, Object obj) {
        //1:上传中 2：上传完成 3：上传失败
        switch (type) {
            case 1:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showUpLoadDialog(Integer.parseInt(obj.toString()));
                    }
                });
                break;
            case 2:
                HashMap<String, String> map = (HashMap<String, String>) obj;
                switch (currImg) {
                    case R.id.iv_img1:
                        imgPath1 = map.get("result");//oss 图片路径
                        break;
                    case R.id.iv_img2:
                        imgPath2 = map.get("result");//oss 图片路径
                        break;
                    case R.id.iv_img3:
                        imgPath3 = map.get("result");//oss 图片路径
                        break;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //显示本地图片路径
                        ImageUtil.showImage(map.get("localImagePath"), findViewById(currImg));
                    }
                });
                if (loadingDialog != null) {
                    loadingDialog.dismiss();
                }
                break;
            case 3:
                ToastUtil.showCenterToast(obj.toString());
                break;
        }
    }

    //图片上传dialog
    private void showUpLoadDialog(int progress) {
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, String.format("上传图片中%s%%", progress));
            loadingDialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
                @Override
                public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                        //主动关闭图片上传
                        if (loadingDialog != null && loadingDialog.isShowing()) {
                            LogUtil.d("onKey", "KEYCODE_BACK ACTION_DOWN");
                            if (upLoadTask != null && !upLoadTask.isCompleted() && !upLoadTask.isCanceled()) {
                                upLoadTask.cancel();
                            }
                            loadingDialog.dismiss();
                        }
                        return true;
                    }
                    return false;
                }
            });
            loadingDialog.show();
        } else {
            if (!loadingDialog.isShowing()) {
                loadingDialog.show();
            }
            loadingDialog.setLoadingText(String.format("上传图片中%s%%", progress));
        }
    }
}
