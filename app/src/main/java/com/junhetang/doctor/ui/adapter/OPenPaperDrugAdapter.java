package com.junhetang.doctor.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.ui.bean_jht.DrugBean;

import java.util.List;

/**
 * PatientAdapter 患者列表adapter
 * Create at 2018/4/14 下午11:21 by mayakun
 */
public class OPenPaperDrugAdapter extends BaseQuickAdapter<DrugBean, BaseViewHolder> {

    private int showMaxNum = 3;
    private boolean isShowAll = false;

    public OPenPaperDrugAdapter(Context context, @Nullable List<DrugBean> data, int showMaxNum) {
        super(R.layout.item_show_drug, data);
        this.showMaxNum = showMaxNum;
    }

    @Override
    protected void convert(BaseViewHolder helper, DrugBean item) {
        helper.setText(R.id.tv_drugname, item.drug_name)
                .setText(R.id.tv_num, item.drug_num + item.unit)
                .setText(R.id.tv_usertype, item.decoction);
    }

    @Override
    public int getItemCount() {
        if (isShowAll) {
            return mData == null ? 0 : mData.size();
        } else {
            return mData.size() > showMaxNum ? showMaxNum : mData.size();
        }
    }

    //是否显示全部
    public void setIsShowAll(boolean isShowAll) {
        this.isShowAll = isShowAll;
        notifyDataSetChanged();
    }

}
