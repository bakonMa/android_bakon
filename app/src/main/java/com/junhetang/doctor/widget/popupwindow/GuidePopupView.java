package com.junhetang.doctor.widget.popupwindow;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.ui.adapter.GuideAdapter;
import com.junhetang.doctor.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * JzrPopupListView
 * Create at 2018/9/5 上午9:57 by mayakun
 */
public class GuidePopupView extends PopupWindow {
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.tv_iknow)
    TextView tvIknow;

    private Context mContext;
    private int type;
    private List<Integer> guideImage = new ArrayList();
    private GuideAdapter guideAdapter;
    private String spKey;

    public GuidePopupView(Context context, int type) {
        this.mContext = context;
        this.type = type;
        initView();
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 点击外面的控件也可以使得PopUpWindow dimiss
//        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.dir_popupwindow_anim);
        // 实例化一个ColorDrawable颜色为半透明
        this.setBackgroundDrawable(new BitmapDrawable());
    }

    private void initView() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_guide_popupview, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        ButterKnife.bind(this, view);

        switch (type) {
//            case GUIDE_TYPE.GUIDE_TYPE_1:
//                spKey = SPConfig.SP_GUIDE_V120_1;
//                guideImage.add(R.drawable.guide1_1);
//                guideImage.add(R.drawable.guide1_2);
//                break;
//            case GUIDE_TYPE.GUIDE_TYPE_2:
//                spKey = SPConfig.SP_GUIDE_V120_2;
//                guideImage.add(R.drawable.guide2_1);
//                guideImage.add(R.drawable.guide2_2);
//                break;
//            case GUIDE_TYPE.GUIDE_TYPE_3:
//                spKey = SPConfig.SP_GUIDE_V120_3;
//                guideImage.add(R.drawable.guide3_1);
//                break;
//            case GUIDE_TYPE.GUIDE_TYPE_4:
//                spKey = SPConfig.SP_GUIDE_V120_4;
//                guideImage.add(R.drawable.guide4_1);
//                break;
        }

        guideAdapter = new GuideAdapter(mContext, guideImage);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(guideAdapter);

        //只有一张图的时候
        tvIknow.setVisibility(guideImage.size() == 1 ? View.VISIBLE : View.GONE);

        //最后一页显示【知道了】
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                tvIknow.setVisibility(position == guideImage.size() - 1 ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.tv_iknow)
    void btnClick() {
        if (mViewPager.getCurrentItem() == guideImage.size() - 1) {
            //设置true 下次不再显示
            DocApplication.getAppComponent().dataRepo().appSP().setBoolean(spKey, true);
            dismiss();
        }
    }

    public void show(View parent) {
        showAtLocation(parent, Gravity.CENTER, 0, 0);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        UIUtils.lightOff(((Activity) mContext));
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        UIUtils.lightOn((Activity) mContext);
    }

    public interface GUIDE_TYPE {
        int GUIDE_TYPE_1 = 1;
        int GUIDE_TYPE_2 = 2;
        int GUIDE_TYPE_3 = 3;
        int GUIDE_TYPE_4 = 4;
    }
}
