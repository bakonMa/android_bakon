package com.junhetang.doctor.ui.activity.fragment;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.junhetang.doctor.R;
import com.junhetang.doctor.config.H5Config;
import com.junhetang.doctor.ui.activity.WebViewActivity;
import com.junhetang.doctor.ui.activity.find.GuildNewsListActivity;
import com.junhetang.doctor.ui.base.BaseFragment;
import com.junhetang.doctor.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * FindFragment 发现
 * Create at 2018/4/14 下午3:17 by mayakun
 */
public class FindFragment extends BaseFragment {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;

    @Override
    protected int provideRootLayout() {
        return R.layout.fragment_find;
    }

    @Override
    protected void initView() {
        initToolbar();
    }

    //共同头部处理
    private void initToolbar() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(getActivity()))
                .setTitle("发现")
                .setStatuBar(R.color.white)
                .setLeft(false)
                .blank()
                .bind();
    }

    @OnClick({R.id.tv_books, R.id.tv_encyclopedia,
            R.id.tv_guildnews, R.id.tv_healtheducation})
    public void onViewClicked(View view) {
        switch (view.getId()) { //个人卡片
            case R.id.tv_books://经典书籍
                WebViewActivity.startAct(actContext(),
                        false,
                        WebViewActivity.WEB_TYPE.WEB_TYPE_BOOKS,
                        "",
                        H5Config.H5_BOOKS);
                break;
            case R.id.tv_encyclopedia://中医百科
                WebViewActivity.startAct(actContext(),
                        false,
                        WebViewActivity.WEB_TYPE.WEB_TYPE_BAIKE,
                        "",
                        H5Config.H5_BAIKE);
                break;
            case R.id.tv_guildnews://行业追踪
                Intent intent = new Intent(actContext(), GuildNewsListActivity.class);
                intent.putExtra("type", 0);
                startActivity(intent);
                break;
            case R.id.tv_healtheducation://健康教育
                Intent intentEdu = new Intent(actContext(), GuildNewsListActivity.class);
                intentEdu.putExtra("type", 1);
                startActivity(intentEdu);
                break;
        }
    }

    @Override
    protected void setupActivityComponent() {
    }

}
