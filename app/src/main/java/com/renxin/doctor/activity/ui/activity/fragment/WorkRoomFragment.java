package com.renxin.doctor.activity.ui.activity.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Message;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.renxin.doctor.activity.BuildConfig;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.application.DocApplication;
import com.renxin.doctor.activity.injection.components.DaggerFragmentComponent;
import com.renxin.doctor.activity.injection.modules.FragmentModule;
import com.renxin.doctor.activity.nim.message.SessionHelper;
import com.renxin.doctor.activity.ui.activity.home.OpenPaperCameraActivity;
import com.renxin.doctor.activity.ui.activity.home.OpenPaperOnlineActivity;
import com.renxin.doctor.activity.ui.base.BaseFragment;
import com.renxin.doctor.activity.ui.contact.WorkRoomContact;
import com.renxin.doctor.activity.ui.nimview.RecentActivity;
import com.renxin.doctor.activity.utils.ToastUtil;
import com.renxin.doctor.activity.utils.imageloader.BannerImageLoader;
import com.tbruyelle.rxpermissions.RxPermissions;
import com.trello.rxlifecycle.LifecycleTransformer;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;

import java.util.Arrays;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * WorkRoomFragment
 * Create at 2018/3/31 上午9:27 by mayakun
 */

public class WorkRoomFragment extends BaseFragment implements WorkRoomContact.View {

    @BindView(R.id.id_ll_top)
    LinearLayout idLlTop;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.tv_notification)
    TextView tvNotification;

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_workroom2;
    }

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build().inject(this);
    }

    String[] images = {"http://t.388g.com/uploads/allimg/160711/5-160G10U313.jpg", "http://pic32.photophoto.cn/20140923/0005018399183460_b.jpg",
            "http://pic.sc.chinaz.com/files/pic/pic9/201804/zzpic11253.jpg", "http://pics.sc.chinaz.com/files/pic/pic9/201804/bpic6452.jpg"};

    @Override
    protected void initView() {
        //设置图片加载器
        banner.setImageLoader(new BannerImageLoader());
        //设置图片集合
        banner.setImages(Arrays.asList(images));
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(1500);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.RIGHT);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        requestPermissions();
    }

    @OnClick({R.id.tv_add_patient, R.id.tv_online_paper, R.id.tv_camera_patient, R.id.tv_comm_paper,
            R.id.tv_ask_paper, R.id.tv_flow_paper, R.id.tv_camera, R.id.tv_notice,
            R.id.id_history, R.id.id_notification})
    void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.tv_add_patient:
//                NimUIKit.startP2PSession(actContext(), "3ef2e56a2f9476de092743cbd577a900", null);
                SessionHelper.startP2PSession(actContext(), "3ef2e56a2f9476de092743cbd577a900");
                break;
            case R.id.id_notification:
                startActivity(new Intent(actContext(), RecentActivity.class));
                break;
            case R.id.tv_camera_patient://拍照开方
                startActivity(new Intent(actContext(), OpenPaperCameraActivity.class));
                break;
            case R.id.tv_online_paper://在线开方
                startActivity(new Intent(actContext(), OpenPaperOnlineActivity.class));
                break;
        }

    }

    @Override
    public void onSuccess(Message message) {

    }

    @Override
    public void onError(String errorCode, String errorMsg) {

    }

    @Override
    public Activity provideContext() {
        return getActivity();
    }

    @Override
    public LifecycleTransformer toLifecycle() {
        return bindToLifecycle();
    }


    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(getActivity());
        rxPermissions.setLogging(BuildConfig.DEBUG);
        rxPermissions
                .request(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .subscribe(new rx.Observer<Boolean>() {
                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (!aBoolean) {
                            ToastUtil.show("请求权限失败");
                        }
                    }

                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                });
    }
}
