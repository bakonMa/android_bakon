package com.renxin.doctor.activity.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.util.TypedValue;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.renxin.doctor.activity.application.DocApplication;

/**
 * UIUtils
 * Create by mayakun at 2018/3/27 上午9:36
 */

public class UIUtils {

    /**
     * 判断连续多次点击时间（600ms）
     *
     * @return 多次连续点击返回 true 否则返回 false
     */
    private static long lastClickTime = 0;

    public static boolean isDoubleClick() {
        if (System.currentTimeMillis() - lastClickTime < 600) {
            lastClickTime = System.currentTimeMillis();
            return true;
        } else {
            lastClickTime = System.currentTimeMillis();
            return false;
        }
    }

    /**
     * popularwindow时改变背景透明度
     */
    public static void lightOff(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 0.4f;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * popularwindow时改变背景透明度
     */
    public static void lightOn(Activity activity) {
        WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        activity.getWindow().setAttributes(lp);
    }

    /**
     * VectorDrawable 转 bitmap
     */
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * VectorDrawable 转 bitmap
     */
    public static Bitmap getBitmapFromVectorDrawable(Context context, int drawableId, int size) {
        Drawable drawable = ContextCompat.getDrawable(context, drawableId);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = (DrawableCompat.wrap(drawable)).mutate();
        }
        Bitmap bitmap = Bitmap.createBitmap(dp2px(context, size), dp2px(context, size), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);

        return bitmap;
    }

    /**
     * 获取color
     */
    public static int getColor(int res) {
        return ContextCompat.getColor(DocApplication.getInstance(), res);
    }

    /**
     * 获取String
     */
    public static String getString(int res) {
        return DocApplication.getInstance().getResources().getString(res);
    }

    /**
     * 获取资源文件
     */
    public static Drawable getDrawable(int res) {
        return ContextCompat.getDrawable(DocApplication.getInstance(), res);
    }

    /**
     * px(像素)转成dp
     */
    public static int px2dp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * dp转成px(像素)
     */
    public static int dp2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * sp转成px(像素)
     */
    public static int sp2px(Context context, float spVal) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP,
                spVal, context.getResources().getDisplayMetrics());
    }

    /**
     * px(像素)转成sp
     */
    public static float px2sp(Context context, float pxVal) {
        return (pxVal / context.getResources().getDisplayMetrics().scaledDensity);
    }

    /**
     * 获取屏幕宽度
     */
    public static int getScreenWidth(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = wm.getDefaultDisplay().getWidth();
        return width;
    }

    /**
     * 获取屏幕高度
     */
    public static int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        return height;
    }

    public static void setCompoundDrawable(View view, int widthdp, int paddingdp, int resId, int gravity) {
        if (resId > 0) {
            int width = dp2px(DocApplication.getInstance(), widthdp);
            BitmapDrawable drawable = (BitmapDrawable) getDrawable(resId);
            drawable.setBounds(0, 0, width, (int) ((float) width * ((float) drawable.getBitmap().getHeight() / (float) drawable.getBitmap().getWidth())));
            Drawable[] drawables;
            if (view instanceof TextView) {
                TextView compoundView = (TextView) view;
                compoundView.setCompoundDrawablePadding(dp2px(DocApplication.getInstance(), paddingdp));
                drawables = compoundView.getCompoundDrawables();
                switch (gravity) {
                    case 3:
                        compoundView.setCompoundDrawables(drawable, drawables[1], drawables[2], drawables[3]);
                        break;
                    case 5:
                        compoundView.setCompoundDrawables(drawables[0], drawables[1], drawable, drawables[3]);
                        break;
                    case 48:
                        compoundView.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3]);
                        break;
                    case 80:
                        compoundView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawable);
                }
            } else if (view instanceof EditText) {
                EditText compoundView = (EditText) view;
                compoundView.setCompoundDrawablePadding(dp2px(DocApplication.getInstance(), paddingdp));
                drawables = compoundView.getCompoundDrawables();
                switch (gravity) {
                    case 3:
                        compoundView.setCompoundDrawables(drawable, drawables[1], drawables[2], drawables[3]);
                        break;
                    case 5:
                        compoundView.setCompoundDrawables(drawables[1], drawables[2], drawable, drawables[3]);
                        break;
                    case 48:
                        compoundView.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3]);
                        break;
                    case 80:
                        compoundView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawable);
                }
            } else if (view instanceof RadioButton) {
                RadioButton compoundView = (RadioButton) view;
                compoundView.setCompoundDrawablePadding(dp2px(DocApplication.getInstance(), paddingdp));
                drawables = compoundView.getCompoundDrawables();
                switch (gravity) {
                    case 3:
                        compoundView.setCompoundDrawables(drawable, drawables[1], drawables[2], drawables[3]);
                        break;
                    case 5:
                        compoundView.setCompoundDrawables(drawables[1], drawables[2], drawable, drawables[3]);
                        break;
                    case 48:
                        compoundView.setCompoundDrawables(drawables[0], drawable, drawables[2], drawables[3]);
                        break;
                    case 80:
                        compoundView.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawable);
                }
            }
        }

    }

}