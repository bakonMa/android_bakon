package com.jht.doctor.ui.activity.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;
import com.jht.doctor.config.EventConfig;
import com.jht.doctor.config.SPConfig;
import com.jht.doctor.data.eventbus.Event;
import com.jht.doctor.ui.activity.mine.LoginActivity;
import com.jht.doctor.ui.base.BaseAppCompatActivity;
import com.jht.doctor.ui.fragment.HomeFragment;
import com.jht.doctor.ui.fragment.MineFragment;
import com.jht.doctor.ui.fragment.OrderFragment;
import com.jht.doctor.ui.fragment.WorkRoomFragment;
import com.jht.doctor.utils.StringUtils;
import com.jht.doctor.widget.FragmentTabHost;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseAppCompatActivity {

    @BindView(R.id.id_tab_host)
    FragmentTabHost idTabHost;


    //定义一个布局
    private LayoutInflater layoutInflater;

    //定义数组来存放Fragment界面
    private Class fragmentArray[] = {WorkRoomFragment.class, HomeFragment.class, OrderFragment.class, MineFragment.class};

    //定义数组来存放按钮图片
    private int mImageViewArray[] = {R.drawable.tab_home_btn, R.drawable.tab_home_btn, R.drawable.tab_order_btn, R.drawable.tab_mine_btn};

    //Tab选项卡的文字
    private String mTextviewArray[] = {"工作室", "患者", "发现", "我"};

    private String currentTag = "首页";
    private String previousTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        //实例化布局对象
        layoutInflater = LayoutInflater.from(this);

        //实例化TabHost对象，得到TabHost
        idTabHost.setup(this, getSupportFragmentManager(), R.id.id_content);
        //分割线不显示
        idTabHost.getTabWidget().setShowDividers(TabWidget.SHOW_DIVIDER_NONE);
        //得到fragment的个数
        int count = fragmentArray.length;
        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = idTabHost.newTabSpec(mTextviewArray[i]).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            idTabHost.addTab(tabSpec, fragmentArray[i], null);
            //设置Tab按钮的背景
            //idTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.color.white);
            //idTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_bg_tab_host);
        }
        idTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equals("订单")) {
                    previousTag = currentTag;
                    if (StringUtils.isEmpty(CustomerApplication.getAppComponent().dataRepo().appSP().getString(SPConfig.SP_STR_TOKEN, ""))) {
                        idTabHost.setCurrentTabByTag(previousTag);
                        Intent intent = new Intent(actContext(), LoginActivity.class);
                        intent.putExtra(LoginActivity.FROM_KEY, LoginActivity.ORDER_ACTIVITY);
                        startActivity(intent);
                    }
                } else {
                    currentTag = tabId;
                }
            }
        });
    }


    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = layoutInflater.inflate(R.layout.tab_item_view, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageViewArray[index]);

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextviewArray[index]);

        return view;
    }


    //控制fragment跳转
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event != null) {
            switch (event.getCode()) {
                case EventConfig.CONTROL_FRAGMENT:
                    String tag = (String) event.getData();
                    idTabHost.setCurrentTabByTag(tag);
                    break;
            }

        }
    }

    public String getPreviousTag() {
        return previousTag;
    }

    @Override
    protected boolean isUseEventBus() {
        return true;
    }

    @Override
    protected void setupActivityComponent() {

    }
}
