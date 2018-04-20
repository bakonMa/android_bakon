package com.renxin.doctor.activity.ui.nimview;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.config.EventConfig;
import com.renxin.doctor.activity.data.eventbus.Event;
import com.renxin.doctor.activity.ui.base.BaseActivity;
import com.renxin.doctor.activity.widget.toolbar.TitleOnclickListener;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * QuickReplyActivity  快速回复
 * Create at 2018/4/19 上午10:42 by mayakun
 */
public class QuickReplyActivity extends BaseActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_message_quieckreply;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("快捷回复")
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

    @OnClick({R.id.tv_drug, R.id.tv_chat})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_drug://用药常用语
                Intent intent1 = new Intent(this, CommMessageActivity.class);
                intent1.putExtra("type", 1);
                startActivity(intent1);
                break;
            case R.id.tv_chat://咨询常用语
                Intent intent = new Intent(this, CommMessageActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventCome(Event event) {
        if (event == null) {
            return;
        }
        switch (event.getCode()) {
            case EventConfig.EVENT_KEY_CHAT_SELECT_COMMMSG://选择常用语
                Intent intent = new Intent();
                intent.putExtra("message", event.getData().toString());
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
    }

    @Override
    protected boolean useEventBus() {
        return true;
    }

    @Override
    protected void setupActivityComponent() {
    }

}
