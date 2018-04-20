package com.renxin.doctor.activity.nim.action;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.nim.message.extension.CloseChatAttachment;
import com.renxin.doctor.activity.utils.UIUtils;

/**
 * CloseChatAction  结束咨询
 * Create at 2018/4/19 上午9:49 by mayakun
 */
public class CloseChatAction extends BaseAction {

    public CloseChatAction() {
        super(R.drawable.chat_close, R.string.input_panel_close);
    }

    @Override
    public void onClick() {
        CloseChatAttachment customAttachment = new CloseChatAttachment("咨询结束");
        IMMessage askPaperMessage = MessageBuilder.createCustomMessage(getAccount(),
                getSessionType(),
                UIUtils.getString(R.string.input_panel_close),
                customAttachment);
        sendMessage(askPaperMessage);
        getActivity().finish();
    }
}
