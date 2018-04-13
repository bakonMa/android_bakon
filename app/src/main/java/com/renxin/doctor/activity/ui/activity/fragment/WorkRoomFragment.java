package com.renxin.doctor.activity.ui.activity.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.utils.UIUtils;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.EventConfig;
import com.renxin.doctor.activity.data.eventbus.Event;
import com.renxin.doctor.activity.injection.components.DaggerFragmentComponent;
import com.renxin.doctor.activity.injection.modules.FragmentModule;
import com.renxin.doctor.activity.ui.activity.mine.AuthStep1Activity;
import com.renxin.doctor.activity.ui.activity.login.LoginActivity;
import com.renxin.doctor.activity.ui.adapter.ExamplePagerAdapter;
import com.renxin.doctor.activity.ui.base.BaseFragment;
import com.renxin.doctor.activity.ui.contact.PersonalContact;
import com.renxin.doctor.activity.utils.ImageUtil;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.netease.nimlib.sdk.Observer;
import com.netease.nimlib.sdk.auth.constant.LoginSyncStatus;
import com.trello.rxlifecycle.LifecycleTransformer;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;

/**
 * WorkRoomFragment
 * Create at 2018/3/31 上午9:27 by mayakun
 */

public class WorkRoomFragment extends BaseFragment implements PersonalContact.View {

    @BindView(R.id.viewflipper)
    ViewFlipper viewflipper;
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.img1)
    ImageView img1;
    @BindView(R.id.img2)
    ImageView img2;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.pagertabstrip)
    PagerTabStrip pagertabstrip;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private String[] strings = {"fragment_workroom1", "fragment_workroom2", "fragment_workroom3"};

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build().inject(this);
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_workroom;
    }

    @Override
    protected void initView() {
        initToolbar();
        for (int i = 0; i < 3; i++) {
            View view = LayoutInflater.from(getContext()).inflate(R.layout.item_main, null);
            ((TextView) view.findViewById(R.id.tv_num)).setText(strings[i]);
            viewflipper.addView(view);
        }
        String url = "http://img1.3lian.com/2015/a1/105/d/40.jpg";
        ImageUtil.showCircleImage(url, img1);
        ImageUtil.showRoundImage(url, 50, img2);

        ExamplePagerAdapter adapter = new ExamplePagerAdapter(mDataList);
        viewPager.setAdapter(adapter);
        initMagicInd();
        initPagerTab();

//        NimUIKit.login(new LoginInfo("123456", "123456"), new RequestCallback<LoginInfo>() {
//            @Override
//            public void onSuccess(LoginInfo loginInfo) {
//                ToastUtil.show("Accont=" + loginInfo.getAccount());
//                NIMClient.getService(AuthServiceObserver.class).observeLoginSyncDataStatus(loginSyncStatusObserver, true);
//            }
//
//            @Override
//            public void onFailed(int i) {
//                ToastUtil.show("onFailed=" + i);
//
//            }
//
//            @Override
//            public void onException(Throwable throwable) {
//                LogUtil.d(throwable.getMessage());
//
//            }
//        });

    }

    boolean isSyncComplete;
    private Observer<LoginSyncStatus> loginSyncStatusObserver = new Observer<LoginSyncStatus>() {
        @Override
        public void onEvent(LoginSyncStatus loginSyncStatus) {
            isSyncComplete = (loginSyncStatus == LoginSyncStatus.SYNC_COMPLETED ||
                    loginSyncStatus == LoginSyncStatus.NO_BEGIN);
            ToastUtil.show("isSyncComplete=" + isSyncComplete);
        }
    };

    private static final String[] CHANNELS = new String[]{"CUPCAKE", "DONUT", "HONEYCOMB"};
    private List<String> mDataList = Arrays.asList(CHANNELS);

    private void initMagicInd() {
        magicIndicator.setBackgroundColor(Color.parseColor("#00c853"));
        CommonNavigator commonNavigator = new CommonNavigator(getContext());
//        commonNavigator.setScrollPivotX(0.25f);
        commonNavigator.setAdjustMode(true);//平分模式
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return mDataList == null ? 0 : mDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(mDataList.get(index));
                clipPagerTitleView.setTextColor(Color.parseColor("#f2c4c4"));
                clipPagerTitleView.setClipColor(Color.WHITE);
                clipPagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return clipPagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_MATCH_EDGE);
//                indicator.setYOffset(UIUtil.dip2px(context, 5));
                indicator.setColors(UIUtils.getColor(R.color.color_c1944c));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator, viewPager);

    }

    //使用原生 指示器
    private void initPagerTab() {
        pagertabstrip.setTextColor(UIUtils.getColor(R.color.color_5b6187));// 标题颜色，这里需要带透明度的颜色值
        pagertabstrip.setTabIndicatorColor(Color.RED);// 指示器颜色，这里需要带透明度的颜色值
        pagertabstrip.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);// 字体大小
    }


    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(getActivity()))
                .setTitle("工作室")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setRightText("认证", true, R.color.color_popup_btn)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        startActivity(new Intent(getContext(), LoginActivity.class));
                    }

                    @Override
                    public void rightClick() {
                        super.rightClick();
                        startActivity(new Intent(getContext(), AuthStep1Activity.class));
                    }
                }).bind();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.REFRESH_PERSONAL://刷新个人资料
                    break;
                case EventConfig.REFRESH_MESSAGE_RED_POINT://红点消除
                    break;
            }
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
    }

    @Override
    public void onSuccess(Message message) {

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
