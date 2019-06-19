package com.bakon.android.ui.activity.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bakon.android.R;
import com.bakon.android.application.MyApplication;
import com.bakon.android.config.EventConfig;
import com.bakon.android.config.SPConfig;
import com.bakon.android.data.eventbus.Event;
import com.bakon.android.di.components.DaggerFragmentComponent;
import com.bakon.android.di.modules.FragmentModule;
import com.bakon.android.ui.activity.WebViewActivity;
import com.bakon.android.ui.base.BaseFragment;
import com.bakon.android.ui.bean.BannerBean;
import com.bakon.android.ui.contact.WorkRoomContact;
import com.bakon.android.ui.presenter.WorkRoomPresenter;
import com.bakon.android.utils.ImageUtil;
import com.bakon.android.utils.ToastUtil;
import com.bakon.android.utils.U;
import com.bakon.android.utils.UmengKey;
import com.bakon.android.widget.RelativeWithImage;
import com.bakon.android.widget.dialog.CommonDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.umeng.analytics.MobclickAgent;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * WorkRoomFragment
 * Create at 2018/3/31 上午9:27 by mayakun
 */

public class WorkRoomFragment extends BaseFragment implements WorkRoomContact.View {

    @BindView(R.id.id_ll_top)
    LinearLayout idLlTop;
    @BindView(R.id.llt_shownotice)
    LinearLayout lltShownotice;
    @BindView(R.id.banner)
    Banner banner;
    @BindView(R.id.iv_service_img)
    ImageView ivServiceImg;
    @BindView(R.id.tv_service_name)
    TextView tvServiceName;
    @BindView(R.id.tv_service_message)
    TextView tvServiceMessage;
    @BindView(R.id.tv_service_num)
    TextView tvServiceNum;
    @BindView(R.id.tv_service_time)
    TextView tvServiceTime;
    @BindView(R.id.tv_notification)
    TextView tvNotification;
    @BindView(R.id.id_job_schedule)
    RelativeWithImage idJobSchedule;
    //    @BindView(R.id.tv_checkredpoint)
//    TextView tvCheckredpoint;//审核处方-红点
    @BindView(R.id.tv_chatunreadnum)
    TextView tvChatunreadnum;//消息通知-数字红点
    @BindView(R.id.tv_chatredpoint)
    TextView tvChatReadPoint;//消息通知-红点

    @Inject
    WorkRoomPresenter mPresenter;
    private CommonDialog commonDialog;
    private List<BannerBean> bannerBeans = new ArrayList<>();
    private List<String> imgUrl = new ArrayList<>();

