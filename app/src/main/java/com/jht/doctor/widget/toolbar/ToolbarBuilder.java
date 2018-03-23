package com.jht.doctor.widget.toolbar;

import android.animation.ObjectAnimator;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.utils.DensityUtils;
import com.jht.doctor.utils.LogUtil;
import com.jht.doctor.utils.OsUtil;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

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

    public ToolbarBuilder setStatuBar(int colorRes) {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
            if (colorRes == R.color.white) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M || OsUtil.isMIUI() || OsUtil.isFlyme()) {
//                    ScreenUtils.setStatusBarFontIconDark(context.get(), true);
                    setStatusBarFontIconDark(true);
                } else {
                    colorRes = R.color.statueBar_color;
                }
            }
            View view = toolbar.findViewById(R.id.id_view_statu);
            view.setBackgroundResource(colorRes);
            ViewGroup.LayoutParams lp = view.getLayoutParams();
            lp.height = DensityUtils.getStatusBarHeight(context.get());
            view.setLayoutParams(lp);
            view.setVisibility(View.VISIBLE);
        }
        return this;
    }

    /**
     * 设置Android状态栏的字体颜色，状态栏为亮色的时候字体和图标是黑色，状态栏为暗色的时候字体和图标为白色
     * 目前可以是MIUI6+,Flyme4+，Android6.0+支持切换状态栏的文字颜色为暗色。
     *
     * @param dark 状态栏字体是否为深色
     */
    private void setStatusBarFontIconDark(boolean dark) {
        // 小米MIUI
        try {
            Window window = context.get().getWindow();
            Class clazz = context.get().getWindow().getClass();
            Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            if (dark) {    //状态栏亮色且黑色字体
                extraFlagField.invoke(window, darkModeFlag, darkModeFlag);
            } else {       //清除黑色字体
                extraFlagField.invoke(window, 0, darkModeFlag);
            }
        } catch (Exception e) {
//            e.printStackTrace();
            LogUtil.d("Exception 小米MIUI setStatusBarFontIconDark");
        }

        // 魅族FlymeUI
        try {
            Window window = context.get().getWindow();
            WindowManager.LayoutParams lp = window.getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (dark) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            window.setAttributes(lp);
        } catch (Exception e) {
//            e.printStackTrace();
            LogUtil.d("Exception 魅族FlymeUI setStatusBarFontIconDark");
        }

        // android6.0+系统
        // 这个设置和在xml的style文件中用这个<item name="android:windowLightStatusBar">true</item>属性是一样的
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (dark) {
                context.get().getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            }
            return;
        }
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
