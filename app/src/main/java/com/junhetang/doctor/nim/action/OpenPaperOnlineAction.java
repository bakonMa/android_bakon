package com.junhetang.doctor.nim.action;

import android.content.Intent;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.junhetang.doctor.R;
import com.junhetang.doctor.nim.UserPreferences;
import com.junhetang.doctor.nim.message.extension.OPenPaperAttachment;
import com.junhetang.doctor.ui.activity.home.OpenPaperOnlineActivity;
import com.junhetang.doctor.utils.UIUtils;

/**
 * OpenPaperOnlineAction 在线开方
 * Create at 2018/5/3 上午9:27 by mayakun 
 */
public class OpenPaperOnlineAction extends BaseAction {

    public OpenPaperOnlineAction() {
        super(R.drawable.chat_openpaper_online, R.string.input_panel_openpaper_online);
    }

    @Override
    public void onClick() {
        OpenPaperOnlineActivity.startResultActivity(getActivity(),
                makeRequestCode(RequestCode.SEND_OPENPAPER_ONLINE),
                1,
                getAccount(),
                UserPreferences.getMembNoByAccid(getAccount()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.SEND_OPENPAPER_ONLINE) {
            OPenPaperAttachment customAttachment = new OPenPaperAttachment("您开的处方已提交给药房审核，请注意查收审核信息，并给与回复，祝您工作愉快！");
            IMMessage message = MessageBuilder.createCustomMessage(getAccount(),
                    getSessionType(),
                    UIUtils.getString(R.string.input_panel_openpaper_online),
                    customAttachment);
            sendMessage(message);
        }
    }
}
