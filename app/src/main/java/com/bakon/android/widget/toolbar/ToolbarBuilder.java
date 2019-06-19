package com.bakon.android.widget.toolbar;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bakon.android.R;
import com.bakon.android.utils.OsUtil;
import com.bakon.android.utils.StatusBarUtil;

import java.lang.ref.WeakReference;

/**
 * author:Tang
 */

public class ToolbarBuilder {

    private Toolbar toolbar;
    private WeakReference<FragmentActivity> context;
    TitleOnclickListener titleOnclickListener;
    private boolean isSpread = false;
    private ImageView imageView;

    private ToolbarBuilder(Toolbar toolbar, WeakReference<FragmentActivity> context) {
        this.toolbar = toolbar;
        this.context = context;
    }

    public static ToolbarBuilder builder(Toolbar toolbar, WeakReference<FragmentActivity> context) {
        return new ToolbarBuilder(toolbar, context);
    }

    //设置状态栏颜色
    public ToolbarBuilder setStatuBar(int colorRes) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (colorRes == R.color.white) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || OsUtil.isMIUI() || OsUtil.isFlyme()) {
                    StatusBarUtil.setStatusBarDarkFont(context.get(), true);
                } else {
                    colorRes = R.color.statueBar_color;
                }
            }
            //添加一个状态栏的高度的高度
            View view = toolbar.findViewById(R.id.id_view_statu);
            view.setBackgroundResource(colorRes);
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = StatusBarUtil.getStatusBarHeight(context.get());
            view.setLayoutParams(lp);
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public ToolbarBuilder setTitle(String title) {
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.id_tv_title);
        tvTitle.setText(title);
        return this;
    }

    /**
     * 是否显示close 图标 默认false
     * @param isShow
     * @return
     */
    public ToolbarBuilder isShowClose(boolean isShow) {
        ImageView ivClose = (ImageView) toolbar.findViewById(R.id.id_left_close_image);
        ivClose.setVisibility(isShow ? View.VISIBLE : View.GONE);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleOnclickListener != null) {
                    titleOnclickListener.closeClick();
                }
            }
        });
        return this;
    }

    public ToolbarBuilder setClickTitle(String title) {
        TextView tvTitle = (TextView) toolbar.findViewById(R.id.id_tv_title);
        tvTitle.setText(title);
        LinearLayout layout = toolbar.findViewById(R.id.id_ll_title);
        imageView = toolbar.findViewById(R.id.id_iv_triangle);
        imageView.setVisibility(View.VISIBLE);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleOnclickListener != null) {
                    if (!isSpread) {
                        titleOnclickListener.down();
                        downAnimation();
                    } else {
                        titleOnclickListener.up();
                        upAnimation();
                    }
                }
            }
        });
        return this;
    }

    public void upAnimation() {
        isSpread = false;
        float rotation = imageView.getRotation();
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", -180, 0);
        animator.setDuration(500);
        animator.start();
    }

    private void downAnimation() {
        isSpread = true;
        float rotation = imageView.getRotation();
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "rotation", 0, -180);
        animator.setDuration(500);
        animator.start();
    }

    public ToolbarBuilder setLeft(boolean isShowText) {
        RelativeLayout rl_left = (RelativeLayout) toolbar.findViewById(R.id.id_rl_left);
        rl_left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (titleOnclickListener != null) {
                    titleOnclickListener.leftClick();
                }
            }
        });
        TextView tv_left = (TextView) toolbar.findViewById(R.id.id_tv_left);
        if (isShowText) {
            tv_left.setVisibility(View.VISIBLE);
        }
        return this;
    }

    public ToolbarBuilder blank(){
        ImageView imageView = toolbar.findViewById(R.id.id_left_image);
        imageView.setVisibility(View.GONE);
        return this;
    }

    public ToolbarBuilder setRightImg(int resId, boolean clickable) {
        ImageView iv_right = (ImageView) toolbar.findViewById(R.id.id_img_right);
        iv_right.setImageResource(resId);
        iv_right.setVisibility(View.VISIBLE);
        if (clickable) {
            iv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (titleOnclickListener != null)
                        titleOnclickListener.rightClick();
                }
            });
        }
        return this;
    }

    public ToolbarBuilder setRightText(String text, boolean clickable, int colorRes) {
        TextView tv_right = (TextView) toolbar.findViewById(R.id.id_tv_right);
        tv_right.setText(text);
        tv_right.setTextColor(context.get().getResources().getColor(colorRes));
        tv_right.setVisibility(View.VISIBLE);
        if (clickable) {
            tv_right.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (titleOnclickListener != null) {
                        titleOnclickListener.rightClick();
                    }
                }
            });
        }
        return this;
    }


    public ToolbarBuilder setToolBarColor(int colorRes) {
        toolbar.setBackgroundResource(colorRes);
        return this;
    }

    public ToolbarBuilder setListener(TitleOnclickListener listener) {
        this.titleOnclickListener = listener;
        return this;
    }

    public ToolbarBuilder setLeftImage(int resId) {
        ImageView imageView = toolbar.findViewById(R.id.id_left_image);
        imageView.setImageResource(resId);
        return this;
    }

    public ToolbarBuilder bind() {
        AppCompatActivity appCompatActivity = (AppCompatActivity) context.get();
        appCompatActivity.setSupportActionBar(toolbar);
        return this;
    }
}
