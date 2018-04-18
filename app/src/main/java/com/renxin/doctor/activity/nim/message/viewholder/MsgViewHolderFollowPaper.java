package com.renxin.doctor.activity.nim.message.viewholder;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nim.uikit.business.session.viewholder.MsgViewHolderBase;
import com.netease.nim.uikit.common.ui.recyclerview.adapter.BaseMultiItemFetchLoadAdapter;
import com.netease.nimlib.sdk.msg.constant.MsgDirectionEnum;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.config.H5Config;
import com.renxin.doctor.activity.nim.message.extension.FollowPaperAttachment;
import com.renxin.doctor.activity.ui.nimview.PaperH5Activity;
import com.renxin.doctor.activity.utils.UIUtils;

/**
 * MsgViewHolderFollowPaper 随诊单
 * Create at 2018/4/17 下午7:23 by mayakun
 */
public class MsgViewHolderFollowPaper extends MsgViewHolderBase {

    private TextView tviTps, tvTitle, tvContent;
    private LinearLayout lltContent;
    private FollowPaperAttachment attachment;

    public MsgViewHolderFollowPaper(BaseMultiItemFetchLoadAdapter adapter) {
        super(adapter);
    }

    @Override
    protected int getContentResId() {
        return R.layout.nim_message_item_followpaper;
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
        //todo 修改id
        PaperH5Activity.startResultActivity(context,
                RequestCode.SEND_FOLLOWPAPER_INFO,
                0,
                UIUtils.getString(R.string.input_panel_followpaper),
                H5Config.H5_FOLLOWPAPER_INFO + "1"
//                H5Config.H5_FOLLOWPAPER_INFO + attachment.getLinkID()
        );

    }

    @Override
    protected void bindContentView() {
        attachment = (FollowPaperAttachment) message.getAttachment();
        // MsgDirectionEnum.Out 表示发出去的消息， In 标示收到的消息
        if (message.getDirect() == MsgDirectionEnum.In) {//收到
            tviTps.setVisibility(View.GONE);
            lltContent.setVisibility(View.VISIBLE);
            tvTitle.setText(TextUtils.isEmpty(attachment.getPatients_title()) ? "随诊单（已填写）" : attachment.getPatients_title());
            tvContent.setText("患者 " + attachment.getPatients_name()
                    + "\n性别 " + attachment.getPatients_sex()
                    + "\n年龄 " + attachment.getPatients_age()
                    + "\n症状 " + attachment.getPatients_describe());
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
        return message.getDirect() == MsgDirectionEnum.In;
    }
}
