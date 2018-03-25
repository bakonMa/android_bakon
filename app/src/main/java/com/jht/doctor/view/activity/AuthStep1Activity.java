package com.jht.doctor.view.activity;

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
import android.widget.ScrollView;
import android.widget.TextView;

import com.jht.doctor.BuildConfig;
import com.jht.doctor.R;
import com.jht.doctor.application.DocApplication;
import com.jht.doctor.injection.components.DaggerActivityComponent;
import com.jht.doctor.injection.modules.ActivityModule;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean_jht.BankBean;
import com.jht.doctor.ui.contact.contact_jht.AuthContact;
import com.jht.doctor.ui.presenter.present_jht.AuthPresenter;
import com.jht.doctor.utils.ActivityUtil;
import com.jht.doctor.utils.LogUtil;
import com.jht.doctor.utils.SoftHideKeyBoardUtil;
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

public class AuthStep1Activity extends BaseAppCompatActivity implements AuthContact.View {

    private final int REQUEST_CAMERA_CODE = 101;//拍照
    private final int REQUEST_ALBUM_CODE = 102;//相册
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @Inject
    AuthPresenter mPresenter;
    @BindView(R.id.et_name)
    EditTextlayout etName;
    @BindView(R.id.et_phone)
    EditTextlayout etPhone;
    @BindView(R.id.et_sfz)
    EditTextlayout etSfz;
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

    private String provinceId, cityId;
    private CommonBottomPopupView popupView;
    private OnePopupWheel mPopupWheel;
    private AddressPopupView mAddressPopupView;
    private List<String> typeList = new ArrayList<>();

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
        mPresenter.getBanks();
        // todo 测试数据
        typeList.clear();
        typeList.add("测试科室1");
        typeList.add("测试科室2");
        typeList.add("测试科室3");
        typeList.add("测试科室4");
        typeList.add("测试科室5");
        typeList.add("测试科室6");
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
                        if (o.toString().equals("拍照")) {
                            openCamera();
                        }
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
                    if (cameraPath.exists()) {
                        headerImageUpload(cameraPath);
                    }
                    break;

                //相册
                case REQUEST_ALBUM_CODE:
                    Uri uri = data.getData();
                    String headerPath;
                    if (uri != null && !TextUtils.isEmpty(headerPath = UriUtil.getRealFilePath(this, uri))) {
                        headerImageUpload(new File(headerPath));
                    }
                    break;


                default:
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


    private void headerImageUpload(File jpgImage) {
//        FileUtil
//                .zipImage(jpgImage, 1 * 1024 * 1024)
//                .concatMap(bytes -> HttpClient
//                        .getClient()
//                        .uploadSingleFile(MultipartBody.Part.createFormData("multipartFiles", jpgImage.getName(), RequestBody.create(MediaType.parse(FileUtil.mimeType(jpgImage)), bytes)))
//                )
//                .compose(HttpProvider.compatResult(this))
//                .compose(HttpProvider.bindLiefAndSchedulers(this))
//                .subscribe(new ProgressDialogSubscriber<String>(this , 0) {
//
//                    @Override
//                    public void onSubscribe(Disposable d) {
//                        super.onSubscribe(d);
//                        headerUploadDisposable = d;
//                    }
//
//                    @Override
//                    protected void _onNext(String s) {
//                        if (!TextUtils.isEmpty(s)){
//                            settingHeaderUrl(s);
//                            cardViewPagerAdapter.headerUrlChanged(s);
//                        }else {
//                            ToastUtil.showT(EditCardActivity.this , "未知错误");
//                        }
//                    }
//                });
    }

    @Override
    protected boolean isUseEventBus() {
        return false;
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
        DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case AuthPresenter.GETBANK_OK:
                List<BankBean> bankBeans = (List<BankBean>) message.obj;
                LogUtil.d("bankBeans = " + bankBeans.size());
                DocApplication.getAppComponent().mgrRepo().toastMgr().shortToast(bankBeans.size() + "");
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
