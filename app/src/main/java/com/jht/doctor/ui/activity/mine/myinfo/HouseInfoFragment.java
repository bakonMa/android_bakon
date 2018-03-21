package com.jht.doctor.ui.activity.mine.myinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.manager.GreenDaoHelp;
import com.jht.doctor.ui.activity.mine.PersonalActivity;
import com.jht.doctor.ui.base.BaseAppCompatFragment;
import com.jht.doctor.ui.bean.MyInfoBean;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.utils.U;

import butterknife.BindView;
import butterknife.Unbinder;

/**
 * mayakun
 * 我的资料-房产信息
 */
public class HouseInfoFragment extends BaseAppCompatFragment {

    @BindView(R.id.tv_hourse_are)
    TextView tvHourseAre;
    @BindView(R.id.tv_house_address)
    TextView tvHouseAddress;
    @BindView(R.id.tv_ishas_borrow)
    TextView tvIshasBorrow;
    @BindView(R.id.tv_borrow_from)
    TextView tvBorrowFrom;
    @BindView(R.id.rlt_borrow_from)
    RelativeLayout rltBorrowFrom;
    @BindView(R.id.tv_borrow_money)
    TextView tvBorrowMoney;
    @BindView(R.id.rlt_borrow_money)
    RelativeLayout rltBorrowMoney;
    @BindView(R.id.tv_hourse_type)
    TextView tvHourseType;
    @BindView(R.id.tv_hours_area)
    TextView tvHoursArea;
    @BindView(R.id.tv_hours_name)
    TextView tvHoursName;
    @BindView(R.id.tv_buy_money)
    TextView tvBuyMoney;
    @BindView(R.id.id_buy_money_rlt)
    RelativeLayout idBuyMoneyRlt;
    @BindView(R.id.tv_rent_money)
    TextView tvRentMoney;
    @BindView(R.id.id_rent_money_rlt)
    RelativeLayout idRentMoneyRlt;
    Unbinder unbinder;

    private MyInfoBean.UserHouseDTOBean bean;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_house_info, null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        bean = (MyInfoBean.UserHouseDTOBean) getArguments().get(PersonalActivity.KEY_INFO);
        if (bean != null) {
            //类型，面积，小区名称
            tvHourseType.setText(U.keyToValue(bean.houseType, U.getConfigData().HOUSE_TYPE));
            if ("05".equals(bean.houseType)) {//商铺
                idBuyMoneyRlt.setVisibility(View.VISIBLE);
                idRentMoneyRlt.setVisibility(View.VISIBLE);
                tvBuyMoney.setText(bean.tradingAmt <= 0 ? "0 万元" : RegexUtil.formatMoneyNoZero(bean.tradingAmt/10000) + " 万元");
                tvRentMoney.setText(bean.monthRentalAmount <= 0 ? "0 元" : RegexUtil.formatMoneyNoZero(bean.monthRentalAmount) + " 元");
            } else {
                idBuyMoneyRlt.setVisibility(View.GONE);
                idRentMoneyRlt.setVisibility(View.GONE);
            }
            tvHoursArea.setText(bean.houseArea + "平米");
            tvHoursName.setText(TextUtils.isEmpty(bean.communityName) ? "" : bean.communityName);
            //省市区
            tvHourseAre.setText(GreenDaoHelp.getLongCityName(bean.provinceCode, bean.cityCode, bean.areaCode));
            //具体地址
            tvHouseAddress.setText(TextUtils.isEmpty(bean.detailAddress) ? "" : bean.detailAddress);

            if ("0".equals(bean.hasLoan)) {
                tvIshasBorrow.setText("是");
                rltBorrowFrom.setVisibility(View.VISIBLE);
                rltBorrowMoney.setVisibility(View.VISIBLE);
                tvBorrowFrom.setText("01".equals(bean.loanOrg) ? "银行" : "非银行");
                tvBorrowMoney.setText(bean.loanAmt <= 0 ? "0.00 元" : RegexUtil.formatMoney(bean.loanAmt) + " 元");
            } else {
                tvIshasBorrow.setText("否");
                rltBorrowFrom.setVisibility(View.GONE);
                rltBorrowMoney.setVisibility(View.GONE);
            }
        }
    }

    @Override
    protected void setupActivityComponent() {

    }
}
