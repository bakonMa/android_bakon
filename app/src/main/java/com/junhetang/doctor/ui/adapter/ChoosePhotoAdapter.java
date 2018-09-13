package com.junhetang.doctor.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.utils.ImageUtil;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * PatientAdapter 患者列表adapter
 * Create at 2018/4/14 下午11:21 by mayakun
 */
public class ChoosePhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;
    private int showMaxNum = 9;
    private List<String> data = new CopyOnWriteArrayList<>();

    public ChoosePhotoAdapter(Context context, @Nullable List<String> data, int maxSize) {
        super(R.layout.item_choose_img, data);
        this.data = data;
        this.mContext = context;
        this.showMaxNum = maxSize;
    }

    public ChoosePhotoAdapter(Context context, @Nullable List<String> data) {
        this(context, data, 9);
    }

    @Override
    protected void convert(BaseViewHolder helper, String path) {
        helper.setGone(R.id.tv_close, !TextUtils.isEmpty(path))
                .addOnClickListener(R.id.tv_close)
                .addOnClickListener(R.id.iv_img);
        if (TextUtils.isEmpty(path)) {
            ((ImageView) helper.getView(R.id.iv_img)).setImageResource(0);
        } else {
            ImageUtil.showImage(path, helper.getView(R.id.iv_img));
        }
    }

    @Override
    public int getItemCount() {
        //最多showMaxNum个，不足+1 （显示加号）
        return data.size() >= showMaxNum ? showMaxNum : data.size();
    }

    //处理最后显示多一张添加提示的图片
    public void changeNotifyData() {
        //去除空的
        for (String str : data) {
            if (TextUtils.isEmpty(str)) {
                data.remove(str);
            }
        }
        if (data.size() < showMaxNum) {
            data.add("");
        }
        notifyDataSetChanged();
    }

}
