package com.renxin.doctor.activity.ui.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.EventConfig;
import com.renxin.doctor.activity.data.eventbus.Event;
import com.renxin.doctor.activity.injection.components.DaggerFragmentComponent;
import com.renxin.doctor.activity.injection.modules.FragmentModule;
import com.renxin.doctor.activity.ui.activity.mine.AboutUsActivity;
import com.renxin.doctor.activity.ui.activity.mine.AuthStep1Activity;
import com.renxin.doctor.activity.ui.activity.mine.AuthStep3Activity;
import com.renxin.doctor.activity.ui.activity.mine.SetPriceActivity;
import com.renxin.doctor.activity.ui.activity.mine.SettingActivity;
import com.renxin.doctor.activity.ui.activity.mine.UserInfoActivity;
import com.renxin.doctor.activity.ui.activity.mine.wallet.WalletActivity;
import com.renxin.doctor.activity.ui.base.BaseFragment;
import com.renxin.doctor.activity.ui.bean_jht.UserBaseInfoBean;
import com.renxin.doctor.activity.ui.contact.PersonalContact;
import com.renxin.doctor.activity.ui.presenter.PersonalPresenter;
import com.renxin.doctor.activity.utils.ImageUtil;
import com.renxin.doctor.activity.utils.ShareSDKUtils;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.utils.U;
import com.renxin.doctor.activity.widget.RelativeWithImage;
import com.renxin.doctor.activity.widget.popupwindow.SharePopupWindow;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * MineFragment
 * Create at 2018/3/25 下午5:37 by mayakun
 */

