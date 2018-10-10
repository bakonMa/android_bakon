package com.junhetang.doctor.nim.action;

import android.Manifest;
import android.support.v4.app.FragmentActivity;

import com.junhetang.doctor.R;
import com.junhetang.doctor.utils.ToastUtil;
import com.netease.nim.uikit.business.session.actions.PickImageAction;
import com.netease.nim.uikit.business.session.constant.RequestCode;
import com.netease.nimlib.sdk.chatroom.ChatRoomMessageBuilder;
import com.netease.nimlib.sdk.msg.MessageBuilder;
import com.netease.nimlib.sdk.msg.constant.SessionTypeEnum;
import com.netease.nimlib.sdk.msg.model.IMMessage;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;

/**
 * PhotoAction 照片，拍照
 * Create at 2018/4/18 下午6:27 by mayakun
 */
public class PhotoAction extends PickImageAction {
    private int type;

    public PhotoAction(int type) {
        super(type == 0 ? R.drawable.chat_photo : R.drawable.chat_camera,
                type == 0 ? R.string.input_panel_image : R.string.input_panel_camera, true);
        this.type = type;
    }

    @Override
    protected void onPicked(File file) {
        IMMessage message;
        if (getContainer() != null && getContainer().sessionType == SessionTypeEnum.ChatRoom) {
            message = ChatRoomMessageBuilder.createChatRoomImageMessage(getAccount(), file, file.getName());
        } else {
            message = MessageBuilder.createImageMessage(getAccount(), getSessionType(), file, file.getName());
        }
        sendMessage(message);
    }


    @Override
    public void onClick() {
        //拍照权限
        RxPermissions rxPermissions = new RxPermissions((FragmentActivity) getActivity());
        rxPermissions.request(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA})
                .subscribe(aBoolean -> {
                    if (aBoolean) {
                        int requestCode = makeRequestCode(RequestCode.PICK_IMAGE);
                        showSelector(getTitleId(), requestCode, false, tempFile(), type);
                    } else {
                        ToastUtil.showCenterToast(type == 0 ? "请求相册权限失败败" : "请求照相机权限失");
                    }
                });
    }

}
