package com.jht.doctor.ui.activity.mine;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
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
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.data.eventbus.EventBusUtil;
import com.jht.doctor.data.http.Params;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.ui.bean_jht.BaseConfigBean;
import com.jht.doctor.ui.bean_jht.HospitalBean;
import com.jht.doctor.ui.bean_jht.UploadImgBean;
import com.jht.doctor.ui.contact.AuthContact;
import com.jht.doctor.ui.presenter.present_jht.AuthPresenter;
import com.jht.doctor.utils.ActivityUtil;
import com.jht.doctor.utils.Constant;
import com.jht.doctor.utils.ImageUtil;
import com.jht.doctor.utils.LogUtil;
import com.jht.doctor.utils.SoftHideKeyBoardUtil;
import com.jht.doctor.utils.ToastUtil;
import com.jht.doctor.utils.UriUtil;
import com.jht.doctor.widget.EditTextlayout;
import com.jht.doctor.widget.EditableLayout;
import com.jht.doctor.widget.dialog.CommonDialog;
import com.jht.doctor.widget.popupwindow.CameraPopupView;
import com.jht.doctor.widget.popupwindow.OnePopupWheel;
import com.jht.doctor.widget.popupwindow.ProvCityPopupView;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;
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
 * 认证第一步
 * AuthStep1Activity
 * Create at 2018/4/3 下午3:58 by mayakun
 */
public class AuthStep1Activity extends BaseActivity implements AuthContact.View {

