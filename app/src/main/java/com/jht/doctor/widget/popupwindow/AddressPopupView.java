package com.jht.doctor.widget.popupwindow;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.greendao.City;
import com.jht.doctor.manager.GreenDaoHelp;
import com.jht.doctor.utils.UIUtils;
import com.jht.doctor.widget.PickerView;

import java.util.ArrayList;
import java.util.List;

/**
 * AddressPopupView
 * Create at 2018/3/24 下午9:24 by mayakun
 */

public class AddressPopupView extends PopupWindow implements PopupWindow.OnDismissListener, View.OnClickListener {

    private PickerView provincePickerView, cityPickerView, districtPickerView;
    private TextView provinceTitle, cityTitle, districtTitle;

    private Activity mActivity;

    private TextView btn_cancel, btn_Comfirm;

    private List<City> provinceList, cityList, districtList;

    private int provincePos = 0;
    private int cityPos = 0;
    private int type = 3;//3：默认 显示省市区 2：省市 1：省

    public AddressPopupView(Activity activity, int type, ClickedListener clickedListener) {
        super(activity);
        this.mActivity = activity;
        this.type = type;
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
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setBackgroundDrawable(new BitmapDrawable());
        this.setOnDismissListener(this);
    }

    public AddressPopupView(Activity activity, ClickedListener clickedListener) {
        this(activity, 3, clickedListener);
    }

    private void initView() {
        View view = LayoutInflater.from(mActivity).inflate(R.layout.address_popupview, null);
        this.setContentView(view);
        this.setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        btn_cancel = view.findViewById(R.id.id_btn_cancel);
        btn_cancel.setOnClickListener(this);
        btn_Comfirm = view.findViewById(R.id.id_btn_comfirm);
        btn_Comfirm.setOnClickListener(this);

        provincePickerView = view.findViewById(R.id.id_wheel1);
        cityPickerView = view.findViewById(R.id.id_wheel2);
        districtPickerView = view.findViewById(R.id.id_wheel3);
        provinceTitle = view.findViewById(R.id.id_wheel1_title);
        cityTitle = view.findViewById(R.id.id_wheel2_title);
        districtTitle = view.findViewById(R.id.id_wheel3_title);

        switch (type) {
            case 1:
                cityPickerView.setVisibility(View.GONE);
                cityTitle.setVisibility(View.GONE);
                districtPickerView.setVisibility(View.GONE);
                districtTitle.setVisibility(View.GONE);
                break;
            case 2:
                districtPickerView.setVisibility(View.GONE);
                districtTitle.setVisibility(View.GONE);
                break;
        }
    }

    private void initData() {
        if (provinceList == null || provinceList.isEmpty()) {
            provinceList = GreenDaoHelp.getAllProvince();
        }
        if (type == 2) {
            cityList = GreenDaoHelp.getCityByParentCode(provinceList.get(provincePos).getCityCode());
        } else if (type == 3) {
            cityList = GreenDaoHelp.getCityByParentCode(provinceList.get(provincePos).getCityCode());
            districtList = GreenDaoHelp.getCityByParentCode(cityList.get(cityPos).getCityCode());
        }

    }

    private void initEvent() {
        provincePickerView.setData(CitiesToStrs(provinceList));
        provincePickerView.setOnItemChangedListener(provinceListener);
        if (type == 2) {
            cityPickerView.setData(CitiesToStrs(cityList));
            cityPickerView.setOnItemChangedListener(cityListener);
        } else if (type == 3) {
            cityPickerView.setData(CitiesToStrs(cityList));
            cityPickerView.setOnItemChangedListener(cityListener);
            districtPickerView.setData(CitiesToStrs(districtList));
            provincePickerView.setOnItemChangedListener(provinceListener);
        }
    }

    //省 切换
    PickerView.ItemSelectedListerner provinceListener = new PickerView.ItemSelectedListerner() {
        @Override
        public void onItemChanged(int postion) {
            provincePos = postion;
            cityPos = 0;
            //台湾，香港，澳门 特殊处理
            if (postion == 31 || postion == 32 || postion == 33) {
                cityPickerView.clearView();
                districtPickerView.clearView();
            } else {
                initData();
                if (type == 2) {
                    cityPickerView.setData(CitiesToStrs(cityList));
                } else if (type == 3) {
                    cityPickerView.setData(CitiesToStrs(cityList));
                    districtPickerView.setData(CitiesToStrs(districtList));
                }
            }
        }
    };
    //市 切换
    PickerView.ItemSelectedListerner cityListener = new PickerView.ItemSelectedListerner() {
        @Override
        public void onItemChanged(int postion) {
            cityPos = postion;
            if (type == 3) {
                initData();
                districtPickerView.setData(CitiesToStrs(districtList));
            }
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
                    if (provincePos == 31 || provincePos == 32 || provincePos == 33) {
                        mListener.completeClicked(provinceList.get(provincePos).getCity(),
                                provinceList.get(provincePos).getCityCode() + "");
                    } else {
                        if (type == 1) {
                            mListener.completeClicked(provinceList.get(provincePos).getCity(),
                                    provinceList.get(provincePos).getCityCode() + "");
                            break;
                        } else if (type == 2) {
                            mListener.completeClicked(provinceList.get(provincePos).getCity(),
                                    provinceList.get(provincePos).getCityCode() + "",
                                    cityList.get(cityPos).getCity(),
                                    cityList.get(cityPos).getCityCode() + "");
                        } else if (type == 3) {
                            mListener.completeClicked(provinceList.get(provincePos).getCity(),
                                    provinceList.get(provincePos).getCityCode() + "",
                                    cityList.get(cityPos).getCity(),
                                    cityList.get(cityPos).getCityCode() + "",
                                    districtList.get(districtPickerView.getPosition()).getCity(),
                                    districtList.get(districtPickerView.getPosition()).getCityCode() + "");
                        }
                    }
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
        //        void completeClicked(String addressInfo, String provinceCode, String cityCode, String districtCode);
        void completeClicked(String... info);//str，code，str，code......
    }

    private List<String> CitiesToStrs(List<City> cities) {
        List<String> str = new ArrayList<>();
        for (City city : cities) {
            str.add(city.getCity());
        }
        return str;
    }
}
