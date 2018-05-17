package com.junhetang.doctor.nim.action;

import android.content.Intent;
import android.text.TextUtils;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.junhetang.doctor.R;
import com.junhetang.doctor.ui.nimview.QuickReplyActivity;

/**
 * QuickReplyAction  快速回复
 * Create at 2018/4/19 上午9:49 by mayakun
 */
public class QuickReplyAction extends BaseAction {

    public QuickReplyAction() {
        super(R.drawable.chat_quickreply, R.string.input_panel_quickreply);
    }

    @Override
    public void onClick() {
        //打开快速回复
        Intent intent = new Intent(getActivity(), QuickReplyActivity.class);
        getActivity().startActivityForResult(intent, makeRequestCode(RequestCode.SEND_COMM_MESSAGE));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.SEND_COMM_MESSAGE) {//获取常用语内容，填充输入框
            String message = data.getStringExtra("message");
            if (!TextUtils.isEmpty(message)) {
                getContainer().proxy.setEditeText(message);
            }
        }
    }
}
