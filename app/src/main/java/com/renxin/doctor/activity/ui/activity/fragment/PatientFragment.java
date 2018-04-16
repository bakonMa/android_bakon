package com.renxin.doctor.activity.ui.activity.fragment;

import android.app.Activity;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.config.EventConfig;
import com.renxin.doctor.activity.data.eventbus.Event;
import com.renxin.doctor.activity.injection.components.DaggerFragmentComponent;
import com.renxin.doctor.activity.injection.modules.FragmentModule;
import com.renxin.doctor.activity.ui.adapter.PatientAdapter;
import com.renxin.doctor.activity.ui.base.BaseFragment;
import com.renxin.doctor.activity.ui.bean.PatientBean;
import com.renxin.doctor.activity.ui.contact.PatientContact;
import com.renxin.doctor.activity.ui.presenter.PatientPresenter;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.widget.SideBar;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;
import com.trello.rxlifecycle.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * PatientFragment 患者fragment
 * Create at 2018/4/15 下午5:10 by mayakun
 */

public class PatientFragment extends BaseFragment implements PatientContact.View {

    @Inject
    PatientPresenter mPresenter;
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.recyvleview)
    RecyclerView recycleView;
    @BindView(R.id.sideBar)
    SideBar sideBar;
    @BindView(R.id.indicator)
    TextView indicator;


    private PatientAdapter mAdapter;
    private List<PatientBean> dataList = new ArrayList<>();

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_patient;
    }


    void testData() {
        for (int i = 0; i < 20; i++) {
            dataList.add(new PatientBean(i, "", ((char) (65 + i)) + "32423423"));
        }
    }


   private LinearLayoutManager mLayoutManager;

    @Override
    protected void initView() {
        testData();

        initToolBar();

        recycleView.setLayoutManager(mLayoutManager = new LinearLayoutManager(actContext()));
        mAdapter = new PatientAdapter(actContext(), dataList);
        recycleView.addItemDecoration(new StickyRecyclerHeadersDecoration(mAdapter));
//        recycleView.addItemDecoration(new DividerItemDecoration(actContext(), RecyclerView.VERTICAL));
        recycleView.setAdapter(mAdapter);
        sideBar.setTextView(indicator);
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s, int offsetY) {
                int position = mAdapter.getPositionForSection(s.charAt(0));
                indicator.setText(s);
//                if (offsetY > indicator.getHeight() / 2 && offsetY + indicator.getHeight() / 2 < sideBar.getHeight())
//                    indicator.setTranslationY(offsetY - indicator.getHeight() / 2 + sideBar.getTop());
                if (position != -1) {
                    mLayoutManager.scrollToPositionWithOffset(position, 0);
                }
            }
        });
    }

    private void initToolBar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(getActivity()))
                .setTitle("患者")
                .setStatuBar(R.color.white)
                .setRightImg(R.drawable.icon_add_main, true)
                .blank()
                .setLeft(false)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void rightClick() {
                        super.rightClick();
                        ToastUtil.show("search");
                    }
                })
                .bind();
    }

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.REQUEST_HOMELOAN:
                    //未登录状态下 点击申请  登录成功后返回的逻辑
//                    startActivity(new Intent(actContext(), BasicInfoActivity.class));
                    break;
                case EventConfig.REFRESH_MAX_AMT:
                    break;
            }

        }
    }


    @Override
    public void onError(String errorCode, String errorMsg) {
        ToastUtil.show(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case PatientPresenter.MAX_AMT:
                break;
        }
    }

    @Override
    public Activity provideContext() {
        return getActivity();
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }

    @Override
    public boolean useEventBus() {
        return true;
    }
}
