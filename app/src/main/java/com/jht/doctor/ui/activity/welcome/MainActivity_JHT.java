package com.jht.doctor.ui.activity.welcome;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.jht.doctor.R;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.fragment.HomeFragment;
import com.jht.doctor.ui.fragment.MineFragment;
import com.jht.doctor.ui.fragment.OrderFragment;
import com.jht.doctor.ui.fragment.WorkRoomFragment;
import com.jht.doctor.widget.BottomBarItem;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity_JHT extends BaseAppCompatActivity {

    @BindView(R.id.tab_home)
    BottomBarItem tabHome;
    @BindView(R.id.tab_patient)
    BottomBarItem tabPatient;
    @BindView(R.id.tab_find)
    BottomBarItem tabFind;
    @BindView(R.id.tab_me)
    BottomBarItem tabMe;

    private HashMap<Integer, Fragment> mFragmentMap = new HashMap<>();
    private int currTag = R.id.tab_home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_jht);
        ButterKnife.bind(this);
        initView();
        switchFrgment(currTag);
        tabHome.setUnreadNum(100);
        tabPatient.setUnreadNum(10);
        tabFind.showNotify();
        tabMe.showNotify();

    }

    private void initView() {

    }

    @OnClick({R.id.tab_home, R.id.tab_patient, R.id.tab_find, R.id.tab_me})
    public void tabOnClick(View view) {
        switch (view.getId()) {
            case R.id.tab_home:
            case R.id.tab_patient:
            case R.id.tab_find:
            case R.id.tab_me:
                switchFrgment(view.getId());
                break;
        }
    }

    //切换fragment
    private void switchFrgment(int viewId) {
        if (!mFragmentMap.isEmpty() && currTag == viewId) {
            return;
        }

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        switch (viewId) {
            case R.id.tab_home:
                if (mFragmentMap.get(0) == null) {
                    mFragmentMap.put(0, new WorkRoomFragment());
                }
                transaction.replace(R.id.id_content, mFragmentMap.get(0));
                break;
            case R.id.tab_patient:
                if (mFragmentMap.get(1) == null) {
                    mFragmentMap.put(1, new HomeFragment());
                }
                transaction.replace(R.id.id_content, mFragmentMap.get(1));
                break;
            case R.id.tab_find:
                if (mFragmentMap.get(2) == null) {
                    mFragmentMap.put(2, new OrderFragment());
                }
                transaction.replace(R.id.id_content, mFragmentMap.get(2));
                break;
            case R.id.tab_me:
                if (mFragmentMap.get(3) == null) {
                    mFragmentMap.put(3, new MineFragment());
                }
                transaction.replace(R.id.id_content, mFragmentMap.get(3));
                break;
        }
        transaction.commit();
        currTag = viewId;
        setTabState();
    }

    private void setTabState() {
        tabHome.setStatus(currTag == R.id.tab_home);
        tabPatient.setStatus(currTag == R.id.tab_patient);
        tabFind.setStatus(currTag == R.id.tab_find);
        tabMe.setStatus(currTag == R.id.tab_me);
    }

    @Override
    protected boolean isUseEventBus() {
        return false;
    }

    @Override
    protected void setupActivityComponent() {

    }
}
