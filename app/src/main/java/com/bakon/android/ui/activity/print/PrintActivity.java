package com.bakon.android.ui.activity.print;

import android.content.Context;
import android.print.PrintAttributes;
import android.print.PrintManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bakon.android.R;
import com.bakon.android.ui.base.BaseActivity;
import com.bakon.android.widget.toolbar.ToolbarBuilder;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * PrintActivity  打印服务
 * https://blog.csdn.net/bboxhe/article/details/51001632
 * Create at 2019-06-17 09:35 by mayakun
 */
public class PrintActivity extends BaseActivity {

    @BindView(R.id.id_toolbar)
    Toolbar idToolbar;
    @BindView(R.id.tv_print)
    TextView tv_print;

    @Override
    protected int provideRootLayout() {
        return R.layout.activity_print;
    }

    @Override
    protected void initView() {
        ToolbarBuilder.builder(idToolbar, new WeakReference<FragmentActivity>(this))
                .setTitle("打印服务")
                .setStatuBar(R.color.white)
                .blank()
                .setLeft(false)
                .bind();

    }


    @OnClick({R.id.tv_print})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_print:
                initPrint();
                break;
        }
    }


    private void initPrint() {
        PrintManager printManager = (PrintManager) this.getSystemService(Context.PRINT_SERVICE);
        PrintAttributes.Builder builder = new PrintAttributes.Builder();
        //彩色、黑白
        builder.setColorMode(PrintAttributes.COLOR_MODE_COLOR);
        //纸张尺寸
        builder.setMediaSize(PrintAttributes.MediaSize.UNKNOWN_LANDSCAPE);
        PrintAttributes printAttributes = builder.build();
//        printManager.print()
    }


    @Override
    protected void setupActivityComponent() {

    }

}