    //客服accid
    private String accid;

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_workroom;
    }

    @Override
    protected void setupActivityComponent() {
        DaggerFragmentComponent.builder()
                .fragmentModule(new FragmentModule(this))
                .applicationComponent(MyApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    protected void initView() {
        //获取客服accid login接口获得
        accid = MyApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_SERVICE_ACCID);
        //请求权限
        requestPermissions();

        //Banner初始化
        initBanner();

        //请求数据
        mPresenter.updataToken();//更新token
        mPresenter.getUserIdentifyStatus();//认证状态
        mPresenter.getOPenPaperBaseData();//开方基础数据
        mPresenter.getRedPointStatus();//红点状态
        //首页Banner数据
        mPresenter.getHomeBanner();


    }

    /**
     * fragment 是否隐藏
     *
     * @param hidden false 前台显示 true 隐藏
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            mPresenter.getUserIdentifyStatus();//认证状态
            mPresenter.getRedPointStatus();//红点状态
            //网络问题切换时，没有数据就请求一下
            if (bannerBeans == null || bannerBeans.isEmpty()) {
                //开方基础数据
                mPresenter.getOPenPaperBaseData();
                //首页Banner数据
                mPresenter.getHomeBanner();
            }
        }
    }

    //最后一条消息内容
    private void showLastServiceMessage() {

    }



    //初始化banner
    private void initBanner() {
        //设置图片加载器
        banner.setImageLoader(new ImageLoader() {
            @Override
            public void displayImage(Context context, Object path, ImageView imageView) {
                ImageUtil.showImage(path.toString(), imageView);
            }
        });
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                //Umeng 埋点
                MobclickAgent.onEvent(getActivity(), UmengKey.workroom_banner);
                //URL为空不跳转
                if (TextUtils.isEmpty(bannerBeans.get(position).url)) {
                    return;
                }
                //Banner点击 跳转
                WebViewActivity.startAct(actContext(),
                        true,
                        WebViewActivity.WEB_TYPE.WEB_TYPE_BANNER,
                        "",
                        bannerBeans.get(position).url);
            }
        });
    }

    @OnClick({R.id.tv_add_patient, R.id.tv_online_paper, R.id.tv_camera_patient, R.id.tv_comm_paper,
            R.id.tv_ask_paper, R.id.tv_flow_paper, R.id.tv_notice, R.id.id_job_schedule})
    void btnOnClick(View view) {
        //认证是否通过
        if (!U.isHasAuthOK()) {
            commonDialog = new CommonDialog(getActivity(),
                    R.layout.dialog_auth,
                    U.getAuthStatusMsg(),
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (view.getId() == R.id.btn_gotuauth) {
                            }
                        }
                    });

            commonDialog.show();
            return;
        }

        switch (view.getId()) {

        }
    }


    @OnClick({R.id.rlt_service, R.id.tv_history_paper, R.id.id_notification})
    void unCheckBtnOnClick(View view) {
        switch (view.getId()) {
            case R.id.rlt_service://客服
                break;
            case R.id.tv_history_paper://历史处方
                //Umeng 埋点
                MobclickAgent.onEvent(getActivity(), UmengKey.workroom_history_paper);
                break;
            case R.id.id_notification://消息通知
                //Umeng 埋点
                MobclickAgent.onEvent(getActivity(), UmengKey.workroom_chatlist);
                break;
        }
    }

    @Override
    public void onSuccess(Message message) {
        if (message == null) {
            return;
        }
        switch (message.what) {
            case WorkRoomPresenter.GET_AUTH_STATUS://认证状态
                lltShownotice.setVisibility(U.getAuthStatus() == 2 ? View.GONE : View.VISIBLE);
                tvNotification.setText(U.getAuthStatusMsg());
                //第三方类型用户 不显示【我的门诊】
                idJobSchedule.setVisibility(U.getUserType() == 2 ? View.GONE : View.VISIBLE);
                break;
            case WorkRoomPresenter.GET_BANNER_OK:
                imgUrl.clear();
                bannerBeans.clear();
                bannerBeans = (List<BannerBean>) message.obj;
                for (BannerBean bannerBean : bannerBeans) {
                    imgUrl.add(bannerBean.img_url);
                }
                banner.setImages(imgUrl);
                //banner设置方法全部调用完毕时最后调用
                banner.start();
                break;
        }

    }

    @Override
    public void onError(String errorCode, String errorMsg) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_REDPOINT_HOME_CHECK://红点 审核处方
                //是否有审核处方 暂时删除，以防后用
                //tvCheckredpoint.setVisibility(U.getRedPointExt() > 0 ? View.VISIBLE : View.GONE);
                break;
            case EventConfig.EVENT_KEY_NIM_LOGIN://nim 登录成功
                //客服 初始化
                break;
            case EventConfig.EVENT_KEY_XG_BINDTOKEN://绑定信鸽token
                mPresenter.bindXGToken(event.getData().toString());
                break;
            case EventConfig.EVENT_KEY_BASEDATA_NULL://基础数据空 异常
                mPresenter.getOPenPaperBaseData();
                break;
        }
    }

    @Override
    public boolean useEventBus() {
        return true;
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
    public void onDestroyView() {
        if (commonDialog != null) {
            commonDialog.dismiss();
            commonDialog = null;
        }
        super.onDestroyView();
    }

    /**
     * 下面的代码很关键,没有下面的代码会出现切换tab的时候重影现象：
     *
     * @param menuVisible
     */
    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (this.getView() != null)
            this.getView().setVisibility(menuVisible ? View.VISIBLE : View.GONE);
    }

    private void requestPermissions() {
        RxPermissions rxPermissions = new RxPermissions(this);
        rxPermissions
                .request(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE})
                .subscribe(aBoolean -> {
                    if (!aBoolean) {
                        ToastUtil.show("请求存储权限失败");
                    } else {
//                        showServiceInfo();
                    }
                }
                );
    }
}
