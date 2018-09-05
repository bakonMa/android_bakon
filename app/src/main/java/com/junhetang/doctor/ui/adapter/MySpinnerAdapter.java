package com.junhetang.doctor.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.ui.bean.PatientFamilyBean;
import com.junhetang.doctor.utils.Constant;
import com.junhetang.doctor.utils.UIUtils;

import java.util.List;


public class MySpinnerAdapter extends BaseAdapter implements SpinnerAdapter {

    private Context context;
    private String patientName;
    private List<PatientFamilyBean.JiuzhenBean> list;

    public MySpinnerAdapter(Context context, List<PatientFamilyBean.JiuzhenBean> list, String patientName) {
        this.context = context;
        this.patientName = patientName;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
        TextView tvName = convertView.findViewById(android.R.id.text1);
        if (position == 0) {
            tvName.setText(list.get(position).patient_name);
        } else {
            tvName.setText(String.format("%s(%s)", list.get(position).patient_name, Constant.RELATION_TYPE[list.get(position).relationship]));
        }

        if (!TextUtils.isEmpty(patientName)) {
            if (patientName.equals(list.get(position).patient_name)) {
                UIUtils.setCompoundDrawable(tvName, 15, 5, R.drawable.icon_new, Gravity.LEFT);
            }
        }
        return convertView;
    }

}