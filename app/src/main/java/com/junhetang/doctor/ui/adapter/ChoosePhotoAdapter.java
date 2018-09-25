package com.junhetang.doctor.ui.adapter;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.junhetang.doctor.R;
import com.junhetang.doctor.utils.ImageUtil;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * PatientAdapter 患者列表adapter
 * Create at 2018/4/14 下午11:21 by mayakun
 */
public class ChoosePhotoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    private Context mContext;
    private int showMaxNum = 9;
    private List<String> data = new ArrayList<>();

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
                .setImageResource(R.id.iv_img, 0)
                .addOnClickListener(R.id.tv_close)
                .addOnClickListener(R.id.iv_img);
        if (TextUtils.isEmpty(path)) {
            ((ImageView) helper.getView(R.id.iv_img)).setImageDrawable(null);
        } else {
            ImageUtil.showImage(path, helper.getView(R.id.iv_img));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    //处理最后显示多一张添加提示的图片
    public void changeNotifyData() {
        //******有关对list 遍历的同时，修改list 都可以这样处理，多线程需再考虑
        //去除自己追加空的占位数据
        Iterator<String> iterator = data.iterator();
        while (iterator.hasNext()) {
            String str = iterator.next();
            if (TextUtils.isEmpty(str)) {
                iterator.remove();//可以达到同样删除的效果 会维护modCount和expectedModCount的值的一致性，
                //data.remove(str);这句是不会的，直接删除会引发list ConcurrentModificationException错误
            }
        }

        if (data.size() < showMaxNum) {
            data.add("");
        }
        notifyDataSetChanged();
    }

}
