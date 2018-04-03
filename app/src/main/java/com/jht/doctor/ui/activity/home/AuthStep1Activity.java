package com.jht.doctor.ui.activity.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.jht.doctor.BuildConfig;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.ui.bean_jht.BankBean;
import com.jht.doctor.ui.contact.contact_jht.AuthContact;
import com.jht.doctor.ui.presenter.present_jht.AuthPresenter;
import com.jht.doctor.utils.ActivityUtil;
import com.jht.doctor.utils.ImageUtil;
import com.jht.doctor.utils.LogUtil;
import com.jht.doctor.utils.SoftHideKeyBoardUtil;
import com.jht.doctor.utils.ToastUtil;
import com.jht.doctor.utils.UriUtil;
import com.jht.doctor.widget.EditTextlayout;
import com.jht.doctor.widget.EditableLayout;
import com.jht.doctor.widget.popupwindow.AddressPopupView;
import com.jht.doctor.widget.popupwindow.CommonBottomPopupView;
import com.jht.doctor.widget.popupwindow.OnePopupWheel;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Observer;

/**
 * 认证第一步
 * AuthStep1Activity
 * Create at 2018/4/3 下午3:58 by mayakun
 */
public class AuthStep1Activity extends BaseActivity implements AuthContact.View {

    private final int REQUEST_CAMERA_CODE = 101;//拍照
    private final int REQUEST_ALBUM_CODE = 102;//相册
    private final int REQUEST_CROP_CODE = 103;//裁剪
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.et_name)
    EditTextlayout etName;
    @BindView(R.id.et_address)
    EditableLayout etAddress;
    @BindView(R.id.et_organization)
    EditTextlayout etOrganization;
    @BindView(R.id.et_lab_type)
    EditableLayout etLabType;
    @BindView(R.id.et_title)
    EditableLayout etTitle;
    @BindView(R.id.et_goodat)
    EditableLayout etGoodat;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_mustwrite)
    TextView tvMustwrite;
    @BindView(R.id.rb_nan)
    RadioButton rbNan;
    @BindView(R.id.rb_nv)
    RadioButton rbNv;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;

    @Inject
    AuthPresenter mPresenter;

    private String provinceId, cityId;
    private CommonBottomPopupView popupView;
    private OnePopupWheel mPopupWheel;
    private AddressPopupView mAddressPopupView;
    private List<String> typeList = new ArrayList<>();
    private int sexType = 0;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_auth_step1_jht;
    }

    @Override
    protected void initView() {
        SoftHideKeyBoardUtil.assistActivity(this);
        tvMustwrite.setText(Html.fromHtml("以下均为<font color='#FF0000'>必填项</font>"));
        initToolbar();
//        mPresenter.getBanks();
        // todo 测试数据
        typeList.clear();
        typeList.add("测试科室1");
        typeList.add("测试科室2");
        typeList.add("测试科室3");
        typeList.add("测试科室4");
        typeList.add("测试科室5");
        typeList.add("测试科室6");

        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sexType = (i == R.id.rb_nan ? 0 : 1);
            }
        });
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




    @OnClick({R.id.iv_img, R.id.et_address, R.id.et_lab_type,
            R.id.et_title, R.id.et_goodat, R.id.tv_next_step})
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
                        openCameraOrPhoto(o.toString().equals("拍照"));
                    }
                });
                popupView.show(scrollView);
                break;
            case R.id.et_address:
                mAddressPopupView = new AddressPopupView(this, 2, new AddressPopupView.ClickedListener() {
                    @Override
                    public void completeClicked(String... info) {
                        etAddress.setText(info[0] + "-" + info[2]);
                        provinceId = info[1];
                        cityId = info[3];
                    }
                });
                mAddressPopupView.show(scrollView);
                break;
            case R.id.et_lab_type:
                mPopupWheel = new OnePopupWheel(this, typeList, "请选择科室", new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        etLabType.setText(typeList.get(position));
                    }
                });
                mPopupWheel.show(scrollView);
                break;
            case R.id.et_title:
                mPopupWheel = new OnePopupWheel(this, typeList, "请选择职称", new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        etTitle.setText(typeList.get(position));
                    }
                });
                mPopupWheel.show(scrollView);
                break;
            case R.id.et_goodat:
                mPopupWheel = new OnePopupWheel(this, typeList, new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        etGoodat.setText(typeList.get(position));
                    }
                });
                mPopupWheel.show(scrollView);
                break;
            case R.id.tv_next_step:
                startActivity(new Intent(this, AuthStep2Activity.class));
                break;

        }
    }

    private File cameraPath;

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
                                ActivityUtil.openCamera(AuthStep1Activity.this, cameraPath, REQUEST_CAMERA_CODE);
                            } else {//打开相册
                                ActivityUtil.openAlbum(AuthStep1Activity.this, "image/*", REQUEST_ALBUM_CODE);
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
                        ImageUtil.showImage(cameraPath.getAbsolutePath(), ivImg);
                        mPresenter.uploadImg(cameraPath.getAbsolutePath());
                    }
                    break;
                //相册
                case REQUEST_ALBUM_CODE:
                    Uri uri = data.getData();
                    String headerPath;
                    if (uri != null && !TextUtils.isEmpty(headerPath = UriUtil.getRealFilePath(this, uri))) {
                        LogUtil.d("headerPath=" + headerPath);
                        ImageUtil.showImage(headerPath, ivImg);
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
    public void onError(String errorCode, String errorMsg) {
        ToastUtil.show(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case AuthPresenter.GETBANK_OK:
                List<BankBean> bankBeans = (List<BankBean>) message.obj;
                LogUtil.d("bankBeans = " + bankBeans.size());
                ToastUtil.show(bankBeans.size() + "");
                break;
        }
    }

    @Override
    public Activity provideContext() {
        return this;
    }

//    @Override
//    protected boolean useEventBus() {
//        return true;
//    }

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
