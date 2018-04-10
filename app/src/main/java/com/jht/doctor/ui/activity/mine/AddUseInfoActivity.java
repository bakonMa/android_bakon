package com.jht.doctor.ui.activity.mine;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.jht.doctor.R;
import com.jht.doctor.ui.base.BaseActivity;
import com.jht.doctor.widget.toolbar.TitleOnclickListener;
import com.jht.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * AddUseInfoActivity 完善资料
 * Create at 2018/4/10 上午9:54 by mayakun
 */
public class AddUseInfoActivity extends BaseActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_adduserinfo;
    }

    @Override
    protected void initView() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("完善资料")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .setListener(new TitleOnclickListener() {
                    @Override
                    public void leftClick() {
                        super.leftClick();
                        finish();
                    }
                }).bind();
    }

    @OnClick({R.id.et_notice, R.id.et_intro})
    public void btnOnClick(View view) {
        switch (view.getId()) {
            case R.id.et_notice:
                startActivity(new Intent(this, UserNoticeActivity.class));
                break;
            case R.id.et_intro:
                startActivity(new Intent(this, UserExplainActivity.class));
                break;
        }
    }

    @Override
    protected void setupActivityComponent() {

    }

}
