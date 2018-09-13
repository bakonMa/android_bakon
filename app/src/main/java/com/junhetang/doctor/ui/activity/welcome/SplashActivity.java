package com.junhetang.doctor.ui.activity.welcome;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.junhetang.doctor.BuildConfig;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.SPConfig;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.widget.SplashIndicator;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * SplashActivity  介绍页
 * Create at 2018/6/5 下午2:20 by mayakun
 */

public class SplashActivity extends BaseActivity {

    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.id_indicator)
    SplashIndicator idIndicator;
    @BindView(R.id.tv_enter)
    TextView tvEnter;

    List<Integer> imgList = new ArrayList<>();
    private WelcomeAdapter welcomeAdapter;
    private SparseArray<ImageView> mViewList = new SparseArray<ImageView>();

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

        imgList.add(R.drawable.welcome_1);
        imgList.add(R.drawable.welcome_2);
        imgList.add(R.drawable.welcome_3);
        imgList.add(R.drawable.welcome_4);
        welcomeAdapter = new WelcomeAdapter(this);

        viewPager.setOffscreenPageLimit(1);
        viewPager.setAdapter(welcomeAdapter);
        //状态条
        idIndicator.setViewPager(viewPager);
        //最后一页不显示 状态条
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                idIndicator.setVisibility(position == imgList.size() - 1 ? View.GONE : View.VISIBLE);
                tvEnter.setVisibility(position == imgList.size() - 1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.tv_enter)
    public void enterClick() {
        if (viewPager.getCurrentItem() == imgList.size() - 1) {
            DocApplication.getAppComponent().dataRepo().appSP().setInteger(SPConfig.LAST_ENTER_CODE, BuildConfig.VERSION_CODE);
            startActivity(new Intent(this, TranslucentActivity.class));
            finish();
        }
    }

    class WelcomeAdapter extends PagerAdapter {
        private Context context;

        public WelcomeAdapter(Context context) {
            this.context = context;
        }

        @Override
        public int getCount() {
            return imgList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            ImageView view = mViewList.get(position);
            if (view == null) {
                view = new ImageView(actContext());
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                view.setImageBitmap(ImageUtil.readBitMap(context, imgList.get(position)));
                view.setLayoutParams(params);
                mViewList.append(position, view);
            } else {
                view.setImageBitmap(ImageUtil.readBitMap(context, imgList.get(position)));
            }
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            ImageView imageView = mViewList.get(position);
            //优化 防止oom
            if (imageView != null) {
                BitmapDrawable bitmapDrawable = (BitmapDrawable) imageView.getDrawable();
                if (bitmapDrawable != null) {
                    Bitmap bm = bitmapDrawable.getBitmap();
                    if (bm != null && !bm.isRecycled()) {
                        imageView.setImageResource(0);
                        bm.recycle();
                    }
                }
            }
            container.removeView((View) object);
        }
    }

}
