package com.junhetang.doctor.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.ui.bean.DrugBean;

import java.util.List;

/**
 * PatientAdapter 患者列表adapter
 * Create at 2018/4/14 下午11:21 by mayakun
 */
public class OPenPaperDrugAdapter extends BaseQuickAdapter<DrugBean, BaseViewHolder> {

    private int showMaxNum = 3;
    private boolean isShowAll = false;

    //展开 收起
    public OPenPaperDrugAdapter(Context context, @Nullable List<DrugBean> data, int showMaxNum) {
        super(R.layout.item_show_drug_grid, data);
        this.showMaxNum = showMaxNum;
    }

    public OPenPaperDrugAdapter(Context context, @Nullable List<DrugBean> data) {
        super(R.layout.item_show_drug_grid, data);
        this.showMaxNum = showMaxNum;
    }

    @Override
    protected void convert(BaseViewHolder helper, DrugBean item) {
        //常规 不显示
        String decoction = "常规".equals(item.decoction) ? "" : String.format(" (%s)", item.decoction);
        helper.setText(R.id.tv_druginfo, String.format("%s %s%s%s", item.drug_name, item.drug_num, item.unit, decoction));
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();

//        if (isShowAll) {
//            return mData == null ? 0 : mData.size();
//        } else {
//            return mData.size() > showMaxNum ? showMaxNum : mData.size();
//        }
    }

    //是否显示全部
    public void setIsShowAll(boolean isShowAll) {
        this.isShowAll = isShowAll;
        notifyDataSetChanged();
    }

}
