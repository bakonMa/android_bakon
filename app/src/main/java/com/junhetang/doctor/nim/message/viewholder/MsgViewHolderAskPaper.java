package com.junhetang.doctor.nim.message.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.junhetang.doctor.R;
import com.junhetang.doctor.config.H5Config;
import com.junhetang.doctor.nim.message.extension.AskPaperAttachment;
import com.junhetang.doctor.ui.nimview.PaperH5Activity;
import com.junhetang.doctor.utils.UIUtils;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;

/**
 * MsgViewHolderAskPaper 问诊单
 * Create at 2018/4/17 下午7:23 by mayakun
 */
public class MsgViewHolderAskPaper extends MsgViewHolderBase {

    private TextView tviTps, tvTitle, tvContent;
    private LinearLayout lltContent;
    private AskPaperAttachment attachment;

    public MsgViewHolderAskPaper(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_askpaper;
    }

    @Override
    protected void inflateContentView() {
        tviTps = findViewById(R.id.tv_tips);
        lltContent = findViewById(R.id.llt_content);
        tvTitle = findViewById(R.id.tv_title);
        tvContent = findViewById(R.id.tv_content);
    }

    // 此条消息点击时响应事件
    @Override
    protected void onItemClick() {
        if (message.getDirect() == MsgDirectionEnum.Out) {//发出的不可以点击
            return;
        }
        PaperH5Activity.startResultActivity(context,
                RequestCode.SEND_ASKPAPER_INFO,
                true,
                PaperH5Activity.FORM_TYPE.H5_ASKPAPER,
                UIUtils.getString(R.string.input_panel_askpaper),
                H5Config.H5_ASKPAPER_INFO + attachment.getLinkID(),
                ""
        );
    }

    @Override
    protected void bindContentView() {
        attachment = (AskPaperAttachment) message.getAttachment();
        // MsgDirectionEnum.Out 表示发出去的消息， In 标示收到的消息
        if (message.getDirect() == MsgDirectionEnum.In) {//收到
            tviTps.setVisibility(View.GONE);
            lltContent.setVisibility(View.VISIBLE);
            tvTitle.setText(TextUtils.isEmpty(attachment.getPatients_title()) ? "问诊单（已填写）" : attachment.getPatients_title());
            tvContent.setText("患者 " + (TextUtils.isEmpty(attachment.getPatients_name()) ? "" : attachment.getPatients_name())
                    + "\n性别 " + (TextUtils.isEmpty(attachment.getPatients_sex()) ? "" : attachment.getPatients_sex())
                    + "\n年龄 " + (TextUtils.isEmpty(attachment.getPatients_age()) ? "" : attachment.getPatients_age())
                    + "\n症状 " +(TextUtils.isEmpty(attachment.getPatients_describe()) ? "" : attachment.getPatients_describe()));
        } else {//发出
            tviTps.setVisibility(View.VISIBLE);
            lltContent.setVisibility(View.GONE);
        }
    }

    @Override
    protected boolean isMiddleItem() {
        //发出的居中展示
        return message.getDirect() == MsgDirectionEnum.Out;
    }

    @Override
    protected boolean shouldDisplayReceipt() {
        //收到的显示已读，发出不显示
//        return message.getDirect() == MsgDirectionEnum.In;
        return false;
    }

    //不显示长按菜单
    @Override
    protected boolean onItemLongClick() {
        return true;
    }
}
