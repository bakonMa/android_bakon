package com.bakon.android.widget;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bakon.android.R;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * Created by Tang on 2017/11/13.
 */

public class EmptyView extends LinearLayout {
    @BindView(R.id.iv_img)
    ImageView ivTopImg;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_subtitle)
    TextView tvSubTitle;

    //是否显示图片
    private boolean isShowTopImage;
    private int topImage = R.drawable.msg_1;
    private String titleText;
    private String subTitleText;


    public EmptyView(Context context, String titleText, boolean isShowTopImage) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.empty_view, this, true);
        ButterKnife.bind(view);
        this.titleText = titleText;
        this.isShowTopImage = isShowTopImage;

        init();
    }

    private void init() {
        if (!TextUtils.isEmpty(titleText)) {
            tvTitle.setText(titleText);
        }
        if (!TextUtils.isEmpty(subTitleText)) {
            tvSubTitle.setText(subTitleText);
        }
        ivTopImg.setImageResource(topImage);
        ivTopImg.setVisibility(isShowTopImage ? VISIBLE : GONE);
    }

    public void setTopImage(int topImage) {
        ivTopImg.setVisibility(VISIBLE);
        ivTopImg.setImageResource(topImage);
    }

    public void setTopImgShow(boolean imgShow) {
        ivTopImg.setVisibility(imgShow ? VISIBLE : GONE);
    }

    public void setTitelText(String titleText) {
        if (titleText != null) {
            tvTitle.setText(titleText);
        }
    }

    public void setSubTitelText(String subTitelText) {
        if (subTitelText != null) {
            tvSubTitle.setText(subTitelText);
        }
    }


}
