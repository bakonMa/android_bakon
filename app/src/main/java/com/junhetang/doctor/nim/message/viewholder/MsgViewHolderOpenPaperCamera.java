package com.junhetang.doctor.nim.message.viewholder;

import android.widget.TextView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.junhetang.doctor.R;

/**
 * MsgViewHolderCloseChat 咨询结束
 * Create at 2018/4/19 上午10:11 by mayakun
 */
public class MsgViewHolderOpenPaperCamera extends MsgViewHolderBase {

    protected TextView textMessage;

    public MsgViewHolderOpenPaperCamera(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_openpapercamera;
    }

    @Override
    protected void inflateContentView() {
        textMessage = view.findViewById(R.id.tv_message);
    }

    @Override
    protected void bindContentView() {
        textMessage.setText("您开的处方已提交给药房审核，请注意查收审核信息，并给与回复，祝您工作愉快！");
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
}
