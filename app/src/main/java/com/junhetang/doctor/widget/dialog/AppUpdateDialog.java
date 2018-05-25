package com.junhetang.doctor.widget.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.ui.bean.AppUpdateBean;

import static android.view.KeyEvent.KEYCODE_BACK;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

/**
 * Created by zhaoyun on 2018/1/13.
 */
public class AppUpdateDialog extends Dialog implements View.OnClickListener {

    public interface StartDownloadingListener {
        void startDownloading(String downloadUrl, String netMD5, boolean force);
    }

    public interface CancelListener {
        void onCanceled(boolean force);
    }

    private AppUpdateBean appUpdateBean;
    private StartDownloadingListener startDownloadingListener;
    private CancelListener cancelListener;

    private TextView tv_updatelog;
    private TextView tv_update;
    private FrameLayout fl_progress;
    private ProgressBar pb_progress;

    public AppUpdateDialog(@NonNull Context context, int themeResId, AppUpdateBean appUpdateBean, StartDownloadingListener startDownloadingListener, CancelListener cancelListener) {
        super(context, themeResId);
        this.appUpdateBean = appUpdateBean;
        this.startDownloadingListener = startDownloadingListener;
        this.cancelListener = cancelListener;
    }

    private AppUpdateDialog(@NonNull Context context) {
        super(context);
    }

    private AppUpdateDialog(@NonNull Context context, boolean cancelable,
                            @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    private AppUpdateDialog(@NonNull Context context, AppUpdateBean appUpdateBean, StartDownloadingListener startDownloadingListener, CancelListener cancelListener) {
        this(context, 0, appUpdateBean, startDownloadingListener, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = UIUtils.dp2px(getContext(), 300);
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setAttributes(layoutParams);

        View view = View.inflate(getContext(), R.layout.layout_appupdate, null);
        setContentView(view);
        ImageView iv_close = view.findViewById(R.id.iv_close);
        TextView tv_version = view.findViewById(R.id.tv_version);
        tv_updatelog = view.findViewById(R.id.tv_updatelog);
        tv_update = view.findViewById(R.id.tv_update);
        fl_progress = view.findViewById(R.id.fl_progress);
        pb_progress = view.findViewById(R.id.pb_progress);

        iv_close.setOnClickListener(this);
        iv_close.setVisibility(appUpdateBean.isforced == 1 ? GONE : VISIBLE);
        tv_version.setText("V" + (TextUtils.isEmpty(appUpdateBean.current_version) ? "1.0.0" : appUpdateBean.current_version));
        tv_updatelog.setText(appUpdateBean.comments == null ? "" : appUpdateBean.comments);
        tv_updatelog.setMovementMethod(ScrollingMovementMethod.getInstance());
        if (TextUtils.isEmpty(appUpdateBean.comments)) {
            tv_updatelog.setVisibility(GONE);
        }
        tv_update.setOnClickListener(this);

        setCanceledOnTouchOutside(false);
        setCancelable(appUpdateBean.isforced == 0);
        setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KEYCODE_BACK && cancelListener != null) {
                    cancelListener.onCanceled(appUpdateBean.isforced == 1);
                }
                return false;
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                if (isShowing()) {
                    dismiss();
                }
                if (cancelListener != null) {
                    cancelListener.onCanceled(appUpdateBean.isforced == 1);
                }
                break;

            case R.id.tv_update:
                if (startDownloadingListener != null) {
                    startDownloadingListener.startDownloading(appUpdateBean.down_url, appUpdateBean.md5code, appUpdateBean.isforced == 1);
                }
                break;

            default:
                break;
        }
    }

    public void setProgress(int progress) {
        pb_progress.setProgress(progress);
    }

    public void setUpdateText(String updateText) {
        tv_update.setText(updateText);
    }

    public void switchViewState(boolean isProgressing) {
        if (isProgressing) {
            fl_progress.setVisibility(VISIBLE);
            tv_update.setVisibility(GONE);
        } else {
            fl_progress.setVisibility(GONE);
            tv_update.setVisibility(VISIBLE);
        }
    }

}
