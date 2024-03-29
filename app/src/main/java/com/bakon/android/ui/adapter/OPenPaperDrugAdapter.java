package com.bakon.android.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bakon.android.R;
import com.bakon.android.ui.bean.DrugBean;
import com.bakon.android.utils.UIUtils;

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
        helper.setText(R.id.tv_druginfo,
                String.format("%s %s%s%s", UIUtils.formateDrugName(item.drug_name),
                        item.drug_num, item.unit, decoction));
        if (item.sub_drug_type == 1) {
            UIUtils.setCompoundDrawable(helper.getView(R.id.tv_druginfo), 13, 2, R.drawable.icon_jing, Gravity.LEFT);
        } else {
            TextView textView = helper.getView(R.id.tv_druginfo);
            textView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        }
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
