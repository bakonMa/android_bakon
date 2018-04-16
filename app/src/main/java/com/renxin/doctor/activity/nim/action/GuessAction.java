package com.renxin.doctor.activity.nim.action;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.nim.message.extension.GuessAttachment;

/**
 * Created by hzxuwen on 2015/6/11.
 */
public class GuessAction extends BaseAction {

    public GuessAction() {
        super(R.drawable.message_plus_guess_selector, R.string.input_panel_guess);
    }

    @Override
    public void onClick() {
        GuessAttachment attachment = new GuessAttachment();
        IMMessage message;
        if (getContainer() != null && getContainer().sessionType == SessionTypeEnum.ChatRoom) {
            message = ChatRoomMessageBuilder.createChatRoomCustomMessage(getAccount(), attachment);
        } else {
            message = MessageBuilder.createCustomMessage(getAccount(), getSessionType(), attachment.getValue().getDesc(), attachment);
        }

        sendMessage(message);
    }
}