    private final int REQUEST_CAMERA_CODE = 101;//拍照
    private final int REQUEST_ALBUM_CODE = 102;//相册
    private final int REQUEST_CROP_CODE = 103;//裁剪
    private final int REQUEST_CHOOSE_GOODAT = 104;//选择擅长
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.scrollView)
    ScrollView scrollView;
    @BindView(R.id.et_name)
    EditTextlayout etName;
    @BindView(R.id.et_address)
    EditableLayout etAddress;
    @BindView(R.id.et_organization)
    EditableLayout etOrganization;
    @BindView(R.id.et_lab_type)
    EditableLayout etLabType;
    @BindView(R.id.et_title)
    EditableLayout etTitle;
    @BindView(R.id.et_goodat)
    EditableLayout etGoodat;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;
    @BindView(R.id.iv_img1)
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

    private String headImgURL, provinceStr, cityStr;
    private OnePopupWheel mPopupWheel;
    private ProvCityPopupView mProvCityPopupView;
    private int labId;//科室

    private int sexType = 0;
    private BaseConfigBean baseConfigBean;
    private List<HospitalBean> hospitalBeans = new ArrayList<>();
    private List<String> hospitalStr = new ArrayList<>();//医院
    private List<String> titleList = new ArrayList<>();//职称
    private List<String> departmentStrList = new ArrayList<>();//科室

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_auth_step1;
    }

    @Override
    protected void initView() {
        SoftHideKeyBoardUtil.assistActivity(this);
        tvMustwrite.setText(Html.fromHtml("以下均为<font color='#FF0000'>必填项</font>"));
        initToolbar();
        mPresenter.getDpAndTitles();

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


    @OnClick({R.id.iv_img1, R.id.et_address, R.id.et_lab_type, R.id.et_organization,
            R.id.et_title, R.id.et_goodat, R.id.tv_next_step})
    public void tabOnClick(View view) {
        switch (view.getId()) {
            case R.id.iv_img1:
                CameraPopupView cameraPopupView = new CameraPopupView(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCameraOrPhoto(view.getId() == R.id.llt_camera);
                    }
                });
                cameraPopupView.show(scrollView);

                break;
            case R.id.et_address:
                mProvCityPopupView = new ProvCityPopupView(this, new ProvCityPopupView.ClickedListener() {
                    @Override
                    public void completeClicked(String prov, String city) {
                        provinceStr = prov;
                        cityStr = city;
                        etAddress.setText(prov + "-" + city);
                    }
                });
                mProvCityPopupView.show(scrollView);
                break;
            case R.id.et_organization:
                if (TextUtils.isEmpty(provinceStr) || TextUtils.isEmpty(cityStr)) {
                    ToastUtil.showShort("请先选择地区");
                    return;
                }
                mPresenter.getHospital(provinceStr, cityStr);
                break;
            case R.id.et_lab_type:
                mPopupWheel = new OnePopupWheel(this, departmentStrList, "请选择科室", new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        labId = baseConfigBean.department.get(position).id;
                        etLabType.setText(departmentStrList.get(position));
                    }
                });
                mPopupWheel.show(scrollView);
                break;
            case R.id.et_title:
                mPopupWheel = new OnePopupWheel(this, titleList, "请选择职称", new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        etTitle.setText(titleList.get(position));
                    }
                });
                mPopupWheel.show(scrollView);

                break;
            case R.id.et_goodat:
                Intent intent = new Intent(this, ChooseGoodAtActivity.class);
                intent.putParcelableArrayListExtra("skills", baseConfigBean.skills);
                startActivityForResult(intent, REQUEST_CHOOSE_GOODAT);
                break;
            case R.id.tv_next_step:
                checkData();
                break;

        }

    }

    //数据检测
    private void checkData() {
        if (TextUtils.isEmpty(headImgURL)) {
            ToastUtil.showShort("请选择头像");
            return;
        }
        if (TextUtils.isEmpty(etName.getEditText().getText())) {
            ToastUtil.showShort("请填写姓名");
            return;
        }
        if (TextUtils.isEmpty(provinceStr) || TextUtils.isEmpty(cityStr)) {
            ToastUtil.showShort("请选择地区");
            return;
        }
        if (TextUtils.isEmpty(etOrganization.getText())) {
            ToastUtil.showShort("请选择医疗机构");
            return;
        }
        if (TextUtils.isEmpty(etLabType.getText())) {
            ToastUtil.showShort("请选择科室");
            return;
        }
        if (TextUtils.isEmpty(etTitle.getText())) {
            ToastUtil.showShort("请选择职称");
            return;
        }
        if (TextUtils.isEmpty(etGoodat.getText())) {
            ToastUtil.showShort("请选择擅长疾病");
            return;
        }

        Params params = new Params();
        params.put("header", headImgURL);
        params.put("name", etName.getEditText().getText());
        params.put("sex", sexType);
        params.put("prov", provinceStr);
        params.put("city", cityStr);
        params.put("hospital", etOrganization.getText());
        params.put("department", labId);
        params.put("title", etTitle.getText());
        params.put("skills", etGoodat.getText());
        mPresenter.userIdentify(params);
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
                        mPresenter.uploadImg(cameraPath.getAbsolutePath(), Constant.UPLOADIMG_TYPE_0);
                    }
                    break;
                //相册
                case REQUEST_ALBUM_CODE:
                    Uri uri = data.getData();
                    String headerPath;
                    if (uri != null && !TextUtils.isEmpty(headerPath = UriUtil.getRealFilePath(this, uri))) {
                        LogUtil.d("headerPath=" + headerPath);
                        ImageUtil.showImage(headerPath, ivImg);
                        mPresenter.uploadImg(headerPath, Constant.UPLOADIMG_TYPE_0);
                    }
                    break;
                case REQUEST_CHOOSE_GOODAT://擅长疾病
                    List<BaseConfigBean.Skill> selectSekills = data.getParcelableArrayListExtra("skills");
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < selectSekills.size(); i++) {
                        stringBuffer.append(selectSekills.get(i).name);
                        if (i != selectSekills.size() - 1) {
                            stringBuffer.append(",");
                        }
                    }
                    etGoodat.setText(stringBuffer.toString());
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
        CommonDialog commonDialog = new CommonDialog(this, errorMsg);
        commonDialog.show();
    }


    private CommonDialog commonDialog;

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case AuthPresenter.GET_BASECONFIG://基础数据
                baseConfigBean = (BaseConfigBean) message.obj;
                //职称
                titleList.addAll(baseConfigBean.title);
                //科室
                for (BaseConfigBean.DepartmentBean bean : baseConfigBean.department) {
                    departmentStrList.add(bean.name);
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
            case AuthPresenter.GETHOSPITAL_OK://获取医院列表
                hospitalBeans = (List<HospitalBean>) message.obj;
                hospitalStr.clear();
                for (HospitalBean hospitalBean : hospitalBeans) {
                    hospitalStr.add(hospitalBean.name);
                }
                hospitalStr.add("其他");//最后追加
                mPopupWheel = new OnePopupWheel(this, hospitalStr, "请选择医院", new OnePopupWheel.Listener() {
                    @Override
                    public void completed(int position) {
                        etOrganization.setText(hospitalStr.get(position));
                        //选择其他后，弹出dialog填写
                        if (position == hospitalStr.size() - 1) {
                            commonDialog = new CommonDialog(AuthStep1Activity.this, R.layout.dialog_edite_common,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            if (view.getId() == R.id.btn_ok) {
                                                if (!TextUtils.isEmpty(commonDialog.getCommonEditText())) {
                                                    etOrganization.setText(commonDialog.getCommonEditText());
                                                }
                                            }
                                        }
                                    });
                            commonDialog.show();
                        }
//                        else {
//                            etOrganization.setText(hospitalStr.get(position));
//                        }
                    }
                });
                mPopupWheel.show(scrollView);
                break;
            case AuthPresenter.USER_IDENTIFY_OK://认证信息提交
                ToastUtil.showShort("认证基础信息提交成功");
                //刷新个人认证状态
                EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_AUTH_STATUS));
                startActivity(new Intent(this, AuthStep2Activity.class));
                finish();
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
