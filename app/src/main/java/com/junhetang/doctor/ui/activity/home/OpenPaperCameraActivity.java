package com.junhetang.doctor.ui.activity.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Environment;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.junhetang.doctor.BuildConfig;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.junhetang.doctor.data.http.Params;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.nim.NimU;
import com.junhetang.doctor.ui.activity.patient.PatientFamilyActivity;
import com.junhetang.doctor.ui.adapter.JzrPopupAdapter;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.OPenPaperBaseBean;
import com.junhetang.doctor.ui.bean.PatientFamilyBean;
import com.junhetang.doctor.ui.bean.UploadImgBean;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.ActivityUtil;
import com.junhetang.doctor.utils.Constant;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.utils.KeyBoardUtils;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.RegexUtil;
import com.junhetang.doctor.utils.SoftHideKeyBoardUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.utils.UmengKey;
import com.junhetang.doctor.utils.UriUtil;
import com.junhetang.doctor.utils.imageloader.Glide4Engine;
import com.junhetang.doctor.widget.EditTextlayout;
import com.junhetang.doctor.widget.EditableLayout;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.popupwindow.BottomChoosePopupView;
import com.junhetang.doctor.widget.popupwindow.BottomListPopupView;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.umeng.analytics.MobclickAgent;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnTextChanged;
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
    @BindView(R.id.tv_addpatient)
    TextView tvAddpatient;
    @BindView(R.id.tv_editepatient)
    TextView tv_editepatient;
    @BindView(R.id.llt_jzinfo)
    LinearLayout lltJZinfo;
    @BindView(R.id.et_name)
    AutoCompleteTextView etName;
    @BindView(R.id.rb_nan)
    RadioButton rbNan;
    @BindView(R.id.rb_nv)
    RadioButton rbNv;
    @BindView(R.id.rg_sex)
    RadioGroup rgSex;
    @BindView(R.id.et_age)
    EditTextlayout etAge;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.et_drugclass)
    EditableLayout etDrugClass;
    @BindView(R.id.et_drugstore)
    EditableLayout etDrugstore;
    @BindView(R.id.iv_img1)
    ImageView ivImg1;
    @BindView(R.id.iv_img2)
    ImageView ivImg2;
    @BindView(R.id.iv_img3)
    ImageView ivImg3;
    @BindView(R.id.iv_img1_clean)
    ImageView ivImg1Clean;
    @BindView(R.id.iv_img2_clean)
    ImageView ivImg2Clean;
    @BindView(R.id.iv_img3_clean)
    ImageView ivImg3Clean;
    @BindView(R.id.et_remark)
    EditText etRemark;
    @BindView(R.id.tv_next_step)
    TextView tvNextStep;
    @BindView(R.id.et_serverprice)
    EditText etServerprice;
    @BindView(R.id.rb_comm)
    RadioButton rbComm;

    @Inject
    OpenPaperPresenter mPresenter;

    private int formParent = 0;//是否来自聊天(0 默认不是 1：聊天)
    private int drugClassId = 0;
    private int sexType = 0;
    private String membNo = "";//患者编号，选择患者才有
    private int relationship = 4;//就诊人关系（不是选择 默认4-其他）
    private String pAccid = "";//患者云信 accid
    private int storeId = -1;//药房id

    private GestureDetectorCompat mDetector;//手势
    private OPenPaperBaseBean baseBean;
    private List<String> drugStoreList = new ArrayList<>();//药房
    private List<String> drugClassList = new ArrayList<>();//剂型
    private BottomChoosePopupView bottomChoosePopupView;
    private BottomListPopupView bottomPopupView;
    private boolean isChoosePatient = false;//是否点击的选择就诊人
    private List<PatientFamilyBean.JiuzhenBean> jzrList = new ArrayList<>();//手机号的就诊人

    //带有返回的startActivityForResult-仅限nim中使用 formParent=1
    public static void startResultActivity(Context context, int requestCode, int formParent, String p_accid, String membNo) {
        Intent intent = new Intent(context, OpenPaperCameraActivity.class);
        intent.putExtra("formParent", formParent);
        intent.putExtra("memb_no", membNo);
        intent.putExtra("p_accid", p_accid);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_openpaper_camera;
    }

    @Override
    protected void initView() {
        SoftHideKeyBoardUtil.assistActivity(this);
        formParent = getIntent().getIntExtra("formParent", 0);
        membNo = getIntent().getStringExtra("memb_no");//患者momb_no
        pAccid = getIntent().getStringExtra("p_accid");//患者accid

        //初始基础数据
        initBaseData();
        //设置topbar
        initToolbar();
        //聊天进来不能填写
        tv_editepatient.setVisibility(formParent == 0 ? View.VISIBLE : View.GONE);
        //性别
        rgSex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sexType = (i == R.id.rb_nan ? 0 : 1);
            }
        });

        //手势监听，滑动关闭软键盘
        mDetector = new GestureDetectorCompat(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                InputMethodManager manager = (InputMethodManager) actContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                //滑动距离超过**像素就收起键盘
                if (manager.isActive() && Math.abs(distanceY) > 5) {
                    KeyBoardUtils.hideKeyBoard(scrollView, actContext());
                }
                return Math.abs(distanceY) > 5;
            }
        });

        //添加手势监听
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return false;
            }
        });

    }

    //初始base数据
    private void initBaseData() {
        baseBean = U.getOpenpapeBaseData();
        //基础数据空的时候
        if (null == baseBean || null == baseBean.store || null == baseBean.drug_class) {
            commonDialog = new CommonDialog(this, true, "数据异常，请退出后重试", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_BASEDATA_NULL));
                    finish();
                }
            });
            commonDialog.show();
        }
        //药房
        for (OPenPaperBaseBean.StoreBean bean : baseBean.store) {
            drugStoreList.add(bean.drug_store_name);
        }
        //药房默认选择第一
        if (!drugStoreList.isEmpty()) {
            storeId = baseBean.store.get(0).drug_store_id;
            etDrugstore.setText(baseBean.store.get(0).drug_store_name);
        }
        //剂型
        for (OPenPaperBaseBean.CommBean bean : baseBean.drug_class) {
            drugClassList.add(bean.name);
        }
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("拍照开方")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        onBackPressed();
                    }

                }).bind();
    }


    private JzrPopupAdapter jzrPopupAdapter;
    //填写就诊人 动态提示
    private void initJzrPopup() {
        if (jzrPopupAdapter != null) {
            jzrPopupAdapter.notifyDataSetChanged();
        }

        jzrPopupAdapter = new JzrPopupAdapter(this, jzrList);
        etName.setAdapter(jzrPopupAdapter);
        etName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PatientFamilyBean.JiuzhenBean bean = (PatientFamilyBean.JiuzhenBean) parent.getAdapter().getItem(position);
                chooseJzrPopup(bean);
            }
        });
    }

    //输入手机号监听
    @OnTextChanged(value = R.id.et_phone, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    void afterPhoneChanged(Editable s) {
        if (s.length() == 11 && !isChoosePatient) {//填写时 匹配手机号
            mPresenter.getJZRByPhone(s.toString());
        }
    }

    private String imgPath1, imgPath2, imgPath3;
    private int currImg;

    @OnClick({R.id.tv_addpatient, R.id.tv_editepatient, R.id.et_drugclass, R.id.et_drugstore,
            R.id.iv_img1, R.id.iv_img2, R.id.iv_img3, R.id.iv_img1_clean, R.id.iv_img2_clean,
            R.id.iv_img3_clean, R.id.tv_next_step})
    public void tabOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_addpatient://选择患者
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.camera_choosepatient);
                isChoosePatient = true;
                Intent intent = new Intent();
                if (formParent == 1) {
                    intent.setClass(this, PatientFamilyActivity.class);
                    intent.putExtra("memb_no", membNo);
                } else {
                    intent.setClass(this, JZRListActivity.class);
                }
                intent.putExtra("formtype", 1);//来自 选择患者
                startActivity(intent);
                break;
            case R.id.tv_editepatient://编辑就诊人
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.camera_writepatient);
                isChoosePatient = false;
                writeJzInfo();
                break;
            case R.id.et_drugclass://剂型
                bottomPopupView = new BottomListPopupView(this, "请选择剂型", drugClassList, new BottomListPopupView.OnClickListener() {
                    @Override
                    public void selectItem(int position) {
                        drugClassId = baseBean.drug_class.get(position).id;
                        etDrugClass.setText(drugClassList.get(position));
                    }
                });
                bottomPopupView.show(scrollView);
                break;
            case R.id.et_drugstore://选择药房
                bottomPopupView = new BottomListPopupView(this, "请选择药房", drugStoreList, new BottomListPopupView.OnClickListener() {
                    @Override
                    public void selectItem(int position) {
                        storeId = baseBean.store.get(position).drug_store_id;
                        etDrugstore.setText(drugStoreList.get(position));
                    }
                });
                bottomPopupView.show(scrollView);
                break;
            case R.id.tv_next_step://提交
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.camera_submit);
                checkData();
                break;
            case R.id.iv_img1_clean:
                imgPath1 = "";
                ivImg1.setImageResource(0);
                ivImg1Clean.setVisibility(View.GONE);
                break;
            case R.id.iv_img2_clean:
                imgPath2 = "";
                ivImg2.setImageResource(0);
                ivImg2Clean.setVisibility(View.GONE);
                break;
            case R.id.iv_img3_clean:
                imgPath3 = "";
                ivImg3.setImageResource(0);
                ivImg3Clean.setVisibility(View.GONE);
                break;
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
        }
    }

    //选择的患者的就诊人（第一类）
    private void chooseJzInfo(PatientFamilyBean.JiuzhenBean bean) {
        lltJZinfo.setVisibility(View.VISIBLE);
        etName.setText(RegexUtil.getNameSubString(bean.patient_name));
        etPhone.setText(TextUtils.isEmpty(bean.phone) ? "" : bean.phone);
        membNo = bean.id;
        relationship = bean.relationship;
        pAccid = bean.getIm_accid();
        etName.setEnabled(false);
        etPhone.setEnabled(false);

        //默认及就诊人的时候（性别，年龄 是空，可以修改）
        //默认及就诊人的时候（性别，年龄 是空，可以修改）
        if (bean.sex == 0 || bean.sex == 1) {
            sexType = bean.sex;
            rgSex.check(bean.sex == 0 ? R.id.rb_nan : R.id.rb_nv);
            rbNan.setEnabled(false);
            rbNv.setEnabled(false);
        } else {
            //拍照开方 选择默认就诊人，性别默认男
            sexType = 0;
            //默认就诊人可以修改
            rgSex.check(R.id.rb_nan);
            rbNan.setEnabled(true);
            rbNv.setEnabled(true);
        }
        //年龄
        etAge.setEditeText(bean.age > 0 ? (bean.age + "") : "");
        etAge.setEditeEnable(bean.age <= 0);
    }

    //手写就诊人信息
    private void writeJzInfo() {
        lltJZinfo.setVisibility(View.VISIBLE);
        etName.setEnabled(true);
        etAge.setEditeEnable(true);
        etPhone.setEnabled(true);
        rbNan.setEnabled(true);
        rbNv.setEnabled(true);
        membNo = "";
        relationship = 4;
        pAccid = "";
        etName.setText("");
        etAge.setEditeText("");
        etPhone.setText("");
        rgSex.check(R.id.rb_nan);
        KeyBoardUtils.showKeyBoard(etPhone, this);
    }

    //选择 匹配的就诊人（第二类）
    private void chooseJzrPopup(PatientFamilyBean.JiuzhenBean bean) {
//        etPhone.setEditeText(TextUtils.isEmpty(bean.phone) ? "" : bean.phone);
        etName.setText(RegexUtil.getNameSubString(bean.patient_name));
        etName.setSelection(etName.getText().length());
        etAge.setEditeText(bean.age > 0 ? (bean.age + "") : "");
        rgSex.check(bean.sex == 0 ? R.id.rb_nan : R.id.rb_nv);
        relationship = 4;//关系 其他
        membNo = "";
        pAccid = "";
        KeyBoardUtils.hideKeyBoard(etName, this);
    }

    //数据检测
    private void checkData() {
        if (TextUtils.isEmpty(imgPath1)
                && TextUtils.isEmpty(imgPath2)
                && TextUtils.isEmpty(imgPath3)) {
            ToastUtil.showShort("请上传处方照片");
            return;
        }

        if (TextUtils.isEmpty(etDrugstore.getText())) {
            commonDialog = new CommonDialog(this, "请选择药房");
            commonDialog.show();
            return;
        }

        //图片路径拼接
        StringBuffer imgPath = new StringBuffer();
        if (!TextUtils.isEmpty(imgPath1)) {
            imgPath.append(imgPath1).append(",");
        }
        if (!TextUtils.isEmpty(imgPath2)) {
            imgPath.append(imgPath2).append(",");
        }
        if (!TextUtils.isEmpty(imgPath3)) {
            imgPath.append(imgPath3);
        }

        Params params = new Params();
        params.put("source", formParent == 0 ? 1 : 2);//来源：1：首页，2：聊天
        //患者编号
        if (!TextUtils.isEmpty(membNo)) {
            params.put("memb_no", membNo);
        }
        params.put("relationship", relationship);
        //就诊人信息
        params.put("name", etName.getText().toString().trim());
        params.put("sex", sexType);
        params.put("age", etAge.getEditText().getText());
        params.put("phone", etPhone.getText().toString().trim());

        params.put("store_id", storeId);
        params.put("drug_class", drugClassId);
        params.put("drug_type", rbComm.isChecked() ? 1 : 0);//0：精品 1：普药
        params.put("img_url", imgPath.toString());
        params.put("remark", etRemark.getText().toString().trim());//备注
        //补充收费
        if (!TextUtils.isEmpty(etServerprice.getText().toString().trim())) {
            params.put("service_price", etServerprice.getText().toString().trim());
        }

        mPresenter.openPaperCamera(params);
    }

    //照相机公用file
    public File cameraPath;

    //1:打开相机拍照 2:打开相册
    private void openCameraOrPhoto(boolean isCamera) {
        //根据路径拍照并存储照片
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions.setLogging(BuildConfig.DEBUG);
        rxPermissions
                .request(isCamera ? new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA} : new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
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
//                                chooseImage();
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

    //选择图片 多张
    private void chooseImage() {
        Matisse.from(this)
//                .choose(MimeType.ofImage())
                .choose(MimeType.ofImage(), false)
                .capture(false)//是否提供拍照功能
                .captureStrategy(new CaptureStrategy(true, getPackageName() + ".fileprovider"))//存储到哪里
                .countable(true)//有序选择图片
                .maxSelectable(3) //选择的最大数量
                .gridExpectedSize(UIUtils.dp2px(this, 120))
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED)//图像选择和预览活动所需的方向。
                .thumbnailScale(0.85f) // 缩略图的比例
                .theme(R.style.Matisse_Zhihu)//主题  暗色主题 R.style.Matisse_Dracula
                .imageEngine(new Glide4Engine()) // 使用的图片加载引擎
                .forResult(REQUEST_ALBUM_CODE); // 设置作为标记的请求码
    }

    //临时path  显示用，不需要再加载上传后的path
    private String tempPath;

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
                        tempPath = cameraPath.getAbsolutePath();
                        mPresenter.uploadImg(tempPath, Constant.UPLOADIMG_TYPE_2);
                    }
                    break;
                //相册
                case REQUEST_ALBUM_CODE:
                    Uri uri = data.getData();
                    String imagePath;
                    if (uri != null && !TextUtils.isEmpty(imagePath = UriUtil.getRealFilePath(this, uri))) {
                        LogUtil.d("headerPath=" + imagePath);
                        tempPath = imagePath;
                        mPresenter.uploadImg(imagePath, Constant.UPLOADIMG_TYPE_2);
                    }

