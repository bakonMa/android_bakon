package com.renxin.doctor.activity.nim.action;

import android.content.Intent;

import com.netease.nim.uikit.business.session.actions.BaseAction;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.renxin.doctor.activity.R;
import com.renxin.doctor.activity.nim.UserPreferences;
import com.renxin.doctor.activity.nim.message.extension.OPenPaperAttachment;
import com.renxin.doctor.activity.ui.activity.home.OpenPaperCameraActivity;
import com.renxin.doctor.activity.utils.UIUtils;

/**
 * AskPaperAction  问诊单（菜单点击事件定义）
 * Create at 2018/4/17 下午5:32 by mayakun
 */
public class OpenPaperCameraAction extends BaseAction {

    public OpenPaperCameraAction() {
        super(R.drawable.chat_openpaper_camera, R.string.input_panel_openpaper_camera);
    }

    @Override
    public void onClick() {
        OpenPaperCameraActivity.startResultActivity(getActivity(),
                makeRequestCode(RequestCode.SEND_OPENPAPER_CAMERA),
                1,
                getAccount(),
                UserPreferences.getMembNoByAccid(getAccount()));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RequestCode.SEND_OPENPAPER_CAMERA) {
            OPenPaperAttachment customAttachment = new OPenPaperAttachment("您开的处方已提交给药房审核，请注意查收审核信息，并给与回复，祝您工作愉快！");
            IMMessage message = MessageBuilder.createCustomMessage(getAccount(),
                    getSessionType(),
                    UIUtils.getString(R.string.input_panel_openpaper_camera),
                    customAttachment);
            sendMessage(message);
        }
    }
}
