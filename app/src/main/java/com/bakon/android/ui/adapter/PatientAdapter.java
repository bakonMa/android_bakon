package com.bakon.android.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.bakon.android.R;
import com.bakon.android.ui.bean.PatientBean;
import com.bakon.android.utils.CharacterParser;
import com.bakon.android.utils.ImageUtil;
import com.bakon.android.utils.UIUtils;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

/**
 * PatientAdapter 患者列表adapter
 * Create at 2018/4/14 下午11:21 by mayakun
 */
public class PatientAdapter extends BaseQuickAdapter<PatientBean, BaseViewHolder>
        implements StickyRecyclerHeadersAdapter<PatientAdapter.HeaderViewHolder> {


    public PatientAdapter(Context context, @Nullable List<PatientBean> data) {
        super(R.layout.item_patient, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, PatientBean item) {
        String name = TextUtils.isEmpty(item.remark_name) ? item.nick_name : item.remark_name;
        helper.setText(R.id.tv_name, TextUtils.isEmpty(name) ? "" : name)
                .setText(R.id.tv_from, TextUtils.isEmpty(item.valid_name) ? "" : item.valid_name)
                .setGone(R.id.tv_redpoint, item.is_new == 1);

        helper.setTextColor(R.id.tv_from, UIUtils.getColor(item.is_valid == 1 ? R.color.color_30ad37 : R.color.color_main));
        UIUtils.setCompoundDrawable(helper.getView(R.id.tv_from),12,5,
                (item.is_valid == 1 ? R.drawable.icon_patient_wx : R.drawable.icon_patient_paper), Gravity.LEFT);

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_patient_header, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(HeaderViewHolder holder, int position) {
        TextView textView = holder.itemView.findViewById(R.id.tv_header);
        textView.setText(getFirstLetter(position));
    }

    //侧边快捷栏 使用
    public int getPositionForSection(char section) {
        for (int i = 0; i < getData().size(); i++) {
            String firstChar1 = CharacterParser.getInstance().getSelling(TextUtils.isEmpty(getData().get(i).remark_name) ? getData().get(i).nick_name : getData().get(i).remark_name);
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


    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    //获取 昵称的汉语拼音
    public String getItemSortLetter(int position) {
        return CharacterParser.getInstance().getSelling(TextUtils.isEmpty(getData().get(position).remark_name) ? getData().get(position).nick_name : getData().get(position).remark_name);
    }

    class HeaderViewHolder extends BaseViewHolder {
        HeaderViewHolder(View view) {
            super(view);
        }
    }


}