//                    List<Uri> mSelected = Matisse.obtainResult(data);
//                    if (mSelected != null && !mSelected.isEmpty()) {
//                      todo 循环上传，修改显示逻辑
//                    }
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
            case OpenPaperPresenter.UPLOADIMF_OK://上传成功
                UploadImgBean uploadImgBean = (UploadImgBean) message.obj;
                switch (currImg) {
                    case R.id.iv_img1:
                        imgPath1 = uploadImgBean.url;
                        ImageUtil.showImage(tempPath, ivImg1);
                        ivImg1Clean.setVisibility(View.VISIBLE);
                        break;
                    case R.id.iv_img2:
                        imgPath2 = uploadImgBean.url;
                        ImageUtil.showImage(tempPath, ivImg2);
                        ivImg2Clean.setVisibility(View.VISIBLE);
                        break;
                    case R.id.iv_img3:
                        imgPath3 = uploadImgBean.url;
                        ImageUtil.showImage(tempPath, ivImg3);
                        ivImg3Clean.setVisibility(View.VISIBLE);
                        break;
                }
                ToastUtil.showShort("上传成功");
                break;
            case OpenPaperPresenter.UPLOADIMF_ERROR://上传失败
                switch (currImg) {
                    case R.id.iv_img1:
                        imgPath1 = "";
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
            case OpenPaperPresenter.GET_JZR_BY_PHONE://提示 就诊人数据
                jzrList.clear();
                List<PatientFamilyBean.JiuzhenBean> list = (List<PatientFamilyBean.JiuzhenBean>) message.obj;
                if (list == null || list.isEmpty()) {//没有查询到 不显示
                    return;
                } else {
                    jzrList.addAll(list);
                    initJzrPopup();
                }
                break;
            case OpenPaperPresenter.OPENPAPER_CAMERA_OK://开方ok
                String msg = message.obj.toString();
                //可以拿到paccid 就记录，没有就不记录
                if (!TextUtils.isEmpty(pAccid)) {
                    mPresenter.addChatRecord(NimU.getNimAccount(), pAccid, Constant.CHAT_RECORD_TYPE_3, formParent);
                }
                if (formParent == 1) {//聊天开方
                    setResult(RESULT_OK, new Intent());
                    finish();
                } else {//普通开方
                    commonDialog = new CommonDialog(this, true, TextUtils.isEmpty(msg) ? "处方已上传至药房" : msg, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    });
                    commonDialog.show();
                }
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_CHOOSE_PATIENT://选择患者-就诊人
            case EventConfig.EVENT_KEY_CHOOSE_JZR://选择就诊人
                PatientFamilyBean.JiuzhenBean bean = (PatientFamilyBean.JiuzhenBean) event.getData();
                if (bean != null) {
                    chooseJzInfo(bean);
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        commonDialog = new CommonDialog(this, false, "确定要退出开方吗？", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.btn_ok) {
                    finish();
                }
            }
        });
        commonDialog.show();
    }

    @Override
    protected boolean useEventBus() {
        return true;
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
