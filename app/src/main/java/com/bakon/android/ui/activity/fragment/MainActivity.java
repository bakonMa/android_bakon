package com.bakon.android.ui.activity.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;

import com.bakon.android.R;
import com.bakon.android.application.MyApplication;
import com.bakon.android.config.EventConfig;
import com.bakon.android.data.eventbus.Event;
import com.bakon.android.di.components.DaggerActivityComponent;
import com.bakon.android.di.modules.ActivityModule;
import com.bakon.android.ui.base.BaseActivity;
import com.bakon.android.ui.base.BaseView;
import com.bakon.android.ui.presenter.CommonPresenter;
import com.bakon.android.utils.U;
import com.bakon.android.widget.BottomBarItem;
import com.trello.rxlifecycle2.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity implements BaseView{

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
    private static boolean firstEnter = true; // app是否应打开过

    private CommonPresenter commonPresenter;
    @Override
    protected int provideRootLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        commonPresenter = new CommonPresenter(this);
        switchFrgment(R.id.tab_home);
    }

    @Override
    public void onResume() {
        super.onResume();
        //处理intent 分发进入不同activity
        if (firstEnter) {
            firstEnter = false;
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //处理intent 分发进入不同activity
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
        //隐藏所有fragment
        hideFragment(transaction);

        switch (viewId) {
            case R.id.tab_home:
                if (null == mFragmentMap.get(0)) {
                    mFragmentMap.put(0, new WorkRoomFragment());
                }
                //显示需要显示的fragment
                if (mFragmentMap.get(0).isAdded()) {
                    transaction.show(mFragmentMap.get(0));
                } else {
                    transaction.add(R.id.id_content, mFragmentMap.get(0)).show(mFragmentMap.get(0));
                }
                break;
            case R.id.tab_patient:
                if (null == mFragmentMap.get(1)) {
                    mFragmentMap.put(1, new PatientFragment());
                }
                //显示需要显示的fragment
                if (mFragmentMap.get(1).isAdded()) {
                    transaction.show(mFragmentMap.get(1));
                } else {
                    transaction.add(R.id.id_content, mFragmentMap.get(1)).show(mFragmentMap.get(1));
                }
                break;
            case R.id.tab_find:
                if (null == mFragmentMap.get(2)) {
                    mFragmentMap.put(2, new FindFragment());
                }
                //显示需要显示的fragment
                if (mFragmentMap.get(2).isAdded()) {
                    transaction.show(mFragmentMap.get(2));
                } else {
                    transaction.add(R.id.id_content, mFragmentMap.get(2)).show(mFragmentMap.get(2));
                }
                break;
            case R.id.tab_me:
                if (null == mFragmentMap.get(3)) {
                    mFragmentMap.put(3, new MineFragment());
                }
                //显示需要显示的fragment
                if (mFragmentMap.get(3).isAdded()) {
                    transaction.show(mFragmentMap.get(3));
                } else {
                    transaction.add(R.id.id_content, mFragmentMap.get(3)).show(mFragmentMap.get(3));
                }
                break;
        }
        transaction.commitAllowingStateLoss();
        currTag = viewId;
        setTabState();
    }

    //隐藏所有的fragment
    private void hideFragment(FragmentTransaction transaction) {
        switch (currTag) {
            case 0:
                break;
            case R.id.tab_home:
                transaction.hide(mFragmentMap.get(0));
                break;
            case R.id.tab_patient:
                transaction.hide(mFragmentMap.get(1));
                break;
            case R.id.tab_find:
                transaction.hide(mFragmentMap.get(2));
                break;
            case R.id.tab_me:
                transaction.hide(mFragmentMap.get(3));
                break;
        }
    }

    private void setTabState() {
        tabHome.setStatus(currTag == R.id.tab_home);
        tabPatient.setStatus(currTag == R.id.tab_patient);
        tabFind.setStatus(currTag == R.id.tab_find);
        tabMe.setStatus(currTag == R.id.tab_me);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_REDPOINT_HOME://底部红点 工作室
                //是否有审核处方，暂时不要了
//                int checkPaperNum = U.getRedPointExt();
                int checkPaperNum = 0;
                //是否有系统消息
                int systemMsgNum = U.getRedPointSys();
                //未读消息数
                int unReadMsgNum = 0;

                //tab  是否显示红点
                if (checkPaperNum > 0 || systemMsgNum > 0 || unReadMsgNum > 0) {
                    tabHome.showNotify();
                } else {
                    tabHome.hideNotify();
                }
                break;
            case EventConfig.EVENT_KEY_REDPOINT_PATIENT://底部红点 患者
                //是否有审核处方
                int newFirNum = U.getRedPointFir();
                if (newFirNum > 0) {
                    tabPatient.showNotify();
                } else {
                    tabPatient.hideNotify();
                }
                break;
            case EventConfig.EVENT_KEY_CLOSE_CHAT://结束咨询
                break;
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(MyApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onBackPressed() {
        //模拟HOME键退出界面
        moveTaskToBack(true);
    }

    @Override
    public void onSuccess(Message message) {

    }

    @Override
    public void onError(String errorCode, String errorMsg) {

    }


    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        commonPresenter.unsubscribe();
    }
}
