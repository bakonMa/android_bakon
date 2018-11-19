package com.junhetang.doctor.ui.activity.openoline;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.injection.components.DaggerActivityComponent;
import com.junhetang.doctor.injection.modules.ActivityModule;
import com.junhetang.doctor.ui.base.BaseActivity;
import com.junhetang.doctor.ui.contact.OpenPaperContact;
import com.junhetang.doctor.ui.presenter.OpenPaperPresenter;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.utils.ToastUtil;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.widget.dialog.CommSuperDialog;
import com.junhetang.doctor.widget.toolbar.TitleOnclickListener;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;
import com.trello.rxlifecycle2.LifecycleTransformer;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.greenrobot.greendao.annotation.NotNull;

import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

/**
 * OpenPaperOnlineActivity 在线开方
 * Create at 2018/4/25 下午4:23 by mayakun
 */
public class OpenOnlinMenuActivity extends BaseActivity implements OpenPaperContact.View {
    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    @Inject
    OpenPaperPresenter mPresenter;

    private int formParent = 0;//是否来自聊天(0 默认不是 1：聊天)
    private String membNo = "";//患者编号，选择患者才有
    private String pAccid = "";
    private BaseQuickAdapter mAdapter;
    private List<String> titles;
    private int[] icons = {R.drawable.icon_menu_zy, R.drawable.icon_menu_klj, R.drawable.icon_menu_gao,
            R.drawable.icon_menu_wan, R.drawable.icon_menu_san, R.drawable.icon_menu_xy};

    //带有返回的startActivityForResult-仅限nim中使用 formParent=1
    public static void startResultActivity(Context context, int requestCode, int formParent, String p_accid, String membNo) {
        Intent intent = new Intent(context, OpenOnlinMenuActivity.class);
        intent.putExtra("formParent", formParent);
        intent.putExtra("memb_no", membNo);
        intent.putExtra("p_accid", p_accid);
        ((Activity) context).startActivityForResult(intent, requestCode);
    }

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_openonline_menu;
    }

    @Override
    protected void initView() {
        //来源
        formParent = getIntent().getIntExtra("formParent", 0);
        membNo = getIntent().getStringExtra("memb_no");//患者momb_no
        pAccid = getIntent().getStringExtra("p_accid");//患者accid

        titles = Arrays.asList(UIUtils.getArray(R.array.openonline_menu));
        //设置topbar
        initToolbar();

        mAdapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_openolline_menu, titles) {
            @Override
            protected void convert(BaseViewHolder helper, String item) {
                helper.setText(R.id.tv_title, TextUtils.isEmpty(item) ? "" : item);
                //icon
                ImageUtil.showDraw(icons[helper.getAdapterPosition()], helper.getView(R.id.iv_icon));
            }
        };
        recyclerView.setAdapter(mAdapter);
        //### important! setLayoutManager should be called after setAdapter###
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        //分割线，横线和纵线
        DividerItemDecoration horizontalDivider = new DividerItemDecoration(this, DividerItemDecoration.HORIZONTAL);
        horizontalDivider.setDrawable(getResources().getDrawable(R.drawable.divider_width_2dp));
        DividerItemDecoration verticalDivider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        verticalDivider.setDrawable(getResources().getDrawable(R.drawable.divider_height_2dp));
        recyclerView.addItemDecoration(horizontalDivider);
        recyclerView.addItemDecoration(verticalDivider);

        recyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                ToastUtil.showCenterToast(titles.get(position));
            }
        });

    }

    //获取当前界面可用高度
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("处方类型")
                .setLeft(false)
                .setStatuBar(R.color.white)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        onBackPressed();
                    }
                }).bind();
    }


    @Override
    protected void setupActivityComponent() {
        DaggerActivityComponent.builder()
                .activityModule(new ActivityModule(this))
                .applicationComponent(DocApplication.getAppComponent())
                .build()
                .inject(this);
    }

    @Override
    public void onError(String errorCode, String errorMsg) {
        showCommSuperDialog(errorMsg);
    }

    @Override
    public void onSuccess(Message message) {
        switch (message.what) {
            case OpenPaperPresenter.ADD_COMMPAPER_OK:
                break;

        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_CHOOSE_PATIENT://选择患者-就诊人

                break;
        }
    }

    private CommSuperDialog commSuperDialog;

    //确定 dialog
    private void showCommSuperDialog(@NotNull String msg) {
        commSuperDialog = new CommSuperDialog(this, msg);
        commSuperDialog.show();
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    public Activity provideContext() {
        return this;
    }

    @Override
    public <R> LifecycleTransformer<R> toLifecycle() {
        return bindToLifecycle();
    }


//    void text() {
//
//        Observable.create((ObservableOnSubscribe<Integer>) emitter -> {
//            emitter.onComplete();
//            emitter.onNext(1);
//        })
//                .map(integer -> integer + integer + "")
//                .filter(s -> !TextUtils.isEmpty(s))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(s -> ToastUtil.showCenterToast(s));
//    }
}
