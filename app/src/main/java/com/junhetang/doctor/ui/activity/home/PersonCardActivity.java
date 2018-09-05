package com.junhetang.doctor.ui.activity.home;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.activity.patient.AddPatientActivity;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.bean.UserBaseInfoBean;
import com.junhetang.doctor.ui.contact.PersonalContact;
import com.junhetang.doctor.ui.presenter.PersonalPresenter;
import com.junhetang.doctor.utils.FileUtil;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.utils.ShareSDKUtils;
import com.junhetang.doctor.utils.U;
import com.junhetang.doctor.utils.UmengKey;
import com.junhetang.doctor.widget.dialog.CommonDialog;
import com.junhetang.doctor.widget.popupwindow.SharePopupWindow;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.umeng.analytics.MobclickAgent;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * PersonCardActivity 个人名片
 * Create at 2018/9/3 上午10:56 by mayakun
 */
public class PersonCardActivity extends BaseActivity implements PersonalContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.llt_root)
    LinearLayout lltRoot;
    @BindView(R.id.rlt_person_info)
    RelativeLayout rltPersonInfo;
    @BindView(R.id.iv_headimg)
    ImageView ivHeadimg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_cardimg)
    ImageView tvCardimg;

    @Inject
    PersonalPresenter mPresenter;

    private SharePopupWindow sharePopupWindow;
    private UserBaseInfoBean userInfo;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_person_card;
    }

    @Override
    protected void initView() {
        initToolbar();
        userInfo = U.getUserInfo();
        if (userInfo == null || TextUtils.isEmpty(userInfo.qrcode)) {
            mPresenter.getUserBasicInfo();
        } else {
            showInfo();
        }
    }

    //显示数据
    private void showInfo() {
        if (userInfo == null) {
            return;
        }
        ImageUtil.showCircleImage(userInfo.header, ivHeadimg);
        ImageUtil.showImage(userInfo.qrcode, tvCardimg);
        tvName.setText(TextUtils.isEmpty(userInfo.name) ? "" : userInfo.name);
        tvTitle.setText(TextUtils.isEmpty(userInfo.title) ? "" : userInfo.title);
    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("个人名片")
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


    @OnClick({R.id.dtv_share, R.id.dtv_save, R.id.tv_add})
    public void onBtnClick(View view) {
        switch (view.getId()) {
            case R.id.dtv_share:
                if (sharePopupWindow != null && sharePopupWindow.isShowing()) {
                    return;
                }
                sharePopupWindow = new SharePopupWindow(actContext(), new SharePopupWindow.ShareOnClickListener() {
                    @Override
                    public void onItemClick(SHARE_MEDIA shareType) {
                        ShareSDKUtils.share(PersonCardActivity.this, shareType,
                                userInfo.header, userInfo.share_link, userInfo.share_title, userInfo.share_desc, null);
                    }
                });
                sharePopupWindow.showAtLocation(lltRoot, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.dtv_save:
                //webview 生成图片保存相册
                FileUtil.saveViewToImage(actContext(), rltPersonInfo);
                break;
            case R.id.tv_add:
                //Umeng 埋点
                MobclickAgent.onEvent(this, UmengKey.personcard_add);
                startActivity(new Intent(this, AddPatientActivity.class));
                break;
        }
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
        if (message == null) {
            return;
        }
        switch (message.what) {
            case PersonalPresenter.GET_USEBASE_INFO://个人基本资料
                userInfo = U.getUserInfo();
                showInfo();
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

}
