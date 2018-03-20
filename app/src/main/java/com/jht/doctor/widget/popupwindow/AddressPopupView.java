package com.jht.doctor.widget.popupwindow;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.greendao.City;
import com.jht.doctor.manager.GreenDaoHelp;
import com.jht.doctor.widget.PickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Tang on 2017/11/2.
 */

public class AddressPopupView extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {

    private PickerView provincePickerView, cityPickerView, districtPickerView;

    private Activity mActivity;

    private TextView btn_cancel, btn_Comfirm;

    private List<City> provinceList, cityList, districtList;

    private int provincePos = 0;
    private int cityPos = 0;

    public AddressPopupView(Activity activity, ClickedListener clickedListener) {
        super(activity);
        this.mActivity = activity;
        this.mListener = clickedListener;
        initData();
        initView();
        initEvent();
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 点击外面的控件也可以使得PopUpWindow dimiss
        this.setOutsideTouchable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.dir_popupwindow_anim);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        this.setBackgroundDrawable(dw);
        this.setOnDismissListener(this);
    }

    private void initData() {
        provinceList = GreenDaoHelp.getAllProvince();
        cityList = GreenDaoHelp.getCityByParentCode(provinceList.get(provincePos).getCityCode());
        districtList = GreenDaoHelp.getCityByParentCode(cityList.get(cityPos).getCityCode());
    }

    private void initEvent() {
        provincePickerView.setData(CitiesToStrs(provinceList));
        cityPickerView.setData(CitiesToStrs(cityList));
        districtPickerView.setData(CitiesToStrs(districtList));
        provincePickerView.setOnItemChangedListener(provinceListener);
        cityPickerView.setOnItemChangedListener(cityListener);
    }

    PickerView.ItemSelectedListerner provinceListener = new PickerView.ItemSelectedListerner() {
        @Override
        public void onItemChanged(int postion) {
            provincePos = postion;
            cityPos = 0;
            if (postion == 31 || postion == 32 || postion == 33) {
                cityPickerView.clearView();
                districtPickerView.clearView();
            } else {
                initData();
                cityPickerView.setData(CitiesToStrs(cityList));
                districtPickerView.setData(CitiesToStrs(districtList));
            }
        }
    };

    PickerView.ItemSelectedListerner cityListener = new PickerView.ItemSelectedListerner() {
        @Override
        public void onItemChanged(int postion) {
            cityPos = postion;
            initData();
            districtPickerView.setData(CitiesToStrs(districtList));
        }
    };

    private void initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.address_popupview, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_cancel = (TextView) view.findViewById(R.id.id_btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_Comfirm = (TextView) view.findViewById(R.id.id_btn_comfirm);
        btn_Comfirm.setOnClickListener(this);
        provincePickerView = (PickerView) view.findViewById(R.id.id_wheel1);
        cityPickerView = (PickerView) view.findViewById(R.id.id_wheel2);
        districtPickerView = (PickerView) view.findViewById(R.id.id_wheel3);
    }

    @Override
    public void onDismiss() {
        WindowManager.LayoutParams lp = mActivity.getWindow().getAttributes();
        lp.alpha = 1.0f;
        mActivity.getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.id_btn_cancel:
                dismiss();
                break;
            case R.id.id_btn_comfirm:
                dismiss();
                if (mListener != null) {
                    if (provincePos == 31 || provincePos == 32 || provincePos == 33) {
                        mListener.completeClicked(provincePickerView.getItem()
                                , provinceList.get(provincePos).getCityCode() + ""
                                , ""
                                , "");
                    } else {
                        mListener.completeClicked(provincePickerView.getItem() + "-" + cityPickerView.getItem() + "-" + districtPickerView.getItem()
                                , provinceList.get(provincePos).getCityCode() + ""
                                , cityList.get(cityPos).getCityCode() + ""
                                , districtList.get(districtPickerView.getPosition()).getCityCode() + "");
                    }
                }
                break;
        }
    }

    private ClickedListener mListener;

    public interface ClickedListener {
        void completeClicked(String addressInfo, String provinceCode, String cityCode, String districtCode);
    }

    private List<String> CitiesToStrs(List<City> cities) {
        List<String> str = new ArrayList<>();
        for (City city : cities) {
            str.add(city.getCity());
        }
        return str;
    }
}
