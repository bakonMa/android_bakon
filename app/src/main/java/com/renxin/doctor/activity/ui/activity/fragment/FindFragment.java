package com.renxin.doctor.activity.ui.activity.fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.ui.base.BaseFragment;
import com.renxin.doctor.activity.widget.toolbar.ToolbarBuilder;

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

    @OnClick({R.id.tv_books, R.id.tv_encyclopedia, R.id.tv_policy,
            R.id.tv_daywork, R.id.tv_classroom})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_books://经典书籍
                break;
            case R.id.tv_encyclopedia://中医百科
                break;
            case R.id.tv_policy://政策追踪
                break;
            case R.id.tv_daywork://中医日常
                break;
            case R.id.tv_classroom://培训与讲座
                break;
        }
    }

    @Override
    protected void setupActivityComponent() {
    }

}
