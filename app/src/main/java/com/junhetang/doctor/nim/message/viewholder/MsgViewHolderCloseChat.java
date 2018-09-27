package com.junhetang.doctor.nim.message.viewholder;

import android.widget.TextView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.junhetang.doctor.R;

/**
 * MsgViewHolderCloseChat 咨询结束
 * Create at 2018/4/19 上午10:11 by mayakun
 */
public class MsgViewHolderCloseChat extends MsgViewHolderBase {

    protected TextView textTips;

    public MsgViewHolderCloseChat(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_close;
    }

    @Override
    protected void inflateContentView() {
        textTips = view.findViewById(R.id.tv_tips);
    }

    @Override
    protected void bindContentView() {
        textTips.setText("咨询结束");
    }

    @Override
    protected boolean isMiddleItem() {
        return true;
    }

    @Override
    protected boolean shouldDisplayReceipt() {
        //已读，不显示
        return false;
    }

    //不显示长按菜单
    @Override
    protected boolean onItemLongClick() {
        return true;
    }
}