public class MineFragment extends BaseFragment implements PersonalContact.View {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.id_swipe)
    SwipeRefreshLayout idSwipe;
    @BindView(R.id.id_iv_head)
    ImageView idIvHead;
    @BindView(R.id.tv_authstatus)
    TextView tvAuthstatus;
    @BindView(R.id.tv_authmsg)
    TextView tvAuthmsg;
    @BindView(R.id.llt_auth_status)
    LinearLayout lltAuthStatus;
    @BindView(R.id.tv_username)
    TextView tvUsername;
    @BindView(R.id.tv_usersex)
    TextView tvUsersex;
    @BindView(R.id.tv_userinfo)
    TextView tvUserinfo;
    @BindView(R.id.llt_user_info)
    LinearLayout lltUserInfo;
    @BindView(R.id.id_wallet)
    RelativeWithImage idWallet;
    @BindView(R.id.id_set_price)
    RelativeWithImage idSetPrice;

    @Inject
    PersonalPresenter mPresenter;
    private UserBaseInfoBean baseInfoBean;

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build().inject(this);
    }

    @Override
    protected void initView() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(getActivity()))
                .setTitle("个人中心")
                .setStatuBar(R.color.white)
                .blank()
                .setLeft(false)
                .bind();

        idSwipe.setColorSchemeColors(getResources().getColor(R.color.color_main));
        idSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                changeViewStatus(U.getAuthStatus());
            }
        });
        changeViewStatus(U.getAuthStatus());
    }

    @OnClick({R.id.llt_auth_status, R.id.llt_user_info, R.id.id_collect,
            R.id.id_wallet, R.id.id_set_price, R.id.id_setting, R.id.id_recommend, R.id.id_about})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.llt_user_info:
                startActivity(new Intent(actContext(), UserInfoActivity.class));
                break;
            case R.id.llt_auth_status:
                switch (U.getAuthStatus()) {//状态值，0：未认证 1：审核中；2：审核通过 3：审核失败
                    case 0:
                    case 3:
                        startActivity(new Intent(actContext(), AuthStep1Activity.class));
                        break;
                    case 1:
                        startActivity(new Intent(actContext(), AuthStep3Activity.class));
                        break;
                }
                break;
            case R.id.id_collect:
                break;
            case R.id.id_wallet:
                startActivity(new Intent(actContext(), WalletActivity.class));
                break;
            case R.id.id_set_price:
                startActivity(new Intent(actContext(), SetPriceActivity.class));
                break;
            case R.id.id_setting:
                startActivity(new Intent(actContext(), SettingActivity.class));
                break;
            case R.id.id_recommend://分享app
                SharePopupWindow sharePopupWindow = new SharePopupWindow(actContext(), new SharePopupWindow.ShareOnClickListener() {
                    @Override
                    public void onItemClick(SHARE_MEDIA shareType) {
                        ShareSDKUtils.shareApp(getActivity(), shareType);
                    }
                });
                sharePopupWindow.showAtLocation(idSwipe, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.id_about:
                startActivity(new Intent(actContext(), AboutUsActivity.class));
                break;
        }
    }

    //根据认证状态，修改UI
    //状态值，0：未认证 1：审核中；2：审核通过 3：审核失败
    private void changeViewStatus(int status) {
        if (idSwipe.isRefreshing()) {
            idSwipe.setRefreshing(false);
        }
        switch (status) {
            case 0:
                lltAuthStatus.setVisibility(View.VISIBLE);
                lltUserInfo.setVisibility(View.GONE);
                idWallet.setVisibility(View.GONE);
                idSetPrice.setVisibility(View.GONE);
                tvAuthstatus.setText(R.string.str_auth_0);
                tvAuthmsg.setText(R.string.str_auth_msg_0);
                break;
            case 1:
                lltAuthStatus.setVisibility(View.VISIBLE);
                lltUserInfo.setVisibility(View.GONE);
                idWallet.setVisibility(View.GONE);
                idSetPrice.setVisibility(View.GONE);
                tvAuthstatus.setText(R.string.str_auth_1);
                tvAuthmsg.setText(R.string.str_auth_msg_1);
                break;
            case 2://请求个人资料
                lltAuthStatus.setVisibility(View.GONE);
                lltUserInfo.setVisibility(View.VISIBLE);
                idWallet.setVisibility(View.VISIBLE);
                idSetPrice.setVisibility(View.VISIBLE);
                //获取基本信息
                mPresenter.getUserBasicInfo();
                break;
            case 3:
                lltAuthStatus.setVisibility(View.VISIBLE);
                lltUserInfo.setVisibility(View.GONE);
                idWallet.setVisibility(View.GONE);
                idSetPrice.setVisibility(View.GONE);
                tvAuthstatus.setText(R.string.str_auth_3);
                tvAuthmsg.setText(R.string.str_auth_msg_3);
                break;
        }
    }

    @Override
    public void onSuccess(Message message) {
        if (idSwipe.isRefreshing()) {
            idSwipe.setRefreshing(false);
        }
        if (message == null) {
            return;
        }
        switch (message.what) {
            case PersonalPresenter.GET_USEBASE_INFO://个人基本资料
                baseInfoBean = (UserBaseInfoBean) message.obj;
                ImageUtil.showCircleImage(baseInfoBean.header, idIvHead);
                tvUsername.setText((TextUtils.isEmpty(baseInfoBean.name) ? "" : (baseInfoBean.name + "  ")) + (baseInfoBean.sex == 0 ? "男" : "女"));
                tvUsersex.setText(TextUtils.isEmpty(baseInfoBean.hospital) ? "" : baseInfoBean.hospital);
                tvUserinfo.setText((TextUtils.isEmpty(baseInfoBean.department) ? "" : baseInfoBean.department + "  ") +
                        (TextUtils.isEmpty(baseInfoBean.title) ? "" : baseInfoBean.title));
                break;
        }

    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        //下来刷新关闭
        if (idSwipe.isRefreshing()) {
            idSwipe.setRefreshing(false);
        }
        ToastUtil.show(errorMsg);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.EVENT_KEY_AUTH_STATUS://刷新个人认证状态
                    U.setAuthStatus(1);//本地数据修改
                    changeViewStatus(1);//修改UI状态
                    break;
            }
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public Activity provideContext() {
        return getActivity();
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }

}
