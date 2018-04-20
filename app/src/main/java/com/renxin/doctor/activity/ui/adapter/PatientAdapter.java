package com.renxin.doctor.activity.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.ui.bean.PatientBean;
import com.renxin.doctor.activity.utils.CharacterParser;
import com.renxin.doctor.activity.utils.ImageUtil;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 * PatientAdapter 患者列表adapter
 * Create at 2018/4/14 下午11:21 by mayakun
 */
public class PatientAdapter extends BaseQuickAdapter<PatientBean, PatientAdapter.ViewHolder>
        implements StickyRecyclerHeadersAdapter<PatientAdapter.HeaderViewHolder> {


    public PatientAdapter(Context context, @Nullable List<PatientBean> data) {
        super(R.layout.item_patient, data);
    }

    @Override
    protected void convert(ViewHolder helper, PatientBean item) {
        helper.setText(R.id.tv_name, TextUtils.isEmpty(item.remark_name) ? item.nick_name : item.remark_name)
                .setText(R.id.tv_from, item.memb_class);
        ImageUtil.showCircleImage(item.head_url, helper.getView(R.id.iv_headerimg));

        if (mData.size() == mData.lastIndexOf(item) + 1
                || !getFirstLetter(mData.indexOf(item)).equals(getFirstLetter(mData.indexOf(item) + 1))) {
            helper.setGone(R.id.bottom_line, false);
        } else {
            helper.setGone(R.id.bottom_line, true);
        }
    }

    @Override
    public long getHeaderId(int position) {
        if (mData.isEmpty()) {
            return -1;
        }
        String c = CharacterParser.getInstance().getInitials(getItemSortLetter(position));
        return c.charAt(0);
    }

    @Override
    public HeaderViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_patient_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.tv_header);
        textView.setText(getFirstLetter(position));
    }

    //
    public int getPositionForSection(char section) {
        for (int i = 0; i < getData().size(); i++) {
            String firstChar1 = CharacterParser.getInstance().getSelling(getData().get(i).nick_name);
            String firstChar = CharacterParser.getInstance().getInitials(firstChar1);
            if (firstChar.charAt(0) == section) {
                return i;
            }
        }
        return -1;
    }

    //获取首字母（A~Z #）
    private String getFirstLetter(int pos) {
        return CharacterParser.getInstance().getInitials(getItemSortLetter(pos));
    }

    //获取 昵称的汉语拼音
    public String getItemSortLetter(int position) {
        return CharacterParser.getInstance().getSelling(mData.get(position).nick_name);
    }

    class HeaderViewHolder extends BaseViewHolder {
        HeaderViewHolder(View view) {
            super(view);
        }
    }

    class ViewHolder extends BaseViewHolder {
        ViewHolder(View view) {
            super(view);

        }
    }

}
