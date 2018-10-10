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
import android.text.Html;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.manager.OSSManager;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.BaseConfigBean;
import com.junhetang.doctor.ui.bean.HospitalBean;
import com.junhetang.doctor.ui.contact.AuthContact;
import com.junhetang.doctor.ui.presenter.AuthPresenter;
import com.junhetang.doctor.utils.ActivityUtil;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.SoftHideKeyBoardUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.UmengKey;
import com.junhetang.doctor.utils.UriUtil;
import com.junhetang.doctor.widget.EditTextlayout;
import com.junhetang.doctor.widget.EditableLayout;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.dialog.LoadingDialog;
import com.junhetang.doctor.widget.popupwindow.BottomChoosePopupView;
import com.junhetang.doctor.widget.popupwindow.BottomListPopupView;
import com.junhetang.doctor.widget.popupwindow.ProvCityPopupView;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.umeng.analytics.MobclickAgent;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 认证第一步
 * AuthStep1Activity
 * Create at 2018/4/3 下午3:58 by mayakun
 */
public class AuthStep1Activity extends BaseActivity implements AuthContact.View, OSSManager.OSSUploadCallback {

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
    private ProvCityPopupView mProvCityPopupView;
    private int labId;//科室

    private int sexType = 0;
    private BaseConfigBean baseConfigBean;
    private List<HospitalBean> hospitalBeans = new ArrayList<>();
    private List<String> hospitalStr = new ArrayList<>();//医院
    private List<String> titleList = new ArrayList<>();//职称
    private List<String> departmentStrList = new ArrayList<>();//科室
    //擅长疾病 中间传递使用
    private ArrayList<BaseConfigBean.Skill> selectSkills = new ArrayList<>();
    private LoadingDialog loadingDialog;
    private BottomChoosePopupView bottomChoosePopupView;
    private BottomListPopupView bottomPopupView;
    private OSSAsyncTask upLoadTask;

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
                bottomChoosePopupView = new BottomChoosePopupView(this, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openCameraOrPhoto(view.getId() == R.id.dtv_one);
                    }
                });
                bottomChoosePopupView.show(scrollView);
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
                    ToastUtil.showCenterToast("请先选择地区");
                    return;
                }
                mPresenter.getHospital(provinceStr, cityStr);
                break;
            case R.id.et_lab_type:
                bottomPopupView = new BottomListPopupView(this, "请选择科室", departmentStrList, new BottomListPopupView.OnClickListener() {
                    @Override
                    public void selectItem(int position) {
                        labId = baseConfigBean.department.get(position).id;
                        etLabType.setText(departmentStrList.get(position));
                    }
                });
                bottomPopupView.show(scrollView);
                break;
            case R.id.et_title:
                bottomPopupView = new BottomListPopupView(this, "请选择职称", titleList, new BottomListPopupView.OnClickListener() {
                    @Override
                    public void selectItem(int position) {
                        etTitle.setText(titleList.get(position));
                    }
                });
                bottomPopupView.show(scrollView);

                break;
            case R.id.et_goodat:
                Intent intent = new Intent(this, ChooseGoodAtActivity.class);
                intent.putParcelableArrayListExtra("skills", baseConfigBean.skills);
                intent.putParcelableArrayListExtra("selectskill", selectSkills);
                startActivityForResult(intent, REQUEST_CHOOSE_GOODAT);
                break;
            case R.id.tv_next_step:
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.auth_step1);
                checkData();
                break;

        }

    }

    //数据检测
    private void checkData() {
        if (TextUtils.isEmpty(headImgURL)) {
            ToastUtil.showCenterToast("请选择头像");
            return;
        }
        if (TextUtils.isEmpty(etName.getEditText().getText())) {
            ToastUtil.showCenterToast("请填写姓名");
            return;
        }
        if (TextUtils.isEmpty(provinceStr) || TextUtils.isEmpty(cityStr)) {
            ToastUtil.showCenterToast("请选择地区");
            return;
        }
        if (TextUtils.isEmpty(etOrganization.getText())) {
            ToastUtil.showCenterToast("请选择医疗机构");
            return;
        }
        if (TextUtils.isEmpty(etLabType.getText())) {
            ToastUtil.showCenterToast("请选择科室");
            return;
        }
        if (TextUtils.isEmpty(etTitle.getText())) {
            ToastUtil.showCenterToast("请选择职称");
            return;
        }
        if (TextUtils.isEmpty(etGoodat.getText())) {
            ToastUtil.showCenterToast("请选择擅长疾病");
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
        rxPermissions
                .request(isCamera ? new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA} : new String[]{Manifest.permission.READ_EXTERNAL_STORAGE})
                .subscribe(aBoolean -> {
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
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //裁剪
                case REQUEST_CROP_CODE:
                    upLoadTask = OSSManager.getInstance().uploadImageAsync(0, ImageUtil.cropUri.getPath(), this);
                    break;
                //拍照
                case REQUEST_CAMERA_CODE:
                    LogUtil.d("cameraPath=" + cameraPath.getAbsolutePath());
                    if (cameraPath.exists()) {
                        ImageUtil.doCrop(this, cameraPath, REQUEST_CROP_CODE);
                    }
                    break;
                //相册
                case REQUEST_ALBUM_CODE:
                    Uri uri = data.getData();
                    String headerPath;
                    if (uri != null && !TextUtils.isEmpty(headerPath = UriUtil.getRealFilePath(this, uri))) {
                        LogUtil.d("headerPath=" + headerPath);
                        ImageUtil.doCrop(this, new File(headerPath), REQUEST_CROP_CODE);
                    }
                    break;
                case REQUEST_CHOOSE_GOODAT://擅长疾病
                    selectSkills = data.getParcelableArrayListExtra("skills");
                    StringBuffer stringBuffer = new StringBuffer();
                    for (int i = 0; i < selectSkills.size(); i++) {
                        stringBuffer.append(selectSkills.get(i).name);
                        if (i != selectSkills.size() - 1) {
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
            case AuthPresenter.GETHOSPITAL_OK://获取医院列表
                hospitalBeans = (List<HospitalBean>) message.obj;
                hospitalStr.clear();
                for (HospitalBean hospitalBean : hospitalBeans) {
                    hospitalStr.add(hospitalBean.name);
                }
                hospitalStr.add("其他");//最后追加
                bottomPopupView = new BottomListPopupView(this, "请选择医疗机构", hospitalStr, new BottomListPopupView.OnClickListener() {
                    @Override
                    public void selectItem(int position) {
                        //选择其他后，弹出dialog填写
                        if (position == hospitalStr.size() - 1) {
                            commonDialog = new CommonDialog(AuthStep1Activity.this, "填写医疗机构名称", InputType.TYPE_CLASS_TEXT,
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
                        } else {
                            etOrganization.setText(hospitalStr.get(position));
                        }
                    }
                });
                bottomPopupView.show(scrollView);
                break;
            case AuthPresenter.USER_IDENTIFY_OK://认证信息提交
                //ToastUtil.showCenterToast("认证基础信息提交成功");
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
                //oss 图片路径
                headImgURL = map.get("result");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //显示本地图片路径
                        ImageUtil.showImage(map.get("localImagePath"), ivImg);
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
