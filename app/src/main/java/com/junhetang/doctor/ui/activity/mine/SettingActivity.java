package com.junhetang.doctor.ui.activity.mine;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;

import com.google.gson.Gson;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.manager.OSSManager;
import com.junhetang.doctor.nim.NimManager;
import com.junhetang.doctor.ui.activity.login.LoginActivity;
import com.junhetang.doctor.ui.activity.login.ResetPasswordActivity;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.UserBaseInfoBean;
import com.junhetang.doctor.ui.contact.LoginContact;
import com.junhetang.doctor.ui.presenter.LoginPresenter;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.utils.UriUtil;
import com.junhetang.doctor.utils.imageloader.Glide4Engine;
import com.junhetang.doctor.widget.RelativeWithText;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.dialog.LoadingDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.auth.AuthService;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.internal.entity.CaptureStrategy;

import java.lang.ref.WeakReference;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SettingActivity 设置
 * Create at 2018/4/13 下午2:13 by mayakun
 */
public class SettingActivity extends BaseActivity implements LoginContact.View, OSSManager.OSSUploadCallback {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.st_changeflag)
    Switch stMessageFlag;
    @BindView(R.id.st_chatflag)
    Switch stChatFlag;
    @BindView(R.id.rlt_message)
    RelativeLayout rltMessage;
    @BindView(R.id.rlt_chat)
    RelativeLayout rltChat;
    @BindView(R.id.tv_setprice)
    RelativeWithText tvSetPrice;

    @Inject
    LoginPresenter mPresenter;
    private UserBaseInfoBean bean;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_setting;
    }

    @Override
    protected void initView() {
        initToolbar();

        //是否认证
        boolean hasAuthOK = U.isHasAuthOK();
        rltMessage.setVisibility(hasAuthOK ? View.VISIBLE : View.GONE);
        rltChat.setVisibility(hasAuthOK ? View.VISIBLE : View.GONE);
        tvSetPrice.setVisibility(hasAuthOK ? View.VISIBLE : View.GONE);

        if (hasAuthOK) {
            //消息提醒
            stMessageFlag.setChecked(U.getMessageStatus());
            stMessageFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mPresenter.setPushStatus(b ? 1 : 0);
                }
            });
            //是否开通咨询
            bean = U.getUserInfo();
            if (bean != null) {
                stChatFlag.setChecked(bean.is_consult == 1);
            }
            stChatFlag.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    mPresenter.setChatFlag(b ? 1 : 0);
                }
            });
        }
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("设置")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                }).bind();
    }

    @OnClick({R.id.tv_setprice, R.id.tv_reset_password, R.id.tv_logout})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_setprice:
//                chooseImage();
                startActivity(new Intent(this, SetPriceActivity.class));
                break;
            case R.id.tv_reset_password:
                startActivity(new Intent(this, ResetPasswordActivity.class));
                break;
            case R.id.tv_logout:
                CommonDialog commonDialog = new CommonDialog(this, false, "确定退出当前账号吗？", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (view.getId() == R.id.btn_ok) {
//                            mPresenter.logout();
                            U.logout();
                            NIMClient.getService(AuthService.class).logout();
                            Intent intent = new Intent(DocApplication.getInstance(), LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            finish();
                        }
                    }
                });
                commonDialog.show();
                break;
        }
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .applicationComponent(DocApplication.getAppComponent())
                .activityModule(new ActivityModule(this))
                .build()
                .inject(this);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case LoginPresenter.SETPUSHSTATIS_SUCCESS://设置提醒状态
                U.setMessageStatus(stMessageFlag.isChecked());
                NimManager.setPushStatus(U.getMessageStatus());
                break;
            case LoginPresenter.SET_CHAT_FLAG_SUCCESS://是否开通在线咨询
                bean.is_consult = stChatFlag.isChecked() ? 1 : 0;
                U.saveUserInfo(new Gson().toJson(bean));
                break;
        }
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        ToastUtil.show(errorMsg);
        //请求失败 恢复原来的状态
        stMessageFlag.setChecked(U.getMessageStatus());
        stChatFlag.setChecked(bean.is_consult == 1);
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }


    private final int REQUEST_ALBUM_CODE = 102;//相册

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_ALBUM_CODE:
                    List<Uri> mSelected = Matisse.obtainResult(data);
                    if (mSelected == null || mSelected.isEmpty()) {
                        return;
                    }
                    for (Uri uri : mSelected) {
                        String imagePath;
                        if (uri != null && !TextUtils.isEmpty(imagePath = UriUtil.getRealFilePath(this, uri))) {
                            OSSManager.getInstance().uploadImageAsync("", imagePath, this);
                        }
                    }

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void uploadStatus(int type, Object obj) {
        switch (type) {
            case 1:
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        showProgressDialog(Integer.parseInt(obj.toString()));
                    }
                });

                break;
            case 2:
                LogUtil.d(obj.toString());
                break;
            case 3:
                LogUtil.d(obj.toString());
                break;
        }
    }

    private LoadingDialog loadingDialog;

    private void showProgressDialog(int progress) {
        if (progress == 100) {
            if (loadingDialog != null) {
                loadingDialog.dismiss();
            }
            return;
        }
        if (loadingDialog == null) {
            loadingDialog = new LoadingDialog(this, "上传图片中...");
            loadingDialog.show();
        } else {
            loadingDialog.setLoadingText(String.format("上传图片中...%s%%", progress));
        }
    }
}
