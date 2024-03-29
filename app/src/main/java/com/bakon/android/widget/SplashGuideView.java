package com.bakon.android.widget;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bakon.android.R;
import com.bakon.android.ui.activity.welcome.SplashActivity;
import com.bakon.android.ui.activity.welcome.TranslucentActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @author Tang
 */
public class SplashGuideView extends LinearLayout {

    @BindView(R.id.iv_guide)
    ImageView ivGuide;
    @BindView(R.id.id_tv1)
    TextView idTv1;
    @BindView(R.id.id_tv2)
    TextView idTv2;
    @BindView(R.id.id_tv3)
    TextView idTv3;
    @BindView(R.id.id_btn_enter)
    TextView idBtnEnter;

    public SplashGuideView(Context context, int resId, String tv1, String tv2, String tv3, boolean isShowButton) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.splash_guideitem, this, true);
        ButterKnife.bind(view);
        if (resId > 0) {
            ivGuide.setBackgroundResource(resId);
        }
        idTv1.setText(tv1);
        idTv2.setText(tv2);
        idTv3.setText(tv3);
        if (isShowButton) {
            idBtnEnter.setVisibility(VISIBLE);
            idBtnEnter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, TranslucentActivity.class));
                    ((SplashActivity) context).finish();
                }
            });
        }

    }

    public SplashGuideView(Context context, int resId, boolean isShowButton) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.splash_guideitem, this, true);
        ButterKnife.bind(view);
        if (resId > 0) {
            ivGuide.setBackgroundResource(resId);
        }
        idTv1.setVisibility(GONE);
        idTv2.setVisibility(GONE);
        idTv3.setVisibility(GONE);
        if (isShowButton) {
            idBtnEnter.setVisibility(VISIBLE);
            idBtnEnter.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, TranslucentActivity.class));
                    ((SplashActivity) context).finish();
                }
            });
        }

    }

}
