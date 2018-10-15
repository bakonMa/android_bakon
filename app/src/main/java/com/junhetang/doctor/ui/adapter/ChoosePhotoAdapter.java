package com.junhetang.doctor.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.utils.ImageUtil;
import com.junhetang.doctor.utils.LogUtil;

import java.util.List;

/**
 * PatientAdapter 患者列表adapter
 * Create at 2018/4/14 下午11:21 by mayakun
 */
public class ChoosePhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;
    private int showMaxNum = 9;

    public ChoosePhotoAdapter(Context context, @Nullable List<String> data, int maxSize) {
        super(R.layout.item_choose_img, data);
        this.mContext = context;
        this.showMaxNum = maxSize;
    }

    public ChoosePhotoAdapter(Context context, @Nullable List<String> data) {
        this(context, data, 9);
    }

    @Override
    protected void convert(BaseViewHolder helper, String path) {
        helper.setGone(R.id.tv_close, !TextUtils.isEmpty(path))
                .setImageResource(R.id.iv_img, 0)
                .addOnClickListener(R.id.tv_close)
                .addOnClickListener(R.id.iv_img);
        LogUtil.d("ChoosePhotoAdapter", mData.size() + "---" + path);
        if (TextUtils.isEmpty(path)) {
            ((ImageView) helper.getView(R.id.iv_img)).setImageDrawable(null);
        } else {
            ImageUtil.showImage(path, helper.getView(R.id.iv_img));
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    //处理最后显示多一张添加提示的图片
    public void changeNotifyData() {
        //查找添加的空数据
        int tempPos = -1;
        for (int i = 0; i < mData.size(); i++) {
            if (TextUtils.isEmpty(mData.get(i))) {
                tempPos = i;
                break;
            }
        }
        if (tempPos > -1) {
            mData.remove(tempPos);
        }
        //不足max 显示添加图标
        if (mData.size() < showMaxNum) {
            mData.add("");
        }

        notifyDataSetChanged();
    }

}
