package com.renxin.doctor.activity.ui.activity.home;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.ui.adapter.CommonViewpageFragmentAdapter;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.utils.UIUtils;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;

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
 * CheckPaperActivity 审核处方
 * Create at 2018/5/4 下午3:41 by mayakun
 */
public class CheckPaperActivity extends BaseActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private String[] titles = {"我的处方", "患者处方"};
    private List<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_checkpaper;
    }

    @Override
    protected void initView() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("审核处方")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }

                }).bind();

        fragmentList.add(CheckPaperFragment.newInstance(1));
        fragmentList.add(CheckPaperFragment.newInstance(2));

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
