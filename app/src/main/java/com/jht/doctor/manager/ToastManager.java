package com.jht.doctor.manager;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.jht.doctor.R;
import com.jht.doctor.application.CustomerApplication;

/**
 * @author: ZhaoYun
 * @date: 2017/10/31
 * @project: customer-android-2th
 * @detail:
 */
public final class ToastManager {

    private Toast mToast;
    private final CustomerApplication mApplication;
    private Runnable mRunnable;
    private static Handler sHandler = new Handler(Looper.getMainLooper());

    public ToastManager(CustomerApplication application) {
        mApplication = application;
    }

    public Toast shortToast(final String message) {
        cancel();
        removeCallback();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mToast = new Toast(mApplication);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                View view = LayoutInflater.from(mApplication).inflate(R.layout.layout_toast, null);
                TextView tvMessage = (TextView) view.findViewById(R.id.id_tv_toast);
                //设置文本
                tvMessage.setText(TextUtils.isEmpty(message) ? "" : message);
                //设置视图
                mToast.setView(view);
                //设置显示时长
                mToast.setDuration(Toast.LENGTH_SHORT);
                //显示
                mToast.show();
                removeCallback();
            }
        };
        sHandler.post(mRunnable);
        return mToast;
    }


    public Toast longToast(final String message) {
        cancel();
        removeCallback();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mToast = Toast.makeText(mApplication, message, Toast.LENGTH_LONG);
                mToast.show();
                removeCallback();
            }
        };
        sHandler.post(mRunnable);
        return mToast;
    }

    public Toast centerShortToast(final String message) {
        cancel();
        removeCallback();

        mRunnable = new Runnable() {
            @Override
            public void run() {
                mToast = Toast.makeText(mApplication, message, Toast.LENGTH_LONG);
                mToast.setGravity(Gravity.CENTER, 0, 0);
                mToast.show();
                removeCallback();
            }
        };
        sHandler.post(mRunnable);
        return mToast;
    }

    private void cancel() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    private void removeCallback() {
        if (mRunnable != null) {
            sHandler.removeCallbacks(mRunnable);
            mRunnable = null;
        }
    }

}
