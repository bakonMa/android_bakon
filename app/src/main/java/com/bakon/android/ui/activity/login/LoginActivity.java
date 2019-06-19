package com.bakon.android.ui.activity.login;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;

import com.bakon.android.R;
import com.bakon.android.ui.adapter.CommonViewpageFragmentAdapter;
import com.bakon.android.ui.base.BaseActivity;
import com.bakon.android.utils.ToastUtil;
import com.bakon.android.utils.UIUtils;
import com.bakon.android.widget.toolbar.ToolbarBuilder;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.UIUtil;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ClipPagerTitleView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * mayakun 2017/11/15
 * 登录画面
 */
public class LoginActivity extends BaseActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private String[] titles = {"验证码登录", "密码登录"};
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("登录")
                .setStatuBar(R.color.white)
                .blank()
                .setLeft(false)
                .bind();
        //未登录，sign错误
        String msg = getIntent().getStringExtra("msg");
        if (!TextUtils.isEmpty(msg)) {
            ToastUtil.show(msg);
        }

        fragmentList.add(LoginFragment.newInstance(0));
        fragmentList.add(LoginFragment.newInstance(1));

        initViewpager();
    }

    //初始化指示器
    private void initViewpager() {
        magicIndicator.setBackgroundColor(UIUtils.getColor(R.color.white));
        CommonNavigator commonNavigator = new CommonNavigator(this);
//        commonNavigator.setScrollPivotX(0.25f);
        commonNavigator.setAdjustMode(true);//平分模式
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titles == null ? 0 : titles.length;
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                ClipPagerTitleView clipPagerTitleView = new ClipPagerTitleView(context);
                clipPagerTitleView.setText(titles[index]);
                clipPagerTitleView.setTextSize(UIUtils.sp2px(context, 16));
                clipPagerTitleView.setTextColor(UIUtils.getColor(R.color.color_000));
                clipPagerTitleView.setClipColor(UIUtils.getColor(R.color.color_main));
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
                indicator.setYOffset(UIUtil.dip2px(context, 0.5));
                indicator.setColors(UIUtils.getColor(R.color.color_main));
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);

        //添加分割线
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerPadding(UIUtil.dip2px(this, 10));
        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.splitter));

        CommonViewpageFragmentAdapter viewPageAdapter = new CommonViewpageFragmentAdapter(getSupportFragmentManager(), fragmentList, titles);
        viewPager.setCurrentItem(0);
        viewPager.setOffscreenPageLimit(0);
        viewPager.setAdapter(viewPageAdapter);

        ViewPagerHelper.bind(magicIndicator, viewPager);
    }


    @Override
    protected void setupActivityComponent() {

    }

}
