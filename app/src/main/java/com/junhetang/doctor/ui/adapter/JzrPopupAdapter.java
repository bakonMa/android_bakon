package com.junhetang.doctor.ui.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.ui.bean.PatientFamilyBean;

import java.util.ArrayList;
import java.util.List;


public class JzrPopupAdapter extends BaseAdapter implements Filterable {
    private Context context;
    private List<PatientFamilyBean.JiuzhenBean> showList;//筛选后显示的数据
    private List<PatientFamilyBean.JiuzhenBean> allList;//全部数据


    public JzrPopupAdapter(Context context, List<PatientFamilyBean.JiuzhenBean> list) {
        this.context = context;
        this.allList = list;
    }

    @Override
    public int getCount() {
        return showList == null ? 0 : showList.size();
    }

    @Override
    public Object getItem(int position) {
        return showList == null ? null : showList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = LayoutInflater.from(context).inflate(R.layout.item_choose_patient_dialog, null);
        TextView tvName = convertView.findViewById(R.id.tv_name);
        TextView tvAge = convertView.findViewById(R.id.tv_age);
        TextView tvSex = convertView.findViewById(R.id.tv_sex);

        tvName.setText(TextUtils.isEmpty(showList.get(position).patient_name) ? "" : showList.get(position).patient_name);
        tvAge.setText(showList.get(position).age + "岁");
        tvSex.setText(showList.get(position).sex == 0 ? "男" : "女");

        return convertView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                ArrayList<PatientFamilyBean.JiuzhenBean> newData = new ArrayList<>();
                if (!TextUtils.isEmpty(constraint)) {
                    for (PatientFamilyBean.JiuzhenBean bean : allList) {
                        if (bean.patient_name.startsWith(constraint.toString())) {
                            newData.add(bean);
                        }
                    }
                }
                results.values = newData;
                results.count = newData.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                showList = (ArrayList) results.values;
                notifyDataSetChanged();
            }
        };
        return filter;
    }
}
