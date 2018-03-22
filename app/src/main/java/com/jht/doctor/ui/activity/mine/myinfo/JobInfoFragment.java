package com.jht.doctor.ui.activity.mine.myinfo;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jht.doctor.R;
import com.jht.doctor.ui.activity.mine.PersonalActivity;
import com.jht.doctor.ui.base.BaseAppCompatFragment;
import com.jht.doctor.ui.bean.MyInfoBean;
import com.jht.doctor.utils.RegexUtil;
import com.jht.doctor.utils.U;

import butterknife.BindView;

/**
 * mayakun
 * 我的资料-工作信息
 */
public class JobInfoFragment extends BaseAppCompatFragment {

    @BindView(R.id.tv_company_name)
    TextView tvCompanyName;
    @BindView(R.id.tv_job_property)
    TextView tvJobProperty;
    @BindView(R.id.tv_job_trade)
    TextView tvJobTrade;
    @BindView(R.id.tv_job_level)
    TextView tvJobLevel;
    @BindView(R.id.tv_job_income)
    TextView tvJobIncome;

    private MyInfoBean.UserJobDTOBean bean;

    @Override
    public View setViewId(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_job_info, null);
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        bean = (MyInfoBean.UserJobDTOBean) getArguments().get(PersonalActivity.KEY_INFO);
        if (bean != null) {
            tvCompanyName.setText(TextUtils.isEmpty(bean.companyName) ? "" : bean.companyName);
            tvJobProperty.setText(U.keyToValue(bean.companyType, U.getConfigData().COMPANY_TYPE));
            tvJobTrade.setText(U.keyToValue(bean.industry, U.getConfigData().INDUSTRY));
            tvJobLevel.setText(U.keyToValue(bean.position, U.getConfigData().POSITION_LEVEL));
            tvJobIncome.setText(RegexUtil.formatMoney(bean.monthlyIncome) + " 元");
        }
    }

    @Override
    protected void setupActivityComponent() {

    }

}
