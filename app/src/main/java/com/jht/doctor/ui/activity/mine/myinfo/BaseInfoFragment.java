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

import butterknife.BindView;

/**
 * mayakun
 * 我的资料-基本信息
 */
public class BaseInfoFragment extends BaseAppCompatFragment {

    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_idnum)
    TextView tvIdnum;
    private MyInfoBean.UserDTOBean bean;

    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_base_info, null);
    }

    @Override
    public void initData(Bundle savedInstanceState) {
        bean = (MyInfoBean.UserDTOBean) getArguments().get(PersonalActivity.KEY_INFO);
        if (bean != null) {
            tvName.setText(TextUtils.isEmpty(bean.userName) ? "" : bean.userName);
            tvIdnum.setText(TextUtils.isEmpty(bean.certNo) ? "" : RegexUtil.hideID(bean.certNo));
        }
    }

    @Override
    protected void setupActivityComponent() {

    }

}
