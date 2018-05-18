package com.junhetang.doctor.nim.message.viewholder;

import android.text.TextUtils;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.junhetang.doctor.R;
import com.junhetang.doctor.nim.message.extension.FirstMessageAttachment;

/**
 * MsgViewHolderFirstMessage 患者发给医生的第一天消息（患者信息）
 * Create at 2018/4/24 上午9:29 by mayakun
 */
public class MsgViewHolderFirstMessage extends MsgViewHolderBase {

    private TextView tvTitle, tvContent;
    private FirstMessageAttachment attachment;

    public MsgViewHolderFirstMessage(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_first;
    }

    @Override
    protected void inflateContentView() {
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
    }

    @Override
    protected void bindContentView() {
        attachment = (FirstMessageAttachment) message.getAttachment();
        // MsgDirectionEnum.Out 表示发出去的消息， In 标示收到的消息
        if (message.getDirect() == MsgDirectionEnum.In) {//收到
            tvTitle.setText(TextUtils.isEmpty(attachment.getPatients_title()) ? "患者信息" : attachment.getPatients_title());
            tvContent.setText("患者 " + (TextUtils.isEmpty(attachment.getPatients_name()) ? "" : attachment.getPatients_name())
                    + "\n性别 " + (TextUtils.isEmpty(attachment.getPatients_sex()) ? "" : attachment.getPatients_sex())
                    + "\n年龄 " + (TextUtils.isEmpty(attachment.getPatients_age()) ? "" : attachment.getPatients_age()));
        }
    }
}
