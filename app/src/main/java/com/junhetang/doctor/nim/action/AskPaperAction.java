package com.junhetang.doctor.nim.action;

import android.content.Intent;
import android.text.TextUtils;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.junhetang.doctor.R;
import com.junhetang.doctor.config.H5Config;
import com.junhetang.doctor.nim.message.extension.AskPaperAttachment;
import com.junhetang.doctor.ui.nimview.PaperH5Activity;
import com.junhetang.doctor.utils.UIUtils;

/**
 * AskPaperAction  问诊单（菜单点击事件定义）
 * Create at 2018/4/17 下午5:32 by mayakun
 */
public class AskPaperAction extends BaseAction {

    public AskPaperAction() {
        super(R.drawable.chat_askpaper, R.string.input_panel_askpaper);
    }

    @Override
    public void onClick() {
        PaperH5Activity.startResultActivity(getActivity(),
                makeRequestCode(RequestCode.SEND_ASKPAPER),
                true,
                PaperH5Activity.FORM_TYPE.H5_ASKPAPER,
                UIUtils.getString(R.string.input_panel_askpaper),
                H5Config.H5_ASKPAPER,
                getAccount()
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.SEND_ASKPAPER) {
            //问诊单h5中【男性-0，女性-1，儿童-2】
            String typeID = data.getStringExtra("typeID");
            AskPaperAttachment customAttachment = new AskPaperAttachment(TextUtils.isEmpty(typeID) ? "0" : typeID);
            IMMessage askPaperMessage = MessageBuilder.createCustomMessage(getAccount(),
                    getSessionType(),
                    UIUtils.getString(R.string.input_panel_askpaper),
                    customAttachment);
            sendMessage(askPaperMessage);
        }
    }
}
