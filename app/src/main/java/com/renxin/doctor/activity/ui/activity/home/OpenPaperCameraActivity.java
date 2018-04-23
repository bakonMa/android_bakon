package com.renxin.doctor.activity.ui.activity.home;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.renxin.doctor.activity.BuildConfig;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerActivityComponent;
import com.renxin.doctor.activity.injection.modules.ActivityModule;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.ui.bean.OPenPaperBaseBean;
import com.renxin.doctor.activity.ui.bean_jht.UploadImgBean;
import com.renxin.doctor.activity.ui.contact.OpenPaperContact;
import com.renxin.doctor.activity.ui.presenter.OpenPaperPresenter;
import com.renxin.doctor.activity.ui.presenter.present_jht.AuthPresenter;
import com.renxin.doctor.activity.utils.ActivityUtil;
import com.renxin.doctor.activity.utils.LogUtil;
import com.renxin.doctor.activity.utils.SoftHideKeyBoardUtil;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.utils.UriUtil;
import com.renxin.doctor.activity.widget.EditTextlayout;
import com.renxin.doctor.activity.widget.EditableLayout;
import com.renxin.doctor.activity.widget.dialog.CommonDialog;
import com.renxin.doctor.activity.widget.popupwindow.CameraPopupView;
import com.renxin.doctor.activity.widget.popupwindow.OnePopupWheel;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.LifecycleTransformer;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Observer;

/**
 * OpenPaperCameraActivity  拍照开方
 * Create at 2018/4/23 下午5:29 by mayakun
 */
public class OpenPaperCameraActivity extends BaseActivity implements OpenPaperContact.View {

