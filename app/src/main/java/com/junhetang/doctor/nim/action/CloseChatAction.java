package com.junhetang.doctor.nim.action;

import com.junhetang.doctor.config.EventConfig;
import com.junhetang.doctor.data.eventbus.Event;
import com.junhetang.doctor.data.eventbus.EventBusUtil;
import com.netease.nim.uikit.api.NimUIKit;
import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.junhetang.doctor.R;
import com.junhetang.doctor.nim.message.extension.CloseChatAttachment;
import com.junhetang.doctor.utils.UIUtils;

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
        //删除会话列表
        NimUIKit.deleteRecentContact2(getAccount());
        //调用 记录接口
        EventBusUtil.sendEvent(new Event(EventConfig.EVENT_KEY_CLOSE_CHAT, getAccount()));
        //关闭页面
        getActivity().finish();
    }
}
