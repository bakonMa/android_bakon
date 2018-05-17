package com.renxin.doctor.activity.ui.activity.welcome;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.widget.SplashGuideView;
import com.renxin.doctor.activity.widget.SplashIndicator;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Tang
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.id_indicator)
    SplashIndicator idIndicator;
    ArrayList<SplashGuideView> arrayList = new ArrayList<>();
    GuideAdapter guideAdapter;

    private void initStatusBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_splash;
    }


    @Override
    protected void setupActivityComponent() {

    }

    @Override
    protected void initView() {
        initStatusBar();
        SplashGuideView guideView1 = new SplashGuideView(this, R.drawable.welcome_1, false);
        SplashGuideView guideView2 = new SplashGuideView(this, R.drawable.welcome_2, false);
        SplashGuideView guideView3 = new SplashGuideView(this, R.drawable.welcome_3, false);
        SplashGuideView guideView4 = new SplashGuideView(this, R.drawable.welcome_4, true);
        arrayList.add(guideView1);
        arrayList.add(guideView2);
        arrayList.add(guideView3);
        arrayList.add(guideView4);
        guideAdapter = new GuideAdapter();
        viewPager.setAdapter(guideAdapter);
        idIndicator.setViewPager(viewPager);
    }

    class GuideAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return arrayList.size();
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            SplashGuideView welcomeGuideView = arrayList.get(position);
            container.addView(welcomeGuideView);
            return welcomeGuideView;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(arrayList.get(position));
        }
    }

}
