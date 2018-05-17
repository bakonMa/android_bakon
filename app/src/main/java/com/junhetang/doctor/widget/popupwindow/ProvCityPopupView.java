package com.junhetang.doctor.widget.popupwindow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.junhetang.doctor.R;
import com.junhetang.doctor.utils.LogUtil;
import com.junhetang.doctor.application.DocApplication;
import com.junhetang.doctor.utils.UIUtils;
import com.junhetang.doctor.widget.PickerView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * ProvCityPopupView  只有省市（json定制数据）
 * Create at 2018/4/8 上午9:46 by mayakun
 */

public class ProvCityPopupView extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {

    private PickerView provincePickerView, cityPickerView;
    private TextView provinceTitle, cityTitle;

    private Activity mActivity;

    private TextView btn_cancel, btn_Comfirm;

    private List<CityBean> dataList = new ArrayList<>();
    private List<String> provList = new ArrayList<>();//显示省份
    private int provincePos = 0;
    private int cityPos = 0;

    public ProvCityPopupView(Activity activity, ClickedListener clickedListener) {
        super(activity);
        this.mActivity = activity;
        this.mListener = clickedListener;
        initView();
        initData();
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 点击外面的控件也可以使得PopUpWindow dimiss
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.dir_popupwindow_anim);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setBackgroundDrawable(new BitmapDrawable());
        this.setOnDismissListener(this);
    }

    private void initData() {
        Gson gson = new Gson();
        InputStream in = null;
        try {
            in = DocApplication.getInstance().getResources().getAssets().open("city_android.json");
            int available = in.available();
            byte[] b = new byte[available];
            in.read(b);
            String json = new String(b, "UTF-8");
            //全部地址
            dataList = gson.fromJson(json, new TypeToken<List<CityBean>>() {
            }.getType());
            for (CityBean cityBean : dataList) {
                provList.add(cityBean.name);
            }
            //初始显示
            provincePickerView.setData(provList);
            cityPickerView.setData(dataList.get(0).city);
        } catch (IOException e) {
            LogUtil.d(e.getMessage());
        }
    }

    private void initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.prov_city_popupview, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_cancel = view.findViewById(R.id.id_btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_Comfirm = view.findViewById(R.id.id_btn_comfirm);
        btn_Comfirm.setOnClickListener(this);

        provincePickerView = view.findViewById(R.id.id_wheel1);
        cityPickerView = view.findViewById(R.id.id_wheel2);
        provinceTitle = view.findViewById(R.id.id_wheel1_title);
        cityTitle = view.findViewById(R.id.id_wheel2_title);
        provincePickerView.setOnItemChangedListener(provinceListener);
        cityPickerView.setOnItemChangedListener(cityListener);
    }


    //省 切换
    PickerView.ItemSelectedListerner provinceListener = new PickerView.ItemSelectedListerner() {
        @Override
        public void onItemChanged(int postion) {
            provincePos = postion;
            cityPickerView.setData(dataList.get(postion).city);
        }
    };
    //市 切换
    PickerView.ItemSelectedListerner cityListener = new PickerView.ItemSelectedListerner() {
        @Override
        public void onItemChanged(int postion) {
            cityPos = postion;
        }
    };

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_cancel:
                dismiss();
                break;
            case R.id.id_btn_comfirm:
                dismiss();
                if (mListener != null) {
                    mListener.completeClicked(dataList.get(provincePos).name, dataList.get(provincePos).city[cityPos]);
                }
                break;
        }
    }

    public void show(View parent) {
        showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
    }

    @Override
    public void showAtLocation(View parent, int gravity, int x, int y) {
        UIUtils.lightOff(mActivity);
        super.showAtLocation(parent, gravity, x, y);
    }

    @Override
    public void onDismiss() {
        UIUtils.lightOn(mActivity);
    }


    private ClickedListener mListener;

    public interface ClickedListener {
        void completeClicked(String prov, String city);
    }


    //自用bean
    public class CityBean {
        public String name;
        public String city[];

    }

}
