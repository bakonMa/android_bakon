package com.junhetang.doctor.nim.action;

import android.content.Intent;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.junhetang.doctor.R;
import com.junhetang.doctor.config.H5Config;
import com.junhetang.doctor.nim.message.extension.FollowPaperAttachment;
import com.junhetang.doctor.ui.nimview.PaperH5Activity;
import com.junhetang.doctor.utils.UIUtils;

/**
 * FollowPaperAction  随诊单（菜单点击事件定义）
 * Create at 2018/4/17 下午5:32 by mayakun
 */
public class FollowPaperAction extends BaseAction {

    public FollowPaperAction() {
        super(R.drawable.chat_followpaper, R.string.input_panel_followpaper);
    }

    @Override
    public void onClick() {
        PaperH5Activity.startResultActivity(getActivity(),
                makeRequestCode(RequestCode.SEND_FOLLOWPAPER),
                true,
                PaperH5Activity.FORM_TYPE.H5_ASKPAPER,
                UIUtils.getString(R.string.input_panel_followpaper),
                H5Config.H5_FOLLOWPAPER
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.SEND_FOLLOWPAPER) {
            FollowPaperAttachment attachment = new FollowPaperAttachment();
            IMMessage message = MessageBuilder.createCustomMessage(getAccount(),
                    getSessionType(),
                    UIUtils.getString(R.string.input_panel_followpaper),
                    attachment);
            sendMessage(message);
        }
    }
}