package com.bakon.android.utils;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bakon.android.R;
import com.bakon.android.application.MyApplication;

/**
 * ToastUtil
 * Create by mayakun at 2018/3/29 下午5:07
 */
public class ToastUtil {

    public static void show(Context context, int no_result) {
        Toast.makeText(context, no_result, Toast.LENGTH_LONG).show();
    }

    public static void show(String no_result) {
        Toast.makeText(MyApplication.getInstance(), no_result, Toast.LENGTH_LONG).show();
    }

    public static void showShort(String no_result) {
        if (TextUtils.isEmpty(no_result)) {
            return;
        }
        Toast.makeText(MyApplication.getInstance(), no_result, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, String no_result) {
        if (TextUtils.isEmpty(no_result)) {
            return;
        }
        Toast.makeText(context, no_result, Toast.LENGTH_SHORT).show();
    }

    /**
     * 显示在中间的toast
     * @param msg
     */
    public static void showCenterToast(String msg) {
        if (TextUtils.isEmpty(msg)) {
            return;
        }
        int padding = UIUtils.dp2px(MyApplication.getInstance(), 10);
        Toast mDefaultGravityToast = new Toast(MyApplication.getInstance());
        mDefaultGravityToast.setGravity(Gravity.CENTER, 0, 0);
        mDefaultGravityToast.setDuration(Toast.LENGTH_SHORT);

        TextView title = new TextView(MyApplication.getInstance());
        title.setBackgroundResource(R.drawable.bg_loading_dialog);
        title.setPadding(2 * padding, (int) (1.5f * padding), 2 * padding, (int) (1.5f * padding));
        title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
        title.setId(R.id.title);
        title.setTextColor(Color.WHITE);
        int width = FrameLayout.LayoutParams.WRAP_CONTENT;
        int height = FrameLayout.LayoutParams.WRAP_CONTENT;
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) new FrameLayout.LayoutParams(width, height);
        title.setLayoutParams(lp);
        title.setText(msg);
        mDefaultGravityToast.setView(title);
        mDefaultGravityToast.show();
    }


    //带图标的toast
//    public static Toast showToastWithImage(String msg, int imageId) {
//        return showToastWithImageCancel(msg, imageId);
//    }
//
//    public static Toast showToastWithImage(String msg) {
//        return showToastWithImageCancel(msg, R.drawable.yes);
//    }
//
//
//    public static Toast showToastWithImageCancel(String msg, int cancel_icon) {
//        Toast mToastWithImage = new Toast(MyApplication.getInstance());
//        mToastWithImage.setGravity(Gravity.CENTER, 0, 0);
//        View convertView = LayoutInflater.from(MyApplication.getInstance()).inflate(R.layout.temp_inflate_toast_layout, null);
//        //图片
//        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
//        icon.setImageResource(cancel_icon);
//        //文字
//        TextView title = (TextView) convertView.findViewById(R.id.tip);
//        title.setText(msg);
//        mToastWithImage.setView(convertView);
//        mToastWithImage.show();
//        return mToastWithImage;
//    }
}