    private final int REQUEST_CAMERA_CODE = 101;//拍照
    private final int REQUEST_ALBUM_CODE = 102;//相册

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.et_name)
    EditTextlayout etName;
    @BindView(R.id.rb_nan)
    RadioButton rbNan;
    @BindView(R.id.rb_nv)
    RadioButton rbNv;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.et_age)
    EditTextlayout etAge;
    @BindView(R.id.et_phone)
    EditTextlayout etPhone;
    @BindView(R.id.et_drugstore)
    EditableLayout etDrugstore;
    @BindView(R.id.et_drugclass)
    EditableLayout etDrugClass;
    @BindView(R.id.rb_yes)
    RadioButton rbYes;
    @BindView(R.id.rb_no)
    RadioButton rbNo;
    @BindView(R.id.rg_daijian)
    RadioGroup rgDaijian;

    @BindView(R.id.tv_next_step)
    TextView tvNextStep;
    @BindView(R.id.iv_img1)
    ImageView ivImg1;
    @BindView(R.id.iv_img2)
    ImageView ivImg2;
    @BindView(R.id.iv_img3)
    ImageView ivImg3;

    @Inject
    OpenPaperPresenter mPresenter;

    private String headImgURL;
    private OnePopupWheel mPopupWheel;
    private int storeId, drugClassId;

    private int sexType = 0;
    private int daijianType = 0;

    private OPenPaperBaseBean baseBean;
    private List<String> drugStoreList = new ArrayList<>();//药房
    private List<String> drugClassList = new ArrayList<>();//剂型

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_openpaper_camera;
    }

    @Override
    protected void initView() {
        SoftHideKeyBoardUtil.assistActivity(this);
        initToolbar();
        //获取基础数据
        mPresenter.getOPenPaperBaseData();
        //性别
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sexType = (i == R.id.rb_nan ? 0 : 1);
            }
        });
        //代煎
        rgDaijian.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sexType = (i == R.id.rb_nan ? 0 : 1);
            }
        });

    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("拍照开方")
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


    @OnClick({R.id.rlt_addpatient, R.id.et_drugstore, R.id.et_drugclass, R.id.tv_otherfee,
            R.id.iv_img1, R.id.iv_img2, R.id.iv_img3, R.id.tv_next_step})
    public void tabOnClick(View view) {
        switch (view.getId()) {
            case R.id.rlt_addpatient:
                ToastUtil.showShort("添加患者");
                break;
            case R.id.et_drugstore:
                mPopupWheel = new OnePopupWheel(this, drugStoreList, "请选择药房", new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        storeId = baseBean.store.get(position).drug_store_id;
                        etDrugstore.setText(drugStoreList.get(position));
                    }
                });
                mPopupWheel.show(scrollView);
                break;
            case R.id.et_drugclass:
                mPopupWheel = new OnePopupWheel(this, drugClassList, "请选择剂型", new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        drugClassId = baseBean.drug_class.get(position).id;
                        etDrugClass.setText(drugClassList.get(position));
                    }
                });
                mPopupWheel.show(scrollView);

                break;
            case R.id.tv_otherfee:
                ToastUtil.showShort("收费");
                break;
            case R.id.tv_next_step://提交
                checkData();
                break;
            case R.id.iv_img1:
            case R.id.iv_img2:
            case R.id.iv_img3:
                CameraPopupView cameraPopupView = new CameraPopupView(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCameraOrPhoto(view.getId() == R.id.llt_camera);
                    }
                });
                cameraPopupView.show(scrollView);

                break;
        }

    }

    //数据检测
    private void checkData() {
//        if (TextUtils.isEmpty(headImgURL)) {
//            ToastUtil.showShort("请选择头像");
//            return;
//        }
//        if (TextUtils.isEmpty(etName.getEditText().getText())) {
//            ToastUtil.showShort("请填写姓名");
//            return;
//        }
//        if (TextUtils.isEmpty(provinceStr) || TextUtils.isEmpty(cityStr)) {
//            ToastUtil.showShort("请选择地区");
//            return;
//        }
//        if (TextUtils.isEmpty(etOrganization.getText())) {
//            ToastUtil.showShort("请选择医疗机构");
//            return;
//        }
//        if (TextUtils.isEmpty(etLabType.getText())) {
//            ToastUtil.showShort("请选择科室");
//            return;
//        }
//        if (TextUtils.isEmpty(etTitle.getText())) {
//            ToastUtil.showShort("请选择职称");
//            return;
//        }
//        if (TextUtils.isEmpty(etGoodat.getText())) {
//            ToastUtil.showShort("请选择擅长疾病");
//            return;
//        }
//
//        Params params = new Params();
//        params.put("header", headImgURL);
//        params.put("name", etName.getEditText().getText());
//        params.put("sex", sexType);
//        params.put("prov", provinceStr);
//        params.put("city", cityStr);
//        params.put("hospital", etOrganization.getText());
//        params.put("department", labId);
//        params.put("title", etTitle.getText());
//        params.put("skills", etGoodat.getText());
//        mPresenter.userIdentify(params);
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
                                ActivityUtil.openCamera(OpenPaperCameraActivity.this, cameraPath, REQUEST_CAMERA_CODE);
                            } else {//打开相册
                                ActivityUtil.openAlbum(OpenPaperCameraActivity.this, "image/*", REQUEST_ALBUM_CODE);
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
//                        ImageUtil.showImage(cameraPath.getAbsolutePath(), ivImg);
//                        mPresenter.uploadImg(cameraPath.getAbsolutePath(), Constant.UPLOADIMG_TYPE_0);
                    }
                    break;
                //相册
                case REQUEST_ALBUM_CODE:
                    Uri uri = data.getData();
                    String headerPath;
                    if (uri != null && !TextUtils.isEmpty(headerPath = UriUtil.getRealFilePath(this, uri))) {
                        LogUtil.d("headerPath=" + headerPath);
//                        ImageUtil.showImage(headerPath, ivImg);
//                        mPresenter.uploadImg(headerPath, Constant.UPLOADIMG_TYPE_0);
                    }
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

    private CommonDialog commonDialog;

    @Override
    public void onError(String errorCode, String errorMsg) {
        commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
    }


    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case OpenPaperPresenter.GET_BASEDATA_0K://基础数据
                baseBean = (OPenPaperBaseBean) message.obj;
                //药房
                for (OPenPaperBaseBean.StoreBean bean : baseBean.store) {
                    drugStoreList.add(bean.drug_store_name);
                }
                //剂型
                for (OPenPaperBaseBean.CommBean bean : baseBean.drug_class) {
                    drugClassList.add(bean.name);
                }
                break;
            case AuthPresenter.UPLOADIMF_OK://上传成功
                UploadImgBean uploadImgBean = (UploadImgBean) message.obj;
                headImgURL = uploadImgBean.url;
                ToastUtil.showShort("上传成功");
                break;
            case AuthPresenter.UPLOADIMF_ERROR://上传失败
                headImgURL = "";
                ToastUtil.showShort("上传失败，请重新选择");
                break;

        }
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }

}
