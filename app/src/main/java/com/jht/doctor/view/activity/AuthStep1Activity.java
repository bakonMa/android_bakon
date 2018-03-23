package com.jht.doctor.view.activity;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ScrollView;

import com.jht.doctor.BuildConfig;
import com.jht.doctor.R;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.utils.SoftHideKeyBoardUtil;
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
import rx.Observable;
import rx.Observer;

public class AuthStep1Activity extends BaseAppCompatActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth_step1_jht);
        ButterKnife.bind(this);
        SoftHideKeyBoardUtil.assistActivity(this);
        initView();
        initToolbar();
    }

    private void initView() {

    }

    //获取当前界面可用高度
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

    private CommonBottomPopupView popupView;

    @OnClick({R.id.iv_img, R.id.et_address, R.id.et_lab_type, R.id.et_goodat})
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
                            openCamera();
                        }
                    }
                });
                popupView.showAtLocation(scrollView, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

                break;
            case R.id.et_address:
                break;
            case R.id.et_lab_type:
                break;
            case R.id.et_goodat:
                break;
        }
    }

    private void openCamera() {
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
//                            if (Environment.MEDIA_MOUNTED.equals(CarBusinessApplication.storageOperator().externalRootDirState())) {
//                                dir = CarBusinessApplication.storageOperator().externalPublicDir(Environment.DIRECTORY_PICTURES);
//                            } else {
//                                dir = CarBusinessApplication.storageOperator().internalCustomDir(Environment.DIRECTORY_PICTURES);
//                            }
//                            if (!dir.exists()) {
//                                dir.mkdirs();
//                            }
//                            cameraPath = new File(dir, UriUtil.headerFileName(EditCardActivity.this));
//                            ActivityUtil.useCamera(EditCardActivity.this, cameraPath, REQUEST_CAMERA_CODE);
                        } else {
//                            ToastUtil.showT(EditCardActivity.this, "请求权限失败");
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
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void setupActivityComponent() {

    }


}
