package com.jht.doctor.utils;

import android.app.Activity;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.support.v4.view.ViewCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.jht.doctor.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * 状态栏相关处理
 * <p>
 * 后期要梳理一下
 * Jay韩參考：http://blog.csdn.net/jdsjlzx/article/details/41643587
 */
public class StatusBarUtil {


    public static boolean isBigerThan18() {
        return VERSION.SDK_INT >= VERSION_CODES.KITKAT;//大于等于4.4
    }

    public static boolean isBigerThan20() {
        return VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP;//大于等于5.0
    }

    public static boolean isBigerThan23() {
        return VERSION.SDK_INT >= VERSION_CODES.M;//大于等于6.0
    }

    /**
     * View类提供了setSystemUiVisibility和getSystemUiVisibility方法，这两个方法实现对状态栏的动态显示或隐藏的操作，以及获取状态栏当前可见性。
     * setSystemUiVisibility(int visibility)方法可传入的实参为：
     * 1. View.SYSTEM_UI_FLAG_VISIBLE：显示状态栏，Activity不全屏显示(恢复到有状态的正常情况)。
     * 2. View.INVISIBLE：隐藏状态栏，同时Activity会伸展全屏显示。
     * 3. View.SYSTEM_UI_FLAG_FULLSCREEN：Activity全屏显示，且状态栏被隐藏覆盖掉。
     * 4. View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN：Activity全屏显示，但状态栏不会被隐藏覆盖，状态栏依然可见，Activity顶端布局部分会被状态遮住。
     * 5. View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * 6. View.SYSTEM_UI_LAYOUT_FLAGS：效果同View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
     * 7. View.SYSTEM_UI_FLAG_HIDE_NAVIGATION：隐藏虚拟按键(导航栏)。有些手机会用虚拟按键来代替物理按键。
     * 8. View.SYSTEM_UI_FLAG_LOW_PROFILE：状态栏显示处于低能显示状态(low profile模式)，状态栏上一些图标显示会被隐藏
     */
    public static void setWindowFlag(Window window) {
        try {
            if (isBigerThan18()) {
                if (window != null) {
                    //Window window = activity.getWindow();
                    window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                            | WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
                    window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    /*| View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION*/
                            | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);

                    //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                    if (isBigerThan20()) {
                        window.setStatusBarColor(Color.TRANSPARENT);
                    }

                    //大于等于6.0
                    if (MIUISetStatusBarLightMode(window, true)) {
                        return;
                    } else if (FlymeSetStatusBarLightMode(window, true)) {
                        return;
                    } else if (isBigerThan23()) {
                        //透明状态栏
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
                        window.setStatusBarColor(Color.TRANSPARENT);//设置背景透明
                        return;
                    }

                    //大于等于5.0
                    if (isBigerThan20()) {
                        //设置透明状态栏,这样才能让 ContentView 向上
                        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

                        //需要设置这个 flag 才能调用 setStatusBarColor 来设置状态栏颜色
                        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                        //设置状态栏颜色
                        //window.setStatusBarColor(Color.BLUE);

                        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);
                        View mChildView = mContentView.getChildAt(0);
                        if (mChildView != null) {
                            //注意不是设置 ContentView 的 FitsSystemWindows, 而是设置 ContentView 的第一个子 View . 使其不为系统 View 预留空间.
                            ViewCompat.setFitsSystemWindows(mChildView, false);
                        }
                        return;
                    }
                    //4.4
                    if (isBigerThan18()) {
                        ViewGroup mContentView = (ViewGroup) window.findViewById(Window.ID_ANDROID_CONTENT);

                        //首先使 ChildView 不预留空间
                        View mChildView = mContentView.getChildAt(0);
                        if (mChildView != null) {
                            ViewCompat.setFitsSystemWindows(mChildView, false);
                        }

                        int statusBarHeight = DensityUtils.getStatusBarHeight(window.getContext());
                        //需要设置这个 flag 才能设置状态栏
                        window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                        //避免多次调用该方法时,多次移除了 View
                        if (mChildView != null && mChildView.getLayoutParams() != null && mChildView.getLayoutParams().height == statusBarHeight) {
                            //移除假的 View.
                            mContentView.removeView(mChildView);
                            mChildView = mContentView.getChildAt(0);
                        }
                        if (mChildView != null) {
                            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mChildView.getLayoutParams();
                            //清除 ChildView 的 marginTop 属性
                            if (lp != null && lp.topMargin >= statusBarHeight) {
                                lp.topMargin -= statusBarHeight;
                                mChildView.setLayoutParams(lp);
                            }
                        }
                        return;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 需要MIUIV6以上
     *
//     * @param activity
     * @param dark     是否把状态栏文字及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
//        Window window = activity.getWindow();
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;

                if (VERSION.SDK_INT >= VERSION_CODES.M) {
                    //开发版 7.7.13 及以后版本采用了系统API，旧方法无效但不会报错，所以两个方式都要加上
                    if (dark) {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    } else {
                        window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    }
                }
            } catch (Exception e) {
                return result;
            }
        }
        return result;
    }

    /**
     * 设置状态栏图标为深色和魅族特定的文字风格
     * 可以用来判断是否为Flyme用户
     *
     * @return boolean 成功执行返回true
     */
    public static boolean FlymeSetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            try {
                WindowManager.LayoutParams lp = window.getAttributes();
                Field darkFlag = WindowManager.LayoutParams.class
                        .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
                Field meizuFlags = WindowManager.LayoutParams.class
                        .getDeclaredField("meizuFlags");
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
                result = true;
            } catch (Exception e) {
                return result;
            }
        }
        return result;
    }

    /**
     * 设置状态栏颜色为App主色
     * 配合{@link # setTranslucentWindows(Activity)}方法使用
     * 主要方法为添加一个View并设置背景色添加到系统contentView中
     *
     * @param activity
     */
    static public void addStatusBarBackground(Activity activity) {
        int height = DensityUtils.getStatusBarHeight(activity);
        if (height <= 0) {
            return;
        }
        FrameLayout layout = (FrameLayout) activity.findViewById(android.R.id.content);
        FrameLayout statusLayout = new FrameLayout(activity);
        statusLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, height));

        TypedValue typedValue = new TypedValue();
        TypedArray a = activity.obtainStyledAttributes(typedValue.data, new int[]{R.attr.colorPrimary});
        int color = a.getColor(0, 0);
        a.recycle();
        statusLayout.setBackgroundColor(color);
        layout.addView(statusLayout);
    }
}
