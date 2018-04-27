package com.renxin.doctor.activity.ui.activity.fragment;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.ui.activity.fragment.FindFragment;
import com.renxin.doctor.activity.ui.activity.fragment.MineFragment;
import com.renxin.doctor.activity.ui.activity.fragment.PatientFragment;
import com.renxin.doctor.activity.ui.activity.fragment.WorkRoomFragment;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.widget.BottomBarItem;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @BindView(R.id.tab_home)
    BottomBarItem tabHome;
    @BindView(R.id.tab_patient)
    BottomBarItem tabPatient;
    @BindView(R.id.tab_find)
    BottomBarItem tabFind;
    @BindView(R.id.tab_me)
    BottomBarItem tabMe;

    private HashMap<Integer, Fragment> mFragmentMap = new HashMap<>();
    private int currTag = 0;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_main_jht;
    }

    @Override
    protected void initView() {
        tabHome.setUnreadNum(100);
        tabPatient.setUnreadNum(10);
        tabFind.showNotify();
        tabMe.showNotify();

        mFragmentMap.put(0, new WorkRoomFragment());
        mFragmentMap.put(1, new PatientFragment());
        mFragmentMap.put(2, new FindFragment());
        mFragmentMap.put(3, new MineFragment());

//        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//        transaction.add(fragment对象);
//        transaction.addToBackStack(null);
//        transaction.commit();

        switchFrgment(R.id.tab_home);
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
        if (currTag == viewId) {
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
                    mFragmentMap.put(1, new PatientFragment());
                }
                transaction.replace(R.id.id_content, mFragmentMap.get(1));
                break;
            case R.id.tab_find:
                if (mFragmentMap.get(2) == null) {
                    mFragmentMap.put(2, new FindFragment());
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
    protected boolean useEventBus() {
        return false;
    }

    @Override
    protected void setupActivityComponent() {

    }
}
