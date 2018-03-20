package com.jht.doctor.ui.activity.mine.myinfo;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.jht.doctor.R;
import com.jht.doctor.ui.activity.mine.PersonalActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.bean.MyInfoBean;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * mayakun
 * 我的资料
 */
public class ShowMyInfoActivity extends BaseAppCompatActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;

    //显示信息的类型
    private int infoType;
    //对应类型的fragment
    private Fragment fragment;
    private int[] titles = {R.string.info_base, R.string.info_job, R.string.info_house};
    private MyInfoBean myInfoBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_my_info);
        ButterKnife.bind(this);
        infoType = getIntent().getIntExtra("type", 0);
        myInfoBean = getIntent().getParcelableExtra(PersonalActivity.KEY_INFO);
        //title
        initToolbar();
        //具体详情
        Bundle bundle = new Bundle();
        switch (infoType) {
            case 0://基本信息
                fragment = new BaseInfoFragment();
                if (myInfoBean != null) {
                    bundle.putParcelable("info", myInfoBean.userDTO);
                }
                break;
            case 1://工作信息
                fragment = new JobInfoFragment();
                if (myInfoBean != null) {
                    bundle.putParcelable("info", myInfoBean.userJobDTO);
                }
                break;
            case 2://房产信息
                fragment = new HouseInfoFragment();
                if (myInfoBean != null) {
                    bundle.putParcelable("info", myInfoBean.userHouseDTO);
                }
                break;
        }
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.frame_layout, fragment).commit();
    }

    //title
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<AppCompatActivity>(this))
                .setTitle(getResources().getString(titles[infoType]))
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

    @Override
    protected void setupActivityComponent() {
    }

}
