package com.jht.doctor.ui.activity.mine.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.jht.doctor.R;
import com.jht.doctor.ui.base.BaseAppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * mayakun 2018/1/8
 * 主页
 */
public class HomeActivity extends BaseAppCompatActivity {

    @BindView(R.id.tab_home)
    LinearLayout tabHome;
    @BindView(R.id.tab_order)
    LinearLayout tabOrder;
    @BindView(R.id.tab_me)
    LinearLayout tabMe;
    @BindView(R.id.home_viewpage)
    ViewPager homeViewpage;
    private HomeFragmentAdapter fragmentAdapter;
    private List<Fragment> fragmentList;
    private int currPos = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        changeStatus();
        fragmentList = new ArrayList<>();
        fragmentList.add(new TestFragment());
        fragmentList.add(new TestFragment());
        fragmentList.add(new TestFragment());

        fragmentAdapter = new HomeFragmentAdapter(getSupportFragmentManager(), fragmentList);
        homeViewpage.setCurrentItem(0);
        homeViewpage.setOffscreenPageLimit(2);

        homeViewpage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(currPos == position){
                    return;
                }
                currPos = position;
                changeStatus();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        homeViewpage.setAdapter(fragmentAdapter);
    }

    @OnClick({R.id.tab_home, R.id.tab_order, R.id.tab_me})
    void tabOnclick(View view) {
        switch (view.getId()) {
            case R.id.tab_home:
                currPos = 0;
                break;
            case R.id.tab_order:
                currPos = 1;
                break;
            case R.id.tab_me:
                currPos = 2;
                break;
        }
        changeStatus();
    }

    @Override
    protected void setupActivityComponent() {

    }

    public class HomeFragmentAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public HomeFragmentAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    private void changeStatus() {
        homeViewpage.setCurrentItem(currPos);
        tabHome.setSelected(currPos == 0);
        tabOrder.setSelected(currPos == 1);
        tabMe.setSelected(currPos == 2);
    }

}

